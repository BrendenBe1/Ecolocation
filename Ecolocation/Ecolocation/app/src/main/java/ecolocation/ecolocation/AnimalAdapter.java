package ecolocation.ecolocation;

import android.content.Context;
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
        animalPic.setImageDrawable(currAnimal.getpicture());
        nameText.setText(currAnimal.getName());
        seekBar.setProgress(currAnimal.getPopulation());

        return rowView;
    }
}
