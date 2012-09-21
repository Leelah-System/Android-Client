package com.leelah.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.smartnsoft.droid4me.support.v4.app.SmartFragment;

public final class CalculatorFragment
    extends SmartFragment<Bar.BarAggregate>
{

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.calculator, null);

    return view;
  }

  public void onFulfillDisplayObjects()
  {

  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {

  }

  public void onRetrieveDisplayObjects()
  {

  }

  public void onSynchronizeDisplayObjects()
  {
  }

}
