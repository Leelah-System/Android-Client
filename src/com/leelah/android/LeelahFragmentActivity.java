package com.leelah.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.leelah.android.ws.LeelahSystemServices;
import com.leelah.android.ws.LeelahSystemServices.LeelahApiStatusViewer;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentials;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentialsInformations;
import com.smartnsoft.droid4me.support.v4.app.SmartFragmentActivity;

public abstract class LeelahFragmentActivity
    extends SmartFragmentActivity<Bar.BarAggregate>
    implements LeelahCredentialsInformations, LeelahApiStatusViewer
{

  public abstract void onFulfillDisplayObjects();

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    LeelahSystemServices.getInstance().setLeelahCredentialsInformations(this);
  }

  public abstract void onRetrieveDisplayObjects();

  public abstract void onSynchronizeDisplayObjects();

  public LeelahCredentials getCredentials()
  {
    return new LeelahCredentials(getPreferences().getString(LoginActivity.USER_LOGIN, ""), getPreferences().getString(LoginActivity.USER_PASSWORD, ""));
  }

  public String getServerURL()
  {
    return getPreferences().getString(LoginActivity.SERVER_ADDRESS, "");
  }

  public void OnApiStatusSucced(final String message)
  {
    runOnUiThread(new Runnable()
    {
      public void run()
      {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
      }
    });
  }

  public void OnApiStatusError(String message)
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
    builder.setTitle(R.string.Service_error_title);
    builder.setMessage(message);
    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.dismiss();
      }
    });
  }

}
