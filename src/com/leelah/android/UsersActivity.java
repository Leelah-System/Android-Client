package com.leelah.android;

import com.leelah.android.bar.Bar.BarRefreshFeature;
import com.leelah.android.bar.Bar.BarShowBackFeature;
import com.leelah.android.fragments.UserListFragment;

public final class UsersActivity
    extends LeelahFragmentActivity
    implements BarRefreshFeature, BarShowBackFeature
{

  private UserListFragment userList;

  @Override
  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.users);
    super.onRetrieveDisplayObjects();

    userList = (UserListFragment) getSupportFragmentManager().findFragmentById(R.id.usersFragment);
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
    userList.onBarRefresh();
  }

}
