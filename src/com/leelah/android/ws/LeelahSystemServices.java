package com.leelah.android.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.JSONException;

import android.content.SharedPreferences;

import com.leelah.android.Constants;
import com.leelah.android.LoginActivity;
import com.leelah.android.bo.CategoriesResult;
import com.leelah.android.bo.Category;
import com.leelah.android.bo.Category.CategoryDetails;
import com.leelah.android.bo.GoogleImage;
import com.leelah.android.bo.GoogleImage.GoogleImageItem;
import com.leelah.android.bo.Order;
import com.leelah.android.bo.Order.OrderDetails;
import com.leelah.android.bo.OrdersResult;
import com.leelah.android.bo.Product;
import com.leelah.android.bo.Product.ProductDetails;
import com.leelah.android.bo.ProductResult;
import com.leelah.android.bo.User;
import com.leelah.android.bo.UserResult;
import com.leelah.android.bo.UsersResult;
import com.leelah.android.bo.WebServiceResult;
import com.smartnsoft.droid4me.cache.Persistence;
import com.smartnsoft.droid4me.cache.Persistence.PersistenceException;
import com.smartnsoft.droid4me.cache.Values.CacheException;
import com.smartnsoft.droid4me.ws.WSUriStreamParser;
import com.smartnsoft.droid4me.ws.WSUriStreamParser.KeysAggregator;
import com.smartnsoft.droid4me.ws.WebServiceCaller;
import com.smartnsoft.droid4me.ws.WithCacheWSUriStreamParser.SimpleIOStreamerSourceKey;
import com.smartnsoft.droid4me.wscache.BackedWSUriStreamParser;

/**
 * @author Jocelyn Girard
 * @since 2011-03-01
 */
public final class LeelahSystemServices
    extends WebServiceCaller
{

  public enum ScopeProduct
  {
    with_stock, without_stock, search_text, search_reference, search_label, search_name, search_description, price
  }

  public final static class LeelahCredentials
  {
    public String login;

    public String password;

    public LeelahCredentials(String login, String password)
    {
      this.login = login;
      this.password = password;
    }

  }

  private static final class AddUserWrapper
  {
    public User user;

    public AddUserWrapper()
    {
      this(new User());
    }

    public AddUserWrapper(User user)
    {
      this.user = user;
    }
  }

  public interface LeelahApiStatusViewer
  {
    public void OnApiStatusSucced(String message);

    public void OnApiStatusError(String message);
  }

  public interface LeelahCredentialsInformations
  {
    public LeelahCredentials getCredentials();

    public String getServerURL();
  }

  private static volatile LeelahSystemServices instance;

  private LeelahCredentialsInformations leelahCredentialsInformations;

  public String token;

  private LeelahApiStatusViewer leelahApiStatusViewer;

  public static LeelahSystemServices getInstance()
  {
    if (instance == null)
    {
      synchronized (LeelahSystemServices.class)
      {
        if (instance == null)
        {
          instance = new LeelahSystemServices();
        }
      }
    }
    return instance;
  }

  private LeelahSystemServices()
  {
  }

  public void setLeelahCredentialsInformations(LeelahCredentialsInformations leelahCredentialsInformations)
  {
    this.leelahCredentialsInformations = leelahCredentialsInformations;
  }

  public void setLeelahApiStatus(LeelahApiStatusViewer leelahApiStatusViewer)
  {
    this.leelahApiStatusViewer = leelahApiStatusViewer;
  }

  @Override
  protected String getUrlEncoding()
  {
    return Constants.WEBSERVICES_HTML_ENCODING;
  }

  private String serializeObjectToJson(Object object)
  {
    try
    {
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(Feature.WRITE_NULL_MAP_VALUES, false);
      objectMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
      final String json = objectMapper.writeValueAsString(object);
      if (log.isDebugEnabled())
      {
        log.debug("Converting Object ( " + object.getClass().getSimpleName() + " )to json String: \"" + json + "\"");
      }
      return json;
    }
    catch (JsonGenerationException exception)
    {
      if (log.isErrorEnabled())
      {
        log.error("Error while generating json !", exception);
      }
    }
    catch (JsonMappingException jsonMappingException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Error while mapping json !", jsonMappingException);
      }
    }
    catch (IOException ioException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Input/Output error while reading InputStream !", ioException);
      }
    }
    return null;
  }

  private Object deserializeJson(InputStream inputStream, Class<?> valueType)
  {
    final ObjectMapper objectMapper = new ObjectMapper();
    try
    {
      final String json = getString(inputStream);
      if (log.isDebugEnabled())
      {
        log.debug("Recieving json: " + json);
      }
      if (json.startsWith("{"))
      {
        return objectMapper.readValue(json, valueType);
      }
    }
    catch (JsonParseException jsonParseException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Error while parsing json !", jsonParseException);
      }
    }
    catch (JsonMappingException jsonMappingException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Error while mapping json !", jsonMappingException);
      }
    }
    catch (IOException ioException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Input/Output error while reading InputStream !", ioException);
      }
    }
    return null;
  }

  private HttpEntity createPostBody(final Object object)
      throws CallException
  {
    final String json = serializeObjectToJson(object);
    if (log.isDebugEnabled())
    {
      log.debug("Json to send after serializeToJson :" + json);
    }
    final StringEntity httpEntity;
    try
    {
      httpEntity = new StringEntity(json, HTTP.UTF_8);
      httpEntity.setContentType("application/json");
    }
    catch (UnsupportedEncodingException exception)
    {
      throw new CallException(exception);
    }
    return httpEntity;
  }

  public boolean hasParameters(SharedPreferences preferences)
  {
    return preferences.contains(LoginActivity.SERVER_ADDRESS) && preferences.contains(LoginActivity.USER_LOGIN) && preferences.contains(LoginActivity.USER_PASSWORD);
  }

  public void checkApiStatus(WebServiceResult webServiceResult)
  {
    if (leelahApiStatusViewer != null)
    {
      if (log.isDebugEnabled())
      {
        log.debug("Checking the webservice result : " + webServiceResult.success);
      }
      if (webServiceResult.success == true)
      {
        leelahApiStatusViewer.OnApiStatusSucced(webServiceResult.msg);
      }
      else
      {
        leelahApiStatusViewer.OnApiStatusError(webServiceResult.msg);
      }
    }
  }

  // https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=Mousse%20au%20chocolat&imgsz=xlarge&imgtype=photo&rsz=4&safe=active

  private final BackedWSUriStreamParser.BackedUriStreamedMap<UserResult, User, JSONException, PersistenceException> authenticateStreamParser = new BackedWSUriStreamParser.BackedUriStreamedMap<UserResult, User, JSONException, PersistenceException>(Persistence.getInstance(0), this)
  {

    public KeysAggregator<User> computeUri(User parameter)
    {
      final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
      postParams.add(new BasicNameValuePair("json", serializeObjectToJson(parameter)));
      postParams.add(new BasicNameValuePair("login", parameter.login));
      postParams.add(new BasicNameValuePair("password", parameter.password));

      UrlEncodedFormEntity entity = null;
      try
      {
        entity = new UrlEncodedFormEntity(postParams);
      }
      catch (UnsupportedEncodingException exception)
      {
        if (log.isErrorEnabled())
        {
          log.error("Problems when initializing Entity with post parameters", exception);
        }
      }

      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + parameter.address, "api/authenticate", null), CallType.Post, entity), parameter);

    }

    public UserResult parse(User parameter, InputStream inputStream)
        throws JSONException
    {
      final UserResult userResult = (UserResult) deserializeJson(inputStream, UserResult.class);
      checkApiStatus(userResult);
      if (userResult.success == false)
      {
        throw new JSONException(userResult.msg);
      }
      return userResult;
    }

  };

  public User authenticate(User u)
      throws CacheException
  {
    final UserResult user = authenticateStreamParser.backed.getValue(false, null, u);
    token = user.result.token;
    return user.result;
  }

  private final BackedWSUriStreamParser.BackedUriStreamedMap<List<GoogleImageItem>, String, JSONException, PersistenceException> requestGoogleImageStreamParser = new BackedWSUriStreamParser.BackedUriStreamedMap<List<GoogleImageItem>, String, JSONException, PersistenceException>(Persistence.getInstance(), this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      final Map<String, String> uriParameters = new HashMap<String, String>();

      uriParameters.put("v", "1.0");
      uriParameters.put("q", parameter);
      uriParameters.put("imgsz", "xlarge");
      uriParameters.put("imgtype", "photo");
      uriParameters.put("safe", "active");

      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("https://ajax.googleapis.com/ajax/services/search/images", null, uriParameters)), parameter);
    }

    public List<GoogleImageItem> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final GoogleImage googleImage = (GoogleImage) deserializeJson(inputStream, GoogleImage.class);
      return googleImage.responseData.results;
    }

  };

  public List<GoogleImageItem> requestGoogleImage(boolean fromCache, String query)
      throws CacheException
  {
    return requestGoogleImageStreamParser.backed.getRetentionValue(fromCache, Constants.RETENTION_PERIOD_IN_MILLISECONDS, null, query);
  }

  private final BackedWSUriStreamParser.BackedUriStreamedValue<List<CategoryDetails>, Void, JSONException, PersistenceException> getCategoriesStreamParser = new BackedWSUriStreamParser.BackedUriStreamedValue<List<CategoryDetails>, Void, JSONException, PersistenceException>(Persistence.getInstance(), this)
  {

    public KeysAggregator<Void> computeUri(Void parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/catalog/categories", null)),
          parameter);
    }

    public List<CategoryDetails> parse(Void parameter, InputStream inputStream)
        throws JSONException
    {
      final CategoriesResult categoriesResult = (CategoriesResult) deserializeJson(inputStream, CategoriesResult.class);
      checkApiStatus(categoriesResult);
      return categoriesResult.result.categories;
    }

  };

  public List<CategoryDetails> getCategories(boolean fromCache)
      throws CacheException
  {
    return getCategoriesStreamParser.backed.getRetentionValue(fromCache, Constants.RETENTION_PERIOD_IN_MILLISECONDS, null, null);
  }

  private final BackedWSUriStreamParser.BackedUriStreamedMap<List<ProductDetails>, String, JSONException, PersistenceException> getProductsCategoryStreamParser = new BackedWSUriStreamParser.BackedUriStreamedMap<List<ProductDetails>, String, JSONException, PersistenceException>(Persistence.getInstance(), this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/catalog/categories/" + parameter,
              null)), parameter);
    }

    public List<ProductDetails> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final ProductResult products = (ProductResult) deserializeJson(inputStream, ProductResult.class);
      checkApiStatus(products);
      return products.result.products;
    }

  };

  public List<ProductDetails> getProductsByCateogry(boolean fromCache, int categoryId)
      throws CacheException
  {
    return getProductsCategoryStreamParser.backed.getRetentionValue(fromCache, Constants.RETENTION_PERIOD_IN_MILLISECONDS, null, Integer.toString(categoryId));
  }

  private final BackedWSUriStreamParser.BackedUriStreamedValue<List<ProductDetails>, Void, JSONException, PersistenceException> getProductsStreamParser = new BackedWSUriStreamParser.BackedUriStreamedValue<List<ProductDetails>, Void, JSONException, PersistenceException>(Persistence.getInstance(), this)
  {

    public KeysAggregator<Void> computeUri(Void parameter)
    {
      final Map<String, String> getParameters = new HashMap<String, String>();
      // getParameters.put("scope", "with_stock");

      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/catalog/products", getParameters)),
          parameter);
    }

    public List<ProductDetails> parse(Void parameter, InputStream inputStream)
        throws JSONException
    {
      final ProductResult products = (ProductResult) deserializeJson(inputStream, ProductResult.class);
      checkApiStatus(products);
      return products.result.products;
    }

  };

  public List<ProductDetails> getProducts(boolean fromCache)
      throws CacheException
  {
    return getProductsStreamParser.backed.getRetentionValue(fromCache, Constants.RETENTION_PERIOD_IN_MILLISECONDS, null, null);
  }

  private final BackedWSUriStreamParser.BackedUriStreamedValue<List<User>, String, JSONException, PersistenceException> getUsersStreamParser = new BackedWSUriStreamParser.BackedUriStreamedValue<List<User>, String, JSONException, PersistenceException>(Persistence.getInstance(), this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/users", null)), null);
    }

    public List<User> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final UsersResult usersResult = (UsersResult) deserializeJson(inputStream, UsersResult.class);
      checkApiStatus(usersResult);
      return usersResult.result.users;
    }

  };

  public List<User> getUsers(boolean fromCache)
      throws CacheException, CallException
  {
    return getUsersStreamParser.backed.getRetentionValue(fromCache, Constants.RETENTION_PERIOD_IN_MILLISECONDS, null, "users");
  }

  private final WSUriStreamParser<Boolean, String, JSONException> deleteProductStreamParser = new WSUriStreamParser<Boolean, String, JSONException>(this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/product/" + parameter, null), CallType.Delete, null),
          parameter);
    }

    public Boolean parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final WebServiceResult products = (WebServiceResult) deserializeJson(inputStream, WebServiceResult.class);
      checkApiStatus(products);
      return products.success;
    }

  };

  public Boolean deleteProduct(String id)
      throws CacheException, CallException
  {
    return deleteProductStreamParser.getValue(id);
  }

  private final WSUriStreamParser<Boolean, String, JSONException> deleteUserStreamParser = new WSUriStreamParser<Boolean, String, JSONException>(this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/user/" + parameter, null), CallType.Delete, null),
          parameter);
    }

    public Boolean parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final WebServiceResult products = (WebServiceResult) deserializeJson(inputStream, WebServiceResult.class);
      checkApiStatus(products);
      return products.success;
    }

  };

  public Boolean deleteUser(String id)
      throws CacheException, CallException
  {
    return deleteUserStreamParser.getValue(id);
  }

  public boolean addCategorie(Category category)
      throws CallException
  {
    final InputStream inputStream = getInputStream(
        computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/catalog/categories", null), CallType.Post,
        createPostBody(category.category));
    final CategoriesResult categoriesResult = (CategoriesResult) deserializeJson(inputStream, CategoriesResult.class);
    checkApiStatus(categoriesResult);
    return categoriesResult.success;
  }

  public boolean addUser(User user)
      throws CallException
  {
    final AddUserWrapper addUserWrapper = new AddUserWrapper(user);
    final InputStream inputStream = getInputStream(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/users", null),
        CallType.Post, createPostBody(addUserWrapper));
    final WebServiceResult wsResult = (WebServiceResult) deserializeJson(inputStream, WebServiceResult.class);
    checkApiStatus(wsResult);
    return wsResult.success;
  }

  public boolean addProduct(Product product)
      throws CallException
  {
    final InputStream inputStream = getInputStream(
        computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/catalog/products", null), CallType.Post,
        createPostBody(product));
    final WebServiceResult wsResult = (WebServiceResult) deserializeJson(inputStream, WebServiceResult.class);
    checkApiStatus(wsResult);
    return wsResult.success;
  }

  public boolean addOrder(Order order)
      throws CallException
  {
    final InputStream inputStream = getInputStream(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/orders", null),
        CallType.Post, createPostBody(order));
    final WebServiceResult wsResult = (WebServiceResult) deserializeJson(inputStream, WebServiceResult.class);
    checkApiStatus(wsResult);
    return wsResult.success;
  }

  private final BackedWSUriStreamParser.BackedUriStreamedValue<List<OrderDetails>, String, JSONException, PersistenceException> getOrdersStreamParser = new BackedWSUriStreamParser.BackedUriStreamedValue<List<OrderDetails>, String, JSONException, PersistenceException>(Persistence.getInstance(), this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/orders", null)), null);
    }

    public List<OrderDetails> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final OrdersResult ordersResult = (OrdersResult) deserializeJson(inputStream, OrdersResult.class);
      checkApiStatus(ordersResult);
      return ordersResult.result.orders;
    }

  };

  public List<OrderDetails> getOrders(boolean fromCache)
      throws CacheException, CallException
  {
    return getOrdersStreamParser.backed.getRetentionValue(fromCache, Constants.RETENTION_PERIOD_IN_MILLISECONDS, null, "orders");
  }

  public boolean updateOrder(OrderDetails order)
      throws CallException
  {
    final InputStream inputStream = getInputStream(
        computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/orders/" + order.id, null), CallType.Put, createPostBody(order));
    final WebServiceResult wsResult = (WebServiceResult) deserializeJson(inputStream, WebServiceResult.class);
    checkApiStatus(wsResult);
    return wsResult.success;
  }
}
