package com.leelah.android;

import java.util.List;

import com.leelah.android.bar.Bar.BarRefreshFeature;
import com.leelah.android.bar.Bar.BarShowHomeFeature;
import com.leelah.android.fragments.OrdersDetailsFragment;
import com.leelah.android.fragments.OrdersTypeListFragment;
import com.smartnsoft.droid4me.menu.StaticMenuCommand;

public class OrdersActivity
    extends LeelahFragmentActivity
    implements BarShowHomeFeature, BarRefreshFeature
{

  private OrdersTypeListFragment ordersType;

  private com.leelah.android.fragments.OrdersDetailsFragment ordersDetail;

  @Override
  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.orders);
    super.onRetrieveDisplayObjects();
  }

  @Override
  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    super.onRetrieveBusinessObjects();

    ordersType = (OrdersTypeListFragment) getSupportFragmentManager().findFragmentById(R.id.ordersTypesFragment);
    ordersDetail = (OrdersDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.ordersDetailsFragment);
  }

  @Override
  public void onFulfillDisplayObjects()
  {

  }

  @Override
  public void onSynchronizeDisplayObjects()
  {

  }

  @Override
  public List<StaticMenuCommand> getMenuCommands()
  {
    return super.getMenuCommands();
  }

  public void onBarRefresh()
  {
    ordersType.onBarRefresh();
  }

}
