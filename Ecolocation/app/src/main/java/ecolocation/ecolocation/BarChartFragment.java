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
 *  Created by Chandler on 3/19/2018.
 *
 *  This class builds and sets up a bar chart to display the nutrient movement of the current
 *  ecosystem and the historic version of the ecosystem (from the Pleistocene Era). If the user
 *  wants to explore "what-if" scenarios (removing sets of populations) then it will display a third
 *  bar representing the nutrient movement of the hypothetical situation
 */

public class BarChartFragment extends Fragment {
    private BarChart barChart;

    /**
     *  Encapsulates the creation of a BarChartFragment. It returns an instance of a
     *  BarChartFragment.
     *
     * @return  BarChartFragment
     */
    public static BarChartFragment newInstance(){
        BarChartFragment fragment = new BarChartFragment();
        return fragment;
    }

    /**
     *  Creating the view the fragment will displayed in and wiring up the BarChart widget.
     *
     * @param inflater              used to inflate the view
     * @param container             the parent view that the fragment's UI should be attached to
     * @param savedInstanceState    if it's not null, this is a previous stat's version
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_bar_chart, container,
                false);

        // ------------ Customize Bar Chart
        barChart = (BarChart) view.findViewById(R.id.barChart);

        setUpBarChart();

        return view;
    }

    /**
     *  All of the customizations for creating the BarChart and the data for it
     */
    private void setUpBarChart(){
        // setting up some of the bar chart's attributes
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

/**
 * This class formats the X-Axis for the Bar Chart
 */
class MyXAxisValueFormatter implements AxisValueFormatter {
    private String[] mValues;

    // constructor
    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        return mValues[(int) value];
    }


    public int getDecimalDigits() { return 0; }
}