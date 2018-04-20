package ecolocation.ecolocation;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by Chandler on 3/19/2018.
 */

public class SpatialMapFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // widget
    private Spinner spinner;
    private ImageView mapImg;

    private boolean isCurrentMap = true;   //when true show current map, when false show historic

    public static SpatialMapFragment newInstance(){
        SpatialMapFragment fragment = new SpatialMapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_spatial_mapping, container,
                false);

        mapImg = (ImageView) view.findViewById(R.id.map_img);

        // ------------ Set Up Spinner to set up which map to show
        spinner = (Spinner) view.findViewById(R.id.spinner);

        //set up list of values to show in spinner
        String[] mapStrings = {"Current Nutrient Dispersal Potential", "Historic Nutrient Dispersal"
        + " Potential"};

        // set up adapter & the layout type for its list of values
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout
                .simple_spinner_item, mapStrings);
        //set up layout for when it drops down
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // add listener
        spinner.setOnItemSelectedListener(this);

        return view;
    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        Resources res = getActivity().getResources();
        // position 0 = current map, position 1 = historic map
        if(pos == 0){
            isCurrentMap = true;
            mapImg.setImageDrawable(res.getDrawable(R.drawable.cropped_global_nutrient_current));
        }
        else{
            isCurrentMap = false;
            mapImg.setImageDrawable(res.getDrawable(R.drawable.cropped_global_nutrient_past));

        }
    }

    public void onNothingSelected(AdapterView<?> parent){
        // do nothing
    }
}