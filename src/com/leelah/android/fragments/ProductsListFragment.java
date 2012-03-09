package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;

import com.leelah.android.R;
import com.leelah.android.TitleBar;
import com.leelah.android.bo.Product;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public class ProductsListFragment
    extends SmartListViewFragment<TitleBar.TitleBarAggregate, GridView>
{

  private static final class ProductViewHolder
  {

    public ProductViewHolder(View view)
    {
    }

    public void update(Product businessObject)
    {

    }

  }

  private final static class ProductWrapper
      extends SimpleBusinessViewWrapper<Product>
  {

    public ProductWrapper(Product businessObject)
    {
      super(businessObject, 0, R.layout.product_list_item);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, Product businessObject)
    {
      return new ProductViewHolder(view);
    }

    @Override
    protected void updateView(Activity activity, Object businessViewHolder, View view, Product businessObject, int position)
    {
      ((ProductViewHolder) businessViewHolder).update(businessObject);
    }

  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    wrappers.add(new ProductWrapper(new Product()));
    wrappers.add(new ProductWrapper(new Product()));
    wrappers.add(new ProductWrapper(new Product()));
    wrappers.add(new ProductWrapper(new Product()));
    wrappers.add(new ProductWrapper(new Product()));

    return wrappers;
  }

}
