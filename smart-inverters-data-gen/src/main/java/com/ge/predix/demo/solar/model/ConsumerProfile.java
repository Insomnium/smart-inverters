package com.ge.predix.demo.solar.model;

import java.util.List;

/**
 * Created by 212539039 on 4/26/2017.
 */
public class ConsumerProfile {

    private Double minValue;
    private Double maxValue;
    private Double delta;
    private AssetCategory assetCategory;
    private WeekDayType weekDayType;
    private List<Interval> intervals;
    private String uri;

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public AssetCategory getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public WeekDayType getWeekDayType() {
        return weekDayType;
    }

    public void setWeekDayType(WeekDayType weekDayType) {
        this.weekDayType = weekDayType;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "ConsumerProfile{" +
                "minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", delta=" + delta +
                ", assetCategory=" + assetCategory +
                ", weekDayType=" + weekDayType +
                ", intervals=" + intervals +
                ", uri='" + uri + '\'' +
                '}';
    }
}
