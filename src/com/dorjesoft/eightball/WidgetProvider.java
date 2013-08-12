package com.dorjesoft.eightball;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class WidgetProvider extends AppWidgetProvider {

	// private static final String LOG = WidgetProvider.class.getSimpleName();

	public static final String ACTION_APPWIDGET_CLICK = "8BALL_ACTION_CLICK"; //$NON-NLS-1$
	public static final String ACTION_APPWIDGET_NONE = "8BALL_ACTION_INITIALIZE"; //$NON-NLS-1$

	public static void message(Context context, String type) {
		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(),
				UpdateWidgetService.class);

		intent.putExtra(UpdateWidgetService.EXTRAS_TYPE, type);

		// Update the widgets via the service
		context.startService(intent);
	}

	public static void message(Context context) {
		message(context, ACTION_APPWIDGET_NONE);
	}

	@Override
	public void onEnabled(Context context) {
		message(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		message(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (ACTION_APPWIDGET_CLICK.equals(intent.getAction())) {
			message(context, ACTION_APPWIDGET_CLICK);
			return;
		}
		message(context);
		super.onReceive(context, intent);
	}

}