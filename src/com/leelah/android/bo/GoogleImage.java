package com.leelah.android.bo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public final class GoogleImage
{

  @JsonIgnoreProperties(ignoreUnknown = true)
  public final static class GoogleImageItem
  {
    public int width;

    public int height;

    public String imageId;

    public int tbWidth;

    public int tbHeight;

    public String unescapedUrl;

    public String url;

    public String tbUrl;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public final static class ResponseData
  {
    public List<GoogleImageItem> results;
  }

  public int responseStatus;

  public String responseDetails;

  public ResponseData responseData;
}
