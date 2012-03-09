package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.leelah.android.R;
import com.leelah.android.TitleBar;
import com.leelah.android.bo.Product;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public class CartListFragment
    extends SmartListViewFragment<TitleBar.TitleBarAggregate, ListView>
    implements OnClickListener
{

  private static final class CartViewHolder
  {

    public CartViewHolder(View view)
    {
    }

    public void update(Product businessObject)
    {

    }

  }

  private final static class CartWrapper
      extends SimpleBusinessViewWrapper<Product>
  {

    public CartWrapper(Product businessObject)
    {
      super(businessObject, 0, R.layout.cart_list_item);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, Product businessObject)
    {
      return new CartViewHolder(view);
    }

    @Override
    protected void updateView(Activity activity, Object businessViewHolder, View view, Product businessObject, int position)
    {
      ((CartViewHolder) businessViewHolder).update(businessObject);
    }

  }

  private View submitLayout;

  private Button submitButton;

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    submitLayout = LayoutInflater.from(getCheckedActivity()).inflate(R.layout.cart_submit_command, null);
    submitButton = (Button) submitLayout.findViewById(R.id.submitButton);
  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    wrappers.add(new CartWrapper(new Product()));
    wrappers.add(new CartWrapper(new Product()));
    wrappers.add(new CartWrapper(new Product()));

    return wrappers;
  }

  @Override
  public void onFulfillDisplayObjects()
  {
    super.onFulfillDisplayObjects();

    getWrappedListView().addHeaderFooterView(false, true, submitLayout);
  }

  public void onClick(View view)
  {
    if (view == submitButton)
    {

    }
  }

}
