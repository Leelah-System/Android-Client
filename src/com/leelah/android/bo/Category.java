package com.leelah.android.bo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class Category
{

  // "created_at": "2012-03-15T00:31:10+01:00",
  // "description": null,
  // "id": 1,
  // "label": "Cool",
  // "name": "Yoooo",
  // "sup_category_id": null,
  // "updated_at": "2012-03-15T00:31:10+01:00"

  @JsonIgnoreProperties(ignoreUnknown = true)
  public final static class CategoryDestails
  {
    public int id;

    public String description;

    public String label;

    public String name;
  }

  public CategoryDestails category;
}
