package com.leelah.android.bo;

import java.util.List;

public class CategoriesResult
    extends WebServiceResult
{

  private static final long serialVersionUID = 2550759817643443106L;

  public static class Result
  {
    public List<Category> categories;
  }

  public Result result;
}
