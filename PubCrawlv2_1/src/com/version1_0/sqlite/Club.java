package com.version1_0.sqlite;

public class Club {

	private  String name;
	private int id;
	private double longitude;
	private double latitude;

	/**
	 * constructor
	 * @param name
	 * @param latitude
	 * @param longitude
	 */
	 public Club(String name, double latitude, double longitude) {
		 this.setName(name);
		 this.setLatitude(latitude);
		 this.setLongitude(longitude);
	}
	 
	 /**
	  * get name to return pub name
	  * @return name
	  */
	public String getName() {
		return name;
	}
/**
 * set name
 * @param name
 */
	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getId() {
		return id;
	}
/**
 * 
 * @param id
 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		
		return name;
	}
	

	}

