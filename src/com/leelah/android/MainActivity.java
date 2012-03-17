package com.leelah.android;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.leelah.android.fragments.CartListFragment;
import com.leelah.android.fragments.CategoriesListFragment;
import com.leelah.android.fragments.MainFragment;
import com.leelah.android.fragments.ProductsListFragment;
import com.leelah.android.ws.LeelahSystemServices;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentials;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentialsInformations;
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
    implements Bar.BarRefreshFeature, LeelahCredentialsInformations
{

  private CategoriesListFragment categoriesFragment;

  private ProductsListFragment productsFragment;

  private CartListFragment cartFragment;

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
    LeelahSystemServices.getInstance().setLeelahCredentialsInformations(this);
    categoriesFragment = (CategoriesListFragment) getSupportFragmentManager().findFragmentById(R.id.categoriesFragment);
    productsFragment = (ProductsListFragment) getSupportFragmentManager().findFragmentById(R.id.productsFragment);
    cartFragment = (CartListFragment) getSupportFragmentManager().findFragmentById(R.id.cartFragment);
  }

  public void onFulfillDisplayObjects()
  {
    getAggregate().getAttributes().setTitle(getString(R.string.applicationName));
  }

  public void onSynchronizeDisplayObjects()
  {

  }

  @Override
  public List<StaticMenuCommand> getMenuCommands()
  {
    final List<StaticMenuCommand> commands = new ArrayList<StaticMenuCommand>();
    commands.add(new ActionMenuCommand(R.string.Menu_call_server, '1', 'm', android.R.drawable.ic_menu_myplaces, MenuItem.SHOW_AS_ACTION_ALWAYS, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
      }
    }));
    commands.add(new ActionMenuCommand(R.string.Menu_empty_cart, '1', 'm', android.R.drawable.ic_menu_delete, MenuItem.SHOW_AS_ACTION_ALWAYS, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.Cart_empty_cart_dialog_title);
        builder.setMessage(R.string.Cart_empty_cart_dialog_message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
            cartFragment.emptyCart();
            dialog.dismiss();
          }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
            dialog.dismiss();
          }
        });
        builder.show();
      }
    }));
    commands.add(new ActionMenuCommand(R.string.Menu_admin_mode, '1', 'm', android.R.drawable.ic_menu_manage, MenuItem.SHOW_AS_ACTION_NEVER, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        startActivity(new Intent(MainActivity.this, OrdersActivity.class));
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
    categoriesFragment.onTitleBarRefresh();
    productsFragment.onTitleBarRefresh();
  }

  public LeelahCredentials getCredentials()
  {
    return new LeelahCredentials(getPreferences().getString(LoginActivity.USER_LOGIN, ""), getPreferences().getString(LoginActivity.USER_PASSWORD, ""));
  }

  public String getServerURL()
  {
    return getPreferences().getString(LoginActivity.SERVER_ADDRESS, "");
  }

}
