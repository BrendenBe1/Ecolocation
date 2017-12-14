package ecolocation.ecolocation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Brenden on 12/5/2017.
 */

public class New_AnimalAdapter extends ArrayAdapter<New_Animal> {
    private final Context context;
    private final ArrayList<New_Animal> animalList;

    public New_AnimalAdapter(Context context, ArrayList<New_Animal> animalList){
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
        New_Animal currAnimal = animalList.get(position);



        //get the rowView from the inflater
        //The rowView allows acccess to the widgets on the layout
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        //------------- Initializing Widgets from list_item
        TextView nameText = (TextView) rowView.findViewById(R.id.txt_animal_name);

        //----------- Setting Up Values of Widgets
        nameText.setText(currAnimal.getBinomial());
        Log.d("text NUM", "hhh");

        return rowView;
    }
}
