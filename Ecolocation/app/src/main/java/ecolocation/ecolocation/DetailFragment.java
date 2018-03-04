package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailFragment extends android.support.v4.app.Fragment {
    //widgets
    private ImageView animalPic;
    private TextView binomialText;
    private TextView nameText;
    private TextView descText;
    private TextView wikiLink;
    private TextView massText;
    private TextView endangeredLevel;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //------------- implementing widgets
        animalPic = (ImageView) view.findViewById(R.id.pic_animal);
        binomialText = (TextView) view.findViewById(R.id.txt_binomial);
        nameText = (TextView) view.findViewById(R.id.txt_animal_name);
        descText = (TextView) view.findViewById(R.id.txt_desc);
        wikiLink = (TextView) view.findViewById(R.id.txt_wiki_link);
        massText = (TextView) view.findViewById(R.id.txt_mass);
        endangeredLevel = (TextView) view.findViewById(R.id.txt_endangered_level);

        //set contents of widgets
        animalPic.setImageDrawable(animal.getPicture());
        nameText.setText(animal.getName());
        binomialText.setText(capitalize(animal.getBinomial()));
        massText.setText(String.valueOf(animal.getMass()) + " kg");
        endangeredLevel.setText(capitalize(animal.getThreatLevel().getName()));

        //-------------- Handling Cases For Empty Description and/or Wiki Links
        //if the description is empty, then set the TextView to says so
        if(animal.getDescription().equals("")){
            descText.setText("No description available");
        }
        else{
            descText.setText(animal.getDescription());
        }

        //create onclick handler for wikiLink
        String url = animal.getWikiLink();
        if(url.contains("wikipedia.org")){
            wikiLink.setText("Go to Wikipedia Page");

            wikiLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("url", animal.getWikiLink());
                    startActivity(intent);
                }
            });
        }
        else{
            wikiLink.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    //capitalize each word
    public static String capitalize(String str){
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
