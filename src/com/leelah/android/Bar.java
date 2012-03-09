package com.leelah.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;

import com.smartnsoft.droid4me.app.ActivityController;
import com.smartnsoft.droid4me.app.AppPublics;
import com.smartnsoft.droid4me.app.ProgressHandler;
import com.smartnsoft.droid4me.log.Logger;
import com.smartnsoft.droid4me.log.LoggerFactory;

/**
 * Gathers in one place the handling of the "Android 2.0" title bar
 * 
 * @author Édouard Mercier
 * @since 2011.06.22
 */
public abstract class Bar
{

  public static interface BarDiscardedFeature
  {
  }

  public static interface BarSetupFeature
  {
    void onTitleBarSetup(BarAttributes titleBarAttributes);
  }

  public static interface BarTheme
  {
    int getThemeResourceId();
  }

  public static interface BarRefreshFeature
  {
    void onTitleBarRefresh();
  }

  // public static interface BarShowLogoffFeature
  // {
  // }

  public static interface BarShowBackFeature
  {
  }

  public static interface BarShowHomeFeature
  {
  }

  public static abstract class BarAttributes
      extends ProgressHandler
  {

    public abstract void setTitle(CharSequence title);

    // public abstract void setTitle(CharSequence title, CharSequence subTitle);

    public abstract void setShowHome(int iconResourceId, View.OnClickListener onClickListener);

    protected abstract void setShowRefresh(View.OnClickListener onClickListener);

    protected abstract void toggleRefresh(Activity activity, boolean isLoading);

    // protected abstract void setShowSearch(View.OnClickListener onClickListener);

    protected abstract void setEnabled(boolean enabled);

    protected abstract void apply();

    public abstract void setShowAction1(int iconResourceId, View.OnClickListener onClickListener);

    public abstract void setShowAction2(int iconResourceId, View.OnClickListener onClickListener);

    public abstract void setShowAction3(int iconResourceId, View.OnClickListener onClickListener);

    public abstract void setShowAction4(int iconResourceId, View.OnClickListener onClickListener);

    // public abstract void setShowAction1(int iconResourceId, CharSequence text, View.OnClickListener onClickListener);

    // public abstract void setShowAction2(int iconResourceId, CharSequence text, View.OnClickListener onClickListener);

    // public abstract void setShowAction3(int iconResourceId, CharSequence text, View.OnClickListener onClickListener);

    // public abstract void setShowAction4(int iconResourceId, CharSequence text, View.OnClickListener onClickListener);

    public abstract boolean isHome(View view);

    public abstract boolean isRefresh(View view);

    // public abstract boolean isSearch(View view);

    public abstract boolean isAction1(View view);

    public abstract boolean isAction2(View view);

    public abstract boolean isAction3(View view);

    public abstract boolean isAction4(View view);

  }

  public static class BarAggregate
      extends AppPublics.LoadingBroadcastListener
      implements View.OnClickListener
  {

    protected final boolean customTitleSupported;

    protected final Intent homeActivityIntent;

    private BarAttributes attributes;

    protected BarRefreshFeature onRefresh;

    protected boolean titleBarFeaturesSet;

    public BarAggregate(Activity activity, boolean customTitleSupported, Intent homeActivityIntent)
    {
      super(activity, true);
      this.customTitleSupported = customTitleSupported;
      this.homeActivityIntent = homeActivityIntent;
    }

    BarAttributes getAttributes()
    {
      return attributes;
    }

    void setAttributes(BarAttributes titleBarAttributes)
    {
      attributes = titleBarAttributes;
    }

    public void updateBarAttributes(Activity activity, int defaultHomeResourceId)
    {
      final BarAttributes titleBarAttributes = getAttributes();

      titleBarAttributes.apply();

      if (titleBarFeaturesSet == false)
      {
        if (activity instanceof Bar.BarSetupFeature)
        {
          final Bar.BarSetupFeature titleBarSetupFeature = (Bar.BarSetupFeature) activity;
          titleBarSetupFeature.onTitleBarSetup(titleBarAttributes);
        }
        if (activity instanceof Bar.BarShowHomeFeature)
        {
          titleBarAttributes.setShowHome(defaultHomeResourceId, this);
        }
        if (activity instanceof Bar.BarRefreshFeature)
        {
          setOnRefresh((Bar.BarRefreshFeature) activity);
        }
        titleBarFeaturesSet = true;
      }
    }

    public void setOnRefresh(BarRefreshFeature titleBarRefreshFeature)
    {
      this.onRefresh = titleBarRefreshFeature;
      attributes.setShowRefresh(titleBarRefreshFeature != null ? this : null);
    }

    @Override
    protected void onLoading(boolean isLoading)
    {
      if (attributes != null)
      {
        attributes.toggleRefresh(getActivity(), isLoading);
      }
    }

    public void onClick(View view)
    {
      if (attributes.isHome(view) == true && homeActivityIntent != null)
      {
        goToHomeActivity();
      }
      else if (attributes.isRefresh(view) == true && onRefresh != null)
      {
        onRefresh.onTitleBarRefresh();
      }
    }

    public void goToHomeActivity()
    {
      getActivity().startActivity(homeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
      getActivity().finish();
    }

  }

  protected static final Logger log = LoggerFactory.getInstance(Bar.class);

  public final static String DO_NOT_APPLY_TITLE_BAR = "doNotApplyTitleBar";

  protected final Intent homeActivityIntent;

  protected final int defaultHomeResourceId;

  protected final int defaultThemeResourceId;

  public Bar(Intent homeActivityIntent, int defaultHomeResourceId, int defaultThemeResourceId)
  {
    this.homeActivityIntent = homeActivityIntent;
    this.defaultHomeResourceId = defaultHomeResourceId;
    this.defaultThemeResourceId = defaultThemeResourceId;
  }

  public abstract boolean onLifeCycleEvent(Activity activity, Object component, ActivityController.Interceptor.InterceptorEvent event);

  public void onLifeCycleEventApplyTheme(Activity activity, Object component, ActivityController.Interceptor.InterceptorEvent event)
  {
    if (component != null)
    {
      // We do not handle the Fragment instances
      return;
    }
    if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore && activity instanceof TitleBar.BarDiscardedFeature == false)
    {
      if (activity instanceof BarTheme)
      {
        activity.setTheme(((BarTheme) activity).getThemeResourceId());
      }
      else
      {
        int activityTheme = 0;
        try
        {
          activityTheme = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0).theme;
        }
        catch (NameNotFoundException exception)
        {
          if (log.isErrorEnabled())
          {
            log.error("Cannot determine the activity '" + activity.getClass().getName() + "' theme", exception);
          }
        }
        activity.setTheme(activityTheme > 0 ? activityTheme : defaultThemeResourceId);
      }
    }
  }

  protected BarAggregate newTitleBarAggregate(Activity activity, Intent homeActivityIntent, boolean requestWindowFeature)
  {
    return new BarAggregate(activity, requestWindowFeature, homeActivityIntent);
  }

}
