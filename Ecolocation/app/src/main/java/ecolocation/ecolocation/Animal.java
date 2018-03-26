package ecolocation.ecolocation;

import android.graphics.drawable.Drawable;

/**
 * Created by Chandler on 11/28/2017.
 * This class represents an animal with the following attributes: common name, scientific name
 * (binomial), threat level, image, wikipedia description, wikipedia link, mass, animal type
 * (current or historic [Pleistocene Era]), and range map if it's a historic animal
 */

/**
 *  This enumeration is used to determine which type of animal we are using
 *
 *  It's useful for determining if an ImageView should be visible or not to display historic range
 *  map or current_nutrient retrieval so we know which table to get current_nutrient from, and more.
 */
enum AnimalType {
    CURRENT_MAMMAL,
    HISTORIC_MAMMAL
}

/**
 *  This enumeration is used to represent the threat level of animal. Making this enumeration
 *  guarantees that only the values below are entered as a threat level and makes sorting by threat
 *  level much easier.
 */
enum ThreatLevel {
    //define each type of threat level
    LEAST_CONCERNED (0, "Least Concern"),
    NEAR_THREATENED (1, "Near Threatened"),
    VULNERABLE (2, "Vulnerable"),
    ENDANGERED (3, "Endangered"),
    CRITICALLY_ENDANGERED (4, "Critically Endangered"),
    EXTINCT_IN_THE_WILD (5, "Extinct in the Wild"),
    EXTINCT (6, "Extinct"),
    DATA_DEFICIENT (8, "Data Deficient"),
    NOT_EVALUATED (9, "Not Evaluated");

    private int value;
    private String name;
    ThreatLevel(int value, String name){
        this.value = value;
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public int getValue(){
        return value;
    }
}

public class Animal {
    // attributes
    private String binomial;
    private String name;    // common name if applicable
    private Drawable picture;
    private Drawable rangeMap;  // only applicable for historic mammals
    private String description;
    private String wikiLink;
    private ThreatLevel threatLevel;
    private double mass; // in kilograms
    private AnimalType type;    // tells if the animal is on the current or historic list

    //--------- Constructors
    // empty constructor
    public Animal(){}

    public Animal(String binomial, String name, Drawable picture, String description, String url,
                  String threatLevel, double mass, AnimalType type){
        this.binomial = binomial;
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.wikiLink = url;
        this.threatLevel = determineThreatLevel(threatLevel);
        this.mass = mass;
        this.type = type;
    }

    /**
     *  Converts a string version of the threat level into the enumeration
     *
     * @param string this is the value stored in the database to represent the threat level
     * @return  the enumeration of the threat level the string was representing
     */
    private ThreatLevel determineThreatLevel(String string){
        ThreatLevel threatLevel;
        switch (string){
            case "LC":
                threatLevel = ThreatLevel.LEAST_CONCERNED;
                break;
            case "NT":
                threatLevel = ThreatLevel.NEAR_THREATENED;
                break;
            case "VU":
                threatLevel = ThreatLevel.VULNERABLE;
                break;
            case "EN":
                threatLevel = ThreatLevel.ENDANGERED;
                break;
            case "CR":
                threatLevel = ThreatLevel.CRITICALLY_ENDANGERED;
                break;
            case "EW":
                threatLevel = ThreatLevel.EXTINCT_IN_THE_WILD;
                break;
            case "EX":
                threatLevel = ThreatLevel.EXTINCT;
                break;
            case "DD":
                threatLevel = ThreatLevel.DATA_DEFICIENT;
                break;
            case "NE":
                threatLevel = ThreatLevel.NOT_EVALUATED;
                break;
            default:
                threatLevel = ThreatLevel.DATA_DEFICIENT;
                break;
        }
        return threatLevel;
    }

    @Override
    public boolean equals(Object obj) {
        Animal animalObject = (Animal) obj;
        return this.getBinomial().equals(animalObject.getBinomial());
    }

    //------ Setters
    public void setImage( Drawable image ) { this.picture = image; }

    public void setRangeMap(Drawable rangeMap) {
        this.rangeMap = rangeMap;
    }

    //------ Getters
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

    public String getWikiLink(){
        return wikiLink;
    }

    public ThreatLevel getThreatLevel() {
        return threatLevel;
    }

    public double getMass() {
        return mass;
    }

    public AnimalType getType(){
        return type;
    }

    public Drawable getRangeMap() {
        return rangeMap;
    }
}
