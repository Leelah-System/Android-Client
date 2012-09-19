package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.LeelahSystemApplication;
import com.leelah.android.R;
import com.leelah.android.bar.Bar;
import com.leelah.android.bo.Category.CategoryDetails;
import com.leelah.android.phone.ProductsActivity;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.AppPublics.SendLoadingIntent;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.ObjectEvent;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public class CategoriesListFragment
    extends SmartListViewFragment<Bar.BarAggregate, ListView>
    implements BusinessObjectsRetrievalAsynchronousPolicy, Bar.BarRefreshFeature, SendLoadingIntent
{

  private static final class CategoryViewHolder
  {

    private final TextView name;

    public CategoryViewHolder(View view)
    {
      name = (TextView) view.findViewById(R.id.name);
    }

    public void update(CategoryDetails businessObject)
    {
      name.setText(businessObject.name);
    }

  }

  public static final String CHANGE_CATEGORY = "changeCategory";

  public static final String SELECTED_CATEGORY = CategoriesListFragment.CHANGE_CATEGORY + ".selectedCategory";

  private final static class CategoryWrapper
      extends SimpleBusinessViewWrapper<CategoryDetails>
  {

    public CategoryWrapper(CategoryDetails businessObject)
    {
      super(businessObject, 0, R.layout.category_list_item);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, CategoryDetails businessObject)
    {
      return new CategoryViewHolder(view);
    }

    @Override
    protected void updateView(Activity activity, Object businessViewHolder, View view, CategoryDetails businessObject, int position)
    {
      ((CategoryViewHolder) businessViewHolder).update(businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, CategoryDetails businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        if (LeelahSystemApplication.isTabletMode == true)
        {
          activity.sendBroadcast(new Intent(CategoriesListFragment.CHANGE_CATEGORY).putExtra(CategoriesListFragment.SELECTED_CATEGORY, businessObject.id));
        }
        else
        {
          activity.startActivity(new Intent(activity, ProductsActivity.class).putExtra(CategoriesListFragment.SELECTED_CATEGORY, businessObject.id));
        }
      }

      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

    @Override
    public boolean containsText(CategoryDetails businessObject, String lowerText)
    {
      return businessObject.name.contains(lowerText);
    }

  }

  private boolean firstLaunch = true;

  private boolean fromCache = true;

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    if (LeelahSystemApplication.isTabletMode == true)
    {
      getWrappedListView().getListView().setBackgroundResource(R.drawable.left_mini_book);
      getWrappedListView().getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      getWrappedListView().getListView().setTextFilterEnabled(true);
    }
  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    final List<CategoryDetails> categories;
    try
    {
      categories = LeelahSystemServices.getInstance().getCategories(fromCache);
    }
    catch (Exception exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }

    final CategoryDetails allCategory = new CategoryDetails();
    allCategory.id = -1;
    allCategory.name = getString(R.string.Category_all_categories);
    wrappers.add(new CategoryWrapper(allCategory));

    for (CategoryDetails category : categories)
    {
      wrappers.add(new CategoryWrapper(category));
    }
    fromCache = true;
    return wrappers;
  }

  @Override
  public void onSynchronizeDisplayObjects()
  {
    if (getWrappedListView().getListView().getCount() > 0 && firstLaunch == true)
    {
      firstLaunch = false;
      getWrappedListView().getListView().setSelection(0);
    }
  }

  @Override
  public void onFulfillDisplayObjects()
  {
    super.onFulfillDisplayObjects();
  }

  public void onBarRefresh()
  {
    fromCache = false;
    refreshBusinessObjectsAndDisplay(true);
  }

}
