package com.leelah.android;

import com.leelah.android.bar.Bar.BarRefreshFeature;
import com.leelah.android.bar.Bar.BarShowBackFeature;

public final class UsersActivity
    extends LeelahFragmentActivity
    implements BarRefreshFeature, BarShowBackFeature
{

  @Override
  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.users);
    super.onRetrieveDisplayObjects();
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

  }

}
