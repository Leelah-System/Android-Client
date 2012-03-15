package com.leelah.android.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
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
import com.leelah.android.bo.Product;
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

  public interface LeelahCredentialsInformations
  {
    public LeelahCredentials getCredentials();

    public String getServerURL();
  }

  private static volatile LeelahSystemServices instance;

  private LeelahCredentialsInformations leelahCredentialsInformations;

  private String token;

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

  public boolean hasParameters(SharedPreferences preferences)
  {
    return preferences.contains(LoginActivity.SERVER_ADDRESS) && preferences.contains(LoginActivity.USER_LOGIN) && preferences.contains(LoginActivity.USER_PASSWORD);
  }

  private final WSUriStreamParser<List<Category>, String, JSONException> getCategoriesStreamParser = new WSUriStreamParser<List<Category>, String, JSONException>(this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/category", null)), parameter);
    }

    public List<Category> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final CategoriesResult categoriesResult = (CategoriesResult) deserializeJson(inputStream, CategoriesResult.class);
      return categoriesResult.result.categories;
    }

  };

  public List<Category> getCategories(String address)
      throws CacheException, CallException
  {
    return getCategoriesStreamParser.getValue(address);
  }

  private final WSUriStreamParser<List<Product>, String, JSONException> getProductsStreamParser = new WSUriStreamParser<List<Product>, String, JSONException>(this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/product", null)), parameter);
    }

    public List<Product> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final ProductResult products = (ProductResult) deserializeJson(inputStream, ProductResult.class);
      return products.result.products;
    }

  };

  public List<Product> getProducts(String address)
      throws CacheException, CallException
  {
    return getProductsStreamParser.getValue(address);
  }

  private final WSUriStreamParser<List<User>, String, JSONException> getUsersStreamParser = new WSUriStreamParser<List<User>, String, JSONException>(this)
  {

    public KeysAggregator<String> computeUri(String parameter)
    {
      return SimpleIOStreamerSourceKey.fromUriStreamerSourceKey(
          new HttpCallTypeAndBody(computeUri("http://" + leelahCredentialsInformations.getServerURL(), "api/" + token + "/users", null)), parameter);
    }

    public List<User> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final UsersResult usersResult = (UsersResult) deserializeJson(inputStream, UsersResult.class);
      return usersResult.result.users;
    }

  };

  public List<User> getUsers(String address)
      throws CacheException, CallException
  {
    return getUsersStreamParser.getValue(address);
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
      return products.success;
    }

  };

  public Boolean deleteUser(String id)
      throws CacheException, CallException
  {
    return deleteUserStreamParser.getValue(id);
  }

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
    token = user.result.user.token;
    return user.result.user;
  }

}
