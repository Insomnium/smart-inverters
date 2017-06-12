package com.ge.predix.analytics;

import com.ge.predix.demo.analytics.PowerDemand;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by 212525538 on 4/5/2017.
 */
public class PowerDemandTest {


    @Test
    public void sortingTest() throws IOException {
        PowerDemand c = new PowerDemand("{\n" +
                "    \"1493908703031\": 3,\n" +
                "    \"1493905103031\": 4,\n" +
                "    \"1493901503031\": 5,\n" +
                "    \"1493897903031\": 11,\n" +
                "    \"1493894303031\": 3,\n" +
                "    \"1493890703031\": 2,\n" +
                "    \"1493887103031\": 1,\n" +
                "    \"1493883503031\": 0.12,\n" +
                "    \"1493879903031\": 4,\n" +
                "    \"1493876303031\": 7,\n" +
                "    \"1493872703031\": 8,\n" +
                "    \"1493869103031\": 6,\n" +
                "    \"1493865503031\": 9,\n" +
                "    \"1493861903031\": 2,\n" +
                "    \"1493858303031\": 9,\n" +
                "    \"1493854703031\": 11,\n" +
                "    \"1493851103031\": 12,\n" +
                "    \"1493847503031\": 4\n" +
                "}");


       // Map<Long, Double> sortedByPwr = c.getSortedByPwr();=
        TreeSet<Long> expected = new TreeSet<>(Arrays.asList(1493851103031l, 1493897903031l,
                1493854703031l, 1493865503031l,
                1493858303031l, 1493872703031l,
                1493876303031l, 1493869103031l,
                1493901503031l, 1493905103031l,
                1493879903031l, 1493847503031l,
                1493908703031l, 1493890703031l,
                1493894303031l, 1493861903031l,
                1493861903031l, 1493887103031l,
                1493883503031l                ));
        assertEquals(expected, c.keySet());

    }
}
