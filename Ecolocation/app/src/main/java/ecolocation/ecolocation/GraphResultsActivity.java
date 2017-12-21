package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

public class GraphResultsActivity extends AppCompatActivity {
    //widgets
    Button listViewButton;
    //TODO: delete these buttons after tech demo
    Button googleButton;
    Button dbButton;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_results);

        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

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







        //TODO: delete after tech demo
        googleButton = (Button) findViewById(R.id.bttn_graph);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphResultsActivity.this,
                        SpatialMapActivity.class);
                startActivity(intent);
            }
        });

        dbButton = (Button) findViewById(R.id.bttn_database);
        dbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphResultsActivity.this,
                        ListViewActivity.class);
                startActivity(intent);
            }
        });

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

}

