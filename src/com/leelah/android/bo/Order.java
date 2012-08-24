package com.leelah.android.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Order
    implements Serializable
{

  private static final long serialVersionUID = 1L;

  public static final class OrderItem
      implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public int product_id;

    public int quantity;
  }

  public static final class OrderDetails
      implements Serializable
  {

    private static final long serialVersionUID = 1L;

    public String reference;

    public int status;

    public List<OrderItem> order_lines_attributes;

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
