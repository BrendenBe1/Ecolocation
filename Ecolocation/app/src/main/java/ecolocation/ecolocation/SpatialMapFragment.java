package ecolocation.ecolocation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

public class SpatialMapFragment extends Fragment implements OnMapReadyCallback {
    // google maps variables
    private GoogleMap map;
    private ArrayList<WeightedLatLng> currentNutrientList;
    private HeatmapTileProvider provider;
    private TileOverlay overlay;

    public static SpatialMapFragment newInstance(){
        SpatialMapFragment fragment = new SpatialMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_spatial_mapping, container,
                false);

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map){
        this.map = map;
        addHeatMap();
        updateLocationUI();
    }

    private void addHeatMap(){
        List<WeightedLatLng> list = null;

        //get the current_nutrient
        try{
            list = readJSONData(R.raw.data_current);
        } catch (JSONException e){
            Toast.makeText(getContext(), "Problems reading json", Toast.LENGTH_SHORT).show();
        }

        // create the gradient from bright green to dark green
        int[] colors = {Color.rgb(255, 0, 0), Color.rgb(51, 0, 0)};
        float[] startPoints = {0.2f, 1f};   // indicates when to transition
        Gradient gradient = new Gradient(colors, startPoints);


        provider = new HeatmapTileProvider.Builder().weightedData(list)
                .gradient(gradient).build();
        //change the radius
        provider.setRadius(50);
//        provider.setOpacity(1);
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

    /*
       * Set the location controls on the map.
       *
       * If the user granted location permissions, then enable My Location Layer & related controls
       * on the map. Otherwise, disable them & set current location to null;
    */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.1982, -111.6513),
                8));
        map.getUiSettings().setZoomControlsEnabled(true);
    }
}
