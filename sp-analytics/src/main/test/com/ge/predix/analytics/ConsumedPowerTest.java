package com.ge.predix.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.predix.demo.analytics.ConsumedPower;
import com.ge.predix.demo.analytics.GeneratedPower;
import com.ge.predix.demo.analytics.OptimalRoiForSolarSystem;
import com.ge.predix.demo.analytics.PowerDemand;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by 212525538 on 8/5/2017.
 */
public class ConsumedPowerTest {

    @Test
    public void parseTest() throws Exception {
        String demand = "{\n" +
                "    \"1493869103031\": 0,\n" +
                "    \"1493872703031\": 50,\n" +
                "    \"1493876303031\": 40,\n" +
                "    \"1493879903031\": 0,\n" +
                "    \"1493883503031\": 0,\n" +
                "    \"1493887103031\": 100,\n" +
                "    \"1493890703031\": 100\n" +
                "}";
        String generatedPwr = "{\n" +
                "    \"1493869103031\": 10,\n" +
                "    \"1493872703031\": 15,\n" +
                "    \"1493876303031\": 20,\n" +
                "    \"1493879903031\": 30,\n" +
                "    \"1493883503031\": 40,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 50,\n" +
                "    \"1493894303031\": 40,\n" +
                "    \"1493897903031\": 30,\n" +
                "    \"1493901503031\": 20,\n" +
                "    \"1493908703031\": 15,\n" +
                "    \"1493905103031\": 10\n" +
                "}";

        String json = "{ " +
                "\"powerDemand\": "+ demand  + "," +
                "\"generatedPower\": "+ generatedPwr  + "," +
                "\"batteryCapacity\": 1000 }";
        System.out.println(json);


        OptimalRoiForSolarSystem roi = new OptimalRoiForSolarSystem();


        String resultstr =  roi.calculate(json);
        System.out.println(resultstr);

        Map result = new ObjectMapper().readValue(resultstr, HashMap.class);

        assertEquals(result.get("1493869103031"), 0.0);
        assertEquals(result.get("1493872703031"), 0.0);
        assertEquals(result.get("1493876303031"), 40.0);
        assertEquals(result.get("1493879903031"), 0.0);
        assertEquals(result.get("1493883503031"), 0.0);
        assertEquals(result.get("1493887103031"), 100.0);
        assertEquals(result.get("1493890703031"), 0.0);

    }

    @Test
    public void simpleTest() throws IOException {
        String demand = "{\n" +
                "    \"1493869103031\": 0,\n" +
                "    \"1493872703031\": 50,\n" +
                "    \"1493876303031\": 40,\n" +
                "    \"1493879903031\": 0,\n" +
                "    \"1493883503031\": 0,\n" +
                "    \"1493887103031\": 100,\n" +
                "    \"1493890703031\": 100\n" +
                "}";

        PowerDemand pd = new PowerDemand(demand);

        String generatedPwr = "{\n" +
                "    \"1493869103031\": 10,\n" +
                "    \"1493872703031\": 15,\n" +
                "    \"1493876303031\": 20,\n" +
                "    \"1493879903031\": 30,\n" +
                "    \"1493883503031\": 40,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 50,\n" +
                "    \"1493894303031\": 40,\n" +
                "    \"1493897903031\": 30,\n" +
                "    \"1493901503031\": 20,\n" +
                "    \"1493908703031\": 15,\n" +
                "    \"1493905103031\": 10\n" +
                "}";

        GeneratedPower generated = new GeneratedPower(generatedPwr);

        ConsumedPower result = new OptimalRoiForSolarSystem().calculateInternal(generated, pd, 1000.0);

        Map<Long, Double> expected = new TreeMap<>();
        expected.put(1493869103031l, 0.0);
        expected.put(1493872703031l, 0.0);
        expected.put(1493876303031l, 40.0);
        expected.put(1493879903031l, 0.0);
        expected.put(1493883503031l, 0.0);
        expected.put(1493887103031l, 100.0);
        expected.put(1493890703031l, 0.0);
        assertEquals(expected, result);
    }

    @Test
    public void simpleTest_withZeroWhenNoDemand() throws IOException {
        String demand = "{\n" +
                "    \"1493869103031\": 0,\n" +
                "    \"1493872703031\": 50,\n" +
                "    \"1493876303031\": 40,\n" +
                "    \"1493879903031\": 0,\n" +
                "    \"1493883503031\": 0,\n" +
                "    \"1493887103031\": 100,\n" +
                "    \"1493890703031\": 100,\n" +
                "    \"1493894303031\": 0,\n" +
                "    \"1493897903031\": 0,\n" +
                "    \"1493901503031\": 0,\n" +
                "    \"1493908703031\": 0,\n" +
                "    \"1493905103031\": 0\n" +
                "}";

        PowerDemand pd = new PowerDemand(demand);

        String generatedPwr = "{\n" +
                "    \"1493869103031\": 10,\n" +
                "    \"1493872703031\": 15,\n" +
                "    \"1493876303031\": 20,\n" +
                "    \"1493879903031\": 30,\n" +
                "    \"1493883503031\": 40,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 50,\n" +
                "    \"1493894303031\": 40,\n" +
                "    \"1493897903031\": 30,\n" +
                "    \"1493901503031\": 20,\n" +
                "    \"1493908703031\": 15,\n" +
                "    \"1493905103031\": 10\n" +
                "}";

        GeneratedPower generated = new GeneratedPower(generatedPwr);

        ConsumedPower result = new OptimalRoiForSolarSystem().calculateInternal(generated, pd, 1000.0);

        Map<Long, Double> expected = new TreeMap<>();
        expected.put(1493869103031l, 0.0);
        expected.put(1493872703031l, 0.0);
        expected.put(1493876303031l, 40.0);
        expected.put(1493879903031l, 0.0);
        expected.put(1493883503031l, 0.0);
        expected.put(1493887103031l, 100.0);
        expected.put(1493890703031l, 0.0);
        expected.put(1493894303031l, 0.0);
        expected.put(1493897903031l, 0.0);
        expected.put(1493901503031l, 0.0);
        expected.put(1493908703031l, 0.0);
        expected.put(1493905103031l, 0.0);

        assertEquals(expected, result);
    }

    @Test
    public void generated_equals_consumed() throws IOException {
        String demand = "{\n" +
                "    \"1493869103031\": 0,\n" +
                "    \"1493872703031\": 0,\n" +
                "    \"1493876303031\": 0,\n" +
                "    \"1493879903031\": 100,\n" +
                "    \"1493883503031\": 50,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 0\n" +
                "}";

        PowerDemand pd = new PowerDemand(demand);

        String generatedPwr = "{\n" +
                "    \"1493869103031\": 10,\n" +
                "    \"1493872703031\": 20,\n" +
                "    \"1493876303031\": 30,\n" +
                "    \"1493879903031\": 40,\n" +
                "    \"1493883503031\": 50,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 0\n" +
                "}";

        GeneratedPower generated = new GeneratedPower(generatedPwr);


        ConsumedPower result = new OptimalRoiForSolarSystem().calculateInternal(generated, pd, 100.0);

        Map<Long, Double> expected = new TreeMap<>();
        expected.put(1493869103031l, 0.0);
        expected.put(1493872703031l, 0.0);
        expected.put(1493876303031l, 0.0);
        expected.put(1493879903031l, 100.0);
        expected.put(1493883503031l, 50.0);
        expected.put(1493887103031l, 50.0);
        expected.put(1493890703031l, 0.0);

        assertEquals(expected, result);
    }


    @Test
    public void generated_equals_battery_capacity() throws IOException {
        String demand = "{\n" +
                "    \"1493869103031\": 0,\n" +
                "    \"1493872703031\": 0,\n" +
                "    \"1493876303031\": 0,\n" +
                "    \"1493879903031\": 100,\n" +
                "    \"1493883503031\": 50,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 0\n" +
                "}";

        PowerDemand pd = new PowerDemand(demand);

        String generatedPwr = "{\n" +
                "    \"1493869103031\": 10,\n" +
                "    \"1493872703031\": 20,\n" +
                "    \"1493876303031\": 30,\n" +
                "    \"1493879903031\": 40,\n" +
                "    \"1493883503031\": 50,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 0\n" +
                "}";

        GeneratedPower generated = new GeneratedPower(generatedPwr);


        ConsumedPower result = new OptimalRoiForSolarSystem().calculateInternal(generated, pd, 50.0);

        Map<Long, Double> expected = new TreeMap<>();
        expected.put(1493869103031l, 0.0);
        expected.put(1493872703031l, 0.0);
        expected.put(1493876303031l, 0.0);
        expected.put(1493879903031l, 0.0);
        expected.put(1493883503031l, 50.0);
        expected.put(1493887103031l, 50.0);
        expected.put(1493890703031l, 0.0);

        assertEquals(expected, result);

        expected.clear();

        result = new OptimalRoiForSolarSystem().calculateInternal(generated, pd, 60.0);

        expected = new TreeMap<>();
        expected.put(1493869103031l, 0.0);
        expected.put(1493872703031l, 0.0);
        expected.put(1493876303031l, 0.0);
        expected.put(1493879903031l, 100.0);
        expected.put(1493883503031l, 50.0);
        expected.put(1493887103031l, 50.0);
        expected.put(1493890703031l, 0.0);

        assertEquals(expected, result);
        expected.clear();


        result = new OptimalRoiForSolarSystem().calculateInternal(generated, pd, 0.0);

        expected = new TreeMap<>();
        expected.put(1493869103031l, 0.0);
        expected.put(1493872703031l, 0.0);
        expected.put(1493876303031l, 0.0);
        expected.put(1493879903031l, 0.0);
        expected.put(1493883503031l, 50.0);
        expected.put(1493887103031l, 50.0);
        expected.put(1493890703031l, 0.0);

        assertEquals(expected, result);
        expected.clear();

        result = new OptimalRoiForSolarSystem().calculateInternal(generated, pd, 10.0);

        expected = new TreeMap<>();
        expected.put(1493869103031l, 0.0);
        expected.put(1493872703031l, 0.0);
        expected.put(1493876303031l, 0.0);
        expected.put(1493879903031l, 0.0);
        expected.put(1493883503031l, 50.0);
        expected.put(1493887103031l, 50.0);
        expected.put(1493890703031l, 0.0);

        assertEquals(expected, result);
    }

    @Test
    public void not_enough_generated() throws IOException {
        String demand = "{\n" +
                "    \"1493869103031\": 0,\n" +
                "    \"1493872703031\": 0,\n" +
                "    \"1493876303031\": 0,\n" +
                "    \"1493879903031\": 0,\n" +
                "    \"1493883503031\": 50,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 100,\n" +
                "    \"1493894303031\": 80,\n" +
                "    \"1493897903031\": 0,\n" +
                "    \"1493901503031\": 0,\n" +
                "    \"1493908703031\": 0,\n" +
                "    \"1493905103031\": 0\n" +
                "}";

        PowerDemand pd = new PowerDemand(demand);

        String generatedPwr = "{\n" +
                "    \"1493869103031\": 10,\n" +
                "    \"1493872703031\": 20,\n" +
                "    \"1493876303031\": 30,\n" +
                "    \"1493879903031\": 40,\n" +
                "    \"1493883503031\": 50,\n" +
                "    \"1493887103031\": 50,\n" +
                "    \"1493890703031\": 40,\n" +
                "    \"1493894303031\": 30,\n" +
                "    \"1493897903031\": 20,\n" +
                "    \"1493901503031\": 10,\n" +
                "    \"1493908703031\": 0,\n" +
                "    \"1493905103031\": 0\n" +
                "}";

        GeneratedPower generated = new GeneratedPower(generatedPwr);


        ConsumedPower result = new OptimalRoiForSolarSystem().calculateInternal(generated, pd, 100.0);

        Map<Long, Double> expected = new TreeMap<>();
        expected.put(1493869103031l, 0.0);
        expected.put(1493872703031l, 0.0);
        expected.put(1493876303031l, 0.0);
        expected.put(1493879903031l, 0.0);
        expected.put(1493883503031l, 50.0);
        expected.put(1493887103031l, 50.0);
        expected.put(1493890703031l, 100.0);
        expected.put(1493894303031l, 0.0);
        expected.put(1493897903031l, 0.0);
        expected.put(1493901503031l, 0.0);
        expected.put(1493908703031l, 0.0);
        expected.put(1493905103031l, 0.0);

        assertEquals(expected, result);
    }


}
