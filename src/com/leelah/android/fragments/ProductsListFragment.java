package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.leelah.android.Bar;
import com.leelah.android.R;
import com.leelah.android.bo.Product;
import com.leelah.android.fragments.ProductDetailsDialogFragment.ActionType;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;

public class ProductsListFragment
    extends SmartGridViewFragment<Bar.BarAggregate, GridView>
    implements BusinessObjectsRetrievalAsynchronousPolicy
{

  private static final class ProductAttributes
  {

    private final ImageView image;

    private final TextView productName;

    private final TextView productDescription;

    public ProductAttributes(View view)
    {
      image = (ImageView) view.findViewById(R.id.image);
      productName = (TextView) view.findViewById(R.id.productName);
      productDescription = (TextView) view.findViewById(R.id.description);
    }

    public void update(Product businessObject)
    {
      productName.setText(businessObject.product.name);
      productDescription.setText(businessObject.product.description);
    }

  }

  private final class ProductWrapper
      extends SimpleBusinessViewWrapper<Product>
  {

    public ProductWrapper(Product businessObject)
    {
      super(businessObject, 0, R.layout.product_list_item);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, Product businessObject)
    {
      return new ProductAttributes(view);
    }

    @Override
    protected void updateView(Activity activity, Object viewAttributes, View view, Product businessObject, int position)
    {
      ((ProductAttributes) viewAttributes).update(businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, Product businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        showProductDialog(businessObject);
      }
      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
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

    final List<Product> products;
    try
    {
      products = LeelahSystemServices.getInstance().getProducts("test");
    }
    catch (Exception exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }

    for (Product product : products)
    {
      wrappers.add(new ProductWrapper(product));
    }

    // wrappers.add(new ProductWrapper(new Product()));
    // wrappers.add(new ProductWrapper(new Product()));
    // wrappers.add(new ProductWrapper(new Product()));
    // wrappers.add(new ProductWrapper(new Product()));
    // wrappers.add(new ProductWrapper(new Product()));

    return wrappers;
  }

  @Override
  public void onFulfillDisplayObjects()
  {
    super.onFulfillDisplayObjects();
    final int paddingSize = getResources().getDimensionPixelSize(R.dimen.defaultPadding);
    getWrappedListView().getListView().setNumColumns(GridView.AUTO_FIT);
    getWrappedListView().getListView().setColumnWidth(getResources().getDimensionPixelSize(R.dimen.gridColumnWidth));
    getWrappedListView().getListView().setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
    getWrappedListView().getListView().setVerticalSpacing(paddingSize);
    getWrappedListView().getListView().setHorizontalSpacing(paddingSize);
    getWrappedListView().getListView().setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
  }

  public void onTitleBarRefresh()
  {
    refreshBusinessObjectsAndDisplay(true);
  }

  private void showProductDialog(Product businessObject)
  {
    final ProductDetailsDialogFragment productDetailsDialogFragment = ProductDetailsDialogFragment.newInstance(ActionType.ViewProduct, businessObject);
    productDetailsDialogFragment.show(getFragmentManager(), "dialog");
  }
}
