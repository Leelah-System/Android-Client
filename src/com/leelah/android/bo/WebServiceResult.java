package com.leelah.android.bo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Jocelyn Girard
 * @since 25-01-2011
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebServiceResult
    implements Serializable
{
  private static final long serialVersionUID = -8006853016023208544L;

  public boolean success;

  public String msg;

  @Override
  public String toString()
  {
    return "WebServiceResult [success=" + success + ", msg=" + msg + "]";
  }

}
