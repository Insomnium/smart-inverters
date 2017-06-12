package com.ge.predix.demo.solar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.predix.demo.solar.model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 212525538 on 4/5/2017.
 */
public class OptimalRoiForSolarSystem {

    ObjectMapper mapper = new ObjectMapper();

    public String calculateSmart(String json) throws IOException
    {
        Map<String, Object> map = mapper.readValue(json, HashMap.class);

        Map<String, Number> _generatedPower = (Map<String, Number>) map.get("generatedPower");
        GeneratedPower generated = new GeneratedPower(_generatedPower);

        Number batteryCapacity = (Number) map.get("batteryCapacity");

        Map<String, Number> _powerDemand = (Map<String, Number>) map.get("powerDemand");
        PowerDemand powerDemand = new PowerDemand(_powerDemand);

        Map<String, Number> _price = (Map<String, Number>) map.get("price");
        Price price = new Price(_price);

        Map<Long, Double> c = new HashMap<Long, Double>();
        double minPrice = 9999999.999;
        double maxPrice = 0.0;
        double avgPrice =0.0;
        int counter = 0;
        for (Map.Entry<Long, Double> entry : price.entrySet())
        {
            if (minPrice> entry.getValue())
                minPrice=entry.getValue();
            if (maxPrice < entry.getValue())
                maxPrice = entry.getValue();
            avgPrice+=entry.getValue();
            counter++;
        }
        avgPrice=avgPrice/counter;


        Output output = new Output();
        Double fullNetworkPrice=0.0;
        double batteryEnergy = 0.0;
        double optimizedNetworkPrice=0.0;
        for (Map.Entry<Long, Double> entry : powerDemand.entrySet())
        {

            // for each power level assume that we have power available if
            // the sum of the generated power up until that hour minus previously consumed power
            // is larger than 0

            Long hour = entry.getKey();
            Double pwrDemand = entry.getValue();
            Double generatedPwr = generated.get(hour);
            Double hourNetworkPrice = price.get(hour);



            fullNetworkPrice+=hourNetworkPrice*pwrDemand;


            boolean satisfiedDay = false;
            boolean batteryFull = false;
            double needRemained =pwrDemand;

            //first check if battery capacity will be reached
            //if so fill battery and consume excess;
            c.put(hour, 0.0);
            if (batteryCapacity.doubleValue() < batteryEnergy+generatedPwr)
            {

                c.put(hour, Math.min((batteryEnergy+generatedPwr-batteryCapacity.doubleValue()),needRemained));


                generatedPwr= Math.max(0, generatedPwr-c.get(hour)) ; // adjust remaining power

                batteryEnergy=batteryCapacity.doubleValue();
                batteryFull=true;
                if (c.get(hour)==pwrDemand)
                    satisfiedDay=true ;
                else
                    needRemained=pwrDemand-c.get(hour);
            }

            if (!satisfiedDay)
            {
                if (!batteryFull && hourNetworkPrice<avgPrice)//(minPrice+avgPrice)/2)
                {
                    batteryEnergy += generatedPwr;
                    c.put(hour, 0.0);
                }
                else if (batteryEnergy + generatedPwr >= needRemained)
                {
                    c.put(hour, c.get(hour)+needRemained);
                    batteryEnergy = Math.min(batteryEnergy + generatedPwr - needRemained,batteryCapacity.doubleValue());
                }
                else
                {
                    batteryEnergy = Math.min(batteryEnergy+generatedPwr,batteryCapacity.doubleValue());
                    c.put(hour, c.get(hour)+batteryEnergy);
                    batteryEnergy=0;
                }
            }


            optimizedNetworkPrice+= (pwrDemand-c.get(hour))*hourNetworkPrice;

            batteryEnergy = Math.min(batteryEnergy, batteryCapacity.doubleValue());

            System.out.println(entry.getKey()+" - "+entry.getValue()+"  batteryEnergy="+batteryEnergy);
        }

        System.out.println("min price = "+minPrice);
        System.out.println("max price = "+maxPrice);
        System.out.println("avg price = "+avgPrice);
        System.out.println("end of day bat lvl = "+batteryEnergy);
        System.out.println("full network price = "+fullNetworkPrice +" vs if optimized price ="+optimizedNetworkPrice);

        return mapper.writeValueAsString(c);
    }



    public String calculate(String json) throws IOException {
        Map<String, Object> map = mapper.readValue(json, HashMap.class);
        Map<String, Number> generatedPower = (Map<String, Number>) map.get("generatedPower");
        Number batCapacity = (Number) map.get("batteryCapacity");
        Map<String, Number> powerDemand = (Map<String, Number>) map.get("powerDemand");

        ConsumedPower result = calculateInternal(new GeneratedPower(generatedPower), new PowerDemand(powerDemand), batCapacity.doubleValue());
        return mapper.writeValueAsString(result);
    }

    public ConsumedPower calculateInternal(GeneratedPower generated, PowerDemand powerDemand, Double batteryCapacity) {

        // take out highest powerDemand
        ConsumedPower c = new ConsumedPower();

        double batteryEnergy = 0.0;
        for (Map.Entry<Long, Double> entry : powerDemand.entrySet()) {

            // for each power level assume that we have power available if
            // the sum of the generated power up until that hour minus previously consumed power
            // is larger than 0

            Long hour = entry.getKey();
            Double pwrDemand = entry.getValue();
            Double generatedPwr = generated.get(hour);

            if (batteryEnergy + generatedPwr >= pwrDemand) {
                c.put(hour, pwrDemand);
                batteryEnergy += generatedPwr - pwrDemand;
            } else {
                batteryEnergy += generatedPwr;
                c.put(hour, 0.0);
            }

            batteryEnergy = Math.min(batteryEnergy, batteryCapacity);
        }


        return c;
    }
}