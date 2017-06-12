package com.ge.predix.demo.solar.model;

import java.util.List;

/**
 * Created by 212539039 on 4/27/2017.
 */
public class Generation {
    private List<GeneratorProfile> generation;

    public List<GeneratorProfile> getGeneration() {
        return generation;
    }

    public void setGeneration(List<GeneratorProfile> generation) {
        this.generation = generation;
    }

    @Override
    public String toString() {
        return "Generation{" +
                "generation=" + generation +
                '}';
    }
}
