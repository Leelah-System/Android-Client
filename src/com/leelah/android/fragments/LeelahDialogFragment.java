package com.leelah.android.fragments;

import android.support.v4.app.DialogFragment;

public class LeelahDialogFragment<T>
    extends DialogFragment
{

  protected T businessObject;

  protected final boolean fromCache = true;

  public LeelahDialogFragment(T businessObject)
  {
    this.businessObject = businessObject;
  }

}
