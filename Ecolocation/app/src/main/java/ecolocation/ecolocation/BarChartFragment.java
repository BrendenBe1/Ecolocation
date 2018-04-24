package ecolocation.ecolocation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.*;
import android.widget.*;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.lang.Comparable;

import io.apptik.widget.MultiSlider;

/**
 *  Created by Chandler on 3/19/2018.
 *
 *  This class builds and sets up a bar chart to display the nutrient movement of the current
 *  ecosystem and the historic version of the ecosystem (from the Pleistocene Era). If the user
 *  wants to explore "what-if" scenarios (removing sets of populations) then it will display a third
 *  bar representing the nutrient movement of the hypothetical situation
 */

public class BarChartFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private BarChart barChart1, barChart2;
    private ArrayList<BarEntry> barEntries , barEntries2;
    private MultiSlider multiSlider1;
    private TextView min, max, first, midle, last, tital1, tital2;
    private Space space;
    private Integer threatLevelValueS[], threatLevelValueR[];
    private double whatIf[], animalMassC[];
    private ThreatLevel threatLevel[];
    private int preValueI, preValueD, changes;
    private ArrayList<Animal> animaListC , arrayListH;
    private Spinner spinner1;
    private MultiSpinnerSearch spinner;

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
        barChart1 = view.findViewById(R.id.barChart);
        barChart2 = view.findViewById(R.id.barChart2);
        barChart2.setVisibility(View.INVISIBLE);

        //setUpBarChart();

        // Customize slider
        tital1 = view.findViewById(R.id.tital1);
        tital2 = view.findViewById(R.id.tital2);
        tital1.setVisibility(View.INVISIBLE);
        space = view.findViewById(R.id.space);
        space.setVisibility(View.INVISIBLE);
        min = view.findViewById(R.id.minValue);
        min.setVisibility(View.INVISIBLE);
        max = view.findViewById(R.id.maxValue);
        max.setVisibility(View.INVISIBLE);
        first = view.findViewById(R.id.first);
        first.setVisibility(View.INVISIBLE);
        midle = view.findViewById(R.id.midle);
        midle.setVisibility(View.INVISIBLE);
        last = view.findViewById(R.id.last);
        last.setVisibility(View.INVISIBLE);

        multiSlider1 = view.findViewById(R.id.range_slider);
        multiSlider1.setVisibility(View.INVISIBLE);

        //setup spinner
        spinner = view.findViewById(R.id.spinner);
        spinner.setVisibility(View.INVISIBLE);
        spinner1 = view.findViewById(R.id.spinner1);

        String[] BarCharts = {"Bar chart", "What If"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, BarCharts);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(this);
        dropDownList();
        setUpBarChart();

        return view;
    }

    /**
     *  All of the customizations for creating the BarChart and the data for it
     */
    private void setUpBarChart(){
        //setup barchart data
        threatLevelValueS = new Integer[5];
        threatLevelValueR = new Integer[animaListC.size()];
        animalMassC = new double[animaListC.size()];
        threatLevel = new ThreatLevel[animaListC.size()];
        double[] animalMassH = new double[arrayListH.size()];
        whatIf = new double[animaListC.size()];

        for(int i = 0; i < animaListC.size(); ++i){
            Animal currAnimal = animaListC.get(i);
            animalMassC[i] = currAnimal.getMass();
            threatLevel[i] = currAnimal.getThreatLevel();
            threatLevelValueR[i] = 111;
        }

        for(int i = 0; i < arrayListH.size(); i++){
            Animal historAnimal = arrayListH.get(i);
            animalMassH[i] = historAnimal.getMass();
        }

        for(int i=0; i<animaListC.size()-1; i++){
            for(int j=0; j<animaListC.size()-i-1; j++){
                if (animalMassC[j]>animalMassC[j+1]){
                    double temp = animalMassC[j];
                    ThreatLevel tempt = threatLevel[j];
                    animalMassC[j] = animalMassC[j+1];
                    threatLevel[j] = threatLevel[j+1];
                    animalMassC[j+1] = temp;
                    threatLevel[j+1] = tempt;
                }
            }
        }

        whatIf = animalMassC.clone();
        double nutrDisC = sum(animalMassC);
        double nutrDisH = sum(animalMassH);


        // setting up some of the bar chart1's attributes
        barChart1.setDrawBarShadow(false);
        barChart1.setMaxVisibleValueCount(50);
        barChart1.setPinchZoom(false);
        barChart1.setDrawGridBackground(false);
        barChart1.setDescription("");
        barChart1.setFitBars(true);

        // setting up some of the bar chart1's attributes
        barChart2.setDrawBarShadow(false);
        barChart2.setMaxVisibleValueCount(50);
        barChart2.setPinchZoom(false);
        barChart2.setDrawGridBackground(false);
        barChart2.setDescription("");
        barChart2.setFitBars(true);



        // ---------- Set Up Bars1
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, (float) nutrDisC + (float) nutrDisH));
        barEntries.add(new BarEntry(2f, (float) nutrDisC));
        String[] labels = new String[] {"" ,"Historic", "Current"};
        barChart1.getXAxis().setValueFormatter(new MyXAxisValueFormatter(labels));

        // ---------- Set Up Bars2
        barEntries2 = new ArrayList<>();
        barEntries2.add(new BarEntry(1f, (float) nutrDisC));
        barEntries2.add(new BarEntry(2f, (float) changes));
        String[] labels2 = new String[] {"" ,"Current", "What If"};
        barChart2.getXAxis().setValueFormatter(new MyXAxisValueFormatter(labels2));

        // ----------- Set Up Bar's Data
        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData current_nutrient = new BarData(dataSet);

        current_nutrient.setBarWidth(.8f);

        barChart1.setData(current_nutrient);

        barChart1.getAxisRight().setDrawLabels(false);

        // ----------- Set Up Bar's Data2

        BarDataSet dataSet2 = new BarDataSet(barEntries2, "");
        dataSet2.setDrawValues(false);
        dataSet2.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData whatIf_Nutrient = new BarData(dataSet2);

        whatIf_Nutrient.setBarWidth(.8f);

        barChart2.setData(whatIf_Nutrient);

        barChart2.getAxisRight().setDrawLabels(false);

        // ----------- Set Up Axis
        //x-axis
        XAxis xAxis = barChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // x-axis2
        XAxis xAxis2 = barChart1.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(false);
        xAxis2.setGranularity(1f);

        //y-axis
        YAxis yLAxis = barChart1.getAxisLeft();
        yLAxis.setAxisMinValue(0f);
        yLAxis.setLabelCount(6);
        yLAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        YAxis yRAxis = barChart1.getAxisRight();
        yRAxis.setAxisMinValue(0f);


        // y-axis2
        YAxis yLAxis2 = barChart2.getAxisLeft();
        yLAxis2.setAxisMinValue(0f);
        yLAxis2.setLabelCount(6);
        yLAxis2.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        YAxis yRAxis2 = barChart2.getAxisRight();
        yRAxis2.setAxisMinValue(0f);


        //slider
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
                            if (whatIf[j] <= value && !Arrays.asList(threatLevelValueS).contains(threatLevelValueR[j]))
                            {
                                whatIf[j] = 0;
                                threatLevelValueR[j] = 111;
                            }
                        }

                        changes = (int) sum(whatIf);
                        barEntries2.remove(1);
                        barEntries2.add(new BarEntry(2f, (float) changes));
                        barChart2.notifyDataSetChanged();
                        barChart2.invalidate();
                    }
                    else if(value < preValueI)
                    {
                        for(int j = 0; j < animaListC.size(); j++)
                        {
                            if(animalMassC[j] >= value && animalMassC[j] <= multiSlider.getThumb(1).getValue() && !Arrays.asList(threatLevelValueS).contains(threatLevelValueR[j]))
                            {
                                whatIf[j] = animalMassC[j];
                                threatLevelValueR[j] = threatLevel[j].getValue() + 1;;
                            }

                        }

                        changes = (int) sum(whatIf);
                        barEntries2.remove(1);
                        barEntries2.add(new BarEntry(2f, (float) changes));
                        barChart2.notifyDataSetChanged();
                        barChart2.invalidate();
                    }
                }
                else
                {
                    max.setText(String.valueOf(multiSlider.getThumb(1).getValue()));
                    if(value < preValueD)
                    {
                        for (int j = 0; j < animaListC.size(); j++)
                        {
                            if (whatIf[j] > value && !Arrays.asList(threatLevelValueS).contains(threatLevelValueR[j]))
                            {
                                whatIf[j] = 0;
                                threatLevelValueR[j] = 111;
                            }
                        }

                        changes = (int) sum(whatIf);
                        barEntries2.remove(1);
                        barEntries2.add(new BarEntry(2f, (float) changes));
                        barChart2.notifyDataSetChanged();
                        barChart2.invalidate();
                    }
                    else if(value > preValueD)
                    {
                        for(int j = 0; j < animaListC.size(); j++)
                        {
                            if(animalMassC[j] <= value && animalMassC[j] >= multiSlider.getThumb(0).getValue() && !Arrays.asList(threatLevelValueS).contains(threatLevelValueR[j]))
                            {
                                whatIf[j] = animalMassC[j];
                                threatLevelValueR[j] = threatLevel[j].getValue() + 1;
                            }
                        }

                        changes = (int) sum(whatIf);
                        barEntries2.remove(1);
                        barEntries2.add(new BarEntry(2f, (float) changes));
                        barChart2.notifyDataSetChanged();
                        barChart2.invalidate();
                    }
                }
                preValueI = multiSlider.getThumb(0).getValue();
                preValueD = multiSlider.getThumb(1).getValue();
            }
        });


    }

    private void dropDownList(){


        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.endangered_level_array));
        final List<KeyPairBoolData> listArray = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray.add(h);
        }

        spinner.setItems(listArray, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> list) {
                multiSlider1.repositionThumbs();
                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).isSelected()) {

                        switch (list.get(i).getName()) {
                            case "Least Concern":
                                threatLevelValueS[0] = 1;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 0) {
                                        whatIf[j] = 0;
                                    }
                                }
                                break;

                            case "Near Threatened":
                                threatLevelValueS[1] = 2;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 1) {
                                        whatIf[j] = 0;
                                    }
                                }
                                break;

                            case "Vulnerable":
                                threatLevelValueS[2] = 3;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 2) {
                                        whatIf[j] = 0;
                                    }
                                }
                                break;

                            case "Endangered":
                                threatLevelValueS[3] = 4;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 3) {
                                        whatIf[j] = 0;
                                    }
                                }
                                break;

                            case "Critically Endangered":
                                threatLevelValueS[4] = 5;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 4) {
                                        whatIf[j] = 0;
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    } else {

                        switch (list.get(i).getName()) {
                            case "Least Concern":
                                threatLevelValueS[0] = 0;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 0) {
                                        whatIf[j] = animalMassC[j];
                                    }
                                }
                                break;

                            case "Near Threatened":
                                threatLevelValueS[1] = 0;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 1) {
                                        whatIf[j] = animalMassC[j];
                                    }
                                }
                                break;

                            case "Vulnerable":
                                threatLevelValueS[2] = 0;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 2) {
                                        whatIf[j] = animalMassC[j];
                                    }
                                }
                                break;

                            case "Endangered":
                                threatLevelValueS[3] = 0;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 3) {
                                        whatIf[j] = animalMassC[j];
                                    }
                                }
                                break;

                            case "Critically Endangered":
                                threatLevelValueS[4] = 0;
                                for (int j = 0; j < animaListC.size(); j++) {
                                    if (threatLevel[j].getValue() == 4) {
                                        whatIf[j] = animalMassC[j];
                                    }
                                }
                                break;

                            default:
                                break;

                        }
                    }
                    changes = (int) sum(whatIf);
                    barEntries2.remove(1);
                    barEntries2.add(new BarEntry(2f, (float) changes));
                    barChart2.notifyDataSetChanged();
                    barChart2.invalidate();
                }

            }
        });


    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){

        if(pos == 0){
            barChart1.setVisibility(View.VISIBLE);
            barChart2.setVisibility(View.INVISIBLE);
            tital1.setVisibility(View.INVISIBLE);
            tital2.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.INVISIBLE);
            multiSlider1.setVisibility(View.INVISIBLE);
            max.setVisibility(View.INVISIBLE);
            min.setVisibility(View.INVISIBLE);
            first.setVisibility(View.INVISIBLE);
            midle.setVisibility(View.INVISIBLE);
            last.setVisibility(View.INVISIBLE);
            space.setVisibility(View.INVISIBLE);

        }
        else{
            barChart1.setVisibility(View.INVISIBLE);
            tital1.setVisibility(View.VISIBLE);
            tital2.setVisibility(View.INVISIBLE);
            barChart2.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            multiSlider1.setVisibility(View.VISIBLE);
            max.setVisibility(View.VISIBLE);
            min.setVisibility(View.VISIBLE);
            first.setVisibility(View.VISIBLE);
            midle.setVisibility(View.VISIBLE);
            last.setVisibility(View.VISIBLE);
            space.setVisibility(View.VISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent){
        // do nothing
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
