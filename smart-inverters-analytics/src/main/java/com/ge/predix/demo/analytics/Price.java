/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
 
package com.ge.predix.demo.analytics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author predix -
 */
public class Price extends TreeMap<Long, Double> {


    ObjectMapper mapper = new ObjectMapper();

    public Price(String json) throws IOException {
        HashMap<String, Number> hashMap = mapper.readValue(json, HashMap.class);
        for (Map.Entry<String, Number> entry : hashMap.entrySet()) {
            put(new Long(entry.getKey()), entry.getValue().doubleValue());
        }
    }

    public Price(Map<String, Number> Price) {
        for (Map.Entry<String, Number> entry : Price.entrySet()) {
            put(new Long(entry.getKey()), entry.getValue().doubleValue());
        }
    }

}