package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.Bar;
import com.leelah.android.R;
import com.leelah.android.bo.Product;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListenerProvider;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
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

  private final static class CartWrapper
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

  }

  private final List<CartProduct> cartProducts = new ArrayList<CartListFragment.CartProduct>();

  private View submitLayout;

  private TextView totalPrice;

  private float price;

  private Button submitButton;

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

  public BroadcastListener getBroadcastListener()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
