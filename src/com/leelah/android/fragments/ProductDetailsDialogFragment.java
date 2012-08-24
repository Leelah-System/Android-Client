package com.leelah.android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leelah.android.LeelahSystemApplication;
import com.leelah.android.LeelahSystemApplication.ImageType;
import com.leelah.android.R;
import com.leelah.android.bo.Product.ProductDetails;
import com.leelah.android.fragments.CartListFragment.CartProduct;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewHolder;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;

public class ProductDetailsDialogFragment
    extends DialogFragment
    implements BusinessObjectsRetrievalAsynchronousPolicy
{

  private final class ProductDetailsAttributes
      implements View.OnClickListener
  {

    private final TextView productName;

    private final TextView productDescription;

    private final TextView productReference;

    private final TextView productPrice;

    private final Button addtoCart;

    private final Button quantityPlus;

    private final Button quantityMinus;

    private final TextView productQuantity;

    private final TextView productDispo;

    private final ImageView productImage;

    private int quantity = 1;

    public ProductDetailsAttributes(View view)
    {
      productImage = (ImageView) view.findViewById(R.id.productImage);
      productName = (TextView) view.findViewById(R.id.productName);
      productDescription = (TextView) view.findViewById(R.id.productDescription);
      productReference = (TextView) view.findViewById(R.id.productReference);
      productQuantity = (TextView) view.findViewById(R.id.productQuantity);
      productPrice = (TextView) view.findViewById(R.id.productPrice);
      productDispo = (TextView) view.findViewById(R.id.productDispo);
      addtoCart = (Button) view.findViewById(R.id.buttonCart);
      quantityPlus = (Button) view.findViewById(R.id.buttonAdd);
      quantityMinus = (Button) view.findViewById(R.id.buttonRemove);
    }

    public void update(Activity activity, final ProductDetails businessObject)
    {
      quantity = businessObject instanceof CartProduct ? ((CartProduct) businessObject).quantityOrder : quantity;
      productName.setText(businessObject.name);
      productDescription.setText(businessObject.description);
      productReference.setText("ref.:" + businessObject.reference);
      productPrice.setText(activity.getString(R.string.Price_euro, Float.toString(businessObject.price)));
      productQuantity.setText(Integer.toString(quantity));
      productDispo.setText(businessObject.stock > 0 ? getString(R.string.Product_in_stock) : getString(R.string.Product_not_in_stock));
      productDispo.setBackgroundResource(businessObject.stock > 0 ? R.color.leelah_green : R.color.leelah_red);
      if (businessObject.stock <= 0)
      {
        addtoCart.setEnabled(false);
        quantityMinus.setEnabled(false);
        quantityPlus.setEnabled(false);
      }
      addtoCart.setText(businessObject instanceof CartProduct ? R.string.Product_update_cart : R.string.Product_add_to_cart);
      addtoCart.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View view)
        {
          final Intent intent = new Intent(CartListFragment.ADD_TO_CART).putExtra(CartListFragment.PRODUCT, businessObject).putExtra(CartListFragment.QUANTITY,
              quantity);
          if (businessObject instanceof CartProduct)
          {
            intent.putExtra(ProductDetailsDialogFragment.IS_UPDATE, true);
          }
          view.getContext().sendBroadcast(intent);
          dismiss();
        }
      });
      quantityMinus.setOnClickListener(this);
      quantityPlus.setOnClickListener(this);

      LeelahSystemApplication.requestImageAndDisplay(new Handler(), businessObject.name, productImage, ImageType.Full);
    }

    public void onClick(View view)
    {
      if (quantity >= 1 && quantity <= 10)
      {
        quantity = view == quantityMinus ? quantity - (quantity == 1 ? 0 : 1) : quantity + (quantity == 10 ? 0 : 1);
        productQuantity.setText(Integer.toString(quantity));
      }
    }
  }

  private final class ProductDetailsWrapper
      extends SimpleBusinessViewWrapper<ProductDetails>
  {

    public ProductDetailsWrapper(ProductDetails businessObject)
    {
      super(businessObject, 0, R.layout.product_details);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, ProductDetails businessObject)
    {
      return new ProductDetailsAttributes(view);
    }

    @Override
    protected void updateView(Activity activity, Object viewAttributes, View view, ProductDetails businessObject, int position)
    {
      ((ProductDetailsAttributes) viewAttributes).update(activity, businessObject);
    }
  }

  enum ActionType
  {
    ViewProduct, ModifyProductCart
  }

  private static final String PRODUCT = "product";

  public static final String ACTION = "action";

  public static final String IS_UPDATE = "isUpdate";

  public static ProductDetailsDialogFragment newInstance(ActionType action, ProductDetails product)
  {
    final ProductDetailsDialogFragment fragment = new ProductDetailsDialogFragment();
    final Bundle bundle = new Bundle();
    bundle.putSerializable(PRODUCT, product);
    bundle.putSerializable(ACTION, action);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final ProductDetails product = (ProductDetails) getArguments().getSerializable(PRODUCT);
    final ActionType action = (ActionType) getArguments().getSerializable(ACTION);

    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final BusinessViewHolder<ProductDetails> viewHolder = new BusinessViewHolder<ProductDetails>(new ProductDetailsWrapper(product));
    builder.setView(viewHolder.getView(getActivity()));
    viewHolder.updateView(getActivity());
    return builder.create();
  }

}
