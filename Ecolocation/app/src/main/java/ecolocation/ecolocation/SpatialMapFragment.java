package ecolocation.ecolocation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Chandler on 3/19/2018.
 */

public class SpatialMapFragment extends Fragment implements OnMapReadyCallback,
        AdapterView.OnItemSelectedListener {
    // spinner variables
    private Spinner spinner;
    private boolean isCurrentMap = true;   //when true show current map, when false show historic

    // google maps variables
    private GoogleMap map;
    private HeatmapTileProvider provider;
    private TileOverlay overlay;
    private LatLng chosenLocation;

    public static SpatialMapFragment newInstance(){
        SpatialMapFragment fragment = new SpatialMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        chosenLocation = Ecosystem.get(getContext()).getChosenLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_spatial_mapping, container,
                false);

        // ------------ Google Maps
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // ------------ Set Up Spinner to set up which map to show
        spinner = (Spinner) view.findViewById(R.id.spinner);

        //set up list of values to show in spinner
        String[] mapStrings = {"Current Nutrient Movement", "Historic Nutrient Movement"};

        // set up adapter & the layout type for its list of values
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout
                .simple_spinner_item, mapStrings);
        //set up layout for when it drops down
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // add listener
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map){
        this.map = map;
        addHeatMap();
        updateLocationUI();
        map.addMarker(new MarkerOptions().position(chosenLocation));
    }

    private void addHeatMap(){
        List<WeightedLatLng> list = null;

        //get the current_nutrient
        try{
            if(isCurrentMap){
                list = readJSONData(R.raw.data_current);
            }
            else{
                list = readJSONData(R.raw.data_historic);
            }
        } catch (JSONException e){
            Toast.makeText(getContext(), "Problems reading json", Toast.LENGTH_SHORT).show();
        }

        // ----- Heat Map Customizations
        // set the color scale
        int[] colors = {
                Color.rgb(0, 0, 255),  //blue
                Color.rgb(251, 255, 0),// yellow
                Color.rgb(255, 0, 0), // red
        };
        // set starting points for each color
        float[] startPoints = {0.1f, 0.5f, 1f};

        // set the Gradient
        Gradient gradient = new Gradient(colors, startPoints);


        provider = new HeatmapTileProvider.Builder().weightedData(list)
                .gradient(gradient).build();
        overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));


    }

    /**
     *  taken from https://developers.google.com/maps/documentation/android-api/utility/heatmap
     * @param resource  the resource file to read the JSON current_nutrient
     * @throws JSONException
     */
    private ArrayList<WeightedLatLng> readJSONData(int resource) throws JSONException{
        ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();

        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for(int i=0; i<array.length(); i++){
            JSONObject object = array.getJSONObject(i);

            double lat = object.getDouble("lat");
            double lng = object.getDouble("long");
            double value = object.getDouble("value");

            list.add(new WeightedLatLng(new LatLng(lat*-1, lng), value));
        }

        return list;
    }

    /**
       * Set the location controls on the map.
       *
       * If the user granted location permissions, then enable My Location Layer & related controls
       * on the map. Otherwise, disable them & set current location to null;
    */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation, 0));
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        // position 0 = current map, position 1 = historic map
        if(pos == 0){
            isCurrentMap = true;
        }
        else{
            isCurrentMap = false;
        }

        // ------------ Replace Heat Map w/ Selected Map
        overlay.remove();
        addHeatMap();
    }

    public void onNothingSelected(AdapterView<?> parent){
        // do nothing
    }
}
