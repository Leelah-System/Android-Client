package com.leelah.android.fragments;

import java.text.DecimalFormat;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.leelah.android.bo.Order.OrderDetails;
import com.leelah.android.bo.Order.OrderItemExtented;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListenerProvider;
import com.smartnsoft.droid4me.support.v4.app.SmartFragment;

public final class OrdersDetailsFragment
    extends SmartFragment<Bar.BarAggregate>
    implements BusinessObjectsRetrievalAsynchronousPolicy, BroadcastListenerProvider
{

  public static final String UPDATE_ORDER = "updateOrder";

  public static final String ORDER = "order";

  private OrderDetails order;

  private TextView orderTitle;

  private TextView orderInfos;

  private TextView orderEntreprise;

  private ViewGroup productsLayout;

  public BroadcastListener getBroadcastListener()
  {
    return new BroadcastListener()
    {

      public IntentFilter getIntentFilter()
      {
        return new IntentFilter(OrdersDetailsFragment.UPDATE_ORDER);
      }

      public void onReceive(Intent intent)
      {
        if (intent.getAction().equals(OrdersDetailsFragment.UPDATE_ORDER))
        {
          if (intent.hasExtra(OrdersDetailsFragment.ORDER))
          {
            order = (OrderDetails) intent.getSerializableExtra(OrdersDetailsFragment.ORDER);
            refreshBusinessObjectsAndDisplay();
          }
        }
      }
    };
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.order_details, null);

    orderTitle = (TextView) view.findViewById(R.id.orderTitle);
    orderInfos = (TextView) view.findViewById(R.id.orderInfos);
    orderEntreprise = (TextView) view.findViewById(R.id.orderEntreprise);
    productsLayout = (ViewGroup) view.findViewById(R.id.productsLayout);

    return view;
  }

  public void onRetrieveDisplayObjects()
  {
  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {

  }

  public void onFulfillDisplayObjects()
  {

  }

  public void onSynchronizeDisplayObjects()
  {
    if (order != null)
    {
      orderTitle.setText(getString(R.string.Order_orderTitle, order.reference.replaceAll("-", "")));
      if (order.user != null)
      {
        orderInfos.setText("le " + order.created_at.toLocaleString() + "\npar " + order.user.login + " (" + order.user.email + ")");
      }
      final DecimalFormat decimalFormat = new DecimalFormat("#.## Euros");
      productsLayout.removeAllViews();
      for (OrderItemExtented product : order.order_lines)
      {
        final View view = LayoutInflater.from(getCheckedActivity()).inflate(R.layout.order_details_product, null);
        final TextView name = (TextView) view.findViewById(R.id.productName);
        final TextView price = (TextView) view.findViewById(R.id.productPrice);
        final TextView quantity = (TextView) view.findViewById(R.id.productQuantity);
        final TextView total = (TextView) view.findViewById(R.id.productTotal);

        name.setText(product.product.name);
        price.setText(decimalFormat.format(product.product.price));
        total.setText(decimalFormat.format(product.amount));
        quantity.setText(Integer.toString(product.quantity));

        productsLayout.addView(view);
      }
    }
  }

}
