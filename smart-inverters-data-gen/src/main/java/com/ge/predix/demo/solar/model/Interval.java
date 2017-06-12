package com.ge.predix.demo.solar.model;

import java.util.List;

/**
 * Created by 212539039 on 4/26/2017.
 */
public class Interval {
    private String label;
    private List<ConsumptionPercentage> values;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ConsumptionPercentage> getValues() {
        return values;
    }

    public void setValues(List<ConsumptionPercentage> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "label='" + label + '\'' +
                ", values=" + values +
                '}';
    }
}
