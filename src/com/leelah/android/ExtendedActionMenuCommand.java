package com.leelah.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.smartnsoft.droid4me.framework.Commands.StaticExecutable;
import com.smartnsoft.droid4me.support.v4.menu.ActionMenuCommand;

@TargetApi(14)
public final class ExtendedActionMenuCommand
    extends ActionMenuCommand
{

  private int actionFlags;

  private ActionProvider actionProvider;

  private View actionView;

  public ExtendedActionMenuCommand(CharSequence text, char numericalShortcut, char characterShortcut, int actionEnum, int icon, StaticExecutable executable)
  {
    super(text, numericalShortcut, characterShortcut, actionEnum, icon, executable);
  }

  public ExtendedActionMenuCommand(CharSequence text, char numericalShortcut, char characterShortcut, int actionEnum, StaticExecutable executable)
  {
    super(text, numericalShortcut, characterShortcut, actionEnum, executable);
  }

  public ExtendedActionMenuCommand(int textId, char numericalShortcut, char characterShortcut, int icon, int actionEnum, StaticExecutable executable)
  {
    super(textId, numericalShortcut, characterShortcut, icon, actionEnum, executable);
  }

  public ExtendedActionMenuCommand(int textId, char numericalShortcut, char characterShortcut, int actionEnum, StaticExecutable executable)
  {
    super(textId, numericalShortcut, characterShortcut, actionEnum, executable);
  }

  public ExtendedActionMenuCommand(int textId, char numericalShortcut, char characterShortcut, int icon, int actionFlags, ActionProvider provider,
      StaticExecutable executable)
  {
    super(textId, numericalShortcut, characterShortcut, icon, executable);
    this.actionFlags = actionFlags;
    this.actionProvider = provider;
  }

  public ExtendedActionMenuCommand(int textId, char numericalShortcut, char characterShortcut, int icon, View actionView, StaticExecutable executable)
  {
    super(textId, numericalShortcut, characterShortcut, icon, executable);
    this.actionFlags = 0;
    this.actionView = actionView;
  }

  @Override
  public final MenuItem computeMenuItem(Context arg0, Menu arg1, int arg2)
  {
    final MenuItem menuItem = super.computeMenuItem(arg0, arg1, arg2);

    if (actionProvider != null)
    {
      menuItem.setActionProvider(actionProvider);
    }
    else if (actionView != null)
    {
      menuItem.setActionView(actionView);
      actionFlags = MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW;
    }
    menuItem.setShowAsActionFlags(actionFlags);
    return menuItem;
  }

}
