package com.leelah.android;

import android.view.View;
import android.view.View.OnClickListener;

import com.leelah.android.LeelahSystemApplication.BelongsToUserRegistration;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.SmartActivity;

public final class LoginActivity
    extends SmartActivity<Bar.BarAggregate>
    implements BusinessObjectsRetrievalAsynchronousPolicy, BelongsToUserRegistration, OnClickListener, Bar.BarDiscardedFeature
{
  public static final String USER_PASSWORD = "password";

  public static final String USER_LOGIN = "login";

  public static final String SERVER_ADDRESS = "serverAddress";

  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.login_screen);

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

  public void onClick(View v)
  {

  }

}
