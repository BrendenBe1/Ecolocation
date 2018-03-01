package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailFragment extends android.support.v4.app.Fragment {
    //widgets
    ImageView animalPic;
    TextView nameText;
    TextView descText;
    TextView wikiLink;
    TextView massText;
    TextView populationText;
    TextView dietText;
    TextView endangeredLevel;

    private Animal animal;

    //constants
    static final String SELECTED_ANIMAL = "selected animal";

    //this is necessary b/c fragment arguments (similar to activity extras) cannot be added to the
    //fragment before it's added to the FragmentManager's transactions list. This allows the app to
    // do that
    public static DetailFragment newInstance(String animalBinomial){
        Bundle args = new Bundle();
        args.putString(SELECTED_ANIMAL, animalBinomial);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String animalBinomial = getArguments().getString(SELECTED_ANIMAL);
        animal = Ecosystem.get(getActivity()).getAnimal(animalBinomial);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //------------- implementing widgets
        animalPic = (ImageView) view.findViewById(R.id.pic_animal);
        nameText = (TextView) view.findViewById(R.id.txt_animal_name);
        descText = (TextView) view.findViewById(R.id.txt_desc);
        wikiLink = (TextView) view.findViewById(R.id.txt_wiki_link);
        massText = (TextView) view.findViewById(R.id.txt_mass);
        populationText = (TextView) view.findViewById(R.id.txt_population);
        dietText = (TextView) view.findViewById(R.id.txt_diet);
        endangeredLevel = (TextView) view.findViewById(R.id.txt_endangered_level);

        //set contents of widgets
        //TODO: uncomment the below lines when the information is available
        animalPic.setImageDrawable(animal.getPicture());
        nameText.setText(capitalize(animal.getBinomial()));
        descText.setText(capitalize(animal.getDescription()));
//        wikiLink.setText(animal.get);
        massText.setText(String.valueOf(animal.getMass()));
        endangeredLevel.setText(capitalize(animal.getThreatLevel().getName()));


        return view;
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

    //--------- Menu
    //used to go back to the ListView Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        //go to ListView Activity
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getActivity(), ListViewActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
