package com.leelah.android;

import java.util.List;

import com.smartnsoft.droid4me.menu.StaticMenuCommand;
import com.smartnsoft.droid4me.support.v4.app.SmartFragmentActivity;

public class OrdersActivity
    extends SmartFragmentActivity<Bar.BarAttributes>
    implements Bar.BarShowHomeFeature
{

  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.orders);
  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {

  }

  public void onFulfillDisplayObjects()
  {

  }

  public void onSynchronizeDisplayObjects()
  {

  }

  @Override
  public List<StaticMenuCommand> getMenuCommands()
  {
    return super.getMenuCommands();
  }

}
