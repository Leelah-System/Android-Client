package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.CaisseActivity;
import com.leelah.android.MainActivity;
import com.leelah.android.OrdersActivity;
import com.leelah.android.R;
import com.leelah.android.StatisticsActivity;
import com.smartnsoft.SmartSlidingFragmentActivity;
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

    public Intent intent;

    public MenuItem(int iconId, String name, Intent intent)
    {
      this.iconId = iconId;
      this.name = name;
      this.intent = intent;
    }

  }

  public final static class MenuAttributes
  {

    private final TextView text;

    private final ImageView image;

    public MenuAttributes(View view)
    {
      text = (TextView) view.findViewById(R.id.title);
      image = (ImageView) view.findViewById(R.id.image);
    }

    public void update(MenuItem businessObject)
    {
      text.setText(businessObject.name);
      image.setImageResource(businessObject.iconId);
    }

  }

  private final static class MenuWrapper
      extends SimpleBusinessViewWrapper<MenuItem>
  {

    public MenuWrapper(MenuItem businessObject)
    {
      super(businessObject, 0, R.layout.menu_item);
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
        if (activity instanceof SmartSlidingFragmentActivity)
        {
          final SmartSlidingFragmentActivity<?> smartActivity = (SmartSlidingFragmentActivity<?>) activity;
          smartActivity.showAbove();
        }
        return businessObject.intent;
      }
      return super.computeIntent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    wrappers.add(new MenuWrapper(new MenuItem(R.drawable.book, "Menu", new Intent(getCheckedActivity(), MainActivity.class))));
    wrappers.add(new MenuWrapper(new MenuItem(R.drawable.users, "Utilisateurs", null)));
    wrappers.add(new MenuWrapper(new MenuItem(R.drawable.tag, "Produits & Catégories", new Intent(getCheckedActivity(), MainActivity.class).putExtra(
        MainActivity.IS_ADMIN, true))));
    wrappers.add(new MenuWrapper(new MenuItem(R.drawable.calculator, "Caisse", new Intent(getCheckedActivity(), CaisseActivity.class))));
    wrappers.add(new MenuWrapper(new MenuItem(R.drawable.shopping_cart, "Commandes", new Intent(getCheckedActivity(), OrdersActivity.class))));
    wrappers.add(new MenuWrapper(new MenuItem(R.drawable.chart, "Statistiques", new Intent(getCheckedActivity(), StatisticsActivity.class))));

    return wrappers;
  }

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    getWrappedListView().getListView().setBackgroundResource(R.drawable.background_grey_dark);
  }

}
