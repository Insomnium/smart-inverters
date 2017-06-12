package com.ge.predix.demo.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 212525538 on 4/5/2017.
 */
public class OptimalRoiForSolarSystem {

    ObjectMapper mapper = new ObjectMapper();

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

    public static void main(String[] args)
    {

        Double batteryCapacity = (double) 100 ;
        Map<Long, Double> generated = new HashMap<Long, Double>();
        Map<Long, Double> powerDemand = new HashMap<Long, Double>();
        Map<Long, Double> networkPrice = new HashMap<Long, Double>();
        Map<Long, Double> c = new HashMap<Long, Double>();

        networkPrice.put((long)0, 5.5);
        networkPrice.put((long) 1, 5.5);
        networkPrice.put((long)2, 5.5);
        networkPrice.put((long)3, 5.5);
        networkPrice.put((long)4, 5.5);
        networkPrice.put((long)5, 5.5);
        networkPrice.put((long)6, 5.5);
        networkPrice.put((long)7, 7.5);
        networkPrice.put((long)8, 10.5);
        networkPrice.put((long)9, 12.5);
        networkPrice.put((long)10, 15.5);
        networkPrice.put((long)11, 17.5);
        networkPrice.put((long)12, 19.5);
        networkPrice.put((long)13, 22.5);
        networkPrice.put((long)14, 22.5);
        networkPrice.put((long)15, 22.5);
        networkPrice.put((long)16, 22.5);
        networkPrice.put((long)17, 22.5);
        networkPrice.put((long)18, 22.5);
        networkPrice.put((long)19, 20.5);
        networkPrice.put((long)20, 18.5);
        networkPrice.put((long)21, 15.5);
        networkPrice.put((long)22, 10.5);
        networkPrice.put((long) 23, 7.5);

        powerDemand.put((long)0, 5.5);
        powerDemand.put((long) 1, 5.5);
        powerDemand.put((long)2, 5.5);
        powerDemand.put((long)3, 5.5);
        powerDemand.put((long)4, 5.5);
        powerDemand.put((long)5, 5.5);
        powerDemand.put((long)6, 10.5);
        powerDemand.put((long)7, 20.5);
        powerDemand.put((long)8, 50.5);
        powerDemand.put((long)9, 30.5);
        powerDemand.put((long)10, 30.5);
        powerDemand.put((long)11, 20.5);
        powerDemand.put((long)12, 30.5);
        powerDemand.put((long)13, 20.5);
        powerDemand.put((long)14, 30.5);
        powerDemand.put((long)15, 40.5);
        powerDemand.put((long)16, 50.5);
        powerDemand.put((long)17, 60.5);
        powerDemand.put((long)18, 70.5);
        powerDemand.put((long)19, 50.5);
        powerDemand.put((long)20, 50.5);
        powerDemand.put((long)21, 50.5);
        powerDemand.put((long)22, 40.5);
        powerDemand.put((long) 23, 20.5);

        generated.put((long)0, 0.0);
        generated.put((long) 1, 0.0);
        generated.put((long)2, 0.0);
        generated.put((long)3, 0.0);
        generated.put((long)4, 0.0);
        generated.put((long)5, 0.0);
        generated.put((long)6, 5.0);
        generated.put((long)7, 20.5);
        generated.put((long)8, 20.5);
        generated.put((long)9, 20.5);
        generated.put((long)10, 40.5);
        generated.put((long)11, 40.5);
        generated.put((long)12, 40.5);
        generated.put((long)13, 40.5);
        generated.put((long)14, 20.5);
        generated.put((long)15, 20.5);
        generated.put((long)16, 20.5);
        generated.put((long)17, 20.5);
        generated.put((long)18, 20.5);
        generated.put((long)19, 20.5);
        generated.put((long)20, 20.5);
        generated.put((long)21, 0.0);
        generated.put((long)22, 0.0);
        generated.put((long) 23, 0.0);

        double fullNetworkPrice = 0.0;
        double optimizedNetworkPrice =0.0;

        double minPrice = 9999999.999;
        double maxPrice = 0.0;
        double avgPrice =0.0;

        for (Map.Entry<Long, Double> entry : networkPrice.entrySet())
        {
            if (minPrice> entry.getValue())
                minPrice=entry.getValue();
            if (maxPrice < entry.getValue())
                maxPrice = entry.getValue();
            avgPrice+=entry.getValue();
        }
        avgPrice=avgPrice/24;


        double batteryEnergy = 30;

        //let's test all possible combinations
        int producingHours = 0 ;
        int firstProducing = 0;
        int lastProducing = 0;
        int counter =0;
        for (Map.Entry<Long, Double> entry : generated.entrySet())
        {
            if (entry.getValue()>0)
            {
                producingHours++;
                lastProducing=counter ;
            }
            if (firstProducing==0 && entry.getValue()>0)
                firstProducing=counter;

            counter++;
        }
        System.out.println(firstProducing);
        System.out.println(lastProducing);

        System.out.println(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));

        String goodCombo ="";
        double bestPrice = 9999999.99;
        int nBits = 19;//lastProducing-firstProducing; or go back 24... 32 sec
        int maxNumber = 1 << nBits; //this equals 2^nBits or in java: Math.pow(2,nbits)
        ArrayList<String> binaries = new ArrayList<>();
        for (int i = 0; i < maxNumber; i++) {
            String binary = Integer.toBinaryString(i);
            while (binary.length() != nBits) {
                binary = "0" + binary;
            }
            binary="00000"+binary;
            // for (int a=0; a<firstProducing; a++)
            //         binary = "0"+ binary;
            // for (int a=lastProducing; a<24; a++)
            //         binary =  binary + "0";
            int currentCounter = 0;
            Double batteryPower = 30.0;
            String[] comboV = binary.split("");

            double price = 0;
            for (Map.Entry<Long, Double> entry : powerDemand.entrySet())
            {
                Long hour = entry.getKey();
                Double pwrDemand = entry.getValue();
                Double generatedPwr = generated.get(hour);
                Double hourNetworkPrice = networkPrice.get(hour);

                if (comboV[currentCounter].equals("0"))
                {
                    batteryPower=Math.min(batteryPower+generatedPwr,batteryCapacity);
                    price = price + pwrDemand*hourNetworkPrice;
                }
                else
                {
                    if (generatedPwr+batteryPower>=pwrDemand)
                    {
                        batteryPower=Math.min(batteryPower+generatedPwr-pwrDemand,batteryCapacity);
                    }
                    else
                    {              pwrDemand=pwrDemand-batteryPower-generatedPwr;
                        batteryPower=0.0;
                        price = price + pwrDemand*hourNetworkPrice;
                    }
                }
                currentCounter++;
            }
            if (bestPrice>price)
            {
                bestPrice=price;
                goodCombo=binary;
            }

        }



        System.out.println(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        System.out.println(bestPrice);
        System.out.println(goodCombo);

        int currentCounter = 0;

        Double batteryPower = 30.0;
        String[] comboV = goodCombo.split("");

        double price = 0;
        for (Map.Entry<Long, Double> entry : powerDemand.entrySet())
        {
            double usedPV =0;
            System.out.print(currentCounter+" - ");
            Long hour = entry.getKey();
            Double pwrDemand = entry.getValue();
            Double generatedPwr = generated.get(hour);
            Double hourNetworkPrice = networkPrice.get(hour);

            if (comboV[currentCounter].equals("0"))
            {
                batteryPower=Math.min(batteryPower+generatedPwr,batteryCapacity);
                price = price + pwrDemand*hourNetworkPrice;
                c.put(hour, 0.0);
            }
            else
            {
                if (generatedPwr+batteryPower>=pwrDemand)
                {
                    c.put(hour, usedPV);
                    usedPV=pwrDemand;
                    batteryPower=Math.min(batteryPower+generatedPwr-pwrDemand,batteryCapacity);
                }
                else
                {
                    c.put(hour, usedPV);
                    usedPV=batteryPower+generatedPwr;
                    pwrDemand=pwrDemand-batteryPower-generatedPwr;

                    //batteryPower+=generatedPwr;
                    price = price + pwrDemand*hourNetworkPrice;
                }
            }
            currentCounter++;
            System.out.print(usedPV+" out of "+pwrDemand);
            System.out.print(" with remaining bat= "+batteryPower);
            System.out.println();
        }

        c.clear();
        for (Map.Entry<Long, Double> entry : powerDemand.entrySet()) {

            // for each power level assume that we have power available if
            // the sum of the generated power up until that hour minus previously consumed power
            // is larger than 0

            Long hour = entry.getKey();
            Double pwrDemand = entry.getValue();
            Double generatedPwr = generated.get(hour);
            Double hourNetworkPrice = networkPrice.get(hour);

            fullNetworkPrice+=hourNetworkPrice*pwrDemand;



            boolean satisfiedDay = false;
            boolean batteryFull = false;
            double needRemained =pwrDemand;

            //first check if battery capacity will be reached
            //if so fill battery and consume excess;
            if (batteryCapacity < batteryEnergy+generatedPwr)
            {
                c.put(hour, Math.min((batteryEnergy+generatedPwr-batteryCapacity),pwrDemand));
                generatedPwr-= c.get(hour) ; // adjust remaining power
                batteryEnergy=batteryCapacity;

                if (c.get(hour)==pwrDemand)
                    satisfiedDay=true ;
                else
                    needRemained=pwrDemand-c.get(hour);
            }

            if (!satisfiedDay)
            {
                if (!batteryFull && hourNetworkPrice<avgPrice/2)
                {
                    batteryEnergy += generatedPwr;
                    c.put(hour, 0.0);
                }
                else if (batteryEnergy + generatedPwr >= needRemained)
                {
                    c.put(hour, needRemained);
                    batteryEnergy += generatedPwr - needRemained;
                } else
                {
                    batteryEnergy += generatedPwr;
                    c.put(hour, 0.0);
                }
            }

            optimizedNetworkPrice+= (pwrDemand-c.get(hour))*hourNetworkPrice;

            batteryEnergy = Math.min(batteryEnergy, batteryCapacity);

            System.out.println(entry.getKey()+" - "+entry.getValue()+"  batteryEnergy="+batteryEnergy);
        }



        System.out.println("min price = "+minPrice);
        System.out.println("max price = "+maxPrice);
        System.out.println("avg price = "+avgPrice);
        System.out.println("end of day bat lvl = "+batteryEnergy);
        System.out.println("full network price = "+fullNetworkPrice +" vs if optimized price ="+optimizedNetworkPrice);

        System.out.println(c.values());


    }

}