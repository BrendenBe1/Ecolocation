package ecolocation.ecolocation;

/**
 * Created by Chandler on 11/28/2017.
 * This interface is
 */

public class Animal {
    private String name;    //common name if applicable
    private String picPath; //this is the path name of the pic
    private String description;
    //TODO: consider making diet an enumeration
    private String diet;    //herbivore, omnivore, carnivore
    //TODO: consider making endangeredLevel an enumeration
    private String endangeredLevel;
    private float mass; //in kilograms
    private float population;   //population density

    //--------- Constructors
    public Animal(){
        name = "Elephant";
//        picPath = ;
        description = "Description";
        diet = "Herbivore";
        endangeredLevel = "Vulnerable";
        mass = 7000;
        population = 415000;
    }

    public Animal(String name, String picPath, String description,
                  String diet, String endangeredLevel, float mass, float population){
        this.name = name;
        this.picPath = picPath;
        this.description = description;
        this.diet = diet;
        this.endangeredLevel = endangeredLevel;
        this.mass = mass;
        this.population = population;
    }


    //------ Getters
    public void setPopulation(float population) {
        this.population = population;
    }


    //------ Setters
    public String getName() {
        return name;
    }

    public String getPicPath() {
        return picPath;
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

    public float getMass() {
        return mass;
    }

    public float getPopulation() {
        return population;
    }
}
