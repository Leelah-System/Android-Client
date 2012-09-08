package com.leelah.android.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.leelah.android.R;
import com.smartnsoft.droid4me.support.v4.app.SmartFragment;

public class StatisticsFragment
    extends SmartFragment<Void>
{

  private final float[][] data1 = { { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f }, { 10.0f, 8.0f, 12.0f, 12.0f, 6.0f, 5.0f } };

  private final float[][] data2 = { { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 7.0f }, { 8.0f, 4.0f, 11.0f, 10.0f, 5.0f, 2.0f } };

  private final float[][] data3 = { { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 7.0f }, { 3.0f, 1.0f, 3.0f, Float.NaN, 2.0f, 7.0f } };

  private ViewGroup tableRow1;

  private ViewGroup tableRow2;

  private LineGraphView stat1;

  private LineGraphView stat2;

  private LineGraphView stat3;

  private LineGraphView stat4;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.statistics, null);

    tableRow1 = (ViewGroup) view.findViewById(R.id.tableRow1);
    tableRow2 = (ViewGroup) view.findViewById(R.id.tableRow2);

    stat1 = new LineGraphView(getCheckedActivity(), "GraphViewDemo");
    stat2 = new LineGraphView(getCheckedActivity(), "GraphViewDemo");
    stat3 = new LineGraphView(getCheckedActivity(), "GraphViewDemo");
    stat4 = new LineGraphView(getCheckedActivity(), "GraphViewDemo");

    return view;
  }

  public void onRetrieveDisplayObjects()
  {

  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    // Our datasets

  }

  public void onFulfillDisplayObjects()
  {
    // draw sin curve
    int num = 1000;
    GraphViewData[] data = new GraphViewData[num];
    int v = 0;
    for (int i = 0; i < num; i++)
    {
      v += 0.2;
      data[i] = new GraphViewData(i, Math.sin(Math.random() * v));
    }
    // add data
    stat1.addSeries(new GraphViewSeries("OLOL", null, data));
    stat1.setViewPort(2, 10);
    stat1.setScrollable(true);
    stat1.setScalable(true);
    stat1.setBackgroundColor(Color.BLACK);

    tableRow1.addView(stat1);
    tableRow1.addView(stat2);
    tableRow2.addView(stat3);
    tableRow2.addView(stat4);
  }

  public void onSynchronizeDisplayObjects()
  {

  }

}
