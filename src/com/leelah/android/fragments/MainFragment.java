package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leelah.android.AboutActivity;
import com.leelah.android.Bar;
import com.leelah.android.R;
import com.leelah.android.SettingsActivity;
import com.smartnsoft.droid4me.framework.Commands;
import com.smartnsoft.droid4me.menu.StaticMenuCommand;
import com.smartnsoft.droid4me.support.v4.app.SmartFragment;

public class MainFragment
    extends SmartFragment<Bar.BarAggregate>
{

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.main, null);

    return view;
  }

  public void onRetrieveDisplayObjects()
  {

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
    commands.add(new StaticMenuCommand(R.string.Main_menu_settings, '1', 's', android.R.drawable.ic_menu_preferences, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
      }
    }));
    commands.add(new StaticMenuCommand(R.string.Main_menu_about, '2', 'a', android.R.drawable.ic_menu_info_details, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        startActivity(new Intent(getActivity(), AboutActivity.class));
      }
    }));
    return commands;
  }

}
