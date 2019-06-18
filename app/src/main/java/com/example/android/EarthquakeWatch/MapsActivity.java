package com.example.android.EarthquakeWatch;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(EarthquakeActivity.EXTRA_MESSAGE);

    }

    private MarkerOptions options = new MarkerOptions();
//    private ArrayList<LatLng> latlngs = new ArrayList<>();
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(message, 1);

//            System.out.println(Arrays.toString(addresses.toArray()) + "  came");

            double latitude = 0.0, longitude = 0.0;
            if(addresses.size() > 0) {
                latitude= addresses.get(0).getLatitude();
                longitude= addresses.get(0).getLongitude();
            }
            else {
                Context context = getApplicationContext();
                CharSequence text = "No match City!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }

            List<Earthquake> es = EarthquakeActivity.earqs;
            for (Earthquake e: es) {
                if (Math.abs(e.getLongitude() - longitude) < 15 && Math.abs(e.getLatitude() - latitude) < 15) {
//                    latlngs.add(new LatLng(e.getLatitude(), e.getLongitude()));
                    options.position(new LatLng(e.getLatitude(), e.getLongitude()));
                    options.title(e.getLocation());
                    options.snippet(String.valueOf(e.getMagnitude()));
                    mMap.addMarker(options);
                }
            }
//        latlngs.add(new LatLng(12.334343, 33.43434)); //some latitude and logitude value
//        latlngs.add(new LatLng(12.3343, 35.43434));

        } catch (IOException | IllegalArgumentException em) {
//            em.printStackTrace();
            if (em instanceof  IOException) {
                em.printStackTrace();
            } else {
                em.printStackTrace();
            }

        }


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 15Luwuk1);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


}
