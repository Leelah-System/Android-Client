package com.smartnsoft;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.Droid4mizer;
import com.smartnsoft.droid4me.app.SmartableActivity;
import com.smartnsoft.droid4me.framework.ActivityResultHandler.CompositeHandler;
import com.smartnsoft.droid4me.menu.MenuHandler.Composite;
import com.smartnsoft.droid4me.menu.StaticMenuCommand;

public abstract class SmartSlidingFragmentActivity<AggregateClass>
    extends SlidingFragmentActivity
    implements SmartableActivity<AggregateClass>
{

  private final Droid4mizer<AggregateClass, SmartSlidingFragmentActivity<AggregateClass>> droid4mizer = new Droid4mizer<AggregateClass, SmartSlidingFragmentActivity<AggregateClass>>(this, this, this, null);

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    droid4mizer.onCreate(new Runnable()
    {
      public void run()
      {
        SmartSlidingFragmentActivity.super.onCreate(savedInstanceState);
      }
    }, savedInstanceState);
  }

  @Override
  protected void onNewIntent(Intent intent)
  {
    super.onNewIntent(intent);
    droid4mizer.onNewIntent(intent);
  }

  @Override
  public void onContentChanged()
  {
    super.onContentChanged();
    droid4mizer.onContentChanged();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    droid4mizer.onResume();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    droid4mizer.onSaveInstanceState(outState);
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    droid4mizer.onStart();
  }

  @Override
  protected void onPause()
  {
    try
    {
      droid4mizer.onPause();
    }
    finally
    {
      super.onPause();
    }
  }

  @Override
  protected void onStop()
  {
    try
    {
      droid4mizer.onStop();
    }
    finally
    {
      super.onStop();
    }
  }

  @Override
  protected void onDestroy()
  {
    try
    {
      droid4mizer.onDestroy();
    }
    finally
    {
      super.onDestroy();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    return droid4mizer.onCreateOptionsMenu(super.onCreateOptionsMenu(menu), menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    return droid4mizer.onPrepareOptionsMenu(super.onPrepareOptionsMenu(menu), menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    return droid4mizer.onOptionsItemSelected(super.onOptionsItemSelected(item), item);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item)
  {
    return droid4mizer.onContextItemSelected(super.onContextItemSelected(item), item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    droid4mizer.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * SmartableActivity implementation.
   */

  public final void setHomeIntent(Intent homeIntent)
  {
    droid4mizer.setHomeIntent(homeIntent);
  }

  /**
   * Smartable implementation.
   */

  public AggregateClass getAggregate()
  {
    return droid4mizer.getAggregate();
  }

  public void setAggregate(AggregateClass aggregate)
  {
    droid4mizer.setAggregate(aggregate);
  }

  public Handler getHandler()
  {
    return droid4mizer.getHandler();
  }

  public SharedPreferences getPreferences()
  {
    return droid4mizer.getPreferences();
  }

  public void onException(Throwable throwable, boolean fromGuiThread)
  {
    droid4mizer.onException(throwable, fromGuiThread);
  }

  public void registerBroadcastListeners(BroadcastListener[] broadcastListeners)
  {
    droid4mizer.registerBroadcastListeners(broadcastListeners);
  }

  public void onBusinessObjectsRetrieved()
  {
    droid4mizer.onBusinessObjectsRetrieved();
  }

  public int getOnSynchronizeDisplayObjectsCount()
  {
    return droid4mizer.getOnSynchronizeDisplayObjectsCount();
  }

  public boolean isRefreshingBusinessObjectsAndDisplay()
  {
    return droid4mizer.isRefreshingBusinessObjectsAndDisplay();
  }

  public boolean isFirstLifeCycle()
  {
    return droid4mizer.isFirstLifeCycle();
  }

  public final boolean isInteracting()
  {
    return droid4mizer.isInteracting();
  }

  public void refreshBusinessObjectsAndDisplay(boolean retrieveBusinessObjects, Runnable onOver, boolean immediately)
  {
    droid4mizer.refreshBusinessObjectsAndDisplay(retrieveBusinessObjects, onOver, immediately);
  }

  /**
   * AppInternals.LifeCycleInternals implementation.
   */

  public boolean shouldKeepOn()
  {
    return droid4mizer.shouldKeepOn();
  }

  public Composite getCompositeActionHandler()
  {
    return droid4mizer.getCompositeActionHandler();
  }

  public CompositeHandler getCompositeActivityResultHandler()
  {
    return droid4mizer.getCompositeActivityResultHandler();
  }

  /**
   * Own implementation.
   */

  public List<StaticMenuCommand> getMenuCommands()
  {
    return null;
  };

}
