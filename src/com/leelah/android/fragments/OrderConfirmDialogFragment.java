package com.leelah.android.fragments;

import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leelah.android.LeelahSystemApplication;
import com.leelah.android.R;
import com.leelah.android.bo.Order.OrderDetails;
import com.leelah.android.bo.Order.OrderItemExtented;
import com.leelah.android.ws.Paypal;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPalPayment;

public final class OrderConfirmDialogFragment
    extends LeelahDialogFragment<OrderDetails>
    implements View.OnClickListener
{

  private TextView orderTitle;

  private TextView orderInfos;

  private TextView orderEntreprise;

  private ViewGroup productsLayout;

  private TextView subTotal;

  private TextView tva;

  private TextView totalTotal;

  private Button paymentEspece;

  private Button paymentCards;

  private Button paymentPaypal;

  private ImageView icon;

  private LinearLayout paymentLayout;

  public class LoadPayPalButtonTask
      extends AsyncTask<String, String, CheckoutButton>
  {

    /**
     * The context.
     */
    private final Context context;

    /**
     * Default constructor.
     * 
     * @param context
     *          the context
     */
    public LoadPayPalButtonTask(Context context)
    {
      this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CheckoutButton doInBackground(String... params)
    {
      log.debug("PayPal: Create button !");
      return Paypal.getCheckoutButton(this.context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(CheckoutButton result)
    {
      log.debug("PayPal: display button !");
      super.onPostExecute(result);
      showPayPalButtonDialog(result);
    }
  }

  public OrderConfirmDialogFragment(OrderDetails orderDetails)
  {
    super(orderDetails);
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    new LoadPayPalButtonTask(getCheckedActivity()).execute();
  }

  private void showPayPalButtonDialog(final CheckoutButton button)
  {
    button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    paymentLayout.addView(button);
    button.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View view)
      {
        final float amount = businessObject.amount;
        final String devName = "Leelah-System"; // TODO when multiple devs
        final String devEmail = "leelah_1347965058_biz@smartnsoft.com";// TODO when multiple devs
        final PayPalPayment payPalPayment = Paypal.getNewPayPalPayment(getCheckedActivity(), devName, devEmail, amount);
        final Intent paypalIntent = Paypal.getInstance(getCheckedActivity()).checkout(payPalPayment, getCheckedActivity());
        getCheckedActivity().startActivityForResult(paypalIntent, Paypal.paypalRequestCode);
      }
    });
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_order_details, null);

    orderTitle = (TextView) view.findViewById(R.id.orderTitle);
    orderInfos = (TextView) view.findViewById(R.id.orderInfos);
    orderInfos.setTypeface(LeelahSystemApplication.typeWriterFont);
    productsLayout = (ViewGroup) view.findViewById(R.id.productsLayout);
    subTotal = (TextView) view.findViewById(R.id.subTotal);
    subTotal.setTypeface(LeelahSystemApplication.typeWriterFont);
    tva = (TextView) view.findViewById(R.id.tva);
    tva.setTypeface(LeelahSystemApplication.typeWriterFont);
    totalTotal = (TextView) view.findViewById(R.id.totalTotal);
    totalTotal.setTypeface(LeelahSystemApplication.typeWriterFont);
    paymentEspece = (Button) view.findViewById(R.id.paymentEspece);
    paymentCards = (Button) view.findViewById(R.id.paymentCards);
    paymentPaypal = (Button) view.findViewById(R.id.paymentPaypal);
    icon = (ImageView) view.findViewById(R.id.icon);
    paymentLayout = (LinearLayout) view.findViewById(R.id.paymentLayout);

    paymentCards.setOnClickListener(this);
    paymentEspece.setOnClickListener(this);
    paymentPaypal.setOnClickListener(this);

    final OrderDetails order = businessObject;

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
        final View productView = LayoutInflater.from(getCheckedActivity()).inflate(R.layout.order_details_product, null);
        final TextView name = (TextView) productView.findViewById(R.id.productName);
        final TextView price = (TextView) productView.findViewById(R.id.productPrice);
        final TextView quantity = (TextView) productView.findViewById(R.id.productQuantity);
        final TextView total = (TextView) productView.findViewById(R.id.productTotal);

        name.setTypeface(LeelahSystemApplication.typeWriterFont);
        price.setTypeface(LeelahSystemApplication.typeWriterFont);
        quantity.setTypeface(LeelahSystemApplication.typeWriterFont);
        total.setTypeface(LeelahSystemApplication.typeWriterFont);

        name.setText(product.product.name);
        price.setText(decimalFormat.format(product.product.price));
        total.setText(decimalFormat.format(product.amount));
        quantity.setText(Integer.toString(product.quantity));
        tva.setText(decimalFormat.format(product.amount * 0.055f));

        productsLayout.addView(productView);
      }
      subTotal.setText(decimalFormat.format(order.amount));
      totalTotal.setText(decimalFormat.format(order.amount));
    }

    builder.setView(view);
    builder.setTitle("Paiement de la commande");
    return builder.create();
  }

  public void onClick(View view)
  {
    if (view == paymentPaypal)
    {

    }
    else if (view == paymentEspece || view == paymentCards)
    {
      Toast.makeText(getCheckedActivity(), "Terminal de paiement non connecté !", Toast.LENGTH_LONG).show();
    }
  }
}
