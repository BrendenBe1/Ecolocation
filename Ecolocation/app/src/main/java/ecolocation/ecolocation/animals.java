package ecolocation.ecolocation;

/**
 * Created by Brenden on 12/5/2017.
 */

public class animals {
    private String genus;
    private String species;

    public animals(String genus, String species){
        this.genus = genus;
        this.species = species;
    }

    public String getGenus(){return genus;}
    public String getSpecies(){return species;};
}
