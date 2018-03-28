package ecolocation.ecolocation;

//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.github.mikephil.charting.animation.Easing;
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.TileOverlay;
//import com.google.android.gms.maps.model.TileOverlayOptions;
//import com.google.maps.android.heatmaps.Gradient;
//import com.google.maps.android.heatmaps.HeatmapTileProvider;
//import com.google.maps.android.heatmaps.WeightedLatLng;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class GraphResultsActivity extends AppCompatActivity implements OnMapReadyCallback {

//    //widgets
//    Button listViewBttn;
//    BarChart barChart;
//
//    //animal variables
//    ArrayList<Animal> currentMammalList;
//    ArrayList<Animal> historicMammalList;
//
//    // spatial map variables
//    LatLng chosenLocation;
//    double[][] currNutrientMovement = new double[360][180];
//    ArrayList<WeightedLatLng> currentNutrientList;
//
//    GoogleMap map;
//    HeatmapTileProvider provider;
//    TileOverlay overlay;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_graph_results);
//
//        //--------- Get Ecosystem Data
//        //need to get coordinates and initialize Ecosystem
//        Ecosystem ecosystem = Ecosystem.get(this);
//        if(getIntent().hasExtra("COORDS")){
//            chosenLocation = getIntent().getExtras().getParcelable("COORDS");
//            Log.d("LATITUDE graph: ", String.valueOf(chosenLocation.latitude));
//
//            //get Ecosystem instance and get database info & set coordinates for it
//            currentMammalList = ecosystem.getCurrentList(chosenLocation);
//            historicMammalList = ecosystem.getHistoricList(chosenLocation);
//        }
//        else{
//            currentMammalList = ecosystem.getCurrentList();
//            historicMammalList = ecosystem.getHistoricList();
//        }
//
//
//        //----------- Toolbar Setup
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);
//
//        //----------- Widgets
//        listViewBttn = (Button) findViewById(R.id.bttn_list);
//        listViewBttn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(GraphResultsActivity.this,
//                        ListViewActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        //-------- BarChart
//        barChart = (BarChart) findViewById(R.id.barChart);
//
//        barChart.setDrawBarShadow(false);
//        barChart.setDrawValueAboveBar(true);
//        barChart.setMaxVisibleValueCount(50);
//        barChart.setPinchZoom(false);
//        barChart.setDrawGridBackground(true);
//        barChart.setDescription("");
//        barChart.setFitBars(true);
//
//        barChart.animateY(4000, Easing.EasingOption.EaseInQuart);
//
//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//
//        barEntries.add(new BarEntry(1f, 10f));
//        barEntries.add(new BarEntry(2f, 7f));
//        barEntries.add(new BarEntry(3f, 0f));
//
//        BarDataSet dataSet = new BarDataSet(barEntries, "");
//
//        String[] labels = new String[] {"" ,"Historic", "Current", "Change"};
//
//        barChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(labels));
//
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        BarData current_nutrient = new BarData(dataSet);
//        current_nutrient.setBarWidth(.8f);
//
//        barChart.setData(current_nutrient);
//
//        barChart.getAxisRight().setDrawLabels(false);
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f);
//
//        YAxis yLAxis = barChart.getAxisLeft();
//        yLAxis.setAxisMinValue(0f);
//        yLAxis.setLabelCount(6);
//        yLAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//
//        YAxis yRAxis = barChart.getAxisRight();
//        yRAxis.setAxisMinValue(0f);
//
//        //--------- Spatial Map
//        //get a handle to the map fragment
//        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFrag.getMapAsync(this);
//
//        currentNutrientList = new ArrayList<WeightedLatLng>();
//
//    }
//
////    public class MyXAxisValueFormatter implements AxisValueFormatter {
////
////        private String[] mValues;
////
////        public MyXAxisValueFormatter(String[] values) {
////            this.mValues = values;
////        }
////
////        @Override
////        public String getFormattedValue(float value, AxisBase axis) {
////
////            return mValues[(int) value];
////        }
////
////
////        public int getDecimalDigits() { return 0; }
////    }
//
//    //--------- Methods for Creating the Spatial Map
    @Override
    public void onMapReady(GoogleMap map){
//        this.map = map;
//        addHeatMap();
//        updateLocationUI();
    }
//
//    private void addHeatMap(){
//        List<WeightedLatLng> list = null;
//
//        //get the current_nutrient
//        try{
//            list = readJSONData(R.raw.data_current);
//        } catch (JSONException e){
//            Toast.makeText(this, "Problems reading json", Toast.LENGTH_SHORT).show();
//        }
//
//        // create the gradient from bright green to dark green
//        int[] colors = {Color.rgb(255, 0, 0), Color.rgb(51, 0, 0)};
//        float[] startPoints = {0.2f, 1f};   // indicates when to transition
//        Gradient gradient = new Gradient(colors, startPoints);
//
//
//        provider = new HeatmapTileProvider.Builder().weightedData(list)
//                .gradient(gradient).build();
//        //change the radius
//        provider.setRadius(50);
////        provider.setOpacity(1);
//        overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
//
//
//    }
//
//    /**
//     *  taken from https://developers.google.com/maps/documentation/android-api/utility/heatmap
//     * @param resource  the resource file to read the JSON current_nutrient
//     * @throws JSONException
//     */
//    private ArrayList<WeightedLatLng> readJSONData(int resource) throws JSONException{
//        ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
//
//        InputStream inputStream = getResources().openRawResource(resource);
//        String json = new Scanner(inputStream).useDelimiter("\\A").next();
//        JSONArray array = new JSONArray(json);
//        for(int i=0; i<array.length(); i++){
//            JSONObject object = array.getJSONObject(i);
//
//            double lat = object.getDouble("lat");
//            double lng = object.getDouble("long");
//            double value = object.getDouble("value");
//
//            list.add(new WeightedLatLng(new LatLng(lat*-1, lng), value));
//        }
//
//        return list;
//    }
//
//    /*
//       * Set the location controls on the map.
//       *
//       * If the user granted location permissions, then enable My Location Layer & related controls
//       * on the map. Otherwise, disable them & set current location to null;
//    */
//    private void updateLocationUI() {
//        if (map == null) {
//            return;
//        }
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.1982, -111.6513),
//                10));
//        map.getUiSettings().setZoomControlsEnabled(true);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putBoolean("Ecosystem has been initialized", true);
//    }
}
