package com.ge.predix.demo.solar.model;

/**
 * Created by 212539039 on 4/24/2017.
 */
public enum AssetCategory {
    PV("Photovoltaic Panel"), B("Battery"), IC("Industrial Consumer"), CC("Commercial Consumer"), RC("Residential Consumer");

    private final String code;

    private AssetCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AssetCategory fromCode(String code) {
        switch(code) {
            case "Photovoltaic Panel": {
                return PV;
            }
            case "Battery": {
                return B;
            }
            case "Industrial ConsumerProfile": {
                return IC;
            }
            case "Commercial ConsumerProfile": {
                return CC;
            }
            case "Residential ConsumerProfile": {
                return RC;
            }
            default: {
                throw new IllegalArgumentException("Not a valid AssetCategory code!");
            }
        }
    }

    @Override
    public String toString() {
        return code;
    }

}
