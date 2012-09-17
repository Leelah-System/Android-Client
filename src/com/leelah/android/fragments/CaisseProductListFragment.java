package com.leelah.android.fragments;

import com.leelah.android.R;

public final class CaisseProductListFragment
    extends ProductsListFragment
{

  @Override
  protected int getProductLayout()
  {
    return R.layout.caisse_product_list_item;
  }

  @Override
  public void onFulfillDisplayObjects()
  {
    super.onFulfillDisplayObjects();
    getWrappedListView().getListView().setColumnWidth(getResources().getDimensionPixelSize(R.dimen.caisseGridColumnWidth));
  }

}
