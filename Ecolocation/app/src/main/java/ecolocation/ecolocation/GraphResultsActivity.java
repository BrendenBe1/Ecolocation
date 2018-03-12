package ecolocation.ecolocation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GraphResultsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //widgets
    Button listViewBttn;
    ImageView spatialImg;
    BarChart barChart;

    //animal variables
    ArrayList<Animal> currentMammalList;
    ArrayList<Animal> historicMammalList;

    // spatial map variables
    LatLng chosenLocation;
    double[][] currNutrientMovement = new double[360][180];
    ArrayList<WeightedLatLng> currentNutrientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_results);

        //--------- Get Ecosystem Data
        //need to get coordinates and initialize Ecosystem
        Ecosystem ecosystem = Ecosystem.get(this);
        if(getIntent().hasExtra("COORDS")){
            chosenLocation = getIntent().getExtras().getParcelable("COORDS");
            Log.d("LATITUDE graph: ", String.valueOf(chosenLocation.latitude));

            //get Ecosystem instance and get database info & set coordinates for it
            currentMammalList = ecosystem.getCurrentList(chosenLocation);
            historicMammalList = ecosystem.getHistoricList(chosenLocation);
        }
        else{
            currentMammalList = ecosystem.getCurrentList();
            historicMammalList = ecosystem.getHistoricList();
        }


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
                startActivity(intent);
            }
        });

        spatialImg = (ImageView) findViewById(R.id.img_spatial);


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
        //get a handle to the map fragment
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        currentNutrientList = new ArrayList<WeightedLatLng>();
        displaySpatialMap();

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
    }

    private void displaySpatialMap(){
        Bitmap currentMap = BitmapFactory.decodeResource(getResources(), R.drawable
                .current_nutrient_map);
        int height = currentMap.getWidth();
        int width = currentMap.getHeight();
        int newHeight = 300;
        int newWidth = 300;
        float scaledHeight = ((float) newWidth) / width;
        float scaledWidth = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaledWidth, scaledHeight);
        Bitmap currentMapZoomed = Bitmap.createBitmap(currentMap, 0, 0, width, height, matrix,
                true);
        spatialImg.setImageBitmap(currentMapZoomed);
    }

        @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("Ecosystem has been initialized", true);
    }

    private void createNutrientArray(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("current_nut_mov.txt"));
            String line = br.readLine();

            int row = 0;
            while(line != null){
                String[] rowStrings = line.split(" ");

                for(int col = 0; col<rowStrings.length; col++){
                    double value = Double.valueOf(rowStrings[col];
                    WeightedLatLng coordinateValue = new WeightedLatLng(col, row, value);
                    currentNutrientList.add(coordinateValue);
                }

                row += 1;
            }

            br.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }
}

