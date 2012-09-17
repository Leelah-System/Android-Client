package com.leelah.android;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.Window;

import com.leelah.android.bar.Bar;
import com.leelah.android.bo.Category.CategoryDetails;
import com.leelah.android.bo.User;
import com.leelah.android.ws.LeelahSystemServices;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentials;
import com.leelah.android.ws.LeelahSystemServices.LeelahCredentialsInformations;
import com.smartnsoft.droid4me.LifeCycle;
import com.smartnsoft.droid4me.app.SmartSplashScreenActivity;

/**
 * The first activity displayed while the application is loading.
 * 
 * @author Jocelyn Girard
 * @since 2012.02.10
 */
public final class LeelahSystemSplashScreenActivity
    extends SmartSplashScreenActivity<Bar.BarAggregate, Void>
    implements LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy, Bar.BarDiscardedFeature, LeelahCredentialsInformations
{

  private final static int MISSING_SD_CARD_DIALOG_ID = 0;

  @Override
  protected Dialog onCreateDialog(int id)
  {
    if (id == LeelahSystemSplashScreenActivity.MISSING_SD_CARD_DIALOG_ID)
    {
      return new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.applicationName).setMessage(
          R.string.LeelahSystemSplashScreen_dialogMessage_noSdCard).setPositiveButton(android.R.string.ok, new OnClickListener()
      {
        public void onClick(DialogInterface dialog, int which)
        {
          finish();
        }
      }).create();
    }
    return super.onCreateDialog(id);
  }

  @Override
  protected boolean requiresExternalStorage()
  {
    return true;
  }

  @Override
  protected void onNoExternalStorage()
  {
    showDialog(LeelahSystemSplashScreenActivity.MISSING_SD_CARD_DIALOG_ID);
  }

  @Override
  protected Class<? extends Activity> getNextActivity()
  {
    return MainActivity.class;
  }

  @Override
  protected void onRetrieveDisplayObjectsCustom()
  {
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setContentView(LayoutInflater.from(this).inflate(R.layout.leelahsystem_splash_screen, null));
    setProgressBarIndeterminateVisibility(true);
  }

  @Override
  protected Void onRetrieveBusinessObjectsCustom()
      throws BusinessObjectUnavailableException
  {
    if (LeelahSystemApplication.hasCredentialsInformations(this) == true)
    {
      // On récup les données des EditText de l'interface
      final String login = getPreferences().getString(LoginActivity.USER_LOGIN, "");
      final String password = getPreferences().getString(LoginActivity.USER_PASSWORD, "");
      final String address = getPreferences().getString(LoginActivity.SERVER_ADDRESS, "");

      // Création de l'objet parametre pour le webservice
      final User user = new User(login, password, address);

      try
      {
        LeelahSystemServices.getInstance().setLeelahCredentialsInformations(this);
        LeelahSystemServices.getInstance().authenticate(user);
        LeelahSystemServices.getInstance().getUsers();
        final List<CategoryDetails> categories = LeelahSystemServices.getInstance().getCategories(false);
        LeelahSystemServices.getInstance().getProducts(false);
        for (CategoryDetails category : categories)
        {
          LeelahSystemServices.getInstance().getProductsByCateogry(false, category.id);
        }
      }
      catch (Exception exception)
      {
        throw new BusinessObjectUnavailableException(exception);
      }
    }
    else
    {
      try
      {
        Thread.sleep(2500);
      }
      catch (InterruptedException exception)
      {
        if (log.isErrorEnabled())
        {
          log.error("An interruption occurred while displaying the splash screen", exception);
        }
      }
    }
    return null;
  }

  public LeelahCredentials getCredentials()
  {
    return new LeelahCredentials(getPreferences().getString(LoginActivity.USER_LOGIN, ""), getPreferences().getString(LoginActivity.USER_PASSWORD, ""));
  }

  public String getServerURL()
  {
    return getPreferences().getString(LoginActivity.SERVER_ADDRESS, "");
  }
}
