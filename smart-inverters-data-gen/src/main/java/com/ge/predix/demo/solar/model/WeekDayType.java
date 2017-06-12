package com.ge.predix.demo.solar.model;

/**
 * Created by 212539039 on 4/26/2017.
 */
public enum WeekDayType {
    WEEKEND("Weekend"), WORKING("Working");
    private final String code;

    private WeekDayType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static WeekDayType fromCode(String code) {
        switch(code) {
            case "Weekend": {
                return WEEKEND;
            }
            case "Working": {
                return WORKING;
            }
            default: {
                throw new IllegalArgumentException("Not a valid WeekDayType code!");
            }
        }
    }

    @Override
    public String toString() {
        return code;
    }
}

