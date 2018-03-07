package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chandler on 3/6/2018.
 *
 * For tab layout:
 *      https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout#create-fragment
 */

public class ListViewFragment extends Fragment {
    //widgets
    private TextView textView;
    ListView listView;

    //variables for creating the list
    private ArrayList<Animal> animalList;
    private AnimalAdapter adapter;

    private static final String ARG_PAGE = "ARG_PAGE";
    /**
     * 1 = current mammals
     * 2 = historic mammals
     */
    private int pageType;
    private AnimalType animalType;

    public static ListViewFragment newInstance(int page){
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Get the arguments to determine which list to display
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        pageType = getArguments().getInt(ARG_PAGE);
    }

    /**
     * Initialize widgets and other UI elements
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        //---------- Setting Up Widgets
        //get animal list
        if(pageType == 0){
            //use current mammals list
            animalList = Ecosystem.get(getContext()).getCurrentList();
            animalType = AnimalType.CURRENT_MAMMAL;
        }
        else{
            animalList = Ecosystem.get(getContext()).getHistoricList();
            animalType = AnimalType.HISTORIC_MAMMAL;
        }

        //-------- Implementing Widgets
        listView = (ListView) view.findViewById(R.id.list_view);

        // ----------- Adapter Stuff
        // setting up the individual list items with the adapter
        adapter = new AnimalAdapter(getContext(), animalList);
        Ecosystem ecosystem = Ecosystem.get(getContext());
        ecosystem.setAdapter(adapter);
        //the adapter fills the list with the array list
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal currAnimal = animalList.get(position);
                Intent intent = DetailActivity.newIntent(getActivity(),
                        currAnimal.getBinomial(), animalType);

                startActivity(intent);
            }
        });


        return view;
    }









}
