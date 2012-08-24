package com.leelah.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.leelah.android.R;
import com.leelah.android.bo.User;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.app.SmartCommands;
import com.smartnsoft.droid4me.app.SmartCommands.DialogGuardedCommand;

public final class AddUserDialogFragment
    extends LeelahDialogFragment<User>
{

  public AddUserDialogFragment()
  {
    super(new User());
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_user, null);
    final EditText firstName = (EditText) view.findViewById(R.id.firstName);
    final EditText lastName = (EditText) view.findViewById(R.id.lastName);
    final EditText login = (EditText) view.findViewById(R.id.login);
    final EditText password = (EditText) view.findViewById(R.id.password);
    final EditText email = (EditText) view.findViewById(R.id.email);
    builder.setView(view);
    builder.setTitle(R.string.add_user_title);
    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        final String firstnameValue = firstName.getEditableText().toString();
        final String lastnameValue = lastName.getEditableText().toString();
        final String loginValue = login.getEditableText().toString();
        final String passwordValue = password.getEditableText().toString();
        final String emailValue = email.getEditableText().toString();

        if (firstnameValue.isEmpty())
        {
          firstName.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (lastnameValue.isEmpty())
        {
          lastName.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (loginValue.isEmpty())
        {
          login.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (passwordValue.isEmpty())
        {
          password.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (emailValue.isEmpty())
        {
          email.setError(getActivity().getString(R.string.add_user_error));
        }
        else
        {
          final ProgressDialog progress = new ProgressDialog(getActivity());
          progress.setMessage(getString(R.string.loading));
          progress.setIndeterminate(true);
          progress.show();
          SmartCommands.execute(new DialogGuardedCommand(getActivity(), AddUserDialogFragment.this, "An error occured when add user" + emailValue, R.string.add_user_error_message, progress)
          {
            @Override
            protected void runGuardedDialog()
                throws Exception
            {
              businessObject.first_name = firstnameValue;
              businessObject.last_name = lastnameValue;
              businessObject.login = loginValue;
              businessObject.password = passwordValue;
              businessObject.email = emailValue;
              LeelahSystemServices.getInstance().addUser(businessObject);
              dialog.dismiss();
            }
          });
        }
      }
    });
    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.dismiss();
      }
    });
    return builder.create();
  }
}
