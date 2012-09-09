package com.leelah.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.leelah.android.fragments.MenuFragment;
import com.leelah.android.ws.LeelahSystemServices;
import com.leelah.android.ws.LeelahSystemServices.LeelahApiStatusViewer;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentials;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentialsInformations;
import com.smartnsoft.SmartSlidingFragmentActivity;

public abstract class LeelahFragmentActivity
    extends SmartSlidingFragmentActivity<Bar.BarAggregate>
    implements LeelahCredentialsInformations, LeelahApiStatusViewer
{

  public abstract void onFulfillDisplayObjects();

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent)
  {
    final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    if (scanResult != null)
    {
      sendBroadcast(new Intent(LeelahActivity.BARCODE_SCANNER_ACTION).putExtra(LeelahActivity.BARCODE_SCANNER_RESULT, scanResult));
    }
    else
    {
      super.onActivityResult(requestCode, resultCode, intent);
    }
  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    LeelahSystemServices.getInstance().setLeelahCredentialsInformations(this);
  }

  public void onRetrieveDisplayObjects()
  {
    // set the Behind View
    setBehindContentView(R.layout.menu_frame);
    getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

    // customize the SlidingMenu
    getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
    getSlidingMenu().setShadowDrawable(R.drawable.shadow);
    getSlidingMenu().setBehindOffset((int) (getWindowManager().getDefaultDisplay().getWidth() / 1.3));
    // customize the ActionBar
    if (Build.VERSION.SDK_INT >= 11)
    {
      getActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

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
