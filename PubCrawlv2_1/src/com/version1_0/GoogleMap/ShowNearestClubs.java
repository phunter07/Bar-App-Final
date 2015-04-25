package com.version1_0.GoogleMap;

/**
 * the class represents google maps, it is used to show the nearest 3 bars
 * @author Jiang Zhe Heng
 */

import java.util.ArrayList;
import java.util.Collections;

import java.util.LinkedList;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.version1_0.ClubCrawl.MyApplication;
import com.version1_0.ClubCrawl.R;
import com.version1_0.sqlite.Club;

public class ShowNearestClubs extends FragmentActivity implements
		LocationListener {

	GoogleMap mGoogleMap;
	ArrayList<LatLng> mMarkerPoints;
	double mLatitude = 0;
	double mLongitude = 0;
	Button startMyTrip;
	Button leave;

	LinkedList<Club> clubList = new LinkedList<Club>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_quick_drink);
		// add itself into activityList
		MyApplication.getInstance().addActivity(this);
		for (int loop = 0; loop < 3; loop++) {
			clubList.add(MyApplication.getInstance().getAllClubList().get(loop));
		}

		startMyTrip = (Button) findViewById(R.id.startMyTrip);
		leave = (Button) findViewById(R.id.stop);

		startMyTrip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClassName("com.version1_0.ClubCrawl",
						"com.version1_0.GoogleMap.GoogleMaps");
				startActivity(intent);
				try {
					ShowNearestClubs.this.finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		leave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyApplication.getInstance().exitSystem(); 

			}
		});

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Initializing
			mMarkerPoints = new ArrayList<LatLng>();

			// Getting reference to SupportMapFragment of the activity_main
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Getting Map for the SupportMapFragment
			mGoogleMap = fm.getMap();

			// Enable MyLocation Button in the Map
			mGoogleMap.setMyLocationEnabled(true);

			// Getting Current Location From GPS
			Location location = MyApplication.getInstance().getMyLocation();

			if (location != null) {
				onLocationChanged(location);
				// if get the my location, clubList should be sorted
				Collections.sort(clubList, new ClubSortComparator(location));

			}

			// Setting onclick event listener for the map
			mGoogleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng point) {

					// Already map contain destination location
					if (mMarkerPoints.size() > 1) {

						FragmentManager fm = getSupportFragmentManager();
						mMarkerPoints.clear();
						mGoogleMap.clear();
						LatLng startPoint = new LatLng(mLatitude, mLongitude);
						drawMarker(startPoint);
					}
					// draw clubs

					for (Club club : clubList) {
						
						drawMarker(new LatLng(club.getLatitude(), club
								.getLongitude()));


						// // Checks, whether start and end locations are
						// captured
						// if(mMarkerPoints.size() >= 2){
						// LatLng origin = mMarkerPoints.get(0);
						// LatLng dest = mMarkerPoints.get(1);
						//
						// // Getting URL to the Google Directions API
						// String url = getDirectionsUrl(origin, dest);
						//
						// DownloadTask downloadTask = new DownloadTask();
						//
						// // Start downloading json data from Google Directions
						// API
						// downloadTask.execute(url);
						// }
					}
					MyApplication.getInstance().getHashMap()
					.put("clubList", clubList);
				}
			});
		}
	}

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	 // Inflate the menu; this adds items to the action bar if it is present.
	 getMenuInflater().inflate(R.menu.main, menu);
	 return true;
	 }

	private void drawMarker(LatLng point) {
		mMarkerPoints.add(point);

		// Creating MarkerOptions
		MarkerOptions options = new MarkerOptions();

		// Setting the position of the marker
		options.position(point);

		/**
		 * For the start location, the color of marker is GREEN and for the end
		 * location, the color of marker is RED.
		 */
		if (mMarkerPoints.size() == 1) {
			options.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		} else {
			options.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		}

		// Add new marker to the Google Map Android API V2
		mGoogleMap.addMarker(options);
	}

	@Override
	public void onLocationChanged(Location location) {
		// Draw the marker, if destination location is not set
		if (mMarkerPoints.size() < 2) {

			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			LatLng point = new LatLng(mLatitude, mLongitude);
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
			drawMarker(point);
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}
