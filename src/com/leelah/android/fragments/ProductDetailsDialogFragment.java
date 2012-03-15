package com.leelah.android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leelah.android.R;
import com.leelah.android.bo.Product;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewHolder;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;

public class ProductDetailsDialogFragment
    extends DialogFragment
    implements BusinessObjectsRetrievalAsynchronousPolicy
{

  private final class ProductDetailsAttributes
  {

    private final TextView productName;

    private final TextView productDescription;

    private final TextView productReference;

    private final TextView productPrice;

    private final Button addtoCart;

    public ProductDetailsAttributes(View view)
    {
      productName = (TextView) view.findViewById(R.id.productName);
      productDescription = (TextView) view.findViewById(R.id.productDescription);
      productReference = (TextView) view.findViewById(R.id.productReference);
      productPrice = (TextView) view.findViewById(R.id.productQuantity);
      addtoCart = (Button) view.findViewById(R.id.buttonCart);
    }

    public void update(Activity activity, final Product businessObject)
    {
      productName.setText(businessObject.product.name);
      productDescription.setText(businessObject.product.description);
      productReference.setText(businessObject.product.reference);
      productPrice.setText(Float.toString(businessObject.product.price));

      addtoCart.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View view)
        {
          view.getContext().sendBroadcast(new Intent(CartListFragment.ADD_TO_CART).putExtra(CartListFragment.PRODUCT, businessObject.product));
          dismiss();
        }
      });

    }

  }

  private final class ProductDetailsWrapper
      extends SimpleBusinessViewWrapper<Product>
  {

    public ProductDetailsWrapper(Product businessObject)
    {
      super(businessObject, 0, R.layout.product_details);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, Product businessObject)
    {
      return new ProductDetailsAttributes(view);
    }

    @Override
    protected void updateView(Activity activity, Object viewAttributes, View view, Product businessObject, int position)
    {
      ((ProductDetailsAttributes) viewAttributes).update(activity, businessObject);
    }

  }

  enum ActionType
  {
    ViewProduct, ModifyProductCart
  }

  private static final String PRODUCT = "product";

  private static final String ACTION = "action";

  public static ProductDetailsDialogFragment newInstance(ActionType action, Product product)
  {
    final ProductDetailsDialogFragment fragment = new ProductDetailsDialogFragment();
    final Bundle bundle = new Bundle();
    bundle.putSerializable(PRODUCT, product);
    bundle.putSerializable(ACTION, action);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final Product product = (Product) getArguments().getSerializable(PRODUCT);
    final ActionType action = (ActionType) getArguments().getSerializable(ACTION);

    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final BusinessViewHolder<Product> viewHolder = new BusinessViewHolder<Product>(new ProductDetailsWrapper(product));
    builder.setView(viewHolder.getView(getActivity()));
    viewHolder.updateView(getActivity());
    return builder.create();
  }

}
