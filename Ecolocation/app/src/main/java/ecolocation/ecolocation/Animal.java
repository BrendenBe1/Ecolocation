package ecolocation.ecolocation;

import android.graphics.drawable.Drawable;

/**
 * Created by Chandler on 11/28/2017.
 * This interface is
 */

public class Animal {
    private String binomial;
    private String name;    //common name if applicable
    private Drawable picture;
    private String description;
    //TODO: consider making diet an enumeration
    private String diet;    //herbivore, omnivore, carnivore
    //TODO: consider making endangeredLevel an enumeration
    private String endangeredLevel;
    private double mass; //in kilograms
    private int population;   //population density

    //--------- Constructors
    public Animal(){
        name = "Elephant";
        //    picture =
        description = "Description";
        diet = "Herbivore";
        endangeredLevel = "Vulnerable";
        mass = 7000;
        population = 415000;
    }

    public Animal(String binomial, String name, Drawable picture, String description,
                  String diet, String endangeredLevel, double mass, int population){
        this.binomial = binomial;
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.diet = diet;
        this.endangeredLevel = endangeredLevel;
        this.mass = mass;
        this.population = population;
    }


    //------ Getters
    public void setPopulation(int population) {
        this.population = population;
    }
    public void setImage( Drawable image ) { this.picture = image; }


    //------ Setters
    public String getBinomial() {
        return binomial;
    }
    public String getName() {
        return name;
    }

    public Drawable getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public String getDiet() {
        return diet;
    }

    public String getEndangeredLevel() {
        return endangeredLevel;
    }

    public double getMass() {
        return mass;
    }

    public int getPopulation() {
        return population;
    }
}
