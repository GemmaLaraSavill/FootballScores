package nanodegree.gemma.footballscores.widgets;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import nanodegree.gemma.footballscores.R;
import nanodegree.gemma.footballscores.DatabaseContract;
import nanodegree.gemma.footballscores.MainActivity;
import nanodegree.gemma.footballscores.Utilies;

/**
 * Created by Gemma S. Lara Savill on 01/03/2016.
 */
public class TodaysMatchWidgetIntentService extends IntentService {

private static final String[] MATCH_COLUMNS = {
        DatabaseContract.scores_table.HOME_COL,
        DatabaseContract.scores_table.AWAY_COL,
        DatabaseContract.scores_table.HOME_GOALS_COL,
        DatabaseContract.scores_table.AWAY_GOALS_COL,
        DatabaseContract.scores_table.TIME_COL

};
// these indices must match the projection
    public static final int COL_HOME = 0;
    public static final int COL_AWAY = 1;
    public static final int COL_HOME_GOALS = 2;
    public static final int COL_AWAY_GOALS = 3;
    public static final int COL_MATCHTIME = 4;

    private static final String LOG = "Widget";
    private String homeTeam;
    private String awayTeam;
    private String score;
    private Cursor cursor;
    private String time;
    private String description;
    private int homeLogo;
    private int awayLogo;

    public TodaysMatchWidgetIntentService() {
        super("TodaysMatchWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
               TodaysMatchWidgetProvider.class));

        // Get the latest match from the ContentProvider
        String[] date = new String[1];
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        // get the next match in the next three days for the widget
        boolean foundMatch = false;
        for (int i = 0;i < 2;i++) {
            Date widgetDate = new Date(System.currentTimeMillis()+((i)*86400000));
            date[0] = mformat.format(widgetDate);
            Uri matchesUri = DatabaseContract.scores_table.buildScoreWithDate();
            cursor = getContentResolver().query(matchesUri, MATCH_COLUMNS, null, date, null);
            if (cursor.getCount()>0) {
                // got a match!
                foundMatch = true;
//                Log.v(LOG, "got a match on "+date[0]);
                break;
            }
        }
        if(!foundMatch)  {
//            Log.v(LOG, "no future match for widget");
            // look for the latest match in the past then
            for (int i = -1;i >-3;i--) {
                Date widgetDate = new Date(System.currentTimeMillis()+((i)*86400000));
                date[0] = mformat.format(widgetDate);
                Uri matchesUri = DatabaseContract.scores_table.buildScoreWithDate();
                cursor = getContentResolver().query(matchesUri, MATCH_COLUMNS, null, date, null);
                if (cursor.getCount()>0) {
                    // got a match!
//                    Log.v(LOG, "got a match on " + date[0]);
                    break;
                }
            }
        }


        // Extract the match data from the Cursor
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            homeTeam = cursor.getString(COL_HOME);
            awayTeam = cursor.getString(COL_AWAY);
            score = Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS));
            time = cursor.getString(COL_MATCHTIME);
            description = getString(R.string.a11y_home_name)+" "+homeTeam+" "+getString(R.string.a11y_away_name)+" "+awayTeam+" "+getString(R.string.a11y_score)+" "+score+" "+getString(R.string.a11y_time)+" "+time;
//            Log.v(LOG, "description: "+description);
            cursor.close();
            homeLogo = Utilies.getTeamCrestByTeamName(homeTeam);
            awayLogo = Utilies.getTeamCrestByTeamName(awayTeam);
        }

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            // Find the correct layout based on the widget's width
            int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);
            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
            int largeWidth = getResources().getDimensionPixelSize(R.dimen.widget_today_large_width);
            int layoutId;
            if (widgetWidth >= largeWidth) {
                layoutId = R.layout.widget_today_match_large;
            } else {
                layoutId = R.layout.widget_today_match;
            }
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setTextViewText(R.id.w_home_name, homeTeam);
            views.setTextViewText(R.id.w_away_name, awayTeam);
            views.setTextViewText(R.id.w_data_textview, time);
            views.setTextViewText(R.id.w_score_textview, score);
            views.setImageViewResource(R.id.w_home_logo, homeLogo);
            views.setImageViewResource(R.id.w_away_logo, awayLogo);

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, description);
            }

            // Create an Intent to open the app if we tap the widget
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
       views.setContentDescription(R.id.widget, description);
    }

}
