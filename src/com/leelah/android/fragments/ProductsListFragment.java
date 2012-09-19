package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.leelah.android.CaisseActivity;
import com.leelah.android.LeelahSystemApplication;
import com.leelah.android.LeelahSystemApplication.ImageType;
import com.leelah.android.MainActivity;
import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.leelah.android.bo.Product.ProductDetails;
import com.leelah.android.fragments.CartListFragment.CartProduct;
import com.leelah.android.fragments.ProductDetailsDialogFragment.ActionType;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListenerProvider;
import com.smartnsoft.droid4me.app.AppPublics.SendLoadingIntent;
import com.smartnsoft.droid4me.app.SmartCommands;
import com.smartnsoft.droid4me.framework.Commands;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.menu.MenuCommand;

public class ProductsListFragment
    extends SmartGridViewFragment<Bar.BarAggregate, GridView>
    implements BusinessObjectsRetrievalAsynchronousPolicy, SendLoadingIntent, BroadcastListenerProvider
{

  private static final class ProductAttributes
  {

    private final ImageView image;

    private final TextView productName;

    private final TextView productDescription;

    public ProductAttributes(View view)
    {
      image = (ImageView) view.findViewById(R.id.image);
      productName = (TextView) view.findViewById(R.id.productName);
      productDescription = (TextView) view.findViewById(R.id.description);
    }

    public void update(final Handler handler, final ProductDetails businessObject)
    {
      productName.setText(businessObject.name);
      productDescription.setText(businessObject.description);
      LeelahSystemApplication.requestImageAndDisplay(handler, businessObject.name, image, ImageType.Thumbnail);
    }

  }

  private final class ProductWrapper
      extends SimpleBusinessViewWrapper<ProductDetails>
  {

    public ProductWrapper(ProductDetails businessObject)
    {
      super(businessObject, 0, getProductLayout());
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, ProductDetails businessObject)
    {
      return new ProductAttributes(view);
    }

    @Override
    protected void updateView(Activity activity, Object viewAttributes, View view, ProductDetails businessObject, int position)
    {
      ((ProductAttributes) viewAttributes).update(getHandler(), businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, ProductDetails businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        if (activity instanceof CaisseActivity)
        {
          final Intent intent = new Intent(CartListFragment.ADD_TO_CART).putExtra(CartListFragment.PRODUCT, businessObject).putExtra(CartListFragment.QUANTITY,
              1);
          if (businessObject instanceof CartProduct)
          {
            intent.putExtra(ProductDetailsDialogFragment.IS_UPDATE, true);
          }
          activity.sendBroadcast(intent);
        }
        else
        {
          showProductDialog(getFragmentManager(), businessObject);
        }
      }
      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

    @Override
    public boolean containsText(ProductDetails businessObject, String lowerText)
    {
      return businessObject.name.contains(lowerText);
    }

    @Override
    public List<MenuCommand<ProductDetails>> getMenuCommands(final Activity activity, ProductDetails businessObject)
    {
      final List<MenuCommand<ProductDetails>> menus = new ArrayList<MenuCommand<ProductDetails>>();

      menus.add(new MenuCommand<ProductDetails>("Editer", '1', 'e', new Commands.Executable<ProductDetails>()
      {

        public boolean isEnabled(final ProductDetails businessObject)
        {
          return isAdmin;
        }

        public boolean isVisible(final ProductDetails businessObject)
        {
          return isAdmin;
        }

        public void run(final ProductDetails businessObject)
        {

        }
      }));

      menus.add(new MenuCommand<ProductDetails>("Supprimer", '2', 's', new Commands.Executable<ProductDetails>()
      {

        public boolean isEnabled(final ProductDetails businessObject)
        {
          return isAdmin;
        }

        public boolean isVisible(final ProductDetails businessObject)
        {
          return isAdmin;
        }

        public void run(final ProductDetails businessObject)
        {
          final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
          builder.setTitle(businessObject.name);
          builder.setMessage("Supprimer le produit ?");
          builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialog, int which)
            {
              final ProgressDialog progressDialog = new ProgressDialog(activity);
              progressDialog.setIndeterminate(true);
              progressDialog.setMessage(getString(R.string.loading));
              progressDialog.setCancelable(true);
              progressDialog.show();
              SmartCommands.execute(new SmartCommands.DialogGuardedCommand(activity, "Error while update the status of Order !", R.string.progressDialogMessage_unhandledProblem, progressDialog)
              {
                @Override
                protected void runGuardedDialog()
                    throws Exception
                {
                  LeelahSystemServices.getInstance().deleteProduct(businessObject.id);
                  refreshBusinessObjectsAndDisplay();
                }
              });
            }
          });
          builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialog, int which)
            {

            }
          });
          builder.show();
        }
      }));

      return menus;
    }
  }

  private int categoryId = -1;

  private boolean fromCache = true;

  private boolean isAdmin;

  public BroadcastListener getBroadcastListener()
  {
    return new BroadcastListener()
    {
      public IntentFilter getIntentFilter()
      {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CategoriesListFragment.CHANGE_CATEGORY);
        return intentFilter;
      }

      public void onReceive(Intent intent)
      {
        if (CategoriesListFragment.CHANGE_CATEGORY.equals(intent.getAction()))
        {
          categoryId = intent.getIntExtra(CategoriesListFragment.SELECTED_CATEGORY, -1);
          refreshBusinessObjectsAndDisplay(true);
        }
      }

    };
  }

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    isAdmin = getCheckedActivity().getIntent().getBooleanExtra(MainActivity.IS_ADMIN, false);

    if (LeelahSystemApplication.isTabletMode == true)
    {
      getWrappedListView().getListView().setBackgroundResource(isAdmin == false ? R.drawable.right_book : R.drawable.clipboard);
    }
    getWrappedListView().getListView().setTextFilterEnabled(true);
    categoryId = getCheckedActivity().getIntent().getIntExtra(CategoriesListFragment.SELECTED_CATEGORY, -1);

  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    final List<ProductDetails> products;
    try
    {
      products = categoryId > -1 ? LeelahSystemServices.getInstance().getProductsByCateogry(fromCache, categoryId)
          : LeelahSystemServices.getInstance().getProducts(fromCache);
    }
    catch (Exception exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }

    if (products != null)
    {
      for (ProductDetails product : products)
      {
        if (product != null)
        {
          wrappers.add(new ProductWrapper(product));
        }
      }
    }
    fromCache = true;
    return wrappers;
  }

  @Override
  public void onFulfillDisplayObjects()
  {
    super.onFulfillDisplayObjects();
    final int paddingSize = getResources().getDimensionPixelSize(R.dimen.defaultPadding);
    getWrappedListView().getListView().setNumColumns(GridView.AUTO_FIT);
    getWrappedListView().getListView().setColumnWidth(getResources().getDimensionPixelSize(R.dimen.gridColumnWidth));
    // getWrappedListView().getListView().setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
    if (LeelahSystemApplication.isTabletMode == true)
    {
      getWrappedListView().getListView().setVerticalSpacing(paddingSize);
      getWrappedListView().getListView().setHorizontalSpacing(paddingSize);
    }
    getWrappedListView().getListView().setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
  }

  public void onBarRefresh()
  {
    fromCache = false;
    refreshBusinessObjectsAndDisplay(true);
  }

  static void showProductDialog(FragmentManager fragmentManager, ProductDetails businessObject)
  {
    final ProductDetailsDialogFragment productDetailsDialogFragment = ProductDetailsDialogFragment.newInstance(ActionType.ViewProduct, businessObject);
    productDetailsDialogFragment.show(fragmentManager, "dialog");
  }

  protected int getProductLayout()
  {
    return isAdmin == true ? R.layout.caisse_product_list_item : R.layout.product_list_item;
  }

}
