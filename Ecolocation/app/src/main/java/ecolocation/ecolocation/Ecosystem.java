package ecolocation.ecolocation;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

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

//    public void retrieveData(Context context){
//        this.context = context;
//        currentList = getAnimalData(AnimalType.CURRENT_MAMMAL);
//        historicList = getAnimalData(AnimalType.HISTORIC_MAMMAL);
//        adapter = null;
//
//    }

    /**
     * The private constructor for Ecosystem
     *
     * @param context application context
     */
    private Ecosystem(Context context){
        currentList = new ArrayList<Animal>();
        historicList = new ArrayList<Animal>();
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

    public void setCurrentList(ArrayList<Animal> list){
        currentList = list;
    }

    /**
     *  Gets the already initialized list of Pleistocene Era mammals
     *
     * @return  ArrayList of Pleistocene mammals for selected location
     */
    public ArrayList<Animal> getHistoricList(){
        return historicList;
    }

    public void setHistoricList(ArrayList<Animal> list){
        historicList = list;
    }

    // -------- Chosen Location's Getter & Setters
    public LatLng getChosenLocation() {
        return chosenLocation;
    }

    public void setChosenLocation(LatLng chosenLocation) {
        this.chosenLocation = chosenLocation;
    }
}
