package com.ge.predix.demo.solar.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 212525538 on 4/5/2017.
 */
public class GeneratedPower extends TreeMap<Long, Double> {

    ObjectMapper mapper = new ObjectMapper();

    public GeneratedPower(String json) throws IOException {
        HashMap<String, Number> hashMap = mapper.readValue(json, HashMap.class);
        for (Map.Entry<String, Number> entry : hashMap.entrySet()) {
            put(new Long(entry.getKey()), entry.getValue().doubleValue());
        }
    }

    public GeneratedPower(Map<String, Number> generatedPower) {
        for (Map.Entry<String, Number> entry : generatedPower.entrySet()) {
            put(new Long(entry.getKey()), entry.getValue().doubleValue());
        }
    }
}
