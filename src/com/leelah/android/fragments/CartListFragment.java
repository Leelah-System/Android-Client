package com.leelah.android.fragments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.leelah.android.LeelahActivity;
import com.leelah.android.LeelahSystemApplication;
import com.leelah.android.LeelahSystemApplication.ImageType;
import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.leelah.android.bo.Order;
import com.leelah.android.bo.Order.OrderDetails;
import com.leelah.android.bo.Order.OrderItem;
import com.leelah.android.bo.Product.ProductDetails;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListenerProvider;
import com.smartnsoft.droid4me.app.SmartCommands;
import com.smartnsoft.droid4me.app.SmartCommands.DialogGuardedCommand;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public class CartListFragment
    extends SmartListViewFragment<Bar.BarAggregate, ListView>
    implements OnClickListener, BusinessObjectsRetrievalAsynchronousPolicy, BroadcastListenerProvider
{

  public class CartProduct
      extends ProductDetails
  {
    private static final long serialVersionUID = 7172872330572247666L;

    public int quantityOrder;

    public CartProduct(ProductDetails product)
    {
      this.category_id = product.category_id;
      this.stock = product.stock;
      this.description = product.description;
      this.id = product.id;
      this.label = product.label;
      this.name = product.name;
      this.picture_attributes = product.picture_attributes;
      this.price = product.price;
      this.reference = product.reference;
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      CartProduct other = (CartProduct) obj;
      if (!getOuterType().equals(other.getOuterType()))
        return false;
      return true;
    }

    private CartListFragment getOuterType()
    {
      return CartListFragment.this;
    }

  }

  private static final class CartViewHolder
  {

    private final ImageView productImage;

    private final TextView productName;

    private final TextView productPrice;

    private final TextView productQuantity;

    public CartViewHolder(View view)
    {
      productImage = (ImageView) view.findViewById(R.id.productImage);
      productName = (TextView) view.findViewById(R.id.productName);
      productPrice = (TextView) view.findViewById(R.id.productPrice);
      productQuantity = (TextView) view.findViewById(R.id.productQuantity);

      productName.setTypeface(LeelahSystemApplication.typeWriterFont);
      productPrice.setTypeface(LeelahSystemApplication.typeWriterFont);
      productQuantity.setTypeface(LeelahSystemApplication.typeWriterFont);
    }

    public void update(Handler handler, CartProduct businessObject)
    {
      productName.setText(businessObject.name);
      productPrice.setText(productPrice.getResources().getString(R.string.Price_euro, Float.toString(businessObject.price)));
      productQuantity.setText("x" + businessObject.quantityOrder);
      LeelahSystemApplication.requestImageAndDisplay(handler, businessObject.name, productImage, ImageType.Thumbnail);
    }

  }

  private final class CartWrapper
      extends SimpleBusinessViewWrapper<CartProduct>
  {

    public CartWrapper(CartProduct businessObject)
    {
      super(businessObject, 0, R.layout.cart_list_item);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, CartProduct businessObject)
    {
      return new CartViewHolder(view);
    }

    @Override
    protected void updateView(Activity activity, Object businessViewHolder, View view, CartProduct businessObject, int position)
    {
      ((CartViewHolder) businessViewHolder).update(getHandler(), businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, CartProduct businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        ProductsListFragment.showProductDialog(getFragmentManager(), businessObject);
      }
      else if (objectEvent == ObjectEvent.WipedLeftToRight || objectEvent == ObjectEvent.WipedRightToLeft)
      {
        cartProducts.remove(position);
        refreshBusinessObjectsAndDisplay();
      }
      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

  }

  protected static final String ADD_TO_CART = "addToCart";

  protected static final String PRODUCT = CartListFragment.ADD_TO_CART + ".product";

  protected static final String QUANTITY = CartListFragment.ADD_TO_CART + ".quantity";

  private final List<CartProduct> cartProducts = new ArrayList<CartListFragment.CartProduct>();

  private View submitLayout;

  private TextView totalPrice;

  private float price;

  private Button submitButton;

  private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

  private Button scannerButton;

  public BroadcastListener getBroadcastListener()
  {
    return new BroadcastListener()
    {
      public IntentFilter getIntentFilter()
      {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(CartListFragment.ADD_TO_CART);
        intentFilter.addAction(LeelahActivity.BARCODE_SCANNER_ACTION);

        return intentFilter;
      }

      public void onReceive(Intent intent)
      {
        if (CartListFragment.ADD_TO_CART.equals(intent.getAction()))
        {
          final ProductDetails product = (ProductDetails) intent.getSerializableExtra(CartListFragment.PRODUCT);
          final int quantity = intent.getIntExtra(CartListFragment.QUANTITY, 1);
          boolean isUpdate = intent.hasExtra(ProductDetailsDialogFragment.IS_UPDATE);
          final CartProduct cartProduct = new CartProduct(product);

          boolean alreadyInCart = false;

          for (CartProduct cartProduct2 : cartProducts)
          {
            if (cartProduct2.id.equals(cartProduct.id))
            {
              cartProduct2.quantityOrder = isUpdate == true ? quantity : cartProduct2.quantityOrder + quantity;
              alreadyInCart = true;
              break;
            }
          }
          if (alreadyInCart == false)
          {
            cartProduct.quantityOrder = quantity;
            cartProducts.add(cartProduct);
          }
          refreshBusinessObjectsAndDisplay();
        }
        else if (intent.getAction().equals(LeelahActivity.BARCODE_SCANNER_ACTION))
        {
          final IntentResult intentResult = (IntentResult) intent.getSerializableExtra(LeelahActivity.BARCODE_SCANNER_RESULT);
          if (intentResult != null)
          {
            final String reference = intentResult.getContents();
            getProductByReference(reference);
          }
        }
      }
    };
  }

  protected void getProductByReference(final String reference)
  {
    final ProgressDialog progressDialog = new ProgressDialog(getCheckedActivity());
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage(getString(R.string.loading));
    progressDialog.setCancelable(true);
    progressDialog.show();
    SmartCommands.execute(new SmartCommands.DialogGuardedCommand(getCheckedActivity(), "Error while update the status of Order !", R.string.progressDialogMessage_unhandledProblem, progressDialog)
    {
      @Override
      protected void runGuardedDialog()
          throws Exception
      {
        final List<ProductDetails> products = LeelahSystemServices.getInstance().getProducts(true);

        for (ProductDetails productDetails : products)
        {
          if (productDetails.reference.equals(reference))
          {
            final Intent intent = new Intent(CartListFragment.ADD_TO_CART).putExtra(CartListFragment.PRODUCT, productDetails).putExtra(
                CartListFragment.QUANTITY, 1);
            getCheckedActivity().sendBroadcast(intent);
            refreshBusinessObjectsAndDisplay();
            return;
          }
        }
        getCheckedActivity().runOnUiThread(new Runnable()
        {
          public void run()
          {
            Toast.makeText(getCheckedActivity(), "Pas de produit pour la référence '" + reference + "' !", Toast.LENGTH_SHORT).show();
          }
        });
      }
    });
  }

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    submitLayout = LayoutInflater.from(getCheckedActivity()).inflate(R.layout.cart_submit_command, null);
    submitButton = (Button) submitLayout.findViewById(R.id.submitButton);
    scannerButton = (Button) submitLayout.findViewById(R.id.scanButton);
    totalPrice = (TextView) submitLayout.findViewById(R.id.totalPrice);

    getWrappedListView().getListView().setBackgroundResource(R.drawable.ticket_de_caisse_mini);
  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    price = 0f;

    for (CartProduct cartProduct : cartProducts)
    {
      price += cartProduct.quantityOrder * cartProduct.price;
      wrappers.add(new CartWrapper(cartProduct));
    }

    return wrappers;
  }

  @Override
  public void onFulfillDisplayObjects()
  {
    super.onFulfillDisplayObjects();
    getWrappedListView().addHeaderFooterView(false, true, submitLayout);
    totalPrice.setTypeface(LeelahSystemApplication.typeWriterFont);
    submitButton.setOnClickListener(this);
    scannerButton.setOnClickListener(this);
  }

  @Override
  public void onSynchronizeDisplayObjects()
  {
    super.onSynchronizeDisplayObjects();

    totalPrice.setText(getString(R.string.Price_euro, decimalFormat.format(price)));
  }

  public void onClick(View view)
  {
    if (view == submitButton)
    {
      final Order order = new Order();
      int quantity = 0;
      order.order.status = 0;
      order.order.reference = String.valueOf(cartProducts.hashCode() + LeelahSystemServices.getInstance().token.hashCode());
      for (CartProduct product : cartProducts)
      {
        final OrderItem orderItem = new OrderItem();
        orderItem.product_id = Integer.parseInt(product.id);
        orderItem.quantity = product.quantityOrder;
        quantity += product.quantityOrder;
        order.order.order_lines_attributes.add(orderItem);
      }
      final AlertDialog.Builder builder = new AlertDialog.Builder(getCheckedActivity());
      builder.setTitle("Confirmer la commande ?");
      builder.setMessage("Acheter " + quantity + " article(s) pour " + totalPrice.getText() + " ?");
      builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int which)
        {
          final ProgressDialog progress = new ProgressDialog(getActivity());
          progress.setMessage(getString(R.string.loading));
          progress.setIndeterminate(true);
          progress.show();
          SmartCommands.execute(new DialogGuardedCommand(getActivity(), CartListFragment.this, "An error occured when add Order", R.string.add_user_error_message, progress)
          {
            @Override
            protected void runGuardedDialog()
                throws Exception
            {
              final OrderDetails orderDetails = LeelahSystemServices.getInstance().addOrder(order);
              getActivity().runOnUiThread(new Runnable()
              {

                public void run()
                {
                  Toast.makeText(getContext(), "Commande confirmée !", Toast.LENGTH_SHORT).show();
                }
              });
              emptyCart();
              final OrderConfirmDialogFragment orderDialogFragment = new OrderConfirmDialogFragment(orderDetails);
              orderDialogFragment.show(getFragmentManager(), "newOrder");
            }
          });
        }
      });
      builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int which)
        {

        }
      });
      builder.show();
    }
    else if (view == scannerButton)
    {
      final IntentIntegrator integrator = new IntentIntegrator(getActivity());
      integrator.initiateScan();
    }
  }

  public void emptyCart()
  {
    cartProducts.clear();
    refreshBusinessObjectsAndDisplay();
  }

}
