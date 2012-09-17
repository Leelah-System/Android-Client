package com.leelah.android.fragments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.leelah.android.bar.Bar.BarRefreshFeature;
import com.leelah.android.bo.Order.OrderDetails;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public final class OrdersTypeListFragment
    extends SmartListViewFragment<Bar.BarAggregate, ListView>
    implements BusinessObjectsRetrievalAsynchronousPolicy, BarRefreshFeature
{

  private final static class OrderAttributes
  {

    private final TextView text;

    private final TextView description;

    private final ImageView icon;

    public OrderAttributes(View view)
    {
      text = (TextView) view.findViewById(R.id.text1);
      description = (TextView) view.findViewById(R.id.description);
      icon = (ImageView) view.findViewById(R.id.icon);
    }

    public void update(OrderDetails businessObject)
    {
      final DecimalFormat decimalFormat = new DecimalFormat("#.## Euros");
      text.setText(text.getResources().getString(R.string.Order_orderItemTitle, businessObject.reference.replaceAll("-", "")));
      description.setText(description.getResources().getString(R.string.Order_orderItemDetails, decimalFormat.format(businessObject.amount),
          businessObject.created_at.toLocaleString()));
    }

  }

  private final static class OrderWrapper
      extends SimpleBusinessViewWrapper<OrderDetails>
  {

    public OrderWrapper(OrderDetails businessObject)
    {
      super(businessObject, 0, R.layout.order_item);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, OrderDetails businessObject)
    {
      return new OrderAttributes(view);
    }

    @Override
    protected void updateView(Activity activity, Object viewAttributes, View view, OrderDetails businessObject, int position)
    {
      ((OrderAttributes) viewAttributes).update(businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, OrderDetails businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        activity.sendBroadcast(new Intent(OrdersDetailsFragment.UPDATE_ORDER).putExtra(OrdersDetailsFragment.ORDER, businessObject));
      }
      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

  }

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    getWrappedListView().getListView().setBackgroundResource(R.drawable.ticket_de_caisse);
    getWrappedListView().getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<OrderDetails> orders;
    try
    {
      orders = LeelahSystemServices.getInstance().getOrders();
    }
    catch (Exception exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }
    if (orders == null)
    {
      throw new BusinessObjectUnavailableException("Orders business object is null !");
    }

    Collections.sort(orders, new Comparator<OrderDetails>()
    {
      public int compare(OrderDetails object1, OrderDetails object2)
      {
        return object2.created_at.compareTo(object1.created_at);
      }
    });

    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();
    for (OrderDetails order : orders)
    {
      wrappers.add(new OrderWrapper(order));
    }
    return wrappers;
  }

  public void onBarRefresh()
  {
    refreshBusinessObjectsAndDisplay();
  }
}
