package animap.animap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    //widgets
    Button runButton;
    Button aboutButton;

    // A default location (Flagstaff, Arizona) and default zoom to use when location permission is
    // not granted.
    private boolean locationPermissionGranted;

    //for default location
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //turning on location services variables
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    //constants
    private static final String GPS_PERMISSION = "gps permission";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //---------- Implementing menu button
        //toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //----------- Implementing Widgets -------------
        runButton = (Button) findViewById(R.id.bttn_run);
        aboutButton = (Button) findViewById(R.id.bttn_about);

        getLocationPermission();
        checkLocationServices();


        //-----------Implementing Event Listeners for When the Buttons Are Clicked
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This tells it what activity to go to & what activity we're on
                Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                intent.putExtra(GPS_PERMISSION, locationPermissionGranted);
                startActivity(intent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
            }
        });
    }


    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Press the 'run' button to begin choosing a location")
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
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

    @Override
    public void onRequestPermissionsResult(int requestcode, String[] permissions, int[] grantResults){
        switch (requestcode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGranted = true;
                }
                else{
                    locationPermissionGranted = false;
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
                break;
        }
    }


    //starts the process of checking if the location is enabled
    private void checkLocationServices(){
        // check if location is on
        final LocationManager manager = (LocationManager) HomeActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(HomeActivity.this)) {
            //gps is already enabled
        }
        else{
            //gps is not enabled
            enableLoc();
        }
        //see if the device is has GPS support
        if(!hasGPSDevice(HomeActivity.this)){
            Toast.makeText(HomeActivity.this,"GPS Not Supported",Toast.LENGTH_SHORT).show();
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
            googleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
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
                                status.startResolutionForResult(HomeActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {}
                            break;
                    }
                }
            });
        }
    }

    // -------- Menu
    // inflate menu custom icons
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.help:
                createDialog();
        }

        return true;
    }
}
