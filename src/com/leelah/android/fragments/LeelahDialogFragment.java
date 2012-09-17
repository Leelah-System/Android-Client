package com.leelah.android.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.leelah.android.bar.Bar;
import com.leelah.android.R;
import com.leelah.android.ws.LeelahSystemServices.LeelahApiStatusViewer;
import com.smartnsoft.droid4me.support.v4.app.SmartDialogFragment;

public class LeelahDialogFragment<T>
    extends SmartDialogFragment<Bar.BarAggregate>
    implements LeelahApiStatusViewer
{

  protected T businessObject;

  protected final boolean fromCache = true;

  public LeelahDialogFragment(T businessObject)
  {
    this.businessObject = businessObject;
  }

  public void OnApiStatusSucced(final String message)
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
      }
    });
  }

  public void OnApiStatusError(String message)
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
