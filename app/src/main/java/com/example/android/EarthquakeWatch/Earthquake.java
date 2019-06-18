package com.example.android.EarthquakeWatch;


public class Earthquake {

    /**
     * other classes cannot change
     */


    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private double longitude;
    private double latitude;

    /** web url where to find more infor */
    private String mUrl;


    public Earthquake(double magnitude, String location, long timeInMilliseconds, String url, double log, double lati){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
        longitude = log;
        latitude  = lati;
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getUrl(){
        return mUrl;
    }
}
