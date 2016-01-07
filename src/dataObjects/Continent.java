package dataObjects;

import java.util.Map;

/**
 * Created by chris on 06.01.2016.
 */
public class Continent {
    private String name;


    private Map<String, Territory> patches;

    public Continent(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public Map<String, Territory> getPatches() {
        return patches;
    }

    public void setPatches(Map<String, Territory> patches) {
        this.patches = patches;
    }
}
