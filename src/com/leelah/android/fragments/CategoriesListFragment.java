package com.leelah.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.leelah.android.R;
import com.leelah.android.TitleBar;
import com.leelah.android.bo.Category;
import com.smartnsoft.droid4me.framework.SmartAdapters.BusinessViewWrapper;
import com.smartnsoft.droid4me.framework.SmartAdapters.SimpleBusinessViewWrapper;
import com.smartnsoft.droid4me.support.v4.app.SmartListViewFragment;

public class CategoriesListFragment
    extends SmartListViewFragment<TitleBar.TitleBarAggregate, ListView>
{

  private static final class CategoryViewHolder
  {

    public CategoryViewHolder(View view)
    {
    }

    public void update(Category businessObject)
    {

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

    wrappers.add(new CategoryWrapper(new Category()));
    wrappers.add(new CategoryWrapper(new Category()));
    wrappers.add(new CategoryWrapper(new Category()));
    wrappers.add(new CategoryWrapper(new Category()));
    wrappers.add(new CategoryWrapper(new Category()));
    wrappers.add(new CategoryWrapper(new Category()));

    return wrappers;
  }

}
