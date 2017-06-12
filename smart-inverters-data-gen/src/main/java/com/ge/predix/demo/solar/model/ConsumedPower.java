package com.ge.predix.demo.solar.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 212525538 on 8/5/2017.
 */
public class ConsumedPower extends TreeMap<Long,Double> {



    public Double calculateConsumedPowerUntil(Long hour) {
        Double result = 0.0;
        for (Map.Entry<Long, Double> entry : entrySet()) {
            if (entry.getKey() >= hour){
                continue;
            }
            result += entry.getValue();
        }
        return result;
    }
}
