package com.ge.predix.demo.solar.model;

/**
 * Created by 212539039 on 4/26/2017.
 */
public class ConsumptionPercentage {
    private int index;
    private double value;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConsumptionPercentage{" +
                "index=" + index +
                ", value=" + value +
                '}';
    }
}
