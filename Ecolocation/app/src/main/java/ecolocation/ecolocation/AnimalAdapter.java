package ecolocation.ecolocation;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chandler on 11/28/2017.
 */

public class AnimalAdapter extends ArrayAdapter<Animal> {
    private final Context context;
    private final ArrayList<Animal> animalList;

    public AnimalAdapter(Context context, ArrayList<Animal> animalList){
        super(context, R.layout.list_item, animalList);

        this.context = context;
        this.animalList = animalList;
    }

    /*
    * This method puts each item of animalList into a list_item.
    * It's responsible for handling the data and the view of the data
    */
    @Override
    public View getView(final int position, View view, ViewGroup parent){
        //create inflator
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //-------- Get Animal object from ArrayList
        Animal currAnimal = animalList.get(position);


        //get the rowView from the inflater
        //The rowView allows acccess to the widgets on the layout
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        //------------- Initializing Widgets from list_item
        ImageView animalPic = (ImageView) rowView.findViewById(R.id.pic_animal);
        TextView nameText = (TextView) rowView.findViewById(R.id.txt_animal_name);
        SeekBar seekBar = (SeekBar) rowView.findViewById(R.id.seek_bar);

        //----------- Setting Up Values of Widgets
        animalPic.setImageDrawable(currAnimal.getPicture());
        nameText.setText(capitalize(currAnimal.getBinomial()));
        seekBar.setProgress(currAnimal.getPopulation());

        //------- Color-Code rows
        Resources res = getContext().getResources();    //allows access to the color files
        switch (currAnimal.getEndangeredLevel()){
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
            case "Extant (resident)":
                nameText.setTextColor(res.getColor(R.color.extantResident));
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

//        switch (currAnimal.getEndangeredLevel()){
//            case "Least Concern":
//                rowView.setBackgroundColor(res.getColor(R.color.leastConcern));
//                break;
//            case "Near Threatened":
//                rowView.setBackgroundColor(res.getColor(R.color.nearThreatened));
//                break;
//            case "Vulnerable":
//                rowView.setBackgroundColor(res.getColor(R.color.vulnerable));
//                break;
//            case "Endangered":
//                rowView.setBackgroundColor(res.getColor(R.color.endagered));
//                break;
//            case "Critically Endangered":
//                rowView.setBackgroundColor(res.getColor(R.color.criticallyEndangered));
//                break;
//            case "Extinct in the Wild":
//                rowView.setBackgroundColor(res.getColor(R.color.extinctInTheWild));
//                break;
//            case "Extinct":
//                rowView.setBackgroundColor(res.getColor(R.color.extinct));
//                break;
//            case "Extant (resident)":
//                rowView.setBackgroundColor(res.getColor(R.color.extantResident));
//                break;
//            case "Not Evaluated":
//                rowView.setBackgroundColor(res.getColor(R.color.notEvaluated));
//                break;
//            case "Data Deficient":
//                rowView.setBackgroundColor(res.getColor(R.color.dataDeficient));
//                break;
//            default:
//                rowView.setBackgroundColor(res.getColor(R.color.notEvaluated));
//        }
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
