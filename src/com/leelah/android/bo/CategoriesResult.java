package com.leelah.android.bo;

import java.util.List;

import com.leelah.android.bo.Category.CategoryDetails;

public class CategoriesResult
    extends WebServiceResult
{

  private static final long serialVersionUID = 2550759817643443106L;

  public static class Result
  {
    public List<CategoryDetails> categories;
  }

  public Result result;
}
