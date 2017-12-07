package ecolocation.ecolocation;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = MapActivity.class.getSimpleName();
    private GoogleMap map;
    private Marker marker;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;


    // A default location (Flagstaff, Arizona) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(35.198284, -111.651299);
    private boolean locationPermissionGranted = true;

    //----- CONSTANTS
    //for default location
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String GPS_PERMISSION = "gps permission";

    //widgets
    TextView latTxt;
    TextView longTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //initialize widgets
        latTxt = findViewById(R.id.txt_lat);
        longTxt = findViewById(R.id.txt_long);

        //see if permission to location was granted
        Bundle extras = getIntent().getExtras();
        locationPermissionGranted = extras.getBoolean(GPS_PERMISSION);

        //used to get the last known location of the device
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getDeviceLocation();

        //get a handle to the map fragment
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    /*
* Use the fuseLocationProvider to get the device's last-known location & use that to position
*   the map.
*/
    public void getDeviceLocation(){
        //get the best & most recent location of the device
        try{

            if(locationPermissionGranted){
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful() && task.getResult() != null)
                        {
                            //set the map's camera position to current location
                            lastKnownLocation = task.getResult();

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()
                            ), DEFAULT_ZOOM));

                            latTxt.setText(String.valueOf(lastKnownLocation.getLatitude()));
                            longTxt.setText(String.valueOf(lastKnownLocation.getLongitude()));

                        }
                        else{
                            //move camera & get UI settings
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,
                                    DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }

                        //places marker on current location
                        setMarker();
                    }

                });
            }
        }
        catch(SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /*
    * Request permissions to access user's location but first checks if the location permission was
    * already granted, if not then it asks.
    */
    private void getLocationPermission(){
        //request permission to use location services
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else{
            locationPermissionGranted = true;
        }

    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        //turn on My Location Layer & related control on the map
        updateLocationUI();
    }

//    shows the draggable marker
    private void setMarker(){
        marker = map.addMarker(new MarkerOptions().position(new LatLng(defaultLocation.latitude,
                defaultLocation.longitude)).title("Default Location").draggable(true));

        //add marker to current location or default location (if location is denied)
        if (lastKnownLocation != null){
            marker.setPosition(new LatLng(lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude()));
        }

        //event listener for marker
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng currLocation = marker.getPosition();
                String currLat = String.valueOf(currLocation.latitude);
                String currLong = String.valueOf(currLocation.longitude);

                latTxt.setText("Lat: " + currLat + ", ");
                longTxt.setText("Long: " + currLong);
            }
        });
    }

    /*
       * Set the location controls on the map.
       *
       * If the user granted location permissions, then enable My Location Layer & related controls
       *   on the map.
       *  Otherwise, disable them & set current location to null;
       */
    private void updateLocationUI(){
        if(map == null){
            return;
        }

        try{
            //see if location permission was granted
            if(locationPermissionGranted){
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }
            //disable My Location layer & controls
            else{
                //disable layer & controls
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);

                //reset lastKnownLocation & ask for permission
                lastKnownLocation = null;
                getLocationPermission();
            }
        }
        catch(SecurityException e){}
    }
}


