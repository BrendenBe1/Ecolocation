package ecolocation.ecolocation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListViewActivity extends AppCompatActivity {
    //Widgets
    ListView listView;
    Button resetButton;
    Button recalcButton;
    Button sortButton;

    //variables for creating the list
    private ArrayList<Animal> animalList;
    private AnimalAdapter adapter;
    static int flag = 0;
    private static Ecosystem sEcosystem;

    //constants
    static final String SELECTED_ANIMAL = "selected animal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //TODO: initialize animalList with database/google drive stuff;
        animalList = fillList();
        sEcosystem = Ecosystem.get(this);
        sEcosystem.setList(animalList);

        //-------- Implementing Widgets
        listView = (ListView) findViewById(R.id.layout_list);
        resetButton = (Button) findViewById(R.id.bttn_reset);
        recalcButton = (Button) findViewById(R.id.bttn_recalc);
        sortButton = (Button) findViewById(R.id.bttn_sort);

        // ----------- Adapter Stuff
        // setting up the individual list items with the adapter
        adapter = new AnimalAdapter(this, animalList);
        //the adapater fills the list with the array list
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal currAnimal = animalList.get(position);
                Intent intent = new Intent(ListViewActivity.this, AnimalDetailActivity.class);
                intent.putExtra(SELECTED_ANIMAL, currAnimal.getName());
                startActivity(intent);
            }
        });


        //-------- Setting Up Event Listeners for Buttons
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: reset to original values
                Intent intent = new Intent( ListViewActivity.this, ListViewActivity.class );
                startActivity( intent );
                finish();
            }
        });

        // go to detailed page
        recalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ListViewActivity.this, AnimalDetailActivity.class );
                startActivity( intent );
                finish();
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

        // default image to display in case something happens
        final Drawable pic = getResources().getDrawable(R.drawable.ic_launcher_background);
        final ArrayList<Animal> list = new ArrayList<>();

        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... Void) {

                OkHttpClient client = new OkHttpClient();
                // animals.php is old db call for just getting binomial
                /*Request request = new Request.Builder()
                        .url("http://18.220.129.239/animals.php?")
                        .build();*/
                Request request = new Request.Builder()
                        .url("http://18.220.129.239/mammals.php?")
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        Animal animal = new Animal(object.getString("binomial"), object.getString("common_name"), pic,
                                "A big cat in Africa", "Carnivore", object.getString("endangered_level"),
                                object.getInt("mass"), object.getInt("population"));


                        //ListViewActivity.this.animalList.add(animal);
                        list.add(animal);
                        Log.d("return", animal.getBinomial());
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
                for(int i=0; i<animalList.size(); i++) {
                    Animal currAnimal = animalList.get(i);
                    loadImageFromURL(currAnimal);
                    Log.d("currAnimal", currAnimal.getBinomial());
                    // Do something with the value
                }
            }
        };

        asyncTask.execute();

        return list;
    }



    // function to load an image into an image view
    private void loadImageFromURL(final Animal animal)
    {
        // create an imageView to hold the picture
        final ImageView imageView = new ImageView(this);
        String url = "http://cefns.nau.edu/~mh973/images/" + animal.getName() + ".jpg";
        // call to get picture
        Picasso.with(this).load(url).error(R.mipmap.ic_launcher).into(imageView, new com.squareup.picasso.Callback(){


            // because the image doesn't load all at once you have to set the image for the animal when it is successful
            @Override
            public void onSuccess()
            {
                Drawable d = imageView.getDrawable();
                animal.setImage(d);
                if( flag == 0 )
                {
                    Intent intent = new Intent( ListViewActivity.this, ListViewActivity.class );
                    startActivity( intent );
                    finish();
                    flag = 1;
                }
            }
            @Override
            public void onError(){}
        });
    }
}
