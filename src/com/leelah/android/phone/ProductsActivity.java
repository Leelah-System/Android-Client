package com.leelah.android.phone;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(android.R.id.content, new ProductsListFragment());
    fragmentTransaction.commit();
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
