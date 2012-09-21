package com.leelah.android.ws;

import java.math.BigDecimal;
import java.util.Locale;

import android.content.Context;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

public class Paypal
{

  /**
   * The PayPal object.
   */
  private static PayPal instance;

  /**
   * @param context
   *          the context
   * @return the PayPal object (initialize if necessary)
   */
  public static PayPal getInstance(Context context)
  {
    if (instance == null)
    {
      instance = PayPal.initWithAppID(context, "APP-80W284485P519543T", PayPal.ENV_SANDBOX);
      instance.setShippingEnabled(false);
      instance.setLanguage(Locale.getDefault().toString()); // en_CA / fr_CA
      // TODO check that language is PayPal valid
    }
    return instance;
  }

  /**
   * The PayPal dialog request code.
   */
  public static final int paypalRequestCode = 10;

  /**
   * @param context
   *          the context
   * @param devName
   *          the developer name
   * @param devEmail
   *          the developer email
   * @param amount
   *          the amount
   * @return the PayPal payment object
   */
  public static PayPalPayment getNewPayPalPayment(Context context, String devName, String devEmail, double amount)
  {
    final PayPalPayment newPayment = new PayPalPayment();
    newPayment.setSubtotal(new BigDecimal(amount)); // 10.00
    newPayment.setCurrencyType("EUR"); // Canadian Dollars
    newPayment.setMerchantName(devName);
    newPayment.setRecipient(devEmail);
    return newPayment;
  }

  /**
   * @param context
   *          the context
   * @return the PayPal checkout button
   */
  public static CheckoutButton getCheckoutButton(Context context)
  {
    return getInstance(context).getCheckoutButton(context, PayPal.BUTTON_152x33, CheckoutButton.TEXT_PAY);
  }

}