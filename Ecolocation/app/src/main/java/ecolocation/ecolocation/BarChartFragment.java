package ecolocation.ecolocation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by Chandler on 3/19/2018.
 */

public class BarChartFragment extends Fragment {
    private BarChart barChart;

    public static BarChartFragment newInstance(){
        BarChartFragment fragment = new BarChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_bar_chart, container,
                false);

        // ------------ Customize Bar Chart
        barChart = (BarChart) view.findViewById(R.id.barChart);

        return view;
    }

    private void setUpBarChart(){
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.setDescription("");
        barChart.setFitBars(true);
        barChart.animateY(4000, Easing.EasingOption.EaseInQuart);

        // ---------- Set Up Bars
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, 10f));
        barEntries.add(new BarEntry(2f, 7f));
        barEntries.add(new BarEntry(3f, 0f));

        // ----------- Set Up Bar's Data
        String[] labels = new String[] {"" ,"Historic", "Current", "Change"};
        barChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(labels));

        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData current_nutrient = new BarData(dataSet);

        current_nutrient.setBarWidth(.8f);

        barChart.setData(current_nutrient);

        barChart.getAxisRight().setDrawLabels(false);

        // ----------- Set Up Axis
        // x-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // y-axis
        YAxis yLAxis = barChart.getAxisLeft();
        yLAxis.setAxisMinValue(0f);
        yLAxis.setLabelCount(6);
        yLAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        YAxis yRAxis = barChart.getAxisRight();
        yRAxis.setAxisMinValue(0f);
    }
}

class MyXAxisValueFormatter implements AxisValueFormatter {

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