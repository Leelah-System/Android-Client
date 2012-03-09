package com.leelah.android.bo;

import java.util.List;

public class UsersResult
    extends WebServiceResult
{

  private static final long serialVersionUID = -9051598722427037170L;

  public static class Result
  {
    public List<User> users;
  }

  public Result result;
}
