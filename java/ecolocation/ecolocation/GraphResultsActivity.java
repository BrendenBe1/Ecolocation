package ecolocation.ecolocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
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

        //-------- Implementing Widgets
        listViewButton = (Button) findViewById(R.id.bttn_list_view);

        //-------- Setting OnClickListeners
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to next activity: ListViewActivity
                Intent intent = new Intent(GraphResultsActivity.this, ListViewActivity.class);
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

       // barEntries.add(new BarEntry(1, 0f, "Curent"));
        barEntries.add(new BarEntry(1, 10f, "Curent"));
        barEntries.add(new BarEntry(2, 7f, "Present"));
        barEntries.add(new BarEntry(3, 3f, "Change"));
        //barEntries.add(new BarEntry(5, 0f, "Curent"));

        BarDataSet dataSet = new BarDataSet(barEntries, "Date set1");


        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(dataSet);
        data.setBarWidth(.8f);

        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);




        //TODO: delete after tech demo
        googleButton = (Button) findViewById(R.id.bttn_google);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphResultsActivity.this,
                        GoogleDriveDemoActivity.class);
                startActivity(intent);
            }
        });

        dbButton = (Button) findViewById(R.id.bttn_database);
        dbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphResultsActivity.this,
                        DatabaseDemoActivity.class);
                startActivity(intent);
            }
        });
    }

}
