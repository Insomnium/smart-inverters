package com.ge.predix.demo.solar.model;

import java.util.List;

/**
 * Created by 212539039 on 4/27/2017.
 */
public class Consumption {
    private List<ConsumerProfile> consumption;

    public List<ConsumerProfile> getConsumption() {
        return consumption;
    }

    public void setConsumption(List<ConsumerProfile> consumption) {
        this.consumption = consumption;
    }

    @Override
    public String toString() {
        return "Consumption{" +
                "consumption=" + consumption +
                '}';
    }
}
