package com.ge.predix.demo.solar.model;

/**
 * Created by 212539039 on 4/26/2017.
 */
public class Generator {
    private GeneratorProfile generatorProfile;
    private String label;

    public GeneratorProfile getGeneratorProfile() {
        return generatorProfile;
    }

    public void setGeneratorProfile(GeneratorProfile generatorProfile) {
        this.generatorProfile = generatorProfile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Generator{" +
                "generatorProfile=" + generatorProfile +
                ", label='" + label + '\'' +
                '}';
    }
}
