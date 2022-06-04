package com.example.gasalarm;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Asosiy extends AppWidgetProvider {
    Button button;
    private static final String ACTION_UPDATE_CLICK_NEXT = "action.UPDATE_CLICK_NEXT";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.asosiy);
//        Toast.makeText(context, "Clicked!!", Toast.LENGTH_SHORT).show();
    }
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.asosiy);

        Intent intentUpdate = new Intent(context, NewAppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] idArray = new int[]{appWidgetId};

        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context,
                appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.appwidget_text1, pendingUpdate);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {

        Intent intent = new Intent(context, getClass()); // An intent directed at the current class (the "self").
        intent.setAction(action);
        Toast.makeText(context, "URRA!!", Toast.LENGTH_SHORT).show();
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.asosiy);

        views.setOnClickPendingIntent(R.id.appwidget_text1   , getPendingSelfIntent(context, ACTION_UPDATE_CLICK_NEXT));
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {


    }
}

