package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;

import com.leelah.android.Bar;
import com.leelah.android.R;
import com.leelah.android.bo.Product;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;

public class ProductsListFragment
    extends SmartGridViewFragment<Bar.BarAggregate, GridView>
    implements BusinessObjectsRetrievalAsynchronousPolicy
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

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();
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

  @Override
  public void onFulfillDisplayObjects()
  {
    super.onFulfillDisplayObjects();
    getWrappedListView().getListView().setNumColumns(GridView.AUTO_FIT);
    getWrappedListView().getListView().setColumnWidth(getResources().getDimensionPixelSize(R.dimen.gridColumnWidth));
    getWrappedListView().getListView().setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.defaultPadding));
    getWrappedListView().getListView().setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.defaultPadding));
    getWrappedListView().getListView().setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
  }

}
