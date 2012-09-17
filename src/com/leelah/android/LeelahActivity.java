package com.leelah.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.leelah.android.bar.Bar;
import com.leelah.android.ws.LeelahSystemServices;
import com.leelah.android.ws.LeelahSystemServices.LeelahApiStatusViewer;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentials;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentialsInformations;
import com.smartnsoft.droid4me.app.SmartActivity;

public abstract class LeelahActivity
    extends SmartActivity<Bar.BarAggregate>
    implements LeelahCredentialsInformations, LeelahApiStatusViewer
{

  public static final String BARCODE_SCANNER_ACTION = "barcodeScannerAction";

  public static final String BARCODE_SCANNER_RESULT = "barCodeScannerResult";

  public abstract void onFulfillDisplayObjects();

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    LeelahSystemServices.getInstance().setLeelahCredentialsInformations(this);
    LeelahSystemServices.getInstance().setLeelahApiStatus(this);
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
    builder.show();
  }

}
