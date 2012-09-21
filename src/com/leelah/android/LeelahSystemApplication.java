package com.leelah.android;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.leelah.android.bo.GoogleImage.GoogleImageItem;
import com.leelah.android.ws.LeelahSystemServices;
import com.smartnsoft.droid4me.app.ActivityController;
import com.smartnsoft.droid4me.app.SmartApplication;
import com.smartnsoft.droid4me.app.SmartCommands;
import com.smartnsoft.droid4me.bo.Business.InputAtom;
import com.smartnsoft.droid4me.cache.DbPersistence;
import com.smartnsoft.droid4me.cache.Persistence;
import com.smartnsoft.droid4me.cache.Values.CacheException;
import com.smartnsoft.droid4me.download.BasisDownloadInstructions;
import com.smartnsoft.droid4me.download.BitmapDownloader;
import com.smartnsoft.droid4me.download.DownloadInstructions;
import com.smartnsoft.droid4me.download.DownloadSpecs;

/**
 * The entry point of the application.
 * 
 * @author Jocelyn Girard
 * @since 2012.02.10
 */
public final class LeelahSystemApplication
    extends SmartApplication
{

  public interface BelongsToUserRegistration
  {

  }

  public enum ImageType
  {
    Full, Thumbnail
  }

  public static class CacheInstructions
      extends DownloadInstructions.AbstractInstructions
  {

    @Override
    public InputStream getInputStream(String imageUid, Object imageSpecs, String url, BasisDownloadInstructions.InputStreamDownloadInstructor downloadInstructor)
        throws IOException
    {
      final InputAtom inputAtom = Persistence.getInstance(1).extractInputStream(url);
      return inputAtom == null ? null : inputAtom.inputStream;
    }

    @Override
    public InputStream onInputStreamDownloaded(String imageUid, Object imageSpecs, String url, InputStream inputStream)
    {
      final InputAtom inputAtom = Persistence.getInstance(1).flushInputStream(url, new InputAtom(new Date(), inputStream));
      return inputAtom == null ? null : inputAtom.inputStream;
    }

    @Override
    public boolean onBindBitmap(boolean downloaded, View view, Bitmap bitmap, String bitmapUid, Object imageSpecs)
    {
      final boolean isLandscape = bitmap.getWidth() >= bitmap.getHeight();
      final Bitmap finalBitmap = Bitmap.createBitmap(bitmap, isLandscape ? bitmap.getWidth() / 2 - bitmap.getHeight() / 2 : 0,
          isLandscape ? 0 : bitmap.getHeight() / 2 - bitmap.getWidth() / 2, isLandscape ? bitmap.getWidth() / 2 + bitmap.getHeight() / 2 : bitmap.getWidth(),
          isLandscape ? bitmap.getHeight() : bitmap.getHeight() / 2 + bitmap.getWidth() / 2);

      // Bitmap resizedBitmap = Bitmap.createBitmap(finalBitmap, 0, 0, width, height, matrix, true);

      if (view instanceof ImageView)
      {
        ((ImageView) view).setImageBitmap(finalBitmap);
      }
      return true;

      // return super.onBindBitmap(downloaded, view, finalBitmap, bitmapUid, imageSpecs);
    }

  }

  public final static DownloadInstructions.Instructions CACHE_IMAGE_INSTRUCTIONS = new LeelahSystemApplication.CacheInstructions();

  public static void requestImageAndDisplay(final Handler handler, final String query, final View view, final ImageType imageType)
  {
    SmartCommands.execute(new Runnable()
    {
      public void run()
      {
        try
        {
          final List<GoogleImageItem> images = LeelahSystemServices.getInstance().requestGoogleImage(true, query);
          if (images.size() > 0)
          {
            BitmapDownloader.getInstance().get(view, imageType == ImageType.Thumbnail ? images.get(0).tbUrl : images.get(0).url,
                new DownloadSpecs.TemporaryAndNoImageSpecs(R.drawable.pack, R.drawable.pack), handler, LeelahSystemApplication.CACHE_IMAGE_INSTRUCTIONS);
          }
        }
        catch (CacheException exception)
        {
          if (log.isWarnEnabled())
          {
            log.warn("An error occurs on google image call for retrieve for product with name '" + query + "'");
          }
        }
      }
    });
  }

  public static boolean isTabletMode;

  public static Typeface typeWriterFont;

  @Override
  protected int getLogLevel()
  {
    return Constants.LOG_LEVEL;
  }

  @Override
  protected SmartApplication.I18N getI18N()
  {
    return new SmartApplication.I18N(getText(R.string.problem), getText(R.string.unavailableItem), getText(R.string.unavailableService), getText(R.string.connectivityProblem), getText(R.string.connectivityProblemRetry), getText(R.string.unhandledProblem), getString(R.string.applicationName), getText(R.string.dialogButton_unhandledProblem), getString(R.string.progressDialogMessage_unhandledProblem));
  }

  @Override
  protected String getLogReportRecipient()
  {
    return Constants.REPORT_LOG_RECIPIENT_EMAIL;
  }

  @Override
  public void onCreateCustom()
  {
    super.onCreateCustom();

    final int screenLayout = getResources().getConfiguration().screenLayout;
    isTabletMode = (screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE || (screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE;

    LeelahSystemApplication.typeWriterFont = Typeface.createFromAsset(getAssets(), "typewcond_regular.otf");

    // We initialize the persistence
    final String directoryName = getPackageManager().getApplicationLabel(getApplicationInfo()).toString();
    final File contentsDirectory = new File(getFilesDir(), directoryName);
    Persistence.CACHE_DIRECTORY_PATHS = new String[] { contentsDirectory.getAbsolutePath(), contentsDirectory.getAbsolutePath() };
    DbPersistence.FILE_NAMES = new String[] { DbPersistence.DEFAULT_FILE_NAME, DbPersistence.DEFAULT_FILE_NAME };
    DbPersistence.TABLE_NAMES = new String[] { "data", "images" };
    Persistence.INSTANCES_COUNT = 2;
    Persistence.IMPLEMENTATION_FQN = DbPersistence.class.getName();

    // We set the BitmapDownloader instances
    BitmapDownloader.INSTANCES_COUNT = 1;
    BitmapDownloader.HIGH_LEVEL_MEMORY_WATER_MARK_IN_BYTES = new long[] { 20 * 1024 * 1024 };
    BitmapDownloader.LOW_LEVEL_MEMORY_WATER_MARK_IN_BYTES = new long[] { 10 * 1024 * 1024 };
  }

  @Override
  protected ActivityController.Redirector getActivityRedirector()
  {
    return new ActivityController.Redirector()
    {
      public Intent getRedirection(Activity activity)
      {
        if (LeelahSystemSplashScreenActivity.isInitialized(LeelahSystemSplashScreenActivity.class) == null)
        {
          // We re-direct to the splash screen activity if the application has not been yet initialized
          if (activity instanceof LeelahSystemSplashScreenActivity)
          {
            return null;
          }
          else
          {
            return new Intent(activity, LeelahSystemSplashScreenActivity.class);
          }
        }
        if (LeelahSystemApplication.hasCredentialsInformations(activity) == false)
        {
          if (activity instanceof BelongsToUserRegistration == false)
          {
            return new Intent(activity, LoginActivity.class);
          }
          else
          {
            return null;
          }
        }
        return null;
      }

    };
  }

  protected static boolean hasCredentialsInformations(Activity activity)
  {
    final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
    return preferences.contains(LoginActivity.SERVER_ADDRESS) && preferences.contains(LoginActivity.USER_LOGIN) && preferences.contains(LoginActivity.USER_PASSWORD);
  }

  @Override
  protected ActivityController.Interceptor getInterceptor()
  {
    return new ActivityController.Interceptor()
    {
      public void onLifeCycleEvent(Activity activity, Object component, ActivityController.Interceptor.InterceptorEvent event)
      {
        final Intent homeActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        final com.leelah.android.bar.Bar bar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
          bar = new com.leelah.android.bar.ActionBar(activity instanceof MainActivity ? null : homeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), R.drawable.icon, R.style.Theme_LeelahSystem);
        }
        else
        {
          bar = new com.leelah.android.bar.TitleBar(homeActivityIntent, R.drawable.title_bar_home, R.style.Theme_LeelahSystem);
        }
        bar.onLifeCycleEvent(activity, component, event);
      }
    };
  }

  @Override
  protected ActivityController.ExceptionHandler getExceptionHandler()
  {
    return super.getExceptionHandler();
  }

}
