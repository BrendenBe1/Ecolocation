package ecolocation.ecolocation;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
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

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

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

        // Customize slider
        tital1 = view.findViewById(R.id.tital1);
        tital2 = view.findViewById(R.id.tital2);
        tital1.setVisibility(View.INVISIBLE);
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
        spinner.setBackgroundColor(4);
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
        barEntries2.add(new BarEntry(2f, (float) sum(whatIf)));
        String[] labels2 = new String[] {"" ,"Current", "What If"};
        barChart2.getXAxis().setValueFormatter(new MyXAxisValueFormatter(labels2));

        // ----------- Set Up Bar's Data
        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData current_nutrient = new BarData(dataSet);

        current_nutrient.setBarWidth(.5f);

        barChart1.setData(current_nutrient);

        barChart1.getAxisRight().setDrawLabels(false);

        // ----------- Set Up Bar's Data2

        BarDataSet dataSet2 = new BarDataSet(barEntries2, "");
        dataSet2.setDrawValues(false);
        dataSet2.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData whatIf_Nutrient = new BarData(dataSet2);

        whatIf_Nutrient.setBarWidth(.5f);

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
                    }
                }
                changes = (int) sum(whatIf);
                barEntries2.remove(1);
                barEntries2.add(new BarEntry(2f, (float) changes));
                barChart2.notifyDataSetChanged();
                barChart2.invalidate();
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
                                    if (threatLevel[j].getValue() >= 4) {
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
                                    if (threatLevel[j].getValue() >= 4) {
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

class MultiSpinner extends AppCompatSpinner implements DialogInterface.OnCancelListener {
    private static final String TAG = MultiSpinnerSearch.class.getSimpleName();
    private List<KeyPairBoolData> items;
    private String defaultText = "";
    private String spinnerTitle = "";
    private SpinnerListener listener;
    private int limit = -1;
    private int selected = 0;
    private LimitExceedListener limitListener;
    MyAdapter adapter;
    public static AlertDialog.Builder builder;
    public static AlertDialog ad;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        TypedArray a = arg0.obtainStyledAttributes(arg1, R.styleable.MultiSpinnerSearch);
        for (int i = 0; i < a.getIndexCount(); ++i) {
            int attr = a.getIndex(i);
            if (attr == com.androidbuts.multispinnerfilter.R.styleable.MultiSpinnerSearch_hintText) {
                spinnerTitle = a.getString(attr);
                defaultText = spinnerTitle;
                break;
            }
        }
        Log.i(TAG, "spinnerTitle: "+spinnerTitle);
        a.recycle();
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    public void setLimit(int limit, LimitExceedListener listener) {
        this.limit = limit;
        this.limitListener = listener;
    }

    public List<KeyPairBoolData> getSelectedItems() {
        List<KeyPairBoolData> selectedItems = new ArrayList<>();
        for(KeyPairBoolData item : items){
            if(item.isSelected()){
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public List<Long> getSelectedIds() {
        List<Long> selectedItemsIds = new ArrayList<>();
        for(KeyPairBoolData item : items){
            if(item.isSelected()){
                selectedItemsIds.add(item.getId());
            }
        }
        return selectedItemsIds;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner

        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                spinnerBuffer.append(items.get(i).getName());
                spinnerBuffer.append(", ");
            }
        }

        String spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        else
            spinnerText = defaultText;

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{spinnerText});
        setAdapter(adapterSpinner);

        if (adapter != null)
            adapter.notifyDataSetChanged();

        listener.onItemsSelected(items);
    }

    @Override
    public boolean performClick() {

        builder = new AlertDialog.Builder(getContext(), com.androidbuts.multispinnerfilter.R.style.myDialog);
        builder.setTitle(spinnerTitle);

        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(com.androidbuts.multispinnerfilter.R.layout.alert_dialog_listview_search, null);
        builder.setView(view);

        final ListView listView = (ListView) view.findViewById(R.id.tital2);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setFastScrollEnabled(false);
        adapter = new MyAdapter(getContext(), items);
        listView.setAdapter(adapter);

        final TextView emptyText = (TextView) view.findViewById(com.androidbuts.multispinnerfilter.R.id.empty);
        listView.setEmptyView(emptyText);

        final EditText editText = (EditText) view.findViewById(com.androidbuts.multispinnerfilter.R.id.alertSearchEditText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.i(TAG, " ITEMS : " + items.size());
                dialog.cancel();
            }
        });

        builder.setOnCancelListener(this);
        ad = builder.show();
        return true;
    }

    public void setItems(List<KeyPairBoolData> items, int position, SpinnerListener listener) {

        this.items = items;
        this.listener = listener;

        StringBuilder spinnerBuffer = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                spinnerBuffer.append(items.get(i).getName());
                spinnerBuffer.append(", ");
            }
        }
        if (spinnerBuffer.length() > 2)
            defaultText = spinnerBuffer.toString().substring(0, spinnerBuffer.toString().length() - 2);

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), com.androidbuts.multispinnerfilter.R.layout.textview_for_spinner, new String[]{defaultText});
        setAdapter(adapterSpinner);

        if (position != -1) {
            items.get(position).setSelected(true);
            //listener.onItemsSelected(items);
            onCancel(null);
        }
    }

    public interface LimitExceedListener {
        void onLimitListener(KeyPairBoolData data);
    }

    //Adapter Class
    public class MyAdapter extends BaseAdapter implements Filterable {

        List<KeyPairBoolData> arrayList;
        List<KeyPairBoolData> mOriginalValues; // Original Values
        LayoutInflater inflater;

        public MyAdapter(Context context, List<KeyPairBoolData> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView textView;
            CheckBox checkBox;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "getView() enter");
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(com.androidbuts.multispinnerfilter.R.layout.item_listview_multiple,  parent, false);
                holder.textView = (TextView) convertView.findViewById(com.androidbuts.multispinnerfilter.R.id.alertTextView);
                holder.checkBox = (CheckBox) convertView.findViewById(com.androidbuts.multispinnerfilter.R.id.alertCheckbox);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final int backgroundColor = (position%2 == 0) ? com.androidbuts.multispinnerfilter.R.color.list_even : com.androidbuts.multispinnerfilter.R.color.list_odd;
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));

            final KeyPairBoolData data = arrayList.get(position);

            holder.textView.setText(data.getName());
            holder.textView.setTypeface(null, Typeface.NORMAL);
            holder.checkBox.setChecked(data.isSelected());

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(data.isSelected()) { // deselect
                        selected--;
                    } else if(selected == limit) { // select with limit
                        if(limitListener != null)
                            limitListener.onLimitListener(data);
                        return;
                    } else { // selected
                        selected++;
                    }

                    final ViewHolder temp = (ViewHolder) v.getTag();
                    temp.checkBox.setChecked(!temp.checkBox.isChecked());

                    data.setSelected(!data.isSelected());
                    Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
                    notifyDataSetChanged();
                }
            });
            if (data.isSelected()) {
                holder.textView.setTypeface(null, Typeface.BOLD);
                holder.textView.setTextColor(Color.WHITE);
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
            holder.checkBox.setTag(holder);

            return convertView;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    arrayList = (List<KeyPairBoolData>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    List<KeyPairBoolData> FilteredArrList = new ArrayList<>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(arrayList); // saves the original data in mOriginalValues
                    }

                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            Log.i(TAG, "Filter : " + mOriginalValues.get(i).getName() + " -> " + mOriginalValues.get(i).isSelected());
                            String data = mOriginalValues.get(i).getName();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
        }
    }
}
