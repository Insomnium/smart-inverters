package com.ge.predix.analytics;

import com.ge.predix.demo.analytics.GeneratedPower;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by 212525538 on 5/5/2017.
 */
public class GeneratedPowerTest {


    @Test
    public void construction() throws IOException {
        GeneratedPower c = new GeneratedPower("{\n" +
                "    \"1493908703031\": 100,\n" +
                "    \"1493905103031\": 110,\n" +
                "    \"1493901503031\": 90,\n" +
                "    \"1493897903031\": 1001,\n" +
                "    \"1493894303031\": 201,\n" +
                "    \"1493890703031\": 321,\n" +
                "    \"1493887103031\": 140,\n" +
                "    \"1493883503031\": 160.12,\n" +
                "    \"1493879903031\": 200,\n" +
                "    \"1493876303031\": 220,\n" +
                "    \"1493872703031\": 300,\n" +
                "    \"1493869103031\": 400,\n" +
                "    \"1493865503031\": 500,\n" +
                "    \"1493861903031\": 550,\n" +
                "    \"1493858303031\": 650,\n" +
                "    \"1493854703031\": 401,\n" +
                "    \"1493851103031\": 302,\n" +
                "    \"1493847503031\": 101\n" +
                "}");

        assertEquals(18, c.size());
        assertEquals(160.12, c.get(1493883503031l),0);
        assertEquals(101, c.get(1493847503031l),0);
    }
}
