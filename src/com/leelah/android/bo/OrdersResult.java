package com.leelah.android.bo;

import java.util.List;

import com.leelah.android.bo.Order.OrderDetails;

public class OrdersResult
    extends WebServiceResult
{

  public static final class Result
  {
    public List<OrderDetails> orders;
  }

  private static final long serialVersionUID = -1L;

  public Result result;
}
