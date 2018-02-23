package ecolocation.ecolocation;

import android.annotation.SuppressLint;
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
    private ArrayList<Animal> animalList;
    private Context context;
    static int flag = 0;    //used for getting pictures

    //private constructor
    private Ecosystem(Context context){
        animalList = new ArrayList<Animal>();
        this.context = context;

        //TODO: fill list with database and images
    }

    //gets the single instance of sEcosystem
    public static Ecosystem get(Context context){
        if(sEcosystem == null){
            sEcosystem = new Ecosystem(context);
        }
        return sEcosystem;
    }

    //---------- for the animalList
    //gets animal data from database
    public ArrayList<Animal> getAnimalList(LatLng coordinates){
        animalList = getAnimalData(coordinates);

        return animalList;
    }

    //returns animal database
    public ArrayList<Animal> getAnimalList(){
        return animalList;
    }

    //returns the wanted animal
    public Animal getAnimal(String scientificName){
        for(Animal animal : animalList){
            if (animal.getBinomial().equals(scientificName)){
                return animal;
            }
        }
        return null;
    }

//    //sets the list b/c most of the work has to be done in an activity
//    public void setList(ArrayList<Animal> list){
//        animalList = list;
//    }
    

    //-------- Getting Data from Databases

    //this is just filling it in with dummy data
    private ArrayList<Animal> getAnimalData(final LatLng coordinates){

        // default image to display in case something happens
        final Drawable pic = context.getResources().getDrawable(R.drawable.ic_launcher_background);
        final ArrayList<Animal> list = new ArrayList<>();

        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... Void) {

                OkHttpClient client = new OkHttpClient();
                RequestBody arguments = new FormBody.Builder()
                        .add("latitude", String.valueOf(coordinates.latitude))
                        .add("longitude", String.valueOf(coordinates.longitude))
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
//                adapter.notifyDataSetChanged();
//                for(int i=0; i<animalList.size(); i++) {
//                    Animal currAnimal = animalList.get(i);
//                    loadImageFromURL(currAnimal);
//                    Log.d("currAnimal", currAnimal.getBinomial());
//                    // Do something with the value
//                }
            }
        };

        asyncTask.execute();

        return list;
    }

    // function to load an image into an image view
    private void loadImageFromURL(final Animal animal)
    {
        // create an imageView to hold the picture
        final ImageView imageView = new ImageView(context);
        String url = "http://cefns.nau.edu/~mh973/images/" + animal.getName() + ".jpg";
        // call to get picture
        Picasso.with(context).load(url).error(R.mipmap.ic_launcher).into(imageView, new com.squareup.picasso.Callback(){


            // because the image doesn't load all at once you have to set the image for the animal when it is successful
            @Override
            public void onSuccess()
            {
                Drawable d = imageView.getDrawable();
                animal.setImage(d);
                if( flag == 0 )
                {
//                    Intent intent = new Intent( ListViewActivity.this, ListViewActivity.class );
//                    startActivity( intent );
//                    finish();
                    flag = 1;
                }
            }
            @Override
            public void onError(){}
        });
    }

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
