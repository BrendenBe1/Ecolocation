package ecolocation.ecolocation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class DatabaseDemoActivity extends AppCompatActivity {
    //Widgets
    ListView listView;
    Button resetButton;
    Button recalcButton;
    Button sortButton;

    //variables for creating the list
    private ArrayList<Animal> animalList;
    private AnimalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive);

        //TODO: initialize animalList with database/google drive stuff;
        animalList = fillList();

        //-------- Implementing Widgets
        listView = (ListView) findViewById(R.id.layout_list);
        resetButton = (Button) findViewById(R.id.bttn_reset);
        recalcButton = (Button) findViewById(R.id.bttn_recalc);
        sortButton = (Button) findViewById(R.id.bttn_sort);


        //setting up the individual list items with the adapter
        adapter = new AnimalAdapter(this, animalList);
        /*
        * This line is doing a lot, the listView will take in individual
        * list_item layouts from the adapter. The adapter is filling in those
        * list_item layouts with the data it gets (that work is done w/n it's
        * own class).
        */
        listView.setAdapter(adapter);

        //-------- Setting Up Event Listeners
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: reset to original values
            }
        });

        recalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: calculate new nutrient dispersal
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: show different ways of sorting
            }
        });
    }

    //this is just filling it in with dummy data
    private ArrayList<Animal> fillList(){
        Drawable pic = getResources().getDrawable(R.drawable.ic_launcher_background);

        Animal lion = new Animal("lion", pic,"A big cat in Africa", "carnivore",
                "vulnerable", 187.5, 20000);

        Animal elephant = new Animal("african elephant", pic, "The largest land mammal",
                "herbivore", "vulnerable", 3500, 415000);

        Animal giraffe = new Animal("giraffe", pic, "An animal with a long neck",
                "herbivore", "vulnerable", 1192, 97500);

        Animal cheetah = new Animal("cheetah", pic, "A very fast animal",
                "carnivore", "vulnerable", 50, 7100);

        Animal zebra = new Animal("zebra", pic, "A striped horse.", "herbivore",
                "near threatened", 250, 150000);

        ArrayList<Animal> list = new ArrayList<Animal>();
        list.add(lion);
        list.add(elephant);
        list.add(giraffe);
        list.add(cheetah);
        list.add(zebra);

        return list;
    }
}
