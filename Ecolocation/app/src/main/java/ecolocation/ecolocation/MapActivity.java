package ecolocation.ecolocation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    //widgets
    Button nextButton;
    EditText latTxt;
    EditText longTxt;

    private static final String TAG = MapActivity.class.getSimpleName();
    private GoogleMap map;
    private Marker marker;
    private CameraPosition cameraPosition;

    // The entry points to the Places API.
    private GeoDataClient geoDataClient;
    private PlaceDetectionClient placeDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;


    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private boolean locationPermissionGranted;

    //----- CONSTANTS
    //for default location
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        // Retrieve location and camera position from saved instance state.
//        if (savedInstanceState != null) {
//            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
//        }

//        //The first two are primary entry points to the Google Places API & Location Services
//        geoDataClient = Places.getGeoDataClient(this, null);
//        placeDetectionClient = Places.getPlaceDetectionClient(this, null);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //get a handle to the map fragment
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        //initialize widgets & set up event listeners
        latTxt = (EditText) findViewById(R.id.txt_lat);
        longTxt = (EditText) findViewById(R.id.txt_long);
        nextButton = (Button) findViewById(R.id.bttn_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: store location chosen to go to next activity

                Intent intent = new Intent(MapActivity.this,
                        GraphResultsActivity.class);
                startActivity(intent);
            }
        });
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
                        if(task.isSuccessful())
                        {
                            //set the map's camera position to current location
                            lastKnownLocation = task.getResult();
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()
                            ), DEFAULT_ZOOM));
                        }
                        else{
                            //log messages
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());

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
        //checks to see if permission is already granted
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = true;
        }
        //permission is not already granted, need to ask for it
        else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                .ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        //TODO: do other activities, will be implemented later

        //TODO: delete?
        getLocationPermission();

        //turn on My Location Layer & related control on the map
        updateLocationUI();

        //Get the current location of the device & set the position of the map
        getDeviceLocation();
    }

    /*
    * Handling the result of the permission request
    */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults){
        locationPermissionGranted = false;
        switch (requestCode){
            //if request is cancelled, the result arrays are empty
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                //if permission is granted, set locationPermissionGranted to true
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
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
        catch(SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //shows the draggable marker
    private void setMarker(){
        marker = map.addMarker(new MarkerOptions().position(new LatLng(defaultLocation.latitude,
                defaultLocation.longitude)).title("Default Location").draggable(true));

        //add marker to current location or default location (if location is denied)
        if (lastKnownLocation != null){
//            map.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(),
//                    lastKnownLocation.getLongitude())).title("Current Location").draggable(true));

            marker.setPosition(new LatLng(lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude()));
            marker.setTitle("Current Location");
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
                String locStr = "Lat: " + currLat + ", Long: " + currLong;

                Toast.makeText(MapActivity.this, locStr, Toast.LENGTH_LONG).show();
            }
        });
    }
}
