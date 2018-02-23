package ecolocation.ecolocation;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Random;

public class GraphResultsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //widgets
    //TODO: delete these buttons after tech demo
    Button listViewBttn;
    BarChart barChart;

    //Spatial Map Variables
    private String[] colorScale;
    private  double[] latitudes = new double[30];  //9.607168 9.761728 = 94
    private double[] longitudes = new double [30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_results);

        final LatLng chosenLocation = getIntent().getExtras().getParcelable("COORDS");
        Log.d("LATITUDE graph: ", String.valueOf(chosenLocation.latitude));

        //----------- Toolbar Setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //----------- Widgets
        listViewBttn = (Button) findViewById(R.id.bttn_list);
        listViewBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphResultsActivity.this,
                        ListViewActivity.class);
                intent.putExtra("COORDS", chosenLocation);
                startActivity(intent);
            }
        });


        //-------- BarChart
        barChart = (BarChart) findViewById(R.id.barChart);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.setDescription("");
        barChart.setFitBars(true);

        barChart.animateY(4000, Easing.EasingOption.EaseInQuart);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(1f, 10f));
        barEntries.add(new BarEntry(2f, 7f));
        barEntries.add(new BarEntry(3f, 0f));

        BarDataSet dataSet = new BarDataSet(barEntries, "");

        String[] labels = new String[] {"" ,"Historic", "Current", "Change"};

        barChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(labels));

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(dataSet);
        data.setBarWidth(.8f);

        barChart.setData(data);

        barChart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        YAxis yLAxis = barChart.getAxisLeft();
        yLAxis.setAxisMinValue(0f);
        yLAxis.setLabelCount(6);
        yLAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        YAxis yRAxis = barChart.getAxisRight();
        yRAxis.setAxisMinValue(0f);

        //--------- Spatial Map
        //Possible colors for a pixel
        colorScale = new String[] {"#E8F5E9","#C8E6C9", "#A5D6A7", "#81C784", "#66BB6A", "#4CAF50",
                "#43A047", "#388E3C", "#2E7D32", "#1B5E20"};

        //get a handle to the map fragment
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

    public class MyXAxisValueFormatter implements AxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            return mValues[(int) value];
        }


        public int getDecimalDigits() { return 0; }
    }

    //--------- Methods for Creating the Spatial Map
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

    //TODO: replace with actual data
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

