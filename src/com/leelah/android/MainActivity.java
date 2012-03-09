package com.leelah.android;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.leelah.android.fragments.MainFragment;
import com.smartnsoft.droid4me.support.v4.app.SmartFragmentActivity;

/**
 * The starting screen of the application.
 * 
 * @author Jocelyn Girard
 * @since 2012.02.10
 */
public final class MainActivity
    extends SmartFragmentActivity<Void>
{

  public void onRetrieveDisplayObjects()
  {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(android.R.id.content, new MainFragment());
    fragmentTransaction.commit();
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

}
