package com.ge.predix.demo.solar.model;

/**
 * Created by 212539039 on 4/27/2017.
 */
public class CustomConsumption {
    private Double customMinValue;
    private Double customMaxValue;

    public Double getCustomMinValue() {
        return customMinValue;
    }

    public void setCustomMinValue(Double customMinValue) {
        this.customMinValue = customMinValue;
    }

    public Double getCustomMaxValue() {
        return customMaxValue;
    }

    public void setCustomMaxValue(Double customMaxValue) {
        this.customMaxValue = customMaxValue;
    }

    @Override
    public String toString() {
        return "CustomConsumption{" +
                "customMinValue=" + customMinValue +
                ", customMaxValue=" + customMaxValue +
                '}';
    }
}
