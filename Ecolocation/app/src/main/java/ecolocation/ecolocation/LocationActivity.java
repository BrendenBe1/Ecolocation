package ecolocation.ecolocation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

import java.math.BigDecimal;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {
    //widgets
    Button nextButton;
    ImageButton locationButton;
    EditText latTxt;
    EditText longTxt;

    private static GoogleMap map;
    private Marker marker;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // location retrieved by the Fused Location Provider.
    private static Location lastKnownLocation;
//    private LatLng chosenLocation;
    // A default location (Flagstaff, Arizona) to use when location permission is not granted.
    private LatLng chosenLocation = new LatLng(35.1982, -111.6513);
    private static boolean locationPermissionGranted;

    //----- CONSTANTS
    private static final int DEFAULT_ZOOM = 4;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String GPS_PERMISSION = "gps permission";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);


        //----------- initialize widgets
        latTxt = (EditText) findViewById(R.id.txt_lat);
        longTxt = (EditText) findViewById(R.id.txt_long);
        nextButton = (Button) findViewById(R.id.bttn_next);
        locationButton = (ImageButton) findViewById(R.id.bttn_location);

        //---------- event listeners for widgets
        // next button event listener
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to ListViewActivity
                startActivity(ListViewActivity.newIntent(LocationActivity.this,
                        chosenLocation));
            }
        });

        //see if permission to location was granted

        // Set a key listener callback so that users can search by pressing "Enter"
        latTxt.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if(event.getAction() == KeyEvent.ACTION_UP){
                        enterOnTextViews();
                    }
                    return true;
                }
                return false;
            }
        });
        longTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER ) {
                    if( event.getAction() == KeyEvent.ACTION_UP ) {
                       enterOnTextViews();
                    }
                    return true;
                }
                return false;
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterOnTextViews();
            }
        });

        if(getIntent().hasExtra(GPS_PERMISSION)){
            Bundle extras = getIntent().getExtras();
            locationPermissionGranted = extras.getBoolean(GPS_PERMISSION);

        }
//        else{
//            locationPermissionGranted = false;
//        }

        //used to get the last known location of the device
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getDeviceLocation();

        //get a handle to the map fragment
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    /**
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
                            chosenLocation = new LatLng(lastKnownLocation.getLatitude(),
                                    lastKnownLocation.getLongitude());

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation,
                                    DEFAULT_ZOOM));
                            setTextViewCoordinates(chosenLocation);
                        }
                        else{
                            //move camera & get UI settings
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation,
                                    DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);

                            setTextViewCoordinates(chosenLocation);
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

    /**
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
        map.setOnMapLongClickListener(this);

        //turn on My Location Layer & related control on the map
        updateLocationUI();
    }

    //---------- shows the draggable marker and related event listeners
    private void setMarker(){
        if(marker == null){
            marker = map.addMarker(new MarkerOptions().position(new LatLng(chosenLocation.latitude,
                    chosenLocation.longitude)).title("Default Location").draggable(true));
        }
        else{
            marker.setPosition(chosenLocation);
        }

        //add marker to current location or default location (if location is denied)
        if (lastKnownLocation != null){
            marker.setPosition(chosenLocation);
        }

        //event listener for marker
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                chosenLocation = marker.getPosition();
                setTextViewCoordinates(chosenLocation);
            }
        });
    }

    /**
       * Set the location controls on the map.
       *
       * If the user granted location permissions, then enable My Location Layer & related controls
       * on the map. Otherwise, disable them & set current location to null;
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
                if(marker == null){
                    setMarker();
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation, DEFAULT_ZOOM));
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
            map.getUiSettings().setZoomControlsEnabled(true);
            setMarker();
        }
        catch(SecurityException e){}
    }

    @Override
    public void onMapLongClick(LatLng point){
        marker.setPosition(point);
    }

    //------------- WORKING WITH THE TEXT VIEWS

    /**
     * user hits enters button on coordinate TextViews
     *      handles the behavior of checking the inputs and changing the last known location and
     *     moves the camera to the new location
     */
    private void enterOnTextViews(){
        //set strings in lat & long TextViews
        updateTextViews();

        //move camera & marker to new location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation, DEFAULT_ZOOM));
        marker.setPosition(chosenLocation);

        updateLocationUI();

        //hides keyboard
        // https://stackoverflow.com/questions/8785023/how-to-close-android-soft-keyboard-programmatically
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
    }

    /**
     * This checks that the strings inside of the latTxt & longTxt are in the correct format. This
     * means: the strings are numbers (can be decimals, and/or negative), are in the correct range
     * for longitude and latitude.
     *
     * If the previous are correct, it puts the numbers in the following format: (##.##, ##.##)
     */
    private void updateTextViews(){
//        String regex = "-?\\d+\\.?\\d+$";
        String regex = "-?\\d+(\\.\\d+$)?";
        String latStr = latTxt.getText().toString();
        String longStr = longTxt.getText().toString();

        //remove the extra parenthesis, commas, and spaces that might be present
        latStr = latStr.replaceAll("[(), ]", "");
        longStr = longStr.replaceAll("[(), ]", "");

        //this makes sures that the given "numbers" are in the correct format:
            //contains only numbers, can be negative/positive, and can be a decimal
        if(latStr.matches(regex) && longStr.matches(regex)){
            /*
            -------- Check that coordinates is in the correct range for latitude & longitude
                Latitude's range is: (-90, 90)
                Longitude's range is: (-180, 180)
         */
            Double latDouble = Double.valueOf(latStr);
            Double longDouble = Double.valueOf(longStr);

            /*
             * first check conditions that we don't want so we can tell the user which one they
             * entered wrong
             */

            //latitude is in the incorrect range
            if(latDouble > 90 || latDouble < -90){
                latTxt.setError("Latitude must be between -90 and 90 degrees");
            }
            //longitude is in the incorrect range
            else if(longDouble > 180 || longDouble < -180){
                longTxt.setError("Longitude must be between -180 and 180");
            }
            //coordinate is in the correct range
            else{
                latStr = "(" + latStr + ", ";
                longStr += ")";

                //change TextView to correct format
                latTxt.setText(latStr);
                longTxt.setText(longStr);

                //save new location
                chosenLocation = new LatLng(latDouble, longDouble);
            }
        }
        else{

            if(!latStr.matches(regex)){
                latTxt.setError("Must enter a valid number.");
            }
            if(!longStr.matches(regex)){
                longTxt.setError("Must enter a valid number.");
            }
        }
    }

    //--------- with the given location, changes the current latitude & longitude to it & their
    //          corresponding TextViews
    private void setTextViewCoordinates(LatLng currLocation){
        //get string of coordinates with 6 decimal places
        BigDecimal currLatDecimal = new BigDecimal(currLocation.latitude);
        BigDecimal currLongDecimal = new BigDecimal(currLocation.longitude);
        String currLat = currLatDecimal.setScale(6, 0).toString();
        String currLong = currLongDecimal.setScale(6, 0).toString();

        latTxt.setText("(" + currLat + ", ");
        longTxt.setText(currLong + ")");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
