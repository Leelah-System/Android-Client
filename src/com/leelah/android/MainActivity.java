package com.leelah.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.leelah.android.fragments.MainFragment;
import com.smartnsoft.droid4me.framework.Commands;
import com.smartnsoft.droid4me.menu.StaticMenuCommand;
import com.smartnsoft.droid4me.support.v4.app.SmartFragmentActivity;
import com.smartnsoft.droid4me.support.v4.menu.ActionMenuCommand;

/**
 * The starting screen of the application.
 * 
 * @author Jocelyn Girard
 * @since 2012.02.10
 */
public final class MainActivity
    extends SmartFragmentActivity<Bar.BarAggregate>
    implements Bar.BarRefreshFeature
{

  public void onRetrieveDisplayObjects()
  {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(android.R.id.content, new MainFragment());
    fragmentTransaction.commit();
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
    final List<StaticMenuCommand> commands = new ArrayList<StaticMenuCommand>();
    commands.add(new ActionMenuCommand(R.string.Menu_admin_mode, '1', 'm', android.R.drawable.ic_menu_manage, MenuItem.SHOW_AS_ACTION_NEVER, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
      }
    }));
    commands.add(new ActionMenuCommand(R.string.Menu_settings, '1', 'm', android.R.drawable.ic_menu_preferences, MenuItem.SHOW_AS_ACTION_NEVER, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
      }
    }));
    return commands;
  }

  public void onTitleBarRefresh()
  {

  }

}
