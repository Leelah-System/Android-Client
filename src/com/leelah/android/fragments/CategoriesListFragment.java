package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.Bar;
import com.leelah.android.R;
import com.leelah.android.bo.Category;
import com.leelah.android.bo.Category.CategoryDestails;
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

    public void update(Category businessObject)
    {
      name.setText(businessObject.category.name);
    }

  }

  public static final String CHANGE_CATEGORY = "changeCategory";

  public static final String SELECTED_CATEGORY = CategoriesListFragment.CHANGE_CATEGORY + ".selectedCategory";

  private final static class CategoryWrapper
      extends SimpleBusinessViewWrapper<Category>
  {

    public CategoryWrapper(Category businessObject)
    {
      super(businessObject, 0, R.layout.category_list_item);
    }

    @Override
    protected Object extractNewViewAttributes(Activity activity, View view, Category businessObject)
    {
      return new CategoryViewHolder(view);
    }

    @Override
    protected void updateView(Activity activity, Object businessViewHolder, View view, Category businessObject, int position)
    {
      ((CategoryViewHolder) businessViewHolder).update(businessObject);
    }

    @Override
    public boolean onObjectEvent(Activity activity, Object viewAttributes, View view, Category businessObject, ObjectEvent objectEvent, int position)
    {
      if (objectEvent == ObjectEvent.Clicked)
      {
        activity.sendBroadcast(new Intent(CategoriesListFragment.CHANGE_CATEGORY).putExtra(CategoriesListFragment.SELECTED_CATEGORY, businessObject.category.id));
      }
      return super.onObjectEvent(activity, viewAttributes, view, businessObject, objectEvent, position);
    }

  }

  private boolean firstLaunch = true;

  private boolean fromCache = true;

  @Override
  public void onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects();

    getWrappedListView().getListView().setBackgroundResource(R.drawable.shadow_left);
    getWrappedListView().getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    final List<Category> categories;
    try
    {
      categories = LeelahSystemServices.getInstance().getCategories(fromCache);
    }
    catch (Exception exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }

    final Category allCategory = new Category();
    allCategory.category = new CategoryDestails();
    allCategory.category.id = -1;
    allCategory.category.name = getString(R.string.Category_all_categories);
    wrappers.add(new CategoryWrapper(allCategory));

    for (Category category : categories)
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

  public void onTitleBarRefresh()
  {
    fromCache = false;
    refreshBusinessObjectsAndDisplay(true);
  }
}
