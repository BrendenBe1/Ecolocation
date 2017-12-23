package ecolocation.ecolocation;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Random;

public class SpatialMapActivity extends AppCompatActivity implements OnMapReadyCallback{
    String[] colorScale;
    double[] latitudes = new double[30];  //9.607168 9.761728 = 94
    double[] longitudes = new double [30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spatial_map);

        colorScale = new String[] {"#E8F5E9","#C8E6C9", "#A5D6A7", "#81C784", "#66BB6A", "#4CAF50",
                "#43A047", "#388E3C", "#2E7D32", "#1B5E20"};


        //get a handle to the map fragment
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map){
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
            }
        } catch (Resources.NotFoundException e) {
        }
        // Position the map's camera near Flagstaff, Arizona
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.179940, -111.645842),
                5));

        //TESTING
        sampleData();
        Random rand = new Random();
        for(int i=0; i<latitudes.length; i++){
            GeoPixel p = new GeoPixel(new LatLng(latitudes[i], longitudes[i]), 0.5,
                    rand.nextInt(10));
            String s = colorScale[p.getValue()];
            PolygonOptions polyOpts = new PolygonOptions().add(p.getTopLeft(), p.getTopRight(),
                    p.getBottomRight(), p.getBottomLeft()).fillColor(
                            Color.parseColor(s)).strokeWidth(1).strokeColor(Color.parseColor(s));
            Polygon poly = map.addPolygon(polyOpts);

        }
    }

    private void sampleData(){
        // 32.092553 --> 36.896137 = 4.803584 --> 96.07168
        // -108.923295 --> -113.804159 = 4.880864 --> 97.61728
        double offset = 0.5*2;
        double lat = 32.092553;
        double latCurr = lat;
        double latEnd = 36.896137 ;
        double lon = -108.923295;
        double lonCurr = lon;
        double lonEnd = -114;

        for(int i =0; i < latitudes.length; i++){
            //set latCurr to beginning
            latitudes[i] = latCurr;

            longitudes[i] = lonCurr;
            lonCurr -= offset;

            //set lonCurr to beginning
            if(lonCurr < lonEnd){
                lonCurr = lon;
                latCurr += offset;
            }
        }
        lonEnd = 0;


    }

    private String scaleColor(int index){
        return "3"+String.valueOf(colorScale[index]);
    }
}