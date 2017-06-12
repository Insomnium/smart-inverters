package com.ge.predix.demo.solar.model;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 212539039 on 5/30/2017.
 */
public class GenericAsset implements Serializable {

    private Map<Object, Object> asset = new HashMap<>();

    @JsonAnyGetter
    public Map<Object,Object> getAsset() {
        return asset;
    }

    @JsonAnySetter
    public void setAsset(Object name, Object value) {
        asset.put(name, value);
    }
}
