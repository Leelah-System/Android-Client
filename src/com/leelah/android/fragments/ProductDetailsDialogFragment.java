package com.leelah.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.leelah.android.R;
import com.leelah.android.bo.Product;

public class ProductDetailsDialogFragment
    extends DialogFragment
{

  public static final String PRODUCT = "product";

  public static ProductDetailsDialogFragment newInstance(Product product)
  {
    final ProductDetailsDialogFragment fragment = new ProductDetailsDialogFragment();
    final Bundle bundle = new Bundle();
    bundle.putSerializable(PRODUCT, product);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final Product product = (Product) getArguments().getSerializable(PRODUCT);
    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.product_details, null);
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(view);
    final AlertDialog dialog = builder.create();
    return dialog;
  }

  // @Override
  // public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  // {
  // final View view = inflater.inflate(R.layout.product_details, container);
  // return view;
  // }

}
