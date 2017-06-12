package com.ge.predix.demo.solar.helper;

import java.time.Instant;

/**
 * Created by 212539039 on 4/25/2017.
 */
public class TimeSeries {
    private double value;
    private Instant instant;

    public TimeSeries() {
    }

    public TimeSeries(double value, Instant instant) {
        this.value = value;
        this.instant = instant;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    @Override
    public String toString() {
        return "TimeSeries{" +
                "value=" + value +
                ", instant=" + instant +
                '}';
    }
}
