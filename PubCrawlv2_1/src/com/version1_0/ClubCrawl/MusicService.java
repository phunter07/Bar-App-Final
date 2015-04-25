package com.version1_0.ClubCrawl;

/**
 * This class represents the music service, when reach the time, 
 * this service will start the alarm music and vibrator the phone, 
 * then give a dialog for user to select going to the next club or extending.
 * @author Jiang Zhe Heng
 */
import java.io.IOException;
import java.util.LinkedList;

import com.version1_0.ClubCrawl.R;
import com.version1_0.sqlite.Club;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

public class MusicService extends Service {
	private MediaPlayer mediaPlayer;
	Vibrator vibrator;
	LinkedList<Club> clubList;

	public MusicService() {
		super();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	public void onCreate() {
		Log.i("service_create", "hello");
		clubList = (LinkedList<Club>) MyApplication.getInstance().getHashMap()
				.get("clubList");
		mediaPlayer = MediaPlayer.create(this, R.raw.all_about_that_bass);
		mediaPlayer.setLooping(true);
		vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("service_start", "hello");
		// to avoid service is called by receiver again when the application is
		// end
		if (mediaPlayer != null && vibrator != null && clubList != null) {
			mediaPlayer.start();
			// check if there are still some clubs to visit
			if (!clubList.isEmpty()) {
				// if yes,create a dialog to another club
				dialogCreate();
			} else {
				// if no,create a dialog to go home
				goHomeDialog();
			}
			long[] pattern = { 3000, 2000 };
			vibrator.vibrate(pattern, 0);
			return super.onStartCommand(intent, flags, startId);
		} else {
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}
	}

	@Override
	public void onDestroy() {
		Log.i("service", "destroy");
		vibrator.cancel();
		mediaPlayer.release();
		MyApplication.getInstance().getHashMap().remove("settingTime");
		super.onDestroy();
	}

	public void goHomeDialog() {
		Builder builder = new AlertDialog.Builder(getApplicationContext());
		builder.setTitle("Alarm");
		builder.setMessage("You have visited all clubs");
		builder.setPositiveButton("End", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {		        
				stopSelf();
				MyApplication.getInstance().exitSystem();

			}
		});
		builder.setNegativeButton("More drink", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				MyApplication.getInstance().exit();

				Intent intent = new Intent();
				intent.setAction("android.intent.action.DrinkSelection");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				stopSelf();
			}
		});
		AlertDialog alertDialog = builder.create();
		// make the dialog always show in the front
		alertDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		// Sets the callback that will be called if a key is dispatched to the
		// dialog.
		alertDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				Log.i("tag", String.valueOf(event.getRepeatCount()));
				// make back key can not work
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {
					return true;
				} else {
					return false;
				}
			}
		});
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();

	}

	public void dialogCreate() {
		Builder builder = new AlertDialog.Builder(getApplicationContext());
		builder.setTitle("Alarm");
		builder.setMessage("It is time to go to another pub");
		builder.setPositiveButton("Go", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent dialogIntent = new Intent();
				dialogIntent.setClassName("com.version1_0.ClubCrawl",
						"com.version1_0.GoogleMap.GoogleMaps");
				dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// the dialog have to get its application then start other
				// activity by this application
				getApplication().startActivity(dialogIntent);
				stopSelf();

			}
		});
		builder.setNegativeButton("Extend 5 Minutes", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				mediaPlayer.stop();
				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				vibrator.cancel();
				Thread delay = new Thread() {

					public void run() {
						try {
							Thread.sleep(10 * 1000);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
						Log.i("thread", "wake");
						Intent intent = new Intent();

						intent.setClass(getApplicationContext(),
								MusicService.class);
						startService(intent);
					};
				};
				delay.start();
			}
		});
		AlertDialog alertDialog = builder.create();
		// make the dialog always show in the front
		alertDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		// Sets the callback that will be called if a key is dispatched to the
		// dialog.
		alertDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				Log.i("tag", String.valueOf(event.getRepeatCount()));
				// make back key can not work
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {
					return true;
				} else {
					return false;
				}
			}
		});
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();

	}

}
