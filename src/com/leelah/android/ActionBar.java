package com.leelah.android;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.smartnsoft.droid4me.app.ActivityController;
import com.smartnsoft.droid4me.app.ActivityController.Interceptor.InterceptorEvent;
import com.smartnsoft.droid4me.app.AppPublics;
import com.smartnsoft.droid4me.app.Smartable;
import com.smartnsoft.droid4me.app.Smarted;
import com.smartnsoft.droid4me.support.v4.app.SmartFragmentActivity;

/**
 * @author Jocelyn Girard
 * @since 2011.12.06
 */
public final class ActionBar
    extends Bar
{

  public static interface ActionBarShowUniverses
  {
  }

  final static class ActionBarAttributes
      extends BarAttributes
  {

    private View.OnClickListener refreshOnClickListener;

    private final android.app.ActionBar actionBar;

    private final FragmentManager fragmentManager;

    private final View refreshBlock;

    private final ImageButton refresh;

    private final ProgressBar refreshProgress;

    protected ActionBarAttributes(Activity activity)
    {
      if (activity instanceof FragmentActivity)
      {
        final FragmentActivity fragmentActivity = (FragmentActivity) activity;
        fragmentManager = fragmentActivity.getSupportFragmentManager();
        actionBar = activity.getActionBar();
      }
      else
      {
        fragmentManager = null;
        actionBar = null;
      }
      if (actionBar != null && actionBar.getCustomView() != null)
      {
        final View customView = actionBar.getCustomView();
        refreshBlock = customView.findViewById(R.id.actionBarRefreshBlock);
        refresh = (ImageButton) customView.findViewById(R.id.actionBarRefresh);
        refreshProgress = (ProgressBar) customView.findViewById(R.id.actionBarRefreshProgress);
      }
      else
      {
        refreshBlock = null;
        refresh = null;
        refreshProgress = null;
      }

      setTitle(activity.getTitle());
      setShowHome(-1, null);
      setShowRefresh(null);
      setShowAction1(-1, null);
      setShowAction2(-1, null);
      setShowAction3(-1, null);
      setShowAction4(-1, null);
    }

    @Override
    public void setTitle(CharSequence title)
    {
      if (actionBar != null)
      {
        actionBar.setTitle(title);
      }
    }

    @Override
    public void setShowHome(int iconResourceId, View.OnClickListener onClickListener)
    {
      if (actionBar != null)
      {
        actionBar.setDisplayHomeAsUpEnabled(iconResourceId != -1);
      }
    }

    @Override
    protected void setShowRefresh(View.OnClickListener onClickListener)
    {
      if (refreshBlock != null)
      {
        refreshBlock.setVisibility(View.VISIBLE);
        refresh.setVisibility(onClickListener != null ? View.VISIBLE : View.INVISIBLE);
        refresh.setOnClickListener(onClickListener);
      }
      // We remember the listener
      this.refreshOnClickListener = onClickListener;
    }

    @Override
    protected void toggleRefresh(Activity activity, boolean isLoading)
    {
      if (activity instanceof SmartFragmentActivity<?>)
      {
        if (refreshOnClickListener != null)
        {
          refresh.setVisibility(isLoading == true ? View.INVISIBLE : View.VISIBLE);
        }
        refreshProgress.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
      }
      // activity.setProgressBarIndeterminateVisibility(isLoading);
    }

    @Override
    protected void setEnabled(boolean enabled)
    {
    }

    @Override
    public void setShowAction1(int iconResourceId, View.OnClickListener onClickListener)
    {
    }

    @Override
    public void setShowAction2(int iconResourceId, View.OnClickListener onClickListener)
    {
    }

    @Override
    public void setShowAction3(int iconResourceId, View.OnClickListener onClickListener)
    {
    }

    @Override
    public void setShowAction4(int iconResourceId, View.OnClickListener onClickListener)
    {
    }

    @Override
    public boolean isHome(View view)
    {
      return false;
    }

    @Override
    public boolean isRefresh(View view)
    {
      return view == refresh;
    }

    @Override
    public boolean isAction1(View view)
    {
      return false;
    }

    @Override
    public boolean isAction2(View view)
    {
      return false;
    }

    @Override
    public boolean isAction3(View view)
    {
      return false;
    }

    @Override
    public boolean isAction4(View view)
    {
      return false;
    }

    @Override
    protected void show(Activity activity, Object progressExtra)
    {
      toggleRefresh(activity, true);
    }

    @Override
    protected void dismiss(Activity activity, Object progressExtra)
    {
      toggleRefresh(activity, false);
    }

    @Override
    protected void apply()
    {
    }

  }

  final static class ActionBarAggregate
      extends BarAggregate
  {

    public ActionBarAggregate(Activity activity, boolean customTitleSupported, Intent homeActivityIntent)
    {
      super(activity, customTitleSupported, homeActivityIntent);
    }

    @Override
    public void updateBarAttributes(Activity activity, int defaultHomeResourceId)
    {
      super.updateBarAttributes(activity, defaultHomeResourceId);

      final ActionBarAttributes actionBarAttributes = (ActionBarAttributes) getAttributes();
      if (activity instanceof Bar.BarShowHomeFeature)
      {
        if (activity instanceof SmartFragmentActivity<?>)
        {
          final SmartFragmentActivity<?> smartFragmentActivity = (SmartFragmentActivity<?>) activity;
          smartFragmentActivity.setHomeIntent(homeActivityIntent);
        }
      }
      if (activity instanceof Bar.BarDiscardedFeature)
      {
        activity.getActionBar().hide();
      }
    }

    @Override
    public void onClick(View view)
    {
      super.onClick(view);

      final ActionBarAttributes actionBarAttributes = (ActionBarAttributes) getAttributes();
    }

    @Override
    public IntentFilter getIntentFilter()
    {
      final IntentFilter intentFilter = super.getIntentFilter();
      return intentFilter;
    }

    @Override
    public void onReceive(Intent intent)
    {
      super.onReceive(intent);
    }

  }

  public final static class ActionBarFragmentAggregate
  {

    private final Smartable<?> smartableFragment;

    private boolean fromCache = true;

    public ActionBarFragmentAggregate(Smartable<?> smartedFragment)
    {
      this.smartableFragment = smartedFragment;
    }

    public void reload()
    {
      setFromCache(false);
      smartableFragment.refreshBusinessObjectsAndDisplay(true, null, false);
    }

    public boolean getFromCache()
    {
      return fromCache;
    }

    public void setFromCache(boolean fromCache)
    {
      this.fromCache = fromCache;
    }

  }

  public ActionBar(Intent homeActivityIntent, int defaultThemeResourceId)
  {
    super(homeActivityIntent, 0, defaultThemeResourceId);
  }

  @Override
  public boolean onLifeCycleEvent(Activity activity, Object component, InterceptorEvent event)
  {
    if (component != null)
    {
      // In the case of a fragment, we only handle the attribute
      if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore)
      {
        if (component instanceof Smartable<?>)
        {
          @SuppressWarnings("unchecked")
          final Smartable<ActionBarFragmentAggregate> smartableFragment = (Smartable<ActionBarFragmentAggregate>) component;
          final ActionBarFragmentAggregate aggregate = new ActionBarFragmentAggregate(smartableFragment);
          smartableFragment.setAggregate(aggregate);
        }
      }
      return false;
    }

    onLifeCycleEventApplyTheme(activity, component, event);

    if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore && activity instanceof TitleBar.BarDiscardedFeature == false)
    {
      if (activity instanceof Smarted<?>)
      {
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        // We ask for the progress bar to be available
        boolean requestWindowFeature = activity.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        smartedActivity.setAggregate(newTitleBarAggregate(activity, homeActivityIntent, requestWindowFeature));
      }
    }
    if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore && activity instanceof TitleBar.BarDiscardedFeature == false)
    {
      // We listen to specific broadcast events
      if (activity instanceof Smarted<?>)
      {
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        final BarAggregate titleBarAggregate = smartedActivity.getAggregate();
        smartedActivity.registerBroadcastListeners(new AppPublics.BroadcastListener[] { titleBarAggregate });
      }
    }
    if (event == ActivityController.Interceptor.InterceptorEvent.onResume && activity instanceof TitleBar.BarDiscardedFeature == false)
    {
      if (activity instanceof Smarted<?>)
      {
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        final BarAggregate titleBarAggregate = smartedActivity.getAggregate();
        if (titleBarAggregate != null && titleBarAggregate.getAttributes() == null)
        {
          if (titleBarAggregate.customTitleSupported == true)
          {
            // By default, the progress bar should be hidden
            activity.setProgressBarIndeterminateVisibility(false);
          }
          ((ActionBarAggregate) titleBarAggregate).setAttributes(new ActionBarAttributes(activity));
        }
        smartedActivity.getAggregate().updateBarAttributes(activity, defaultHomeResourceId);
      }
      return true;
    }
    return false;
  }

  @Override
  protected BarAggregate newTitleBarAggregate(Activity activity, Intent homeActivityIntent, boolean requestWindowFeature)
  {
    return new ActionBarAggregate(activity, requestWindowFeature, homeActivityIntent);
  }

}
