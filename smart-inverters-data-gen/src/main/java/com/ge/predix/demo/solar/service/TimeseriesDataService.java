package com.ge.predix.demo.solar.service;

import com.ge.predix.demo.solar.client.SpringBootRestClient;
import com.ge.predix.demo.solar.helper.DataGenerator;
import com.ge.predix.demo.solar.helper.TimeSeries;
import com.ge.predix.demo.solar.model.Consumer;
import com.ge.predix.demo.solar.model.ConsumerProfile;
import com.ge.predix.demo.solar.model.GeneratorProfile;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.Body;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.DatapointsIngestion;
import com.ge.predix.entity.timeseries.datapoints.queryrequest.DatapointsQuery;
import com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.DatapointsLatestQuery;
import com.ge.predix.entity.timeseries.datapoints.queryresponse.DatapointsResponse;
import com.ge.predix.entity.timeseries.tags.TagsList;
import com.ge.predix.solsvc.restclient.impl.RestClient;
import com.ge.predix.solsvc.timeseries.bootstrap.client.TimeseriesClient;
import com.ge.predix.solsvc.timeseries.bootstrap.config.ITimeseriesConfig;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by 212539039 on 3/29/2017.
 */
@Component
public class TimeseriesDataService {

    @Autowired
    private TimeseriesClient timeseriesClient;

    @Autowired
    private RestClient restClient;

    @Autowired
    @Qualifier("defaultTimeseriesConfig")
    private ITimeseriesConfig timeseriesConfig;

    @Autowired
    private SpringBootRestClient springBootRestClient;

    private static Logger log = LoggerFactory.getLogger(TimeseriesDataService.class);

    @Value("${sensor.max.quality}")
    private int maxQuality;

    @PostConstruct
    public void init() {
        try {
            this.timeseriesClient.createConnectionToTimeseriesWebsocket();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to set up timeseries Websocket Pool timeseriesConfig=" + this.timeseriesConfig, e);
        }
    }

    public ResponseEntity<Object> getSensorDataTags() {
        try {
            List<Header> headers = generateHeaders();
            TagsList tagsList = this.timeseriesClient.listTags(headers);
            return handleResult(tagsList);
        } catch (Throwable e) {
            // TODO: clean
            log.error("unable to get sensor data, config=" + this.timeseriesConfig, e);
            // This is sample code so we need to easily show you what went
            // wrong, please convert your app to show appropriate info to end
            // users. For security
            // reasons do not expose these properties.
            throw new RuntimeException("unable to get sensor data, errorMsg=" + e.getMessage() + ". config=" + this.timeseriesConfig, e);
        }
    }

    public ResponseEntity<Object> getLatestSensorDataPointsByLabel(String label) {
        try {
            if (label == null) {
                return null;
            }
            List<Header> headers = generateHeaders();
            DatapointsLatestQuery dpQuery = buildLatestDatapointsQueryRequest(label);
            DatapointsResponse response = this.timeseriesClient.queryForLatestDatapoint(dpQuery, headers);
            return handleResult(response);
        } catch (Throwable e) {
            // TODO: clean
            log.error("unable to get sensor data, config=" + this.timeseriesConfig, e);
            // This is sample code so we need to easily show you what went
            // wrong, please convert your app to show appropriate info to end
            // users. For security
            // reasons do not expose these properties.
            throw new RuntimeException("unable to get sensor data, errorMsg=" + e.getMessage() + ". config=" + this.timeseriesConfig, e);
        }
    }

    public ResponseEntity<Object> getSensorDataPointsByLabel(String label, Long start, Long end) {
        try {
            if (label == null) {
                return null;
            }
            List<Header> headers = generateHeaders();
            DatapointsQuery dpQuery = buildDatapointsQueryRequest(label, start, end);
            DatapointsResponse response = this.timeseriesClient.queryForDatapoints(dpQuery, headers);
            return handleResult(response);
        } catch (Throwable e) {
            // TODO: clean
            log.error("unable to get sensor data, config=" + this.timeseriesConfig, e);
            // This is sample code so we need to easily show you what went
            // wrong, please convert your app to show appropriate info to end
            // users. For security
            // reasons do not expose these properties.
            throw new RuntimeException("unable to get sensor data, errorMsg=" + e.getMessage() + ". config=" + this.timeseriesConfig, e);
        }
    }

    public ResponseEntity<Object> getSensorDataPoints(String ids, String startDuration, String endDuration, int tagLimit, String tagOrder) {
        try {
            if (ids == null) {
                return null;
            }
            List<Header> headers = generateHeaders();
            DatapointsQuery datapointsQuery = new DatapointsQuery();
            List<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag> tags = new ArrayList<>();
            datapointsQuery.setStart(startDuration);
            if (endDuration == null) {
                datapointsQuery.setEnd(System.currentTimeMillis());
            }
            // datapointsQuery.setStart("1y-ago"); //$NON-NLS-1$
            String[] tagArray = ids.split(","); //$NON-NLS-1$
            List<String> entryTags = Arrays.asList(tagArray);
            for (String entryTag : entryTags) {
                com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag();
                tag.setName(entryTag);
                tag.setLimit(tagLimit);
                tag.setOrder(tagOrder);
                tags.add(tag);
            }
            datapointsQuery.setTags(tags);
            DatapointsResponse response = this.timeseriesClient.queryForDatapoints(datapointsQuery, headers);
            return handleResult(response);
        } catch (Throwable e) {
            // TODO: clean
            log.error("unable to get sensor data, config=" + this.timeseriesConfig, e);
            // This is sample code so we need to easily show you what went
            // wrong, please convert your app to show appropriate info to end
            // users. For security
            // reasons do not expose these properties.
            throw new RuntimeException("unable to get sensor data, errorMsg=" + e.getMessage() + ". config=" + this.timeseriesConfig, e);
        }
    }

    public String generateConsumerValues(int monthsBefore) {
        long dataPointCounter = 0;
        // retrieve the list of available consumers
        List<Consumer> consumerList = springBootRestClient.getAllConsumers();
        // retrieve the list of available consumer profiles
        List<ConsumerProfile> allConsumerProfiles = springBootRestClient.getAllConsumerProfiles();

        // generate data for all consumers
        for (Consumer consumer : consumerList) {
            List<ConsumerProfile> currentConsumerProfileList = allConsumerProfiles
                    .stream()
                    .filter( profile -> consumer.getConsumptionProfile().contains(profile.getUri()))
                    .collect(Collectors.toList());
            DatapointsIngestion dpIngestion = new DatapointsIngestion();
            dpIngestion.setMessageId(String.valueOf(System.currentTimeMillis()));

            Body body = new Body();
            body.setName(consumer.getUri());
            List<Object> dataPoints = new ArrayList<>();

            List<TimeSeries> timeSeriesList = DataGenerator.generateMonthlyConsumption(currentConsumerProfileList, monthsBefore);

            for (TimeSeries timeSeries : timeSeriesList) {
                List<Object> dataPoint = new ArrayList<>();
                Long timestamp = timeSeries.getInstant().toEpochMilli();
                dataPoint.add(timestamp);
                int quality = ThreadLocalRandom.current().nextInt(maxQuality);
                dataPoint.add(timeSeries.getValue());
                dataPoint.add(quality);
                dataPoints.add(dataPoint);
                dataPointCounter++;
            }

            body.setDatapoints(dataPoints);

            com.ge.predix.entity.util.map.Map map = new com.ge.predix.entity.util.map.Map();
            map.put("host", "server1"); //$NON-NLS-2$
            map.put("customer", "FooBarCustomer"); //$NON-NLS-2$

            body.setAttributes(map);

            List<Body> bodies = new ArrayList<>();
            bodies.add(body);

            dpIngestion.setBody(bodies);
            this.timeseriesClient.postDataToTimeseriesWebsocket(dpIngestion);
        }
        return "Successfully created " + dataPointCounter + " consumer measurements";
    }

    private Long generateTimestampsWithinYear(Long current) {
        long yearInMMS = Long.valueOf(31536000000L);
        return ThreadLocalRandom.current().nextLong(current - yearInMMS, current + 1);
    }

    private List<Header> generateHeaders() {
        List<Header> headers = this.restClient.getSecureTokenForClientId();
        this.restClient.addZoneToHeaders(headers, this.timeseriesConfig.getZoneId());
        return headers;
    }

    private DatapointsLatestQuery buildLatestDatapointsQueryRequest(String id) {
        DatapointsLatestQuery datapointsLatestQuery = new DatapointsLatestQuery();
        com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag();
        tag.setName(id);
        List<com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag> tags = new ArrayList<>();
        tags.add(tag);
        datapointsLatestQuery.setTags(tags);
        return datapointsLatestQuery;
    }

    private DatapointsQuery buildDatapointsQueryRequest(String id, Long start, Long end) {
        DatapointsQuery datapointsQuery = new DatapointsQuery();
        com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag();
        tag.setName(id);
        List<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag> tags = new ArrayList<>();
        tags.add(tag);
        datapointsQuery.setTags(tags);
        datapointsQuery.setStart(start);
        datapointsQuery.setEnd(end);
        return datapointsQuery;
    }

    public
    @ResponseBody
    ResponseEntity<Object> handleResult(Object entity) {
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

}
