package com.leelah.android.bar;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leelah.android.R;
import com.smartnsoft.droid4me.app.ActivityController;
import com.smartnsoft.droid4me.app.AppPublics;
import com.smartnsoft.droid4me.app.Smarted;

/**
 * Gathers in one place the handling of the "Android 2.0" title bar
 * 
 * @author Édouard Mercier
 * @since 2011.06.22
 */
public class TitleBar
    extends Bar
{

  static class TitleBarAttributes
      extends BarAttributes
  {

    private final View view;

    private boolean visible = true;

    private View.OnClickListener refreshOnClickListener;

    private final View homeBlock;

    final ImageButton home;

    private final ImageView titleImage;

    final TextView titleText;

    private final View action1Block;

    final ImageButton action1;

    private final View action2Block;

    final ImageButton action2;

    private final View action3Block;

    final ImageButton action3;

    private final View action4Block;

    final ImageButton action4;

    private final View refreshBlock;

    private final View refreshSeparator;

    private final ImageButton refresh;

    private final ProgressBar refreshProgress;

    private final Map<ImageButton, BarAttributes.ActionDetail> actions = new HashMap<ImageButton, BarAttributes.ActionDetail>();

    private BarAttributes.ActionDetail homeDetail;

    private CharSequence titleString;

    private int titleImageResourceId = -1;

    private boolean enabled = true;

    protected TitleBarAttributes(View view)
    {
      this.view = view;
      homeBlock = view.findViewById(R.id.titleBarHomeBlock);
      home = (ImageButton) view.findViewById(R.id.titleBarHome);
      titleImage = (ImageView) view.findViewById(R.id.titleBarTitleImage);
      titleText = (TextView) view.findViewById(R.id.titleBarTitleText);
      action1Block = view.findViewById(R.id.titleBarAction1Block);
      action1 = (ImageButton) view.findViewById(R.id.titleBarAction1);
      action2Block = view.findViewById(R.id.titleBarAction2Block);
      action2 = (ImageButton) view.findViewById(R.id.titleBarAction2);
      action3Block = view.findViewById(R.id.titleBarAction3Block);
      action3 = (ImageButton) view.findViewById(R.id.titleBarAction3);
      action4Block = view.findViewById(R.id.titleBarAction4Block);
      action4 = (ImageButton) view.findViewById(R.id.titleBarAction4);
      refreshBlock = view.findViewById(R.id.titleBarRefreshBlock);
      refreshSeparator = view.findViewById(R.id.titleBarRefreshSeparator);
      refresh = (ImageButton) view.findViewById(R.id.titleBarRefresh);
      refreshProgress = (ProgressBar) view.findViewById(R.id.titleBarRefreshProgress);

      setShowHome(-1, null);
      setShowRefresh(null);
      setShowAction1(-1, null);
      setShowAction2(-1, null);
      setShowAction3(-1, null);
      setShowAction4(-1, null);
    }

    protected TitleBarAttributes(TitleBarAttributes titleBarAttributes)
    {
      view = titleBarAttributes.view;
      homeBlock = titleBarAttributes.homeBlock;
      home = titleBarAttributes.home;
      titleImage = titleBarAttributes.titleImage;
      titleText = titleBarAttributes.titleText;
      action1Block = titleBarAttributes.action1Block;
      action1 = titleBarAttributes.action1;
      action2Block = titleBarAttributes.action2Block;
      action2 = titleBarAttributes.action2;
      action3Block = titleBarAttributes.action3Block;
      action3 = titleBarAttributes.action3;
      action4Block = titleBarAttributes.action4Block;
      action4 = titleBarAttributes.action4;
      refreshBlock = titleBarAttributes.refreshBlock;
      refreshSeparator = titleBarAttributes.refreshSeparator;
      refresh = titleBarAttributes.refresh;
      refreshProgress = titleBarAttributes.refreshProgress;
    }

    @Override
    public final void setEnabled(boolean enabled)
    {
      this.enabled = enabled;
      if (enabled == true)
      {
        apply();
      }
    }

    @Override
    public void setTitle(int imageResourceId)
    {
      if (enabled == true)
      {
        if (imageResourceId == -1)
        {
          return;
        }
        titleImage.setImageResource(imageResourceId);
        titleImage.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.GONE);
      }

      // We remember the values
      titleImageResourceId = imageResourceId;
    }

    @Override
    public void setTitle(CharSequence title)
    {
      if (enabled == true)
      {
        titleImage.setVisibility(View.GONE);
        titleText.setText(title);
        titleText.setVisibility(View.VISIBLE);
      }

      // We remember the values
      titleString = title;
    }

    @Override
    public void setShowHome(int iconResourceId, View.OnClickListener onClickListener)
    {
      if (iconResourceId > 0)
      {
        homeBlock.setVisibility(View.VISIBLE);
        home.setImageResource(iconResourceId);
      }
      else
      {
        homeBlock.setVisibility(View.GONE);
        home.setImageDrawable(null);
      }
      // home.setEnabled(onClickListener != null);
      home.setOnClickListener(onClickListener);

      // We remember the values
      homeDetail = new BarAttributes.ActionDetail(null, iconResourceId, onClickListener);
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
        return home;
      case Title:
        return titleText;
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
      return view == home;
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
        if (iconResourceId > 0)
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
    public void setShowRefresh(View.OnClickListener onClickListener)
    {
      if (enabled == true)
      {
        refreshBlock.setVisibility(View.VISIBLE);
        refreshSeparator.setVisibility(onClickListener != null ? View.VISIBLE : View.INVISIBLE);
        refresh.setVisibility(onClickListener != null ? View.VISIBLE : View.INVISIBLE);
        refresh.setOnClickListener(onClickListener);
      }
      // We remember the listener
      this.refreshOnClickListener = onClickListener;
    }

    @Override
    public void toggleVisibility()
    {
      if (view.getParent() != null && view.getParent() instanceof View)
      {
        final View titleContainer = (View) view.getParent();
        if (visible == true)
        {
          titleContainer.setVisibility(View.INVISIBLE);
        }
        else
        {
          titleContainer.setVisibility(View.VISIBLE);
        }
        visible = !visible;
      }
    }

    @Override
    public void toggleRefresh(Activity activity, boolean isLoading)
    {
      if (enabled == true)
      {
        if (refreshOnClickListener != null)
        {
          refresh.setVisibility(isLoading == true ? View.INVISIBLE : View.VISIBLE);
        }
        refreshProgress.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        // This also makes the refresh block gone, so that there is more space for the title
        refreshBlock.setVisibility(isLoading ? View.VISIBLE : View.GONE);
      }
    }

    @Override
    protected void dismiss(Activity activity, Object progressExtra)
    {
      toggleRefresh(activity, false);
    }

    @Override
    protected void show(Activity activity, Object progressExtra)
    {
      toggleRefresh(activity, true);
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
      setTitle(titleImageResourceId);
      setShowHome(homeDetail == null ? -1 : homeDetail.resourceId, homeDetail == null ? null : homeDetail.onClickListener);
      setShowRefresh(refreshOnClickListener);
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

  }

  static class TitleBarAggregate
      extends BarAggregate
  {

    public TitleBarAggregate(Activity activity, boolean customTitleSupported, Intent homeActivityIntent)
    {
      super(activity, customTitleSupported, homeActivityIntent);
    }

  }

  public TitleBar(Intent homeActivityIntent, int defaultHomeResourceId, int defaultThemeResourceId)
  {
    super(homeActivityIntent, defaultHomeResourceId, defaultThemeResourceId);
  }

  @Override
  protected BarAggregate newBarAggregate(Activity activity, Intent homeActivityIntent, boolean requestWindowFeature)
  {
    return new TitleBarAggregate(activity, requestWindowFeature, homeActivityIntent);
  }

  protected TitleBarAttributes newTitleBarAttributes(View view)
  {
    return new TitleBarAttributes(view);
  }

  protected TitleBarAttributes newTitleBarAttributes(TitleBarAttributes barAttributes)
  {
    return new TitleBarAttributes(barAttributes);
  }

  @Override
  public boolean onLifeCycleEvent(Activity activity, Object component, ActivityController.Interceptor.InterceptorEvent event)
  {
    onLifeCycleEventApplyTheme(activity, component, event);

    if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore && activity instanceof Bar.BarDiscardedFeature == false)
    {
      if (activity instanceof Smarted<?>)
      {
        boolean requestWindowFeature;
        if (activity.getParent() == null)
        {
          try
          {
            requestWindowFeature = activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
          }
          catch (AndroidRuntimeException exception)
          {
            // This means that the activity does not support custom titles
            return true;
          }
        }
        else
        {
          requestWindowFeature = true;
        }
        // We test whether we can customize the title bar
        final BarAggregate titleBarAggregate = newBarAggregate(activity, homeActivityIntent, requestWindowFeature);
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        smartedActivity.setAggregate(titleBarAggregate);
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
          final TitleBarAttributes parentBarAttributes;
          if (activity.getParent() != null && activity.getParent() instanceof Smarted<?>)
          {
            @SuppressWarnings("unchecked")
            final Smarted<BarAggregate> parentSmartedActivity = (Smarted<BarAggregate>) activity.getParent();
            parentBarAttributes = (TitleBarAttributes) parentSmartedActivity.getAggregate().getAttributes();
          }
          else
          {
            parentBarAttributes = null;
          }
          final Activity actualActivity = activity.getParent() == null ? activity : activity.getParent();
          if (parentBarAttributes == null)
          {
            actualActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
            barAggregate.setAttributes(newTitleBarAttributes(actualActivity.findViewById(R.id.titleBar)));
          }
          else
          {
            barAggregate.setAttributes(newTitleBarAttributes(parentBarAttributes));
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
    else if (event == ActivityController.Interceptor.InterceptorEvent.onResume && activity instanceof Bar.BarDiscardedFeature == false)
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
    }
    return false;
  }

}
