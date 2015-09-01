package com.example.maheshjadhav.events;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by maheshjadhav on 02/09/15.
 */
public class MapActivity extends Activity {
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        showMapAndDirections();
    }

    private void showMapAndDirections() {
        double lat = 19.2140596;
        double lan = 72.9784518;
        String eventName = "My Event";
        String eventAddress = "Urvi Park, Thane, India, 400601";

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        if (map != null) {
            showLocationMarker(lat, lan, eventName, eventAddress);
            showMapApp();
        }
    }

    private void showLocationMarker(double lat, double lan, String eventName, String address) {
        LatLng addressLatLng = new LatLng(lat, lan);
        Marker marker = map.addMarker(new MarkerOptions()
                .position(addressLatLng)
                .title(eventName)
                .snippet(address));
        // marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pindrop));
        marker.showInfoWindow();
        marker.setDraggable(false);
        marker.setVisible(true);

        map.setMyLocationEnabled(true);

        // Move the camera instantly to address with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(addressLatLng, 15));
    }

    private void showMapApp() {
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng addressLatLng = marker.getPosition();

                // geo:latitude,longitude?q=query
                // geo:0,0?q=my+street+address
                String geoUri = "geo:" + addressLatLng.latitude + "," + addressLatLng.longitude
                        + "?q=" + Uri.encode(marker.getSnippet());
                Uri gmmIntentUri = Uri.parse(geoUri);

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }
}
