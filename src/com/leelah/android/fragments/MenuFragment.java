package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.MainActivity;
import com.leelah.android.OrdersActivity;
import com.leelah.android.StatisticsActivity;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public final class MenuFragment
    extends SmartListViewFragment<Void, ListView>
{

  public static final class MenuItem
  {
    public int iconId;

    public String name;

    private final Class<?> intentClass;

    public MenuItem(int iconId, String name, Class<?> intentClass)
    {
      this.iconId = iconId;
      this.name = name;
      this.intentClass = intentClass;
    }

  }

  public final static class MenuAttributes
  {

    private final TextView text;

    public MenuAttributes(View view)
    {
      text = (TextView) view.findViewById(android.R.id.text1);
    }

    public void update(MenuItem businessObject)
    {
      text.setText(businessObject.name);
    }

  }

  private final static class MenuWrapper
      extends SimpleBusinessViewWrapper<MenuItem>
  {

    public MenuWrapper(MenuItem businessObject)
    {
      super(businessObject, 0, android.R.layout.simple_list_item_1);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, MenuItem businessObject)
    {
      return new MenuAttributes(view);
    }

    @Override
    protected void updateView(Activity activity, Object viewAttributes, View view, MenuItem businessObject, int position)
    {
      ((MenuAttributes) viewAttributes).update(businessObject);
    }

    @Override
    public Intent computeIntent(Activity activity, Object viewAttributes, View view, MenuItem businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        return new Intent(activity, businessObject.intentClass);
      }
      return super.computeIntent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    wrappers.add(new MenuWrapper(new MenuItem(0, "Menu", MainActivity.class)));
    wrappers.add(new MenuWrapper(new MenuItem(0, "Administration", OrdersActivity.class)));
    wrappers.add(new MenuWrapper(new MenuItem(0, "Statistiques", StatisticsActivity.class)));

    return wrappers;
  }

}
