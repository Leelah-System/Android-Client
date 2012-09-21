package com.leelah.android.bo;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.leelah.android.bo.Product.ProductDetails;

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
  public final static class PictureAttributes
      implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public String name;

    public String label;

    public String description;

    public String data_picture;

    public String path;

  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public final static class CategoryDetails
      implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public Integer id;

    public String description;

    public String label;

    public String name;

    public PictureAttributes picture_attributes;

    public List<ProductDetails> products;

    public String color;

    public CategoryDetails()
    {
      picture_attributes = new PictureAttributes();
    }

    @Override
    public String toString()
    {
      return name;
    }

  }

  public CategoryDetails category;

  public Category()
  {
    category = new CategoryDetails();
  }

}
