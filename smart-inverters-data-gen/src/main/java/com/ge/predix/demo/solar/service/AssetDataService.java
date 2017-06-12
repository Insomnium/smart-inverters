package com.ge.predix.demo.solar.service;

/**
 * Created by 212539039 on 6/9/2017.
 */

import com.ge.predix.demo.solar.model.GenericAsset;
import com.ge.predix.solsvc.bootstrap.ams.common.AssetConfig;
import com.ge.predix.solsvc.bootstrap.ams.factories.ModelFactory;
import com.ge.predix.solsvc.ext.util.JsonMapper;
import com.ge.predix.solsvc.restclient.impl.RestClient;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;

@Component
public class AssetDataService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private AssetConfig assetConfig;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private ModelFactory modelFactory;

    public String createAsset(String assetContent) throws IOException {
        GenericAsset genericAsset = new ObjectMapper().readValue(IOUtils.toInputStream(assetContent), GenericAsset.class);
        return create(genericAsset);
    }

    public String updateAsset(String assetContent) throws IOException {
        GenericAsset genericAsset = new ObjectMapper().readValue(IOUtils.toInputStream(assetContent), GenericAsset.class);
        return update(genericAsset);
    }

    public String deleteAsset(String assetUri) {
        return delete(assetUri);
    }

    private String create(GenericAsset genericAsset) {
        List<Header> headers = restClient.getSecureTokenForClientId();
        this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
        StringBuilder response = new StringBuilder();

        Map<Object, Object> asset = new HashMap(new BeanMap(genericAsset));
        asset.put("uri", "/" + genericAsset.getAsset().get("type") + "/" + genericAsset.getAsset().get("id"));

        HttpResponse httpResponse = modelFactory.createModel(asList(asset), headers);
        response.append(genericAsset.getAsset().get("uri") + ": " + httpResponse.toString() + "\n");
        return response.toString();
    }

    private String update(GenericAsset GenericAsset) {
        List<Header> headers = restClient.getSecureTokenForClientId();
        this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
        StringBuilder response = new StringBuilder();

        HttpResponse httpResponse = modelFactory.updateModel(GenericAsset.getAsset(), headers);
        response.append(GenericAsset.getAsset().get("uri") + ": " + httpResponse.toString() + "\n");
        return response.toString();
    }

    private String delete(String assetUri) {
        List<Header> headers = restClient.getSecureTokenForClientId();
        this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());

        HttpResponse response = this.modelFactory.deleteModel(assetUri, headers);
        return response.toString();
    }

}