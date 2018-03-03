package ecolocation.ecolocation.AnimalInfo;

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
    private String wikiLink;
    private ThreatLevel threatLevel;
    private double mass; //in kilograms

    //--------- Constructors
    public Animal(){
        name = "Elephant";
        //    picture =
        description = "Description";
        threatLevel = ThreatLevel.VULNERABLE;
        mass = 7000;
    }

    public Animal(String binomial, String name, Drawable picture, String description, String url,
                  ThreatLevel threatLevel, double mass){
        this.binomial = binomial;
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.wikiLink = url;
        this.threatLevel = threatLevel;
        this.mass = mass;
    }


    //------ Setters
    public void setImage( Drawable image ) { this.picture = image; }


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

}
