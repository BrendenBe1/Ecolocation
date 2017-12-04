package ecolocation.ecolocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

                 /*
        * Setting up the google maps view w/ a fragment
        * Retreived from https://developers.google.com/maps/documentation/android-api/map-with-marker
        *
        * Getting the SupportMapFragment & request notification when map is ready to use
        */
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_layout);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        //add a marker in Sydney, Australia
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney))
                .setTitle("Marker in Sydney");
        //move map's camera to the marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
