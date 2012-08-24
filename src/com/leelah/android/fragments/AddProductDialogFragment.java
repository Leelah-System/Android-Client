package com.leelah.android.fragments;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.leelah.android.R;
import com.leelah.android.bo.Category.CategoryDetails;
import com.leelah.android.bo.Product;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.app.SmartCommands;
import com.smartnsoft.droid4me.app.SmartCommands.DialogGuardedCommand;
import com.smartnsoft.droid4me.cache.Values.CacheException;

public final class AddProductDialogFragment
    extends DialogFragment
{

  private final boolean fromCache = true;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_product, null);
    final EditText name = (EditText) view.findViewById(R.id.name);
    final EditText label = (EditText) view.findViewById(R.id.label);
    final EditText price = (EditText) view.findViewById(R.id.price);
    final EditText stock = (EditText) view.findViewById(R.id.stock);
    final EditText reference = (EditText) view.findViewById(R.id.reference);
    final EditText description = (EditText) view.findViewById(R.id.description);
    final Spinner categoriesSpinner = (Spinner) view.findViewById(R.id.categories);
    try
    {
      final List<CategoryDetails> categories = LeelahSystemServices.getInstance().getCategories(true);
      categoriesSpinner.setAdapter(new ArrayAdapter<CategoryDetails>(getActivity(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, categories)
      {

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
          if (convertView == null)
          {
            convertView = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_spinner_item, null);
          }
          final TextView text = (TextView) convertView.findViewById(android.R.id.text1);
          text.setText(getItem(position).toString());
          return convertView;
        }

      });
    }
    catch (CacheException exception)
    {
      // Do nothing.
      categoriesSpinner.setEnabled(false);
    }
    builder.setView(view);
    builder.setTitle(R.string.add_product_title);
    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        final String nameValue = name.getEditableText().toString();
        final String labelValue = label.getEditableText().toString();
        final String priceValue = price.getEditableText().toString();
        final String stockValue = stock.getEditableText().toString();
        final String referenceValue = reference.getEditableText().toString();
        final String descriptionValue = description.getEditableText().toString();
        final int categoryId = ((CategoryDetails) categoriesSpinner.getSelectedItem()).id;
        if (nameValue.isEmpty())
        {
          name.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (labelValue.isEmpty())
        {
          label.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (priceValue.isEmpty())
        {
          price.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (stockValue.isEmpty())
        {
          stock.setError(getActivity().getString(R.string.add_user_error));
        }
        else if (referenceValue.isEmpty())
        {
          reference.setError(getActivity().getString(R.string.add_user_error));
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
          SmartCommands.execute(new DialogGuardedCommand(getActivity(), AddProductDialogFragment.this, "An error occured when add product " + nameValue, R.string.add_user_error_message, progress)
          {
            @Override
            protected void runGuardedDialog()
                throws Exception
            {
              final Product product = new Product();
              product.product.category_id = categoryId;
              product.product.name = nameValue;
              product.product.label = labelValue;
              product.product.stock = Integer.parseInt(stockValue);
              product.product.price = Float.parseFloat(priceValue);
              product.product.description = descriptionValue;
              product.product.picture_attributes.path = "";
              product.product.picture_attributes.data_picture = "";
              product.product.picture_attributes.description = "";
              product.product.picture_attributes.label = "";
              product.product.picture_attributes.name = "";
              LeelahSystemServices.getInstance().addProduct(product);
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
