package com.version1_0.ClubCrawl;

/**
 * This is the main application, it is the entrance of the application, 
 * so I can set the global vars here
 * @author Jiang Zhe Heng
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.version1_0.GoogleMap.ClubSortComparator;
import com.version1_0.sqlite.ClubDataSource;
import com.version1_0.sqlite.Club;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.widget.CheckBox;

public class MyApplication extends Application implements LocationListener {

	private HashMap<String, Object> hashMap=new HashMap<String, Object>();
	private List<Club> allClubList;
	private static MyApplication myApplication;
	private List<Activity> activityList = new LinkedList<Activity>();  
	private Location myLocation;
	private ClubDataSource dataSource;
	
	public MyApplication() {
	
	}

	@Override
	public void onCreate() {
		super.onCreate();
		myApplication=this;
		dataSource =new ClubDataSource(this);
		
		dataSource.open();
		dataSource.reWriteTable();
		try {
			//run the sql script
			dataSource.insertFromFile(this,R.raw.bars);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAllClubList(dataSource.getAllClubs());
		// Getting LocationManager object from System Service
					// LOCATION_SERVICE
					LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

					// Creating a criteria object to retrieve provider
					Criteria criteria = new Criteria();

					// Getting the name of the best provider
					String provider = locationManager.getBestProvider(criteria, true);

					// Getting Current Location From GPS
					setMyLocation(locationManager.getLastKnownLocation(provider));
					locationManager.requestLocationUpdates(provider, 20000, 2, this);
					//if find the location, sort the allClubList
					if(myLocation!=null){
						Collections.sort(getAllClubList(), new ClubSortComparator(myLocation));
					}
					dataSource.close();
	}
	
	
	public static MyApplication getInstance(){
		return myApplication;
	}

	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}

	//add the current activity into activityList
	public void addActivity(Activity activity) {  
        activityList.add(activity);  
    }  
  
  //exit all activities
    public void exit() {  
        for (Activity activity : activityList) {  
            activity.finish();  
        }   
    }

    public void exitSystem() {  
        for (Activity activity : activityList) {  
            activity.finish();  
        }   
        System.exit(0);
    }
	public List<Club> getClubList() {
		return getAllClubList();
	}

	public void setClubList(List<Club> clubList) {
		this.setAllClubList(clubList);
	}

	@Override
	public void onLocationChanged(Location location) {
		this.setMyLocation(location);
		Collections.sort(getAllClubList(), new ClubSortComparator(myLocation));
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public Location getMyLocation() {
		return myLocation;
	}

	private void setMyLocation(Location myLocation) {
		this.myLocation = myLocation;
	}

	public List<Club> getAllClubList() {
		return allClubList;
	}

	private void setAllClubList(List<Club> allClubList) {
		this.allClubList = allClubList;
	} 
	
}
