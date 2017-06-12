package com.ge.predix.demo.solar.client;

import com.ge.predix.demo.solar.model.Consumer;
import com.ge.predix.demo.solar.model.ConsumerProfile;
import com.ge.predix.demo.solar.model.Consumption;
import com.ge.predix.demo.solar.model.GeneratorProfile;
import com.ge.predix.solsvc.restclient.config.IOauthRestConfig;
import com.ge.predix.solsvc.restclient.impl.RestClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 212539039 on 3/21/2017.
 */
@Component
public class SpringBootRestClient {

    @Value("${consumer.rest.service.url}")
    private String consumerServiceUri;

    @Value("${consumer.profile.rest.service.url}")
    private String consumerProfileServiceUri;

    @Value("${generation.profile.rest.service.url}")
    private String generationProfileServiceUri;

    @Autowired
    private RestClient restClient;

    @Autowired
    @Qualifier("defaultOauthRestConfig")
    private IOauthRestConfig restConfig;

    @Value("${predix.timeseries.zoneid}")
    private String zoneId;

    @Value("${predix.asset.zoneid}")
    private String predixAssetZoneId;

    private static Logger log = LoggerFactory.getLogger(SpringBootRestClient.class);

    public List<Consumer> getAllConsumers(){
        List<Header> headers = restClient.getSecureTokenForClientId();
        headers.add(new BasicHeader("Predix-Zone-Id", predixAssetZoneId));
        headers.add(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
        headers.add(new BasicHeader("Accept", "application/json;charset=UTF-8"));
        HttpResponse response = restClient.get(consumerServiceUri, headers);
        HttpEntity entity = response.getEntity();
        List<Consumer> consumerList = parseConsumerList(entity);
        return consumerList;
    }

    public List<ConsumerProfile> getAllConsumerProfiles() {
        List<Header> headers = restClient.getSecureTokenForClientId();
        headers.add(new BasicHeader("Predix-Zone-Id", predixAssetZoneId));
        HttpResponse response = restClient.get(consumerProfileServiceUri, headers);
        HttpEntity entity = response.getEntity();
        List<ConsumerProfile> consumerProfileList = parseConsumerProfileList(entity);
        return consumerProfileList;
    }

    public List<GeneratorProfile> getAllGenerationProfiles() {
        List<Header> headers = restClient.getSecureTokenForClientId();
        headers.add(new BasicHeader("Predix-Zone-Id", predixAssetZoneId));
        HttpResponse response = restClient.get(generationProfileServiceUri, headers);
        HttpEntity entity = response.getEntity();
        List<GeneratorProfile> generatorProfileList = parseGeneratorProfileList(entity);
        return generatorProfileList;
    }

    private String parseGenericResponse(HttpEntity entity) {
        try {
            return EntityUtils.toString(entity);
        }
        catch (IOException e) {
            log.error("unable to parse response from entity", e);
        }
        return null;
    }

    private List<Consumer> parseConsumerList(HttpEntity entity) {
        List<Consumer> consumerList = new ArrayList<>();
        try {
            if (entity != null) {
                String resultString = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                consumerList = mapper.readValue(resultString, new TypeReference<List<Consumer>>(){});
            }
        }
        catch (IOException e) {
            log.error("unable to read consumer list data from entity", e);
        }
        return consumerList;
    }

    private List<ConsumerProfile> parseConsumerProfileList(HttpEntity entity) {
        List<ConsumerProfile> consumerProfileList = new ArrayList<>();
        try {
            if (entity != null) {
                String resultString = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
                consumerProfileList = mapper.readValue(resultString, new TypeReference<List<ConsumerProfile>>(){});
            }
        }
        catch (IOException e) {
            log.error("unable to read consumer profile list data from entity", e);
        }
        return consumerProfileList;
    }

    private List<GeneratorProfile> parseGeneratorProfileList(HttpEntity entity) {
        List<GeneratorProfile> generatorProfileList = new ArrayList<>();
        try {
            if (entity != null) {
                String resultString = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
                generatorProfileList = mapper.readValue(resultString, new TypeReference<List<GeneratorProfile>>(){});
            }
        }
        catch (IOException e) {
            log.error("unable to read generator profile list data from entity", e);
        }
        return generatorProfileList;
    }

}
