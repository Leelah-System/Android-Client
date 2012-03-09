package com.leelah.android.bo;

import java.util.List;

public class ProductResult
    extends WebServiceResult
{

  private static final long serialVersionUID = 2550759817643443106L;

  public static class Result
  {
    public List<Product> products;
  }

  public Result result;
}
