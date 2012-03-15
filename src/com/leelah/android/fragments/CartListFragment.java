package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.Bar;
import com.leelah.android.R;
import com.leelah.android.bo.Product;
import com.leelah.android.bo.Product.ProductDetails;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListenerProvider;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public class CartListFragment
    extends SmartListViewFragment<Bar.BarAggregate, ListView>
    implements OnClickListener, BusinessObjectsRetrievalAsynchronousPolicy, BroadcastListenerProvider
{

  public class CartProduct
      extends Product
  {
    public int quantityOrder;
  }

  private static final class CartViewHolder
  {

    public CartViewHolder(View view)
    {
    }

    public void update(CartProduct businessObject)
    {

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
      ((CartViewHolder) businessViewHolder).update(businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, CartProduct businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        ProductsListFragment.showProductDialog(getFragmentManager(), businessObject);
      }
      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

  }

  protected static final String ADD_TO_CART = "addToCart";

  protected static final String PRODUCT = CartListFragment.ADD_TO_CART + ".product";

  private final List<CartProduct> cartProducts = new ArrayList<CartListFragment.CartProduct>();

  private View submitLayout;

  private TextView totalPrice;

  private float price;

  private Button submitButton;

  public BroadcastListener getBroadcastListener()
  {
    return new BroadcastListener()
    {
      public IntentFilter getIntentFilter()
      {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(CartListFragment.ADD_TO_CART);

        return intentFilter;
      }

      public void onReceive(Intent intent)
      {
        if (CartListFragment.ADD_TO_CART.equals(intent.getAction()))
        {
          final CartProduct product = new CartProduct();
          product.quantityOrder = 1;
          product.product = (ProductDetails) intent.getSerializableExtra(CartListFragment.PRODUCT);
          cartProducts.add(product);
          refreshBusinessObjectsAndDisplay();
        }
      }
    };
  }

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    submitLayout = LayoutInflater.from(getCheckedActivity()).inflate(R.layout.cart_submit_command, null);
    submitButton = (Button) submitLayout.findViewById(R.id.submitButton);
    totalPrice = (TextView) submitLayout.findViewById(R.id.totalPrice);

    getWrappedListView().getListView().setBackgroundResource(R.drawable.shadow_right);
  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    price = 0f;

    for (CartProduct cartProduct : cartProducts)
    {
      price += cartProduct.quantityOrder * cartProduct.product.price;
      wrappers.add(new CartWrapper(cartProduct));
    }

    return wrappers;
  }

  @Override
  public void onFulfillDisplayObjects()
  {
    super.onFulfillDisplayObjects();
    getWrappedListView().addHeaderFooterView(false, true, submitLayout);
  }

  @Override
  public void onSynchronizeDisplayObjects()
  {
    super.onSynchronizeDisplayObjects();
    totalPrice.setText(Float.toString(price));
  }

  public void onClick(View view)
  {
    if (view == submitButton)
    {

    }
  }

  public void emptyCart()
  {
    cartProducts.clear();
    refreshBusinessObjectsAndDisplay();
  }

}
