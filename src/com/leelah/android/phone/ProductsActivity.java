package com.leelah.android.phone;

import com.leelah.android.LeelahFragmentActivity;
import com.leelah.android.R;
import com.leelah.android.bar.Bar.BarRefreshFeature;
import com.leelah.android.bar.Bar.BarShowHomeFeature;
import com.leelah.android.fragments.ProductsListFragment;

public final class ProductsActivity
    extends LeelahFragmentActivity
    implements BarRefreshFeature, BarShowHomeFeature
{

  private ProductsListFragment productsFragment;

  @Override
  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.products_phone);
    super.onRetrieveDisplayObjects();
  }

  @Override
  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    super.onRetrieveBusinessObjects();

    productsFragment = (ProductsListFragment) getSupportFragmentManager().findFragmentById(R.id.productsFragment);
  }

  @Override
  public void onFulfillDisplayObjects()
  {

  }

  @Override
  public void onSynchronizeDisplayObjects()
  {

  }

  public void onBarRefresh()
  {
    productsFragment.onBarRefresh();

  }

}
