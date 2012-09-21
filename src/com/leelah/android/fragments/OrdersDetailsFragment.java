package com.leelah.android.fragments;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.leelah.android.bo.Order.OrderDetails;
import com.leelah.android.bo.Order.OrderItemExtented;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListenerProvider;
import com.smartnsoft.droid4me.app.AppPublics.SendLoadingIntent;
import com.smartnsoft.droid4me.app.SmartCommands;
import com.smartnsoft.droid4me.support.v4.app.SmartFragment;

public final class OrdersDetailsFragment
    extends SmartFragment<Bar.BarAggregate>
    implements BusinessObjectsRetrievalAsynchronousPolicy, BroadcastListenerProvider, View.OnClickListener, SendLoadingIntent
{

  public static final String UPDATE_ORDER = "updateOrder";

  public static final String ORDER = "order";

  private OrderDetails order;

  private TextView orderTitle;

  private TextView orderInfos;

  private TextView orderEntreprise;

  private ViewGroup productsLayout;

  private TextView subTotal;

  private TextView tva;

  private TextView totalTotal;

  private Button validButton;

  private Button cancelButton;

  private Button prepareButton;

  private ImageView icon;

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
    subTotal = (TextView) view.findViewById(R.id.subTotal);
    tva = (TextView) view.findViewById(R.id.tva);
    totalTotal = (TextView) view.findViewById(R.id.totalTotal);
    validButton = (Button) view.findViewById(R.id.valid);
    cancelButton = (Button) view.findViewById(R.id.cancel);
    prepareButton = (Button) view.findViewById(R.id.prepare);
    icon = (ImageView) view.findViewById(R.id.icon);

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
    validButton.setOnClickListener(this);
    cancelButton.setOnClickListener(this);
    prepareButton.setOnClickListener(this);
  }

  public void onSynchronizeDisplayObjects()
  {
    if (order != null)
    {
      orderTitle.setText(getString(R.string.Order_orderTitle, order.reference));
      if (order.status == 0)
      {
        icon.setImageResource(R.drawable.asterisk_orange);
      }
      else if (order.status == 1)
      {
        icon.setImageResource(R.drawable.cog);
      }
      else if (order.status == 2)
      {
        icon.setImageResource(R.drawable.accept);
      }
      else if (order.status == -1)
      {
        icon.setImageResource(R.drawable.cancel);
      }
      if (order.user != null)
      {
        orderInfos.setText("le " + order.created_at.toLocaleString() + "\npar " + order.user.login + " (" + order.user.email + ")");
      }
      final DecimalFormat decimalFormat = new DecimalFormat("#.## \u20AC");
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
        tva.setText(decimalFormat.format(product.amount * 0.055f));

        productsLayout.addView(view);
      }
      subTotal.setText(decimalFormat.format(order.amount));
      totalTotal.setText(decimalFormat.format(order.amount));
    }
  }

  @SuppressLint("ParserError")
  public void onClick(final View view)
  {
    if (order != null)
    {
      final ProgressDialog progressDialog = new ProgressDialog(getCheckedActivity());
      progressDialog.setIndeterminate(true);
      progressDialog.setMessage(getString(R.string.loading));
      progressDialog.setCancelable(true);
      progressDialog.show();
      SmartCommands.execute(new SmartCommands.DialogGuardedCommand(getCheckedActivity(), "Error while update the status of Order !", R.string.progressDialogMessage_unhandledProblem, progressDialog)
      {
        @Override
        protected void runGuardedDialog()
            throws Exception
        {
          // 0, 1 ou 2 (Initialisée, En cours, Terminée)
          if (view == validButton)
          {
            order.status = 2;
          }
          else if (view == cancelButton)
          {
            order.status = -1;
          }
          else if (view == prepareButton)
          {
            order.status = 1;
          }
          LeelahSystemServices.getInstance().updateOrder(order);
          refreshBusinessObjectsAndDisplay();
          getCheckedActivity().sendBroadcast(new Intent(OrdersTypeListFragment.REFRESH));
        }
      });
    }
  }
}
