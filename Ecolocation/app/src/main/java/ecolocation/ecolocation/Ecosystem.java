package ecolocation.ecolocation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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

/**
 * Created by Chandler on 12/22/2017.
 *
 * This class is meant for maintaining the list of the current location (a singleton)
 * This can be accessed by any class and will have access to list
 */

public class Ecosystem {
    private static Ecosystem sEcosystem;    //starts w/ 's' to indicate it's a static variable
    private LatLng chosenLocation;

    //animal lists
    private ArrayList<Animal> currentList;   //current mammals
    private ArrayList<Animal> historicList; //historic (Pleistocene Era) Animals

    //needed for updating ListViews once data is retrieved
    private Context context;
    private ListViewFragment.AnimalAdapter adapter;

    /**
     * The private constructor for Ecosystem
     *
     * @param context application context
     */
    private Ecosystem(Context context){
        currentList = new ArrayList<Animal>();
        this.context = context;
    }

    /**
     * Gets the single instance of Ecosystem. If it's not initialized, then it uses the private
     * constructor. Otherwise it returns the current instance.
     *
     * @param context:  application context
     * @return          returns the single instance of Ecosystem
     */
    public static Ecosystem get(Context context){
        if(sEcosystem == null){
            sEcosystem = new Ecosystem(context);
        }
        return sEcosystem;
    }

    /**
     *  Uses the scientificName as a unique id and returns the Animal object that corresponds to the
     *  scientific name.
     *
     * @param scientificName    The scientific name of the animal to return
     * @return                  Animal object with the scientific name entered
     */
    public Animal getAnimal(String scientificName, AnimalType type){
        //get animal from current mammal list if it's a current mammal
        if(type.equals(AnimalType.CURRENT_MAMMAL)){
            for(Animal animal : currentList){
                if (animal.getBinomial().equals(scientificName)){
                    return animal;
                }
            }
        }
        else if(type.equals(AnimalType.HISTORIC_MAMMAL)){
            for(Animal animal : historicList){
                if (animal.getBinomial().equals(scientificName)){
                    return animal;
                }
            }
        }
        return null;
    }

    /**
     * By binding an adapter (one used for a ListView) to this class, we can notify the adapter when
     * changes occurs to the data set. If the list of animals is empty when the ListView page is
     * created, the list can be updated when the data comes in
     *
     * @param adapter   adapter for a ListView object
     */
    public  void setAdapter(ListViewFragment.AnimalAdapter adapter){
        this.adapter = adapter;
    }


    //---------- Initializing & Returning ArrayList<Animal>

    /**
     *  Returns the current mammal list for selected location, which is already initialized
     *
     * @return  Current Mammal List
     */
    public ArrayList<Animal> getCurrentList(){
        return currentList;
    }

    /**
     *  Gets the already initialized list of Pleistocene Era mammals
     *
     * @return  ArrayList of Pleistocene mammals for selected location
     */
    public ArrayList<Animal> getHistoricList(){
        return historicList;
    }

    /**
     * Determines the animals in specified location. It gets each animal in the location along with
     * its corresponding information from the database to initialize the current mammals list.
     //-------- Getting Data from Databases
     *
     * @param type  the type of animals to retrieve: either the current or historic animals
     * @return      ArrayList for current mammals
     */
    private ArrayList<Animal> getAnimalData(final AnimalType type){

        // default image to display in case something happens
        final Drawable pic = context.getResources().getDrawable(R.drawable.ic_launcher_background);
        final ArrayList<Animal> list = new ArrayList<>();

        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Retrieving data...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Integer... Void) {

                OkHttpClient client = new OkHttpClient();
                RequestBody arguments = new FormBody.Builder()
                        .add("latitude", String.valueOf(chosenLocation.latitude))
                        .add("longitude", String.valueOf(chosenLocation.longitude))
                        .build();
                Log.d("latitude:::::::::::", String.valueOf(chosenLocation.latitude));
                Log.d("longitude:::::::::::", String.valueOf(chosenLocation.longitude));

                // determine the correct request object to make depending on animal type
                Request request;
                if(type == AnimalType.CURRENT_MAMMAL){
                    request = new Request.Builder()
                            .url("http://18.222.2.88/get_data.php?")
                            .post(arguments)
                            .build();
                }
                else{
                    request = new Request.Builder()
                            .url("http://18.222.2.88/get_historic_data.php?")
                            .post(arguments)
                            .build();
                }

                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        String binomial = object.getString("binomial");
                        String commonName = object.getString("common_name");
                        String description = object.getString("description");
                        String wikiLink = object.getString("wiki_link");
                        int mass = object.getInt("mass")/1000;  //convert it to kg

                        Animal animal;
                        if(type == AnimalType.CURRENT_MAMMAL){
                            animal = new Animal(binomial, commonName,
                                    pic, description, wikiLink, object.getString("code"), mass,
                                    AnimalType.CURRENT_MAMMAL);
                        }
                        else{
                            animal = new Animal(binomial, commonName, pic, description, wikiLink,
                                    "EX", mass, AnimalType.HISTORIC_MAMMAL);
                        }

                        // make sure there are no repeats
                        if(!list.contains(animal)){
                            list.add(animal);
                        }
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
                dialog.dismiss();
                if(type == AnimalType.CURRENT_MAMMAL) {
                    for(int i = 0; i< currentList.size(); i++) {
                        Animal currAnimal = currentList.get(i);
                        loadImageFromURL(currAnimal);

                        Log.d("currAnimal", currAnimal.getBinomial());
                    }
                }
                else{
                    for(int i = 0; i< historicList.size(); i++) {
                        Animal currAnimal = historicList.get(i);
                        loadImageFromURL(currAnimal);
                        loadImageRangeMap(currAnimal);

                        Log.d("currAnimal", currAnimal.getBinomial());
                    }
                }

            }
        };

        asyncTask.execute();

        return list;
    }

    /**
     * Gets the image for the inputted Animal
     *
     * @param animal    the animal to get the image for
     */
    private void loadImageFromURL(final Animal animal)
    {
        // create an imageView to hold the picture
        final ImageView imageView = new ImageView(context);

        String fileName = "";
        if(animal.getType().equals(AnimalType.CURRENT_MAMMAL)){
            fileName = animal.getBinomial().replace(" ", "-").toLowerCase();
        }
        else{
            fileName = animal.getBinomial().replace(" ", "_").toLowerCase();
        }

        String url = "https://www.cefns.nau.edu/capstone/projects/CS/2018/Ecolocation/images/";

        url += "current/" + fileName + ".jpg";

        // call to get picture
        Picasso.with(context).load(url).error(R.mipmap.ic_launcher).into(imageView, new com.squareup.picasso.Callback(){

            /*
             *  because the image doesn't load all at once you have to set the image for the animal
             *  when it is successful
             */
            @Override
            public void onSuccess()
            {
                Drawable d = imageView.getDrawable();
                animal.setImage(d);
                if(adapter != null){
                    adapter.notifyImageChange(animal.getType());
                }
            }
            @Override
            public void onError(){}
        });
    }

    /**
     *  This method is used for animals that range maps (Historic animals) It gets their
     *  corresponding image of their range map
     *
     * @param animal
     */
    private void loadImageRangeMap(final Animal animal){
        // create an imageView to hold the picture
        final ImageView imageView = new ImageView(context);

        String fileName = animal.getBinomial();
        String url = "https://www.cefns.nau.edu/capstone/projects/CS/2018/Ecolocation/images/" +
                "historic_range/" + fileName + ".png";

        // call to get picture
        Picasso.with(context).load(url).error(R.mipmap.ic_launcher).into(imageView, new com.squareup.picasso.Callback(){

            /*
             *  because the image doesn't load all at once you have to set the image for the animal
             *  when it is successful
             */
            @Override
            public void onSuccess()
            {
                Drawable d = imageView.getDrawable();
                animal.setRangeMap(d);
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(){}
        });
    }

    // -------- Chosen Location's Getter & Setters
    public LatLng getChosenLocation() {
        return chosenLocation;
    }

    public void setChosenLocation(LatLng chosenLocation) {
        this.chosenLocation = chosenLocation;
        currentList = getAnimalData(AnimalType.CURRENT_MAMMAL);
        historicList = getAnimalData(AnimalType.HISTORIC_MAMMAL);
        adapter = null;
    }
}
