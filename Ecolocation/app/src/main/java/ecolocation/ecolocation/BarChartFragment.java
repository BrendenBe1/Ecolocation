package ecolocation.ecolocation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Arrays;

import io.apptik.widget.MultiSlider;


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
    private ArrayList<BarEntry> barEntries;
    private MultiSlider multiSlider1;
    private TextView min, max;
    private double animalMassC[], animalMassH[], whatIf[];
    private int preValueI, preValueD, changes;
    private ArrayList<Animal> animaListC , arrayListH;

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



    private static double sum(double...values){
        double result = 0;
        for (double value: values){
            result += value;
        }
        return result;
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

        Ecosystem ecosystem = Ecosystem.get(getContext());
        animaListC = ecosystem.getCurrentList();
        arrayListH = ecosystem.getHistoricList();

        // ------------ Customize Bar Chart
        barChart = (BarChart) view.findViewById(R.id.barChart);

        //setUpBarChart();

        // Customize slider
        min = (TextView) view.findViewById(R.id.minValue);
        max = (TextView) view.findViewById(R.id.maxValue);
        multiSlider1 = (MultiSlider)view.findViewById(R.id.range_slider);
        setUpBarChart();
        return view;
    }

    /**
     *  All of the customizations for creating the BarChart and the data for it
     */
    private void setUpBarChart(){
        //setup barchart data
        animalMassC = new double[animaListC.size()];
        animalMassH = new double[arrayListH.size()];
        whatIf = new double[animaListC.size()];

        for(int i = 0; i < animaListC.size(); ++i){
            Animal currAnimal = animaListC.get(i);
            animalMassC[i] = currAnimal.getMass();
        }

        for(int i = 0; i < arrayListH.size(); i++){
            Animal historAnimal = arrayListH.get(i);
            animalMassH[i] = historAnimal.getMass();
        }

        Arrays.sort(animalMassC);
        whatIf = animalMassC.clone();
        double nutrDisC = sum(animalMassC);
        double nutrDisH = sum(animalMassH);


        // setting up some of the bar chart's attributes
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.setDescription("");
        barChart.setFitBars(true);
        //barChart.animateY(4000, Easing.EasingOption.EaseInQuart);

        // ---------- Set Up Bars
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, (float) nutrDisC + (float) nutrDisH));
        barEntries.add(new BarEntry(2f, (float) nutrDisC));
        barEntries.add(new BarEntry(3f, (float) changes));

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

        //----------set slider
        if(animalMassC.length != 0)
        {
            multiSlider1.setMax((int)animalMassC[(int)animalMassC.length - 1],true,true);
        }
        else
        {
            multiSlider1.setMax(1);
        }

        min.setText(String.valueOf(multiSlider1.getThumb(0).getValue()));
        max.setText(String.valueOf(multiSlider1.getThumb(1).getValue()));
        multiSlider1.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener()
        {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value)
            {
                if (thumbIndex == 0)
                {
                    min.setText(String.valueOf(multiSlider.getThumb(0).getValue()));
                    if(value > preValueI)
                    {
                        for (int j = 0; j < animaListC.size(); j++)
                        {
                            if (whatIf[j] <= value)
                            {
                                whatIf[j] = 0;
                            }
                        }

                        changes = (int) sum(whatIf);
                        barEntries.remove(2);
                        barEntries.add(new BarEntry(3f, (float) changes));
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    }
                    else if(value < preValueI)
                    {
                        for(int j = 0; j < animaListC.size(); j++)
                        {
                            if(animalMassC[j] >= value && animalMassC[j] <= multiSlider.getThumb(1).getValue())
                            {
                                whatIf[j] = animalMassC[j];
                            }
                        }

                        changes = (int) sum(whatIf);
                        barEntries.remove(2);
                        barEntries.add(new BarEntry(3f, (float) changes));
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    }
                }
                else
                {
                    max.setText(String.valueOf(multiSlider.getThumb(1).getValue()));
                    if(value < preValueD)
                    {
                        for (int j = 0; j < animaListC.size(); j++)
                        {
                            if (whatIf[j] > value)
                            {
                                whatIf[j] = 0;
                            }
                        }

                        changes = (int) sum(whatIf);
                        barEntries.remove(2);
                        barEntries.add(new BarEntry(3f, (float) changes));
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    }
                    else if(value > preValueD)
                    {
                        for(int j = 0; j < animaListC.size(); j++)
                        {
                            if(animalMassC[j] <= value && animalMassC[j] >= multiSlider.getThumb(0).getValue())
                            {
                                whatIf[j] = animalMassC[j];
                            }
                        }

                        changes = (int) sum(whatIf);
                        barEntries.remove(2);
                        barEntries.add(new BarEntry(3f, (float) changes));
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    }
                }

                preValueI = multiSlider.getThumb(0).getValue();
                preValueD = multiSlider.getThumb(1).getValue();
            }
        });
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