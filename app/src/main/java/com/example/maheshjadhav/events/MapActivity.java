package com.example.maheshjadhav.events;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
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
        String eventAddress = "Urvi Park, Pokhran Road Number 2, subhash nagar, Thane West, " +
                "Thane, Maharashtra 400606";

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        // Setting a custom info window adapter for the google map
        map.setInfoWindowAdapter(new InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View infoWinVw = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                // Getting reference to the TextView to set latitude
                TextView infoWinTxtVw = (TextView) infoWinVw.findViewById(R.id.event_address);

                // Getting the position from the marker
                String address = arg0.getSnippet();

                // Setting the address
                infoWinTxtVw.setText(address);

                // Returning the view containing InfoWindow contents
                return infoWinVw;

            }

        });

        if (map != null) {
            showLocationMarker(lat, lan, eventAddress);
            showMapApp();
        }
    }

    private void showLocationMarker(double lat, double lan, String address) {
        LatLng addressLatLng = new LatLng(lat, lan);
        Marker marker = map.addMarker(new MarkerOptions()
                .position(addressLatLng)
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
