package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.android.Bar;
import com.leelah.android.R;
import com.leelah.android.bo.Category;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public class CategoriesListFragment
    extends SmartListViewFragment<Bar.BarAggregate, ListView>
    implements BusinessObjectsRetrievalAsynchronousPolicy
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

  }

  public List<? extends BusinessViewWrapper<?>> retrieveBusinessObjectsList()
      throws BusinessObjectUnavailableException
  {
    final List<BusinessViewWrapper<?>> wrappers = new ArrayList<BusinessViewWrapper<?>>();

    final List<Category> categories;
    try
    {
      categories = LeelahSystemServices.getInstance().getCategories("fdfdsa");
    }
    catch (Exception exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }

    for (Category category : categories)
    {
      wrappers.add(new CategoryWrapper(category));
    }

    return wrappers;
  }
}
