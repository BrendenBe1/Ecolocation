package ecolocation.ecolocation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GoogleDriveDemoActivity extends AppCompatActivity {
    //Widgets
    ListView listView;
    Button resetButton;
    Button recalcButton;
    Button sortButton;
    Drawable[] d;


    //variables for creating the list
    private ArrayList<Animal> animalList;
    private AnimalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

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


        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        ImageView imageView1 = (ImageView)findViewById(R.id.imageView1);
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);
        ImageView imageView3 = (ImageView)findViewById(R.id.imageView3);
        ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);


        String url = "http://cefns.nau.edu/~mh973/images/lion.jpg";
        String url1 = "http://cefns.nau.edu/~mh973/images/elephant.jpg";
        String url2 = "http://cefns.nau.edu/~mh973/images/giraffe.jpg";
        String url3 = "http://cefns.nau.edu/~mh973/images/cheetah.jpg";
        String url4 = "http://cefns.nau.edu/~mh973/images/zebra.jpg";



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

        loadImageFromURL(url, imageView, lion);
        loadImageFromURL(url1, imageView1, elephant);
        loadImageFromURL(url2, imageView2, giraffe);
        loadImageFromURL(url3, imageView3, cheetah);
        loadImageFromURL(url4, imageView4, zebra);

        ArrayList<Animal> list = new ArrayList<Animal>();
        list.add(lion);
        list.add(elephant);
        list.add(giraffe);
        list.add(cheetah);
        list.add(zebra);



        return list;
    }

    // function to load an image into an image view
    private void loadImageFromURL(String url, final ImageView imageView, final Animal animal)
    {
        Picasso.with(this).load(url).error(R.mipmap.ic_launcher).into(imageView, new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess()
            {
                Drawable d = imageView.getDrawable();
                animal.setImage(d);

            }
            @Override
            public void onError(){}
        });
    }
}
