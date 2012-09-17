package com.leelah.android;

import com.leelah.android.bar.Bar.BarRefreshFeature;
import com.leelah.android.fragments.ProductsListFragment;

public final class CaisseActivity
    extends LeelahFragmentActivity
    implements BarRefreshFeature
{

  private ProductsListFragment productsFragment;

  @Override
  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.caisse);
    super.onRetrieveDisplayObjects();

    productsFragment = (ProductsListFragment) getSupportFragmentManager().findFragmentById(R.id.productsFragment);
  }

  @Override
  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    super.onRetrieveBusinessObjects();
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