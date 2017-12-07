package ecolocation.ecolocation;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MapActivity2 extends AppCompatActivity {
    private static final String TAG = MapActivity.class.getSimpleName();
    private GoogleMap map;
    private Marker marker;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;


    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(35.198284, -111.651299);
    private boolean locationPermissionGranted;

    //----- CONSTANTS
    //for default location
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    //turning on location services variables
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    //widgets
    TextView latTxt;
    TextView longTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        //initialize widgets
        latTxt = findViewById(R.id.txt_lat);
        longTxt = findViewById(R.id.txt_long);

        //get permission for location & enable location services if not already enabled
        getLocationPermission();
        checkLocationServices();

        //used to get the last known location of the device
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestcode, String[] permissions, int[] grantResults){
        switch (requestcode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGranted = true;
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                }
                else{
                    locationPermissionGranted = false;
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
                break;
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

    //-------------- This is for turning on the location if it is not already enabled --------------
    //starts the process of checking if the location is enabled
    private void checkLocationServices(){
        // check if location is on
        final LocationManager manager = (LocationManager) MapActivity2.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MapActivity2.this)) {
            Toast.makeText(MapActivity2.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MapActivity2.this,"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();
        }

        //see if the device is has GPS support
        if(!hasGPSDevice(MapActivity2.this)){
            Toast.makeText(MapActivity2.this,"Gps not Supported",Toast.LENGTH_SHORT).show();
        }
    }

    //checks if the device has GPS support
    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null) {return false;}

        final List<String> providers = mgr.getAllProviders();
        if (providers == null){return false;}

        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    //enable location service
    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MapActivity2.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {}

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            //setting parameters needed when enabling location services
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MapActivity2.this, REQUEST_LOCATION);

                                finish();
                            } catch (IntentSender.SendIntentException e) {}
                            break;
                    }
                }
            });
        }
    }
}


