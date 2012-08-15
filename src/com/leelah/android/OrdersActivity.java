package com.leelah.android;

import java.util.List;

import com.smartnsoft.droid4me.menu.StaticMenuCommand;

public class OrdersActivity
    extends LeelahFragmentActivity
    implements Bar.BarShowHomeFeature
{

  @Override
  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.orders);
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

  @Override
  public List<StaticMenuCommand> getMenuCommands()
  {
    return super.getMenuCommands();
  }

}
