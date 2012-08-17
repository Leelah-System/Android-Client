package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.leelah.android.Bar;
import com.leelah.android.LeelahSystemApplication;
import com.leelah.android.LeelahSystemApplication.ImageType;
import com.leelah.android.R;
import com.leelah.android.bo.Product.ProductDetails;
import com.leelah.android.fragments.ProductDetailsDialogFragment.ActionType;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListenerProvider;
import com.smartnsoft.droid4me.app.AppPublics.SendLoadingIntent;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;

public class ProductsListFragment
    extends SmartGridViewFragment<Bar.BarAggregate, GridView>
    implements BusinessObjectsRetrievalAsynchronousPolicy, SendLoadingIntent, BroadcastListenerProvider
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

    public void update(final Handler handler, final ProductDetails businessObject)
    {
      productName.setText(businessObject.name);
      productDescription.setText(businessObject.description);
      LeelahSystemApplication.requestImageAndDisplay(handler, businessObject.name, image, ImageType.Thumbnail);
    }

  }

  private final class ProductWrapper
      extends SimpleBusinessViewWrapper<ProductDetails>
  {

    public ProductWrapper(ProductDetails businessObject)
    {
      super(businessObject, 0, R.layout.product_list_item);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, ProductDetails businessObject)
    {
      return new ProductAttributes(view);
    }

    @Override
    protected void updateView(Activity activity, Object viewAttributes, View view, ProductDetails businessObject, int position)
    {
      ((ProductAttributes) viewAttributes).update(getHandler(), businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, ProductDetails businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        showProductDialog(getFragmentManager(), businessObject);
      }
      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

  }

  private int categoryId = -1;

  private boolean fromCache = true;

  public BroadcastListener getBroadcastListener()
  {
    return new BroadcastListener()
    {
      public IntentFilter getIntentFilter()
      {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CategoriesListFragment.CHANGE_CATEGORY);
        return intentFilter;
      }

      public void onReceive(Intent intent)
      {
        if (CategoriesListFragment.CHANGE_CATEGORY.equals(intent.getAction()))
        {
          categoryId = intent.getIntExtra(CategoriesListFragment.SELECTED_CATEGORY, -1);
          refreshBusinessObjectsAndDisplay(true);
        }
      }

    };
  }

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    getWrappedListView().getListView().setBackgroundResource(android.R.color.white);
    categoryId = getCheckedActivity().getIntent().getIntExtra(CategoriesListFragment.SELECTED_CATEGORY, -1);
  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    final List<ProductDetails> products;
    try
    {
      products = categoryId > -1 ? LeelahSystemServices.getInstance().getProductsByCateogry(fromCache, categoryId)
          : LeelahSystemServices.getInstance().getProducts(fromCache);
    }
    catch (Exception exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }

    if (products != null)
    {
      for (ProductDetails product : products)
      {
        if (product != null)
        {
          wrappers.add(new ProductWrapper(product));
        }
      }
    }
    fromCache = true;
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
    fromCache = false;
    refreshBusinessObjectsAndDisplay(true);
  }

  static void showProductDialog(FragmentManager fragmentManager, ProductDetails businessObject)
  {
    final ProductDetailsDialogFragment productDetailsDialogFragment = ProductDetailsDialogFragment.newInstance(ActionType.ViewProduct, businessObject);
    productDetailsDialogFragment.show(fragmentManager, "dialog");
  }

}
