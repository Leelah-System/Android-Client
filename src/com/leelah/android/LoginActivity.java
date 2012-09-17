package com.leelah.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.leelah.android.LeelahSystemApplication.BelongsToUserRegistration;
import com.leelah.android.bar.Bar.BarDiscardedFeature;
import com.leelah.android.bo.User;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.SmartCommands;
import com.smartnsoft.droid4me.cache.Values.CacheException;

public final class LoginActivity
    extends LeelahActivity
    implements BusinessObjectsRetrievalAsynchronousPolicy, BelongsToUserRegistration, OnClickListener, BarDiscardedFeature
{
  public static final String USER_PASSWORD = "password";

  public static final String USER_LOGIN = "login";

  public static final String SERVER_ADDRESS = "serverAddress";

  private EditText editIpAddress;

  private EditText editLogin;

  private EditText editPassword;

  private Button buttonLogin;

  @Override
  public void onRetrieveDisplayObjects()
  {
    // On set le layout de l'activité
    setContentView(R.layout.login_screen);

    // On récup les éléments graphique de la vue
    editIpAddress = (EditText) findViewById(R.id.serverAddress);
    editLogin = (EditText) findViewById(R.id.login);
    editPassword = (EditText) findViewById(R.id.password);
    buttonLogin = (Button) findViewById(R.id.loginButton);
  }

  @Override
  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    super.onRetrieveBusinessObjects();
  }

  @Override
  public void onFulfillDisplayObjects()
  {
    // Set l'interface de clique au boutton
    buttonLogin.setOnClickListener(this);

    editIpAddress.setText(getPreferences().getString(SERVER_ADDRESS, ""));
    editLogin.setText(getPreferences().getString(USER_LOGIN, ""));
    editPassword.setText(getPreferences().getString(USER_PASSWORD, ""));
  }

  @Override
  public void onSynchronizeDisplayObjects()
  {

  }

  public void onClick(View view)
  {
    if (view == buttonLogin)
    {
      // On récup les données des EditText de l'interface
      final String login = editLogin.getText().toString();
      final String password = editPassword.getText().toString();
      final String address = editIpAddress.getText().toString().isEmpty() == true ? editIpAddress.getHint().toString() : editIpAddress.getText().toString();

      // Création de l'objet parametre pour le webservice
      final User user = new User(login, password, address);

      final ProgressDialog progressDialog = new ProgressDialog(this);
      progressDialog.setIndeterminate(true);
      progressDialog.setMessage(getString(R.string.applicationName));
      progressDialog.show();

      SmartCommands.execute(new SmartCommands.DialogGuardedCommand(this, this, "Error while login the user", R.string.connectivityProblem, progressDialog)
      {

        @Override
        protected void runGuardedDialog()
            throws Exception
        {
          try
          {
            // On contact le serveur pour s'authentifier
            LeelahSystemServices.getInstance().authenticate(user);

            // Si ca réussi on enregistre les informations dans les préférences
            final Editor editorPreferences = getPreferences().edit();
            editorPreferences.putString(SERVER_ADDRESS, address);
            editorPreferences.putString(USER_LOGIN, login);
            editorPreferences.putString(USER_PASSWORD, password);
            editorPreferences.commit();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
          }
          catch (CacheException exception)
          {
            // Si ca plante on log et le framework gère l'exception
            if (log.isErrorEnabled())
            {
              log.error("Error while authenticate user '" + login + "' with password '" + password + "' on server address '" + address + "'", exception);
            }
          }
          progressDialog.dismiss();
        }
      });

    }
  }

}
