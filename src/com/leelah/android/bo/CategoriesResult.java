package com.leelah.android.bo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.leelah.android.bo.Category.CategoryDetails;

public class CategoriesResult
    extends WebServiceResult
{

  private static final long serialVersionUID = 2550759817643443106L;

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Result
  {
    public List<CategoryDetails> categories;
  }

  public Result result;
}
