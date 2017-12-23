package ecolocation.ecolocation;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Chandler on 12/22/2017.
 *
 * This class is meant for maintaining the list of the current location (a singleton)
 * This can be accessed by any class and will have access to list
 */

public class Ecosystem {
    private static Ecosystem sEcosystem;    //starts w/ 's' to indicate it's a static variable
    private ArrayList<Animal> animalList;

    //private constructor
    private Ecosystem(Context context){
        animalList = new ArrayList<Animal>();
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

    //sets the list b/c most of the work has to be done in an activity
    public void setList(ArrayList<Animal> list){
        animalList = list;
    }

}
