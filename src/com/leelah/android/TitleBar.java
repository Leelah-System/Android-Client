package com.leelah.android;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartnsoft.droid4me.app.ActivityController;
import com.smartnsoft.droid4me.app.AppPublics;
import com.smartnsoft.droid4me.app.Smarted;

/**
 * Gathers in one place the handling of the "Android 2.0" title bar
 * 
 * @author Édouard Mercier
 * @since 2011.06.22
 */
public final class TitleBar
    extends Bar
{

  static class TitleBarAttributes
      extends BarAttributes
  {

    protected static class ActionDetail
    {

      private final View block;

      private final int resourceId;

      private final View.OnClickListener onClickListener;

      private ActionDetail(View block, int resourceId, View.OnClickListener onClickListener)
      {
        this.block = block;
        this.resourceId = resourceId;
        this.onClickListener = onClickListener;
      }

    }

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

    private final Map<ImageButton, TitleBarAttributes.ActionDetail> actions = new HashMap<ImageButton, TitleBarAttributes.ActionDetail>();

    private TitleBarAttributes.ActionDetail homeDetail;

    private CharSequence titleString;

    private int titleImageResourceId = -1;

    private boolean enabled = true;

    protected TitleBarAttributes(Activity activity, View view)
    {
      this.view = view;
      homeBlock = view.findViewById(R.id.titleBarHomeBlock);
      home = (ImageButton) view.findViewById(R.id.titleBarHome);
      titleImage = (ImageView) view.findViewById(R.id.titleBarTitleImage);
      titleText = (TextView) view.findViewById(R.id.titleBarTitleText);
      setTitle(activity.getTitle());
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

    protected TitleBarAttributes(Activity activity, TitleBarAttributes titleBar)
    {
      view = titleBar.view;
      homeBlock = titleBar.homeBlock;
      home = titleBar.home;
      titleImage = titleBar.titleImage;
      titleText = titleBar.titleText;
      action1Block = titleBar.action1Block;
      action1 = titleBar.action1;
      action2Block = titleBar.action2Block;
      action2 = titleBar.action2;
      action3Block = titleBar.action3Block;
      action3 = titleBar.action3;
      action4Block = titleBar.action4Block;
      action4 = titleBar.action4;
      refreshBlock = titleBar.refreshBlock;
      refreshSeparator = titleBar.refreshSeparator;
      refresh = titleBar.refresh;
      refreshProgress = titleBar.refreshProgress;
      titleString = activity.getTitle();
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
    public void setShowHome(int iconResourceId, View.OnClickListener onClickListener)
    {
      if (iconResourceId != -1)
      {
        homeBlock.setVisibility(View.VISIBLE);
        home.setImageResource(iconResourceId);
      }
      else
      {
        homeBlock.setVisibility(View.GONE);
        home.setImageDrawable(null);
      }
      home.setEnabled(onClickListener != null);
      home.setOnClickListener(onClickListener);

      // We remember the values
      homeDetail = new TitleBarAttributes.ActionDetail(null, iconResourceId, onClickListener);
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

    protected void setShowAction(View actionBlockView, ImageButton actionButton, int iconResourceId, View.OnClickListener onClickListener)
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
        actions.put(actionButton, new TitleBarAttributes.ActionDetail(actionBlockView, iconResourceId, onClickListener));
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

    public void toggleVisibility(Handler handler)
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
      final Map<ImageButton, TitleBarAttributes.ActionDetail> actions = forgetActions();
      for (Entry<ImageButton, TitleBarAttributes.ActionDetail> entry : actions.entrySet())
      {
        setShowAction(entry.getValue().block, entry.getKey(), entry.getValue().resourceId, entry.getValue().onClickListener);
      }
      setTitle(titleString);
      setTitle(titleImageResourceId);
      setShowHome(homeDetail == null ? -1 : homeDetail.resourceId, homeDetail == null ? null : homeDetail.onClickListener);
      setShowRefresh(refreshOnClickListener);
    }

    protected Map<ImageButton, TitleBarAttributes.ActionDetail> forgetActions()
    {
      final Map<ImageButton, TitleBarAttributes.ActionDetail> previousActions = new HashMap<ImageButton, TitleBarAttributes.ActionDetail>(actions);
      setShowAction1(-1, null);
      setShowAction2(-1, null);
      setShowAction3(-1, null);
      setShowAction4(-1, null);
      return previousActions;
    }

  }

  public TitleBar(Intent homeActivityIntent, int defaultHomeResourceId, int defaultThemeResourceId)
  {
    super(homeActivityIntent, defaultHomeResourceId, defaultThemeResourceId);
  }

  @Override
  public boolean onLifeCycleEvent(Activity activity, Object component, ActivityController.Interceptor.InterceptorEvent event)
  {
    onLifeCycleEventApplyTheme(activity, component, event);

    if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore && activity instanceof TitleBar.BarDiscardedFeature == false)
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
        final BarAggregate titleBarAggregate = newTitleBarAggregate(activity, homeActivityIntent, requestWindowFeature);
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        smartedActivity.setAggregate(titleBarAggregate);
        return true;
      }
    }
    else if (event == ActivityController.Interceptor.InterceptorEvent.onContentChanged && activity instanceof BarDiscardedFeature == false)
    {
      if (activity instanceof Smarted<?>)
      {
        @SuppressWarnings("unchecked")
        final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
        final BarAggregate titleBarAggregate = smartedActivity.getAggregate();
        if (titleBarAggregate != null && titleBarAggregate.customTitleSupported == true && titleBarAggregate.getAttributes() == null)
        {
          final TitleBarAttributes parentTitleBarAttributes;
          if (activity.getParent() != null && activity.getParent() instanceof Smarted<?>)
          {
            @SuppressWarnings("unchecked")
            final Smarted<BarAggregate> parentSmartedActivity = (Smarted<BarAggregate>) activity.getParent();
            parentTitleBarAttributes = (TitleBarAttributes) parentSmartedActivity.getAggregate().getAttributes();
          }
          else
          {
            parentTitleBarAttributes = null;
          }
          final Activity actualActivity = activity.getParent() == null ? activity : activity.getParent();
          if (parentTitleBarAttributes == null)
          {
            actualActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
            titleBarAggregate.setAttributes(new TitleBarAttributes(activity, actualActivity.findViewById(R.id.titleBar)));
          }
          else
          {
            titleBarAggregate.setAttributes(new TitleBarAttributes(activity, parentTitleBarAttributes));
          }
          if (activity.getIntent().hasExtra(TitleBar.DO_NOT_APPLY_TITLE_BAR) == true)
          {
            titleBarAggregate.getAttributes().setEnabled(false);
          }
          titleBarAggregate.updateBarAttributes(activity, defaultHomeResourceId);
          smartedActivity.registerBroadcastListeners(new AppPublics.BroadcastListener[] { titleBarAggregate });
        }
      }
      return true;
    }
    else if (event == ActivityController.Interceptor.InterceptorEvent.onResume && !(activity instanceof TitleBar.BarDiscardedFeature))
    {
      if (activity.getIntent().hasExtra(TitleBar.DO_NOT_APPLY_TITLE_BAR) == false)
      {
        if (activity.getParent() != null && activity instanceof Smarted<?> && activity.getParent() instanceof Smarted<?>)
        {
          @SuppressWarnings("unchecked")
          final Smarted<BarAggregate> smartedActivity = (Smarted<BarAggregate>) activity;
          final BarAggregate titleBarAggregate = smartedActivity.getAggregate();
          if (titleBarAggregate != null && titleBarAggregate.customTitleSupported == true)
          {
            titleBarAggregate.updateBarAttributes(activity, defaultHomeResourceId);
          }
          return true;
        }
      }
    }
    return false;
  }

}
