package ecolocation.ecolocation;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class GoogleDriveDemoActivity extends AppCompatActivity {
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


                        //GoogleDriveDemoActivity.this.animalList.add(animal);
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
            }
            @Override
            public void onError(){}
        });
    }
}
