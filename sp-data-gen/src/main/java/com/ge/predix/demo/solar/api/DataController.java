package com.ge.predix.demo.solar.api;

import com.ge.predix.demo.solar.service.AssetDataService;
import com.ge.predix.demo.solar.service.TimeseriesDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by 212539039 on 3/29/2017.
 */
@ComponentScan
@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private TimeseriesDataService timeseriesDataService;

    @Autowired
    private AssetDataService assetDataService;

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public ResponseEntity<Object> getSensorDataTags() throws Exception {
        return timeseriesDataService.getSensorDataTags();
    }

    @RequestMapping(value = "/tags/data", method = RequestMethod.GET)
    public ResponseEntity<Object> getSensorDataByLabel(@RequestParam String label, @RequestParam Long start, @RequestParam Long end) throws Exception {
        //return timeseriesDataService.getSensorDataPointsByLabel(label, LocalDateTime.now().minusMonths(6).toInstant(ZoneOffset.UTC).toEpochMilli(), LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        return timeseriesDataService.getSensorDataPointsByLabel(label, start, end);
    }

    @RequestMapping(value = "/tags/data/recent", method = RequestMethod.GET)
    public ResponseEntity<Object> getRecentSensorDataByLabel(@RequestParam String label) throws Exception {
        return timeseriesDataService.getLatestSensorDataPointsByLabel(label);
    }

    @RequestMapping(value = "/tags/query", method = RequestMethod.GET)
    public ResponseEntity<Object> getSensorData(
            @RequestParam(value = "ids", required = true) String ids,
            @RequestParam(value = "start", required = true) String startDuration,
            @RequestParam(value = "end", required = true) String endDuration,
            @RequestParam(value = "limit", required = true) int tagLimit,
            @RequestParam(value = "order", required = true) String tagOrder)
            throws Exception {
        return timeseriesDataService.getSensorDataPoints(ids, startDuration, endDuration, tagLimit, tagOrder);
    }

    @RequestMapping(value = "/tags/data/generate", method = RequestMethod.POST)
    // use with Content-Type: application/x-www-form-urlencoded
    public String generateSensorDataForLabel(@RequestParam Integer monthsBefore) throws Exception {
        return timeseriesDataService.generateConsumerValues(monthsBefore);
    }

    @RequestMapping(value = "/assets/create", method = RequestMethod.POST)
    public String createAsset(@RequestBody String asset) throws IOException {
        return assetDataService.createAsset(asset);
    }

    @RequestMapping(value = "/assets/update", method = RequestMethod.PUT)
    public String updateAsset(@RequestBody String asset) throws IOException {
        return assetDataService.updateAsset(asset);
    }

    @RequestMapping(value = "/assets/delete", method = RequestMethod.DELETE)
    public String deleteAsset(@RequestParam String assetUri) throws IOException {
        return assetDataService.deleteAsset(assetUri);
    }

}
