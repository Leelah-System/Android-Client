package com.leelah.android.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.leelah.android.bo.User;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListenerProvider;
import com.smartnsoft.droid4me.support.v4.app.SmartFragment;

public final class UserDetailsFragment
    extends SmartFragment<Bar.BarAggregate>
    implements BroadcastListenerProvider
{

  public static final String USER = "user";

  public static final String UPDATE_USER = "updateUser";

  private User user;

  private TextView firstname;

  private TextView lastname;

  private TextView login;

  private TextView password;

  private TextView email;

  private TextView address;

  public BroadcastListener getBroadcastListener()
  {
    return new BroadcastListener()
    {

      public IntentFilter getIntentFilter()
      {

        return new IntentFilter(UserDetailsFragment.UPDATE_USER);
      }

      public void onReceive(Intent intent)
      {
        if (intent.getAction().equals(UserDetailsFragment.UPDATE_USER))
        {
          if (intent.hasExtra(UserDetailsFragment.USER))
          {
            user = (User) intent.getSerializableExtra(UserDetailsFragment.USER);
            refreshBusinessObjectsAndDisplay();
          }
        }
      }

    };
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.user_details, null);

    firstname = (TextView) view.findViewById(R.id.firstname);
    lastname = (TextView) view.findViewById(R.id.lastname);
    login = (TextView) view.findViewById(R.id.login);
    password = (TextView) view.findViewById(R.id.password);
    email = (TextView) view.findViewById(R.id.email);
    address = (TextView) view.findViewById(R.id.address);

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
    if (user != null)
    {
      firstname.setText(user.first_name);
      lastname.setText(user.last_name);
      login.setText(user.login);
      password.setText(user.password);
      email.setText(user.email);
      address.setText(user.address);
    }
  }

}
