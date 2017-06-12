package com.ge.predix.demo.solar.model;

import java.util.List;

/**
 * Created by 212539039 on 4/26/2017.
 */
public class GeneratorProfile {

    private AssetCategory assetCategory;
    private List<Interval> intervals;

    public AssetCategory getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    @Override
    public String toString() {
        return "GeneratorProfile{" +
                ", assetCategory=" + assetCategory +
                ", intervals=" + intervals +
                '}';
    }
}
