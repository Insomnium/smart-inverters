package com.ge.predix.demo.solar.model;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

/**
 * Created by 212539039 on 4/26/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Consumer {
    private String type;
    private String uri;
    private Set<String> consumptionProfile;
    private CustomConsumption consumption;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Set<String> getConsumptionProfile() {
        return consumptionProfile;
    }

    public void setConsumptionProfile(Set<String> consumptionProfile) {
        this.consumptionProfile = consumptionProfile;
    }

    public CustomConsumption getConsumption() {
        return consumption;
    }

    public void setConsumption(CustomConsumption consumption) {
        this.consumption = consumption;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                ", consumptionProfile='" + consumptionProfile + '\'' +
                ", consumption=" + consumption +
                '}';
    }
}
