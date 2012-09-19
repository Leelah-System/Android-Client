package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.leelah.android.bo.User;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public final class UserListFragment
    extends SmartListViewFragment<Bar.BarAggregate, ListView>
{

  private final static class UserAttributes
  {

    private final TextView name;

    private final TextView email;

    public UserAttributes(View view)
    {
      name = (TextView) view.findViewById(android.R.id.text1);
      email = (TextView) view.findViewById(android.R.id.text2);
    }

    public void update(User businessObject)
    {
      name.setText(businessObject.login);
      email.setText(businessObject.email);

    }

  }

  private final static class UserWrapper
      extends SimpleBusinessViewWrapper<User>
  {

    public UserWrapper(User businessObject)
    {
      super(businessObject, 0, android.R.layout.simple_list_item_2);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, User businessObject)
    {
      return new UserAttributes(view);
    }

    @Override
    protected void updateView(Activity activity, Object viewAttributes, View view, User businessObject, int position)
    {
      ((UserAttributes) viewAttributes).update(businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, User businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        activity.sendBroadcast(new Intent(UserDetailsFragment.UPDATE_USER).putExtra(UserDetailsFragment.USER, businessObject));
      }
      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

  }

  private final boolean fromCache = true;

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    getWrappedListView().getListView().setBackgroundResource(R.drawable.left_book);
  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<User> users;
    try
    {
      users = LeelahSystemServices.getInstance().getUsers(fromCache);
    }
    catch (Exception exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }

    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    for (User user : users)
    {
      wrappers.add(new UserWrapper(user));
    }

    return wrappers;
  }

}
