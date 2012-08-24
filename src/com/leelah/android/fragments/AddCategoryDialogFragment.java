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
import com.leelah.android.bo.Category;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.app.SmartCommands;
import com.smartnsoft.droid4me.app.SmartCommands.DialogGuardedCommand;

public final class AddCategoryDialogFragment
    extends LeelahDialogFragment<Category>
{

  private final boolean fromCache = true;

  public AddCategoryDialogFragment()
  {
    super(new Category());
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_category, null);
    final EditText name = (EditText) view.findViewById(R.id.name);
    final EditText label = (EditText) view.findViewById(R.id.label);
    final EditText description = (EditText) view.findViewById(R.id.description);
    builder.setView(view);
    builder.setTitle(R.string.add_category_title);
    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        final String nameValue = name.getEditableText().toString();
        final String labelValue = label.getEditableText().toString();
        final String descriptionValue = description.getEditableText().toString();
        if (nameValue.isEmpty())
        {
          name.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (labelValue.isEmpty())
        {
          label.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (descriptionValue.isEmpty())
        {
          description.setError(getActivity().getString(R.string.add_user_error));
        }
        else
        {
          final ProgressDialog progress = new ProgressDialog(getActivity());
          progress.setMessage(getString(R.string.loading));
          progress.setIndeterminate(true);
          progress.show();
          SmartCommands.execute(new DialogGuardedCommand(getActivity(), AddCategoryDialogFragment.this, "An error occured when add product " + nameValue, R.string.add_user_error_message, progress)
          {
            @Override
            protected void runGuardedDialog()
                throws Exception
            {
              businessObject.category.name = nameValue;
              businessObject.category.label = labelValue;
              businessObject.category.description = descriptionValue;
              businessObject.category.picture_attributes.path = "";
              businessObject.category.picture_attributes.data_picture = "";
              businessObject.category.picture_attributes.description = "";
              businessObject.category.picture_attributes.label = "";
              businessObject.category.picture_attributes.name = "";
              LeelahSystemServices.getInstance().addCategorie(businessObject);
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
