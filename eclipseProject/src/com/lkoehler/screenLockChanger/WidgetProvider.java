package com.lkoehler.screenLockChanger;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lkoehler.screenLockController.Controller;

public class WidgetProvider extends AppWidgetProvider {

	private Controller controller;

	public static String SIMON = "simon";
	public static String SASCHA = "sascha";
	public static String LEA = "lea";
	public static String ALL = "all";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		controller = new Controller(context);
		controller.setExecute(false);

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		boolean status;
		boolean setAllButton = true;
		status = controller.getStatus("simon");
		remoteViews.setTextViewText(R.id.simonLabel, status == true ? "AN"
				: "AUS");
		if (!status)
			setAllButton = false;

		status = controller.getStatus("sascha");
		remoteViews.setTextViewText(R.id.saschaLabel, status == true ? "AN"
				: "AUS");
		if (!status)
			setAllButton = false;

		status = controller.getStatus("lea");
		remoteViews.setTextViewText(R.id.leaLabel, status == true ? "AN"
				: "AUS");
		if (!status)
			setAllButton = false;

		remoteViews.setTextViewText(R.id.allLabel, setAllButton == true ? "AN"
				: "AUS");

		PendingIntent pintent;
		Intent intent;

		intent = new Intent(context, WidgetProvider.class);
		intent.setAction(SIMON);
		pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.simonLabel, pintent);

		intent = new Intent(context, WidgetProvider.class);
		intent.setAction(SASCHA);
		pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.saschaLabel, pintent);

		intent = new Intent(context, WidgetProvider.class);
		intent.setAction(LEA);
		pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.leaLabel, pintent);

		intent = new Intent(context, WidgetProvider.class);
		intent.setAction(ALL);
		pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.allLabel, pintent);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		controller = new Controller(context);

		if (intent.getAction().equals(SIMON)) {
			controller.toggle("simon");
		} else if (intent.getAction().equals(SASCHA)) {
			controller.toggle("sascha");
		} else if (intent.getAction().equals(LEA)) {
			controller.toggle("lea");
		} else if (intent.getAction().equals(ALL)) {
			controller.toggleAll();
		}

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		int[] appWidgetIds = appWidgetManager
				.getAppWidgetIds(new ComponentName(context,
						WidgetProvider.class));
		if (appWidgetIds.length > 0) {
			new WidgetProvider().onUpdate(context, appWidgetManager,
					appWidgetIds);
		}
	}
}
