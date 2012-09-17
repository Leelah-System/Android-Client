package com.leelah.android.bar;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.leelah.android.R;
import com.smartnsoft.droid4me.app.ActivityController;
import com.smartnsoft.droid4me.app.ActivityController.Interceptor.InterceptorEvent;
import com.smartnsoft.droid4me.app.AppPublics;
import com.smartnsoft.droid4me.app.SmartableActivity;
import com.smartnsoft.droid4me.app.Smarted;

/**
 * @author Édouard Mercier
 * @since 2012.04.03
 */
public class ActionBar
    extends Bar
{

  static class ActionBarAttributes
      extends BarAttributes
  {

    private View.OnClickListener refreshOnClickListener;

    private final android.app.ActionBar actionBar;

    private final FragmentManager fragmentManager;

    private final View connectivityBlock;

    private final View connectivity;

    private final View action1Block;

    final ImageButton action1;

    private final View action2Block;

    final ImageButton action2;

    private final View action3Block;

    final ImageButton action3;

    private final View action4Block;

    final ImageButton action4;

    private final View refreshBlock;

    private final ImageButton refresh;

    private final ProgressBar refreshProgress;

    private final Map<ImageButton, BarAttributes.ActionDetail> actions = new HashMap<ImageButton, BarAttributes.ActionDetail>();

    private CharSequence titleString;

    private boolean enabled = true;

    protected ActionBarAttributes(Activity activity)
    {
      // if (activity instanceof FragmentActivity)
      // {
      // final FragmentActivity fragmentActivity = (FragmentActivity) activity;
      // fragmentManager = fragmentActivity.getSupportFragmentManager();
      // actionBar = activity.getActionBar();
      // }
      // else
      {
        fragmentManager = null;
        actionBar = activity.getActionBar();
      }
      if (actionBar != null && actionBar.getCustomView() != null)
      {
        final View customView = actionBar.getCustomView();
        connectivityBlock = null;// customView.findViewById(R.id.connectivityBlock);
        connectivity = null;// customView.findViewById(R.id.connectivity);
        action1Block = customView.findViewById(R.id.titleBarAction1Block);
        action1 = (ImageButton) customView.findViewById(R.id.titleBarAction1);
        action2Block = customView.findViewById(R.id.titleBarAction2Block);
        action2 = (ImageButton) customView.findViewById(R.id.titleBarAction2);
        action3Block = customView.findViewById(R.id.titleBarAction3Block);
        action3 = (ImageButton) customView.findViewById(R.id.titleBarAction3);
        action4Block = customView.findViewById(R.id.titleBarAction4Block);
        action4 = (ImageButton) customView.findViewById(R.id.titleBarAction4);
        refreshBlock = customView.findViewById(R.id.actionBarRefreshBlock);
        refresh = (ImageButton) customView.findViewById(R.id.actionBarRefresh);
        refreshProgress = (ProgressBar) customView.findViewById(R.id.actionBarRefreshProgress);
      }
      else
      {
        connectivityBlock = null;
        connectivity = null;
        action1Block = null;
        action1 = null;
        action2Block = null;
        action2 = null;
        action3Block = null;
        action3 = null;
        action4Block = null;
        action4 = null;
        refreshBlock = null;
        refresh = null;
        refreshProgress = null;
      }

      setShowHome(-1, null);
      setShowRefresh(null);
      setShowAction1(-1, null);
      setShowAction2(-1, null);
      setShowAction3(-1, null);
      setShowAction4(-1, null);
    }

    protected ActionBarAttributes(ActionBarAttributes actionBarAttributes)
    {
      actionBar = actionBarAttributes.actionBar;
      fragmentManager = actionBarAttributes.fragmentManager;
      connectivityBlock = actionBarAttributes.connectivityBlock;
      connectivity = actionBarAttributes.connectivity;
      action1Block = actionBarAttributes.action1Block;
      action1 = actionBarAttributes.action1;
      action2Block = actionBarAttributes.action2Block;
      action2 = actionBarAttributes.action2;
      action3Block = actionBarAttributes.action3Block;
      action3 = actionBarAttributes.action3;
      action4Block = actionBarAttributes.action4Block;
      action4 = actionBarAttributes.action4;
      refreshBlock = actionBarAttributes.refreshBlock;
      refresh = actionBarAttributes.refresh;
      refreshProgress = actionBarAttributes.refreshProgress;
    }

    @Override
    public void setTitle(int drawableResourceId)
    {
    }

    @Override
    public void setTitle(CharSequence title)
    {
      if (enabled == true)
      {
        if (actionBar != null)
        {
          if (title != null)
          {
            actionBar.setDisplayShowTitleEnabled(false);
          }
          else
          {
            actionBar.setDisplayShowTitleEnabled(false);
          }
        }
      }
      // We remember the values
      titleString = title;
    }

    @Override
    public void toggleVisibility()
    {
      if (enabled == true)
      {
        if (actionBar != null)
        {
          if (actionBar.isShowing() == true)
          {
            actionBar.hide();
          }
          else
          {
            actionBar.show();
          }
        }
      }
    }

    @Override
    public void setShowHome(int iconResourceId, View.OnClickListener onClickListener)
    {
      if (actionBar != null)
      {
        // if (VERSION.SDK_INT >= 14)
        // {
        // // This sets the ActionBar "logo" button enable/disabled state
        // actionBar.setHomeButtonEnabled(onClickListener != null);
        // }
        // if (iconResourceId > 0 && VERSION.SDK_INT >= 14)
        // {
        // actionBar.setIcon(iconResourceId);
        // }
        // if (onClickListener != null)
        // {
        // actionBar.setDisplayShowHomeEnabled(iconResourceId > 0);
        // }
        // actionBar.setDisplayShowHomeEnabled(onClickListener != null);
      }
    }

    public void setShowBackHome(boolean enabled)
    {
      if (actionBar != null)
      {
        actionBar.setDisplayHomeAsUpEnabled(enabled);
      }
    }

    @Override
    public void setShowRefresh(View.OnClickListener onClickListener)
    {
      if (enabled == true)
      {
        if (refreshBlock != null)
        {
          refreshBlock.setVisibility(View.VISIBLE);
          refresh.setVisibility(onClickListener != null ? View.VISIBLE : View.INVISIBLE);
          refresh.setOnClickListener(onClickListener);
        }
      }
      // We remember the listener
      this.refreshOnClickListener = onClickListener;
    }

    @Override
    public void toggleRefresh(Activity activity, boolean isLoading)
    {
      if (refreshOnClickListener != null)
      {
        if (refresh != null)
        {
          refresh.setVisibility(isLoading == true ? View.INVISIBLE : View.VISIBLE);
        }
      }
      if (refreshProgress != null)
      {
        refreshProgress.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
      }
      // activity.setProgressBarIndeterminateVisibility(isLoading);
    }

    @Override
    public void setEnabled(boolean enabled)
    {
      this.enabled = enabled;
      if (enabled == true)
      {
        apply();
      }
    }

    @Override
    public void setShowAction1(int iconResourceId, View.OnClickListener onClickListener)
    {
      setShowAction(action1Block, action1, iconResourceId, onClickListener);
    }

    @Override
    public void setShowAction2(int iconResourceId, View.OnClickListener onClickListener)
    {
      setShowAction(action2Block, action2, iconResourceId, onClickListener);
    }

    @Override
    public void setShowAction3(int iconResourceId, View.OnClickListener onClickListener)
    {
      setShowAction(action3Block, action3, iconResourceId, onClickListener);
    }

    @Override
    public void setShowAction4(int iconResourceId, View.OnClickListener onClickListener)
    {
      setShowAction(action4Block, action4, iconResourceId, onClickListener);
    }

    @Override
    public Object getControl(BarControl barControl)
    {
      switch (barControl)
      {
      case Home:
      default:
        return null;
      case Title:
        return null;
      case Refresh:
        return refreshBlock;
      case Action1:
        return action1;
      case Action2:
        return action2;
      case Action3:
        return action3;
      case Action4:
        return action4;
      }
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
      return view == action1;
    }

    @Override
    public boolean isAction2(View view)
    {
      return view == action2;
    }

    @Override
    public boolean isAction3(View view)
    {
      return view == action3;
    }

    @Override
    public boolean isAction4(View view)
    {
      return view == action4;
    }

    private void setShowAction(View actionBlockView, ImageButton actionButton, int iconResourceId, View.OnClickListener onClickListener)
    {
      if (enabled == true)
      {
        if (iconResourceId != -1)
        {
          actionButton.setImageResource(iconResourceId);
        }
        else
        {
          actionButton.setImageDrawable(null);
        }
        actionBlockView.setVisibility(onClickListener != null ? View.VISIBLE : View.GONE);
        actionButton.setOnClickListener(onClickListener);
      }

      // We remember the values
      if (onClickListener == null)
      {
        actions.remove(actionButton);
      }
      else
      {
        actions.put(actionButton, new BarAttributes.ActionDetail(actionBlockView, iconResourceId, onClickListener));
      }
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
      if (log.isDebugEnabled())
      {
        log.debug("Applying the title bar");
      }
      final Map<ImageButton, BarAttributes.ActionDetail> actions = forgetActions();
      for (Entry<ImageButton, BarAttributes.ActionDetail> entry : actions.entrySet())
      {
        setShowAction(entry.getValue().block, entry.getKey(), entry.getValue().resourceId, entry.getValue().onClickListener);
      }
      setTitle(titleString);
      // setTitle(titleImageResourceId);
      // setShowHome(homeDetail == null ? -1 : homeDetail.resourceId, homeDetail == null ? null : homeDetail.onClickListener);
      setShowRefresh(refreshOnClickListener);
    }

    public void setOnConnectivityListener(View.OnClickListener onClickListener)
    {
      if (connectivity != null)
      {
        connectivity.setOnClickListener(onClickListener);
      }
    }

    private void setHasConnectivity(boolean hasConnectivity)
    {
      if (connectivityBlock != null)
      {
        connectivityBlock.setVisibility(hasConnectivity == false ? View.VISIBLE : View.INVISIBLE);
      }
    }

    private Map<ImageButton, BarAttributes.ActionDetail> forgetActions()
    {
      final Map<ImageButton, BarAttributes.ActionDetail> previousActions = new HashMap<ImageButton, BarAttributes.ActionDetail>(actions);
      setShowAction1(-1, null);
      setShowAction2(-1, null);
      setShowAction3(-1, null);
      setShowAction4(-1, null);
      return previousActions;
    }

    public void setHide()
    {
      if (actionBar != null)
      {
        actionBar.hide();
      }
    }

  }

  public static class ActionBarAggregate
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
      actionBarAttributes.setOnConnectivityListener(this);
      final boolean hasShowBackFeature = activity instanceof Bar.BarShowBackFeature;
      if (activity instanceof Bar.BarHideFeature)
      {
        actionBarAttributes.setHide();
      }
      if (activity instanceof Bar.BarShowHomeFeature || hasShowBackFeature == true)
      {
        actionBarAttributes.setShowHome(defaultHomeResourceId, this);
        actionBarAttributes.setShowBackHome(hasShowBackFeature);
        if (activity instanceof SmartableActivity<?>)
        {
          final SmartableActivity<?> smartableActivity = (SmartableActivity<?>) activity;
          smartableActivity.setHomeIntent(hasShowBackFeature == true ? null : homeActivityIntent);
        }
      }
      else
      {
        actionBarAttributes.setShowHome(-1, null);
        actionBarAttributes.setShowBackHome(false);
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
      if (view == actionBarAttributes.connectivity)
      {
        // Toast.makeText(getActivity(), R.string.ActionBar_noConnectivity, Toast.LENGTH_LONG).show();
      }
    }

    @Override
    public IntentFilter getIntentFilter()
    {
      final IntentFilter intentFilter = super.getIntentFilter();
      intentFilter.addAction(ConnectivityListener.CONNECTIVITY_CHANGED_ACTION);
      return intentFilter;
    }

    @Override
    public void onReceive(Intent intent)
    {
      super.onReceive(intent);

      if (ConnectivityListener.CONNECTIVITY_CHANGED_ACTION.equals(intent.getAction()) == true)
      {
        final boolean hasConnectivity = intent.getBooleanExtra(ConnectivityListener.EXTRA_HAS_CONNECTIVITY, true);
        if (getAttributes() != null)
        {
          ((ActionBarAttributes) getAttributes()).setHasConnectivity(hasConnectivity);
        }
      }
    }

    public void setHasConnectivity(boolean hasConnectivity)
    {
      final ActionBarAttributes actionBarAttributes = (ActionBarAttributes) getAttributes();
      if (actionBarAttributes != null)
      {
        actionBarAttributes.setHasConnectivity(hasConnectivity);
      }
    }

  }

  public ActionBar(Intent homeActivityIntent, int defaultHomeResourceId, int defaultThemeResourceId)
  {
    super(homeActivityIntent, defaultHomeResourceId, defaultThemeResourceId);
  }

  @Override
  protected BarAggregate newBarAggregate(Activity activity, Intent homeActivityIntent, boolean requestWindowFeature)
  {
    return new ActionBarAggregate(activity, requestWindowFeature, homeActivityIntent);
  }

  protected ActionBarAttributes newActionBarAttributes(Activity activity)
  {
    return new ActionBarAttributes(activity);
  }

  protected BarAttributes newActionBarAttributes(ActionBarAttributes barAttributes)
  {
    return new ActionBarAttributes(barAttributes);
  }

  @Override
  public boolean onLifeCycleEvent(Activity activity, Object component, InterceptorEvent event)
  {
    onLifeCycleEventApplyTheme(activity, component, event);

    if (component != null)
    {
      // Here, we only handle the Fragment
      if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore)
      {
        if (activity instanceof Smarted<?> && component instanceof Smarted<?>)
        {
          @SuppressWarnings("unchecked")
          final Smarted<ActionBarAggregate> smartedActivity = (Smarted<ActionBarAggregate>) activity;
          @SuppressWarnings("unchecked")
          final Smarted<ActionBarAggregate> smartedFragment = (Smarted<ActionBarAggregate>) component;
          smartedFragment.setAggregate(smartedActivity.getAggregate());
        }
      }
    }
    if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore && activity instanceof Bar.BarDiscardedFeature == false)
    {
      if (activity instanceof Smarted<?>)
      {
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        // We ask for the progress bar to be available
        boolean requestWindowFeature = true;// activity.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        smartedActivity.setAggregate(newBarAggregate(activity, homeActivityIntent, requestWindowFeature));
      }
    }
    if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore && activity instanceof Bar.BarDiscardedFeature == false)
    {
      // We listen to specific broadcast events
      if (activity instanceof Smarted<?>)
      {
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        final BarAggregate barAggregate = smartedActivity.getAggregate();
        smartedActivity.registerBroadcastListeners(new AppPublics.BroadcastListener[] { barAggregate });
      }
    }
    if (event == ActivityController.Interceptor.InterceptorEvent.onContentChanged && activity instanceof BarDiscardedFeature == false)
    {
      if (activity instanceof Smarted<?>)
      {
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        final BarAggregate barAggregate = smartedActivity.getAggregate();
        if (barAggregate != null && barAggregate.customTitleSupported == true && barAggregate.getAttributes() == null)
        {
          final ActionBarAttributes parentBarAttributes;
          if (activity.getParent() != null && activity.getParent() instanceof Smarted<?>)
          {
            @SuppressWarnings("unchecked")
            final Smarted<BarAggregate> parentSmartedActivity = (Smarted<BarAggregate>) activity.getParent();
            parentBarAttributes = (ActionBarAttributes) parentSmartedActivity.getAggregate().getAttributes();
          }
          else
          {
            parentBarAttributes = null;
          }
          // final Activity actualActivity = activity.getParent() == null ? activity : activity.getParent();
          if (parentBarAttributes == null)
          {
            // actualActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
            barAggregate.setAttributes(newActionBarAttributes(activity));
          }
          else
          {
            barAggregate.setAttributes(newActionBarAttributes(parentBarAttributes));
          }
          if (activity.getIntent().hasExtra(Bar.DO_NOT_APPLY_BAR) == true)
          {
            barAggregate.getAttributes().setEnabled(false);
          }
          barAggregate.updateBarAttributes(activity, defaultHomeResourceId);
        }
      }
      return true;
    }
    if (event == ActivityController.Interceptor.InterceptorEvent.onResume && activity instanceof Bar.BarDiscardedFeature == false)
    {
      if (activity.getIntent().hasExtra(Bar.DO_NOT_APPLY_BAR) == false)
      {
        if (activity.getParent() != null && activity instanceof Smarted<?> && activity.getParent() instanceof Smarted<?>)
        {
          @SuppressWarnings("unchecked")
          final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
          final BarAggregate barAggregate = smartedActivity.getAggregate();
          if (barAggregate != null && barAggregate.customTitleSupported == true)
          {
            barAggregate.updateBarAttributes(activity, defaultHomeResourceId);
          }
          return true;
        }
      }
      return true;
    }
    return false;
  }

}
