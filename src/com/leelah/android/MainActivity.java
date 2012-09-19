package com.leelah.android;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;

import com.leelah.android.bar.Bar.BarRefreshFeature;
import com.leelah.android.bar.Bar.BarShowBackFeature;
import com.leelah.android.bar.Bar.BarTitleFeature;
import com.leelah.android.fragments.AddCategoryDialogFragment;
import com.leelah.android.fragments.AddProductDialogFragment;
import com.leelah.android.fragments.AddUserDialogFragment;
import com.leelah.android.fragments.CartListFragment;
import com.leelah.android.fragments.CategoriesListFragment;
import com.leelah.android.fragments.ProductsListFragment;
import com.smartnsoft.droid4me.framework.Commands;
import com.smartnsoft.droid4me.menu.StaticMenuCommand;
import com.smartnsoft.droid4me.support.v4.menu.ActionMenuCommand;

/**
 * The starting screen of the application.
 * 
 * @author Jocelyn Girard
 * @since 2012.02.10
 */
public final class MainActivity
    extends LeelahFragmentActivity
    implements BarRefreshFeature, BarShowBackFeature, BarTitleFeature
{

  public static final String IS_ADMIN = "isAdmin";

  private CategoriesListFragment categoriesFragment;

  private ProductsListFragment productsFragment;

  private CartListFragment cartFragment;

  private boolean isAdminMode;

  @Override
  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.main);
    super.onRetrieveDisplayObjects();

    getActionBar().setTitle("Menu");
  }

  @Override
  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    super.onRetrieveBusinessObjects();

    isAdminMode = getIntent().getBooleanExtra(MainActivity.IS_ADMIN, false);

    categoriesFragment = (CategoriesListFragment) getSupportFragmentManager().findFragmentById(R.id.categoriesFragment);
    productsFragment = (ProductsListFragment) getSupportFragmentManager().findFragmentById(R.id.productsFragment);
    cartFragment = (CartListFragment) getSupportFragmentManager().findFragmentById(R.id.cartFragment);
  }

  @Override
  public void onFulfillDisplayObjects()
  {
  }

  @Override
  public void onSynchronizeDisplayObjects()
  {
    if (isAdminMode == true)
    {
      getSupportFragmentManager().beginTransaction().hide(cartFragment).commit();
    }
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

      @Override
      public boolean isVisible()
      {
        return !isAdminMode;
      }
    }));
    if (LeelahSystemApplication.isTabletMode == true && isAdminMode == false)
    {
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
    }
    commands.add(new StaticMenuCommand("Add User", '1', 'm', android.R.drawable.ic_menu_add, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        final AddUserDialogFragment addUserDialogFragment = new AddUserDialogFragment();
        addUserDialogFragment.show(getSupportFragmentManager(), "addUser");
      }

      @Override
      public boolean isVisible()
      {
        return isAdminMode;
      }

    }));
    commands.add(new StaticMenuCommand("Add Category", '1', 'm', android.R.drawable.ic_menu_add, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        final AddCategoryDialogFragment addCategoryDialogFragment = new AddCategoryDialogFragment();
        addCategoryDialogFragment.show(getSupportFragmentManager(), "addCategory");
      }

      @Override
      public boolean isVisible()
      {
        return isAdminMode;
      }
    }));
    commands.add(new StaticMenuCommand("Add Product", '1', 'm', android.R.drawable.ic_menu_add, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        final AddProductDialogFragment addProductDialogFragment = new AddProductDialogFragment();
        addProductDialogFragment.show(getSupportFragmentManager(), "addProduct");
      }

      @Override
      public boolean isVisible()
      {
        return isAdminMode;
      }
    }));
    commands.add(new ActionMenuCommand(R.string.Menu_settings, '1', 'm', android.R.drawable.ic_menu_preferences, MenuItem.SHOW_AS_ACTION_NEVER, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
      }

      @Override
      public boolean isVisible()
      {
        return isAdminMode;
      }
    }));
    return commands;
  }

  public void onBarRefresh()
  {
    categoriesFragment.onBarRefresh();
    if (LeelahSystemApplication.isTabletMode == true)
    {
      productsFragment.onBarRefresh();
    }
  }

  public String getBarTitle()
  {
    return "Menu";
  }

}
