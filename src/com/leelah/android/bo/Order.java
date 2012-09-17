package com.leelah.android.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.leelah.android.bo.Product.ProductDetails;

public final class Order
    implements Serializable
{

  private static final long serialVersionUID = 1L;

  public static class OrderItem
      implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public int product_id;

    public int quantity;

  }

  public static final class OrderItemExtented
      extends OrderItem
  {
    public float amount;

    public Date created_at;

    public Date updated_at;

    public ProductDetails product;

    public int order_id;

    public int id;
  }

  public static final class OrderDetails
      implements Serializable
  {

    private static final long serialVersionUID = 1L;

    public int id;

    public int user_id;

    public String reference;

    public float amount;

    public Date created_at;

    public Date updated_at;

    public int status;

    public String status_to_string;

    public List<OrderItem> order_lines_attributes;

    public List<OrderItemExtented> order_lines;

    public User user;

    public OrderDetails()
    {
      order_lines_attributes = new ArrayList<Order.OrderItem>();
    }

  }

  public OrderDetails order;

  public Order()
  {
    order = new OrderDetails();
  }

}
