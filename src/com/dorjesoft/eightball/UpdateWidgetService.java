package com.dorjesoft.eightball;

import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.text.ClipboardManager;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	protected Random rand = new Random();

	static public final String EXTRAS_TYPE = "8BALL_UPDATE_TYPE"; //$NON-NLS-1$

	public String getEightBall() {
		Resources res = getResources();
		String[][] choices = { res.getStringArray(R.array.eightball_yes),
				res.getStringArray(R.array.eightball_maybe),
				res.getStringArray(R.array.eightball_no) };

		int i = Math.abs(rand.nextInt()) % choices.length;
		int j = Math.abs(rand.nextInt()) % choices[i].length;

		return choices[i][j];
	}

	Handler mHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		mHandler = new Handler();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private final Runnable mRemove = new Runnable() {
		@Override
		public void run() {
			handleCommand(new Intent());
			stopSelf();
		}
	};

	/** Returns true if the service "continues running" */
	protected boolean handleCommand(Intent intent) {
		String type = intent.getStringExtra(EXTRAS_TYPE);
		boolean new8ball = (type != null && type
				.equals(WidgetProvider.ACTION_APPWIDGET_CLICK));

		AppWidgetManager manager = AppWidgetManager.getInstance(this
				.getApplicationContext());

		RemoteViews views = new RemoteViews(this.getApplicationContext()
				.getPackageName(), R.layout.widget_layout);

		String eb = getString(R.string.eightball_init);

		if (new8ball) {
			eb = getEightBall();
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(eb);
		}

		views.setTextViewText(R.id.eightball, eb);

		ComponentName thisWidget = new ComponentName(getApplicationContext(),
				WidgetProvider.class);
		int[] widgetIds = manager.getAppWidgetIds(thisWidget);

		Intent clickIntent = new Intent(this.getApplicationContext(),
				WidgetProvider.class);
		clickIntent.setAction(WidgetProvider.ACTION_APPWIDGET_CLICK);
		clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, clickIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.widget_frame, pendingIntent);

		for (int widgetId : widgetIds) {
			manager.updateAppWidget(widgetId, views);
		}

		mHandler.removeCallbacks(mRemove);
		if (new8ball) {
			mHandler.postDelayed(mRemove, 60 * 1000);
		}

		return new8ball;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		handleCommand(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (handleCommand(intent)) {
			return START_STICKY;
		}
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}