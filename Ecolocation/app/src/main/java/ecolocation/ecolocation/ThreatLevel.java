package ecolocation.ecolocation;

/**
 * Created by Chandler on 1/23/2018.
 */

public enum ThreatLevel {
    //define each type of threat level
    LEAST_CONCERNED (0, "Least Concern"),
    NEAR_THREATENED (1, "Near Threatened"),
    VULNERABLE (2, "Vulnerable"),
    ENDANGERED (3, "Endangered"),
    CRITICALLY_ENDANGERED (4, "Critically Endangered"),
    EXTINCT_IN_THE_WILD (5, "Extinct in the Wild"),
    EXTINCT (6, "Extinct"),
    EXTANT (7, "Extant (resident)"),
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
