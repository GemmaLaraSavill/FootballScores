package nanodegree.gemma.footballscores.widgets;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import nanodegree.gemma.footballscores.DatabaseContract;
import nanodegree.gemma.footballscores.R;
import nanodegree.gemma.footballscores.Utilies;



/**
 * Created by Gemma S. Lara Savill on 03/03/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MatchListWidgetRVService extends RemoteViewsService {

    private static final String LOG = "List widget";

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

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor cursor = null;
            private String homeTeam;
            private String awayTeam;
            private String score;
            private String time;
            private String description;
            private int homeLogo;
            private int awayLogo;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (cursor != null) {
                    cursor.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                // get the data
                String[] date = new String[1];
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                Date widgetDate = new Date(System.currentTimeMillis());
                date[0] = mformat.format(widgetDate);
                Uri matchesUri = DatabaseContract.scores_table.buildScoreWithDate();
                cursor = getContentResolver().query(matchesUri, MATCH_COLUMNS, null, date, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }

            @Override
            public int getCount() {
                return cursor == null ? 0 : cursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if (position == AdapterView.INVALID_POSITION ||
                        cursor == null || !cursor.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_match_item);

                homeTeam = cursor.getString(COL_HOME);
                awayTeam = cursor.getString(COL_AWAY);
                score = Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS));
                time = cursor.getString(COL_MATCHTIME);
                description = getString(R.string.a11y_home_name)+" "+homeTeam+" "+getString(R.string.a11y_away_name)+" "+awayTeam+" "+getString(R.string.a11y_score)+" "+score+" "+getString(R.string.a11y_time)+" "+time;
//                Log.v(LOG, "description: " + description);
                // for the widget screen capture to make the preview icon
//                if (homeTeam.equals("Granada CF")) {
//                    score = "2 - 1";
//                } else {
//                    score = "0 - 1";
//                }

                homeLogo = Utilies.getTeamCrestByTeamName(homeTeam);
                awayLogo = Utilies.getTeamCrestByTeamName(awayTeam);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, description);
                }
                // Add the data to the RemoteViews
                views.setTextViewText(R.id.w_home_team, homeTeam);
                views.setTextViewText(R.id.w_away_team, awayTeam);
                views.setTextViewText(R.id.w_time_txt, time);
                views.setTextViewText(R.id.w_score_txt, score);
                views.setImageViewResource(R.id.w_home_logo, homeLogo);
                views.setImageViewResource(R.id.w_away_logo, awayLogo);

                // Create an Intent to open widget if I tap on a match
                Intent fillInIntent = new Intent();
//                Log.d(LOG, "Pending Intent = " + fillInIntent.toString());
                views.setOnClickFillInIntent(R.id.w_match, fillInIntent);

                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_match_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
//                if (cursor.moveToPosition(position))
//                    return cursor.getLong(INDEX_WEATHER_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
