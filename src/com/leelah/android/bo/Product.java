package com.leelah.android.bo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.leelah.android.bo.Category.PictureAttributes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product
    implements Serializable
{

  private static final long serialVersionUID = 1L;

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ProductDetails
      implements Serializable
  {

    private static final long serialVersionUID = -5310872872120289664L;

    public String id;

    public String label;

    public String name;

    public String description;

    public String reference;

    public float price;

    public int stock;

    public int category_id;

    public PictureAttributes picture_attributes;

    public ProductDetails()
    {
      picture_attributes = new PictureAttributes();
    }
  }

  public ProductDetails product;

  public Product()
  {
    product = new ProductDetails();
  }

}
