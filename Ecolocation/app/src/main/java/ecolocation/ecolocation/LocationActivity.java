package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback{
    //widgets
    Button currentLocBttn;
    Button customLocBttn;
    //TODO: delete these buttons after tech demo
    Button graphsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //-------- Initializing Widgets
        currentLocBttn = (Button) findViewById(R.id.bttn_curr_location);
        customLocBttn = (Button) findViewById(R.id.bttn_custom_location);





        //-------- Setting Up Event Listeners
        currentLocBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: select the users current location
                /*
        * Setting up the google maps view w/ a fragment
        * Retreived from https://developers.google.com/maps/documentation/android-api/map-with-marker
        *
        * Getting the SupportMapFragment & request notification when map is ready to use
        */
                SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFrag.getMapAsync(LocationActivity.this);
            }
        });

        customLocBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: let the user pick a location from Google Maps
            }
        });

        //TODO: delete below after tech demo
        graphsButton = (Button) findViewById(R.id.bttn_graphs);
        graphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this,
                        GraphResultsActivity.class);
                startActivity(intent);
            }
        });



        }

  @Override
  public void onMapReady(GoogleMap googleMap){
//      //add a marker in Sydney, Australia
//      LatLng sydney = new LatLng(-33.852, 151.211);
//      googleMap.addMarker(new MarkerOptions().position(sydney))
//              .setTitle("Marker in Sydney");
//      //move map's camera to the marker
//      googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
  }

}
