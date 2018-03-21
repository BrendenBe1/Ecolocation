package ecolocation.ecolocation;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    public void updateListView(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }


}

class AnimalAdapter extends ArrayAdapter<Animal> {
    private final Context context;
    private final ArrayList<Animal> animalList;

    public AnimalAdapter(Context context, ArrayList<Animal> animalList){
        super(context, R.layout.list_item, animalList);

        this.context = context;
        this.animalList = animalList;
    }

    /*
    * This method puts each item of currentMammalList into a list_item.
    * It's responsible for handling the current_nutrient and the view of the current_nutrient
    */
    @Override
    public View getView(final int position, View view, ViewGroup parent){
        //create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //-------- Get Animal object from ArrayList
        Animal currAnimal = animalList.get(position);


        //get the rowView from the inflater
        //The rowView allows access to the widgets on the layout
        // need this to be the solution set for tomorrow.
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        //------------- Initializing Widgets from list_item
        ImageView animalPic = (ImageView) rowView.findViewById(R.id.pic_animal);
        TextView nameText = (TextView) rowView.findViewById(R.id.txt_animal_name);
        TextView binomialText = (TextView) rowView.findViewById(R.id.txt_binomial_title);

        //----------- Setting Up Values of Widgets
        animalPic.setImageDrawable(currAnimal.getPicture());
        nameText.setText(capitalize(currAnimal.getName()));
        binomialText.setText(capitalize(currAnimal.getBinomial()));

        //------- Color-Code rows
        Resources res = getContext().getResources();    //allows access to the color files
        switch (currAnimal.getThreatLevel().getName()){
            case "Least Concern":
                nameText.setTextColor(res.getColor(R.color.leastConcern));
                break;
            case "Near Threatened":
                nameText.setTextColor(res.getColor(R.color.nearThreatened));
                break;
            case "Vulnerable":
                nameText.setTextColor(res.getColor(R.color.vulnerable));
                break;
            case "Endangered":
                nameText.setTextColor(res.getColor(R.color.endagered));
                break;
            case "Critically Endangered":
                nameText.setTextColor(res.getColor(R.color.criticallyEndangered));
                break;
            case "Extinct in the Wild":
                nameText.setTextColor(res.getColor(R.color.extinctInTheWild));
                break;
            case "Extinct":
                nameText.setTextColor(res.getColor(R.color.extinct));
                break;
            case "Not Evaluated":
                nameText.setTextColor(res.getColor(R.color.notEvaluated));
                break;
            case "Data Deficient":
                nameText.setTextColor(res.getColor(R.color.dataDeficient));
                break;
            default:
                nameText.setTextColor(res.getColor(R.color.notEvaluated));
        }

        return rowView;
    }

    //capitalize each word
    public static String capitalize(String str){
        //TODO: check if null

        String capitalized = "";
        String[] parts = str.split(" ");
        for(int i=0; i<parts.length; i++){
            String temp = parts[i].substring(0, 1).toUpperCase();
            temp = temp + parts[i].substring(1) + " ";
            capitalized += temp;
        }

        return capitalized;
    }
}
