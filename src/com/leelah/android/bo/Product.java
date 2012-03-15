package com.leelah.android.bo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class Product
    implements Serializable
{

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ProductDetails
  {

    private static final long serialVersionUID = -5310872872120289664L;

    public String id;

    public String label;

    public String name;

    public String description;

    public String reference;

    public float price;
  }

  public ProductDetails product;
}
