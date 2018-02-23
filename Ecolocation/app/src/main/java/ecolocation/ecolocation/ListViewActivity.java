package ecolocation.ecolocation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListViewActivity extends AppCompatActivity {
    //Widgets
    ListView listView;

    //variables for creating the list
    private ArrayList<Animal> animalList;
    private AnimalAdapter adapter;
    static int flag = 0;
    private static Ecosystem sEcosystem;
    LatLng chosenLocation;

    //constants
    static final String SELECTED_ANIMAL = "selected animal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        chosenLocation = getIntent().getExtras().getParcelable("COORDS"); // get coordinates
        Log.d("LATITUDE: ", String.valueOf(chosenLocation.latitude));
        Log.d("LONGITUDE: ", String.valueOf(chosenLocation.longitude));


        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //fill list with database data
        animalList = fillList();
        sEcosystem = Ecosystem.get(this);
        sEcosystem.setList(animalList);

        //-------- Implementing Widgets
        listView = (ListView) findViewById(R.id.layout_list);

        // ----------- Adapter Stuff
        // setting up the individual list items with the adapter
        adapter = new AnimalAdapter(this, animalList);
        //the adapater fills the list with the array list
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal currAnimal = animalList.get(position);
                Intent intent = DetailActivity.newIntent(ListViewActivity.this,
                        currAnimal.getBinomial());
                startActivity(intent);
            }
        });

    }

    //-------- Getting Data from Databases

    //this is just filling it in with dummy data
    private ArrayList<Animal> fillList(){

        // default image to display in case something happens
        final Drawable pic = getResources().getDrawable(R.drawable.ic_launcher_background);
        final ArrayList<Animal> list = new ArrayList<>();

        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... Void) {

                OkHttpClient client = new OkHttpClient();
                RequestBody arguments = new FormBody.Builder()
                        .add("latitude", String.valueOf(chosenLocation.latitude))
                        .add("longitude", String.valueOf(chosenLocation.longitude))
                        .build();
                // animals.php is old db call for just getting binomial
                Request request = new Request.Builder()
                        .url("http://18.216.195.218/mammals.php?")
                        .post(arguments)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        String binomial = object.getString("binomial");
                        String commonName = object.getString("common_name");
                        String threatStr = object.getString("endangered_level");
                        ThreatLevel threatLevel = determineThreatLevel(threatStr);

                        //TODO: get description
                        Animal animal = new Animal(binomial, commonName, pic,
                                "A big cat in Africa", "Carnivore", threatLevel,
                                object.getInt("mass"), object.getInt("population"));

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

    //------------- Menu
    // inflate menu custom icons
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AnimalSort sorter = new AnimalSort();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.recalc:
                return true;
            case R.id.reset:
                //TODO: replace this with restoring sliders to original values
                Intent intent = new Intent( ListViewActivity.this, ListViewActivity.class );
                finish();
                startActivity(intent);
                return true;

            case R.id.alph_ascending:
                sorter.sort(animalList, SORT_TYPE.BINOMIAL, 0);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.alph_descending:
                sorter.sort(animalList, SORT_TYPE.BINOMIAL, 1);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.pop_ascending:
                sorter.sort(animalList, SORT_TYPE.POPULATION, 0);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.pop_descending:
                sorter.sort(animalList, SORT_TYPE.POPULATION, 1);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.mass_ascending:
                sorter.sort(animalList, SORT_TYPE.MASS, 0);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.mass_descending:
                sorter.sort(animalList, SORT_TYPE.MASS, 1);
                adapter.notifyDataSetChanged();
                return true;

            case  R.id.endang_ascending:
                sorter.sort(animalList, SORT_TYPE.THREAT_LEVEL, 1);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.endang_descending:
                sorter.sort(animalList, SORT_TYPE.THREAT_LEVEL, 0);
                adapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //------Handle Strings
    //converts a string into an enumeration of the threat level
    private ThreatLevel determineThreatLevel(String string){
        string = string.toLowerCase();
        ThreatLevel threatLevel;
        switch (string){
            case "least concerned":
                threatLevel = ThreatLevel.LEAST_CONCERNED;
                break;
            case "near threatened":
                threatLevel = ThreatLevel.NEAR_THREATENED;
                break;
            case "vulnerable":
                threatLevel = ThreatLevel.VULNERABLE;
                break;
            case "endangered":
                threatLevel = ThreatLevel.ENDANGERED;
                break;
            case "critically endangered":
                threatLevel = ThreatLevel.CRITICALLY_ENDANGERED;
                break;
            case "extinct in the wild":
                threatLevel = ThreatLevel.EXTINCT_IN_THE_WILD;
                break;
            case "extinct":
                threatLevel = ThreatLevel.EXTINCT;
                break;
            case "extant (resident)":
                threatLevel = ThreatLevel.EXTANT;
                break;
            case "data deficient":
                threatLevel = ThreatLevel.DATA_DEFICIENT;
                break;
            case "not evaluated":
                threatLevel = ThreatLevel.NOT_EVALUATED;
                break;
            default:
                threatLevel = ThreatLevel.DATA_DEFICIENT;
                break;
        }
        return threatLevel;
    }

}
