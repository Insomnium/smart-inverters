package com.ge.predix.demo.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 212525538 on 4/5/2017.
 */
public class OptimalRoiForSolarSystem {

    ObjectMapper mapper = new ObjectMapper();

    public String calculate(String json) throws IOException {
        Map<String, Object> map = mapper.readValue(json, HashMap.class);
        
        Map<String, Number> _generatedPower = (Map<String, Number>) map.get("generatedPower");
        GeneratedPower generated = new GeneratedPower(_generatedPower);
       
        Number batteryCapacity = (Number) map.get("batteryCapacity");
        
        Map<String, Number> _powerDemand = (Map<String, Number>) map.get("powerDemand");
        PowerDemand powerDemand = new PowerDemand(_powerDemand);
        
		Map<String, Number> _price = (Map<String, Number>) map.get("price");
		Price price = new Price(_price);

        Output output = new Output(); 
        
        //dumb version
        double batteryEnergy = 0.0;
        for (Map.Entry<Long, Double> entry : powerDemand.entrySet()) 
        {
        	
        	Long hour = entry.getKey();
			Double pwrDemand = entry.getValue();
            Double generatedPwr = generated.get(hour);
            Double hourNetworkPrice = price.get(hour);

            // for each power level assume that we have power available if
            // the sum of the generated power up until that hour minus previously consumed power
            // is larger than 0

            output.fullNetworkPrice+=pwrDemand*hourNetworkPrice;

            if (batteryEnergy + generatedPwr >= pwrDemand) 
            {
            	output.PVConsumptionDumb.put(hour, pwrDemand);
                batteryEnergy += generatedPwr - pwrDemand;
            } else {
                batteryEnergy += generatedPwr;
                output.PVConsumptionDumb.put(hour, 0.0);
            }
            
            output.priceWithPVDumb+=(pwrDemand-output.PVConsumptionDumb.get(hour))*hourNetworkPrice;

            batteryEnergy = Math.min(batteryEnergy, batteryCapacity.doubleValue());
        }
        //end dumb version
        
        
        //smart version 
        double fullNetworkPrice = 0.0;
		double optimizedNetworkPrice =0.0;
		double staringDayBattery = 0; 
		
		batteryEnergy = staringDayBattery;
		
		//let's test all possible combinations
		int producingHours = 0 ;
		int firstProducing = 0;
		int lastProducing = 0;
		int counter =0;
		int genHours = 24 ;
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
		double tempBatenergy = batteryEnergy;
		for (Map.Entry<Long, Double> entry : generated.entrySet()) 
		{
			if (entry.getValue()<=0)
				genHours--;
			else if (entry.getValue()+tempBatenergy < powerDemand.get(entry.getKey()))
			{
				tempBatenergy+=entry.getValue();
				genHours--;
			}
			else 
			{
				break;
			}
		}
		
		//System.out.println(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
		
		int goodCount = 0 ;
		
		String goodCombo ="";
		double bestPrice = 9999999.99;
		int nBits = genHours;//19;//lastProducing-firstProducing; or go back 24... 32 sec
		int maxNumber = 1 << nBits; //this equals 2^nBits or in java: Math.pow(2,nbits)
		ArrayList<String> binaries = new ArrayList<>();
		for (int i = 0; i < maxNumber; i++) {
		    String binary = Integer.toBinaryString(i);
		    while (binary.length() != nBits) 
		    {
		        binary = "0" + binary;
		    }
		    
		    for (int a=genHours; a<24; a++)
		    	binary = "0"+ binary;
	
		    int currentCounter = 0;
		
			String[] comboV = binary.split("");
			
			double thisPrice = 0;
			for (Map.Entry<Long, Double> entry : powerDemand.entrySet()) 
			{
				Long hour = entry.getKey();
				Double pwrDemand = entry.getValue();
	            Double generatedPwr = generated.get(hour);
	            Double hourNetworkPrice = price.get(hour);
	            
				if (comboV[currentCounter].equals("0"))
				{
					batteryEnergy=Math.min(batteryEnergy+generatedPwr,batteryCapacity.doubleValue());
					thisPrice = thisPrice + pwrDemand*hourNetworkPrice;
				}
				else 
				{
					if (generatedPwr+batteryEnergy>=pwrDemand)
					{
						batteryEnergy=Math.min(batteryEnergy+generatedPwr-pwrDemand,batteryCapacity.doubleValue());
					}
					else
					{	pwrDemand=pwrDemand-batteryEnergy-generatedPwr;
						batteryEnergy=0.0;
						thisPrice = thisPrice + pwrDemand*hourNetworkPrice;
					}
				}
				currentCounter++;
			}
			if (bestPrice>thisPrice)
			{
				bestPrice=thisPrice;
				goodCombo=binary;
			}

		}
		
		//System.out.println(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
		System.out.println(goodCombo);
       
		int currentCounter = 0;
		
		Double batteryPower = staringDayBattery; 
		String[] comboV = goodCombo.split("");
		
		double thisPrice = 0;
		for (Map.Entry<Long, Double> entry : powerDemand.entrySet()) 
		{
			
			double usedPV =0;
			//System.out.print(currentCounter+" - ");
			Long hour = entry.getKey();
			Double pwrDemand = entry.getValue();
            Double generatedPwr = generated.get(hour);
            Double hourNetworkPrice = price.get(hour);
            
            output.fullNetworkPrice+=pwrDemand*hourNetworkPrice;
            
			if (comboV[currentCounter].equals("0"))
			{
				batteryPower=Math.min(batteryPower+generatedPwr,batteryCapacity.doubleValue());
				thisPrice = thisPrice + pwrDemand*hourNetworkPrice;
				output.PVConsumptionSmart.put(hour, 0.0);
				output.pricePerHourSmart.put(hour, pwrDemand*hourNetworkPrice);
				output.networkConsumptionSmart.put(hour, pwrDemand);
			}
			else 
			{
				if (generatedPwr+batteryPower>=pwrDemand)
				{
					
					usedPV=pwrDemand;
					batteryPower=Math.min(batteryPower+generatedPwr-pwrDemand,batteryCapacity.doubleValue());
					output.PVConsumptionSmart.put(hour, usedPV);
					output.pricePerHourSmart.put(hour, 0.0);
					output.networkConsumptionSmart.put(hour, 0.0);
				}
				else
				{	
					
					usedPV=batteryPower+generatedPwr;
					pwrDemand=pwrDemand-batteryPower-generatedPwr;
					batteryPower=0.0;
					output.PVConsumptionSmart.put(hour, usedPV);
					//batteryPower+=generatedPwr;
					thisPrice = thisPrice + pwrDemand*hourNetworkPrice;
					output.pricePerHourSmart.put(hour, pwrDemand*hourNetworkPrice);
					output.networkConsumptionSmart.put(hour, pwrDemand);
				}
			}
			
			currentCounter++;
			
		}
		
			output.priceWithPVSmart=thisPrice;
			output.batteryLevelSmart=batteryEnergy;
        	System.out.println("end of day bat lvl = "+batteryEnergy);
        
        //end smart 
        
       
        return mapper.writeValueAsString(output);
    }
	
	
	
	public String calculateSmart(String json) throws IOException {
        Map<String, Object> map = mapper.readValue(json, HashMap.class);
        Map<String, Number> generatedPower = (Map<String, Number>) map.get("generatedPower");
        Number batCapacity = (Number) map.get("batteryCapacity");
        Map<String, Number> powerDemand = (Map<String, Number>) map.get("powerDemand");
		Map<String, Number> price = (Map<String, Number>) map.get("price");

        Output result = calculateInternal(new GeneratedPower(generatedPower), new PowerDemand(powerDemand), new Price(price), batCapacity.doubleValue());
        return mapper.writeValueAsString(result);
    }
	
	public static Output calculateInternal(Map<Long, Double> generated, Map<Long, Double> powerDemand, Map<Long, Double> networkPrice, Double batteryCapacity) 
	{
		Output output = new Output();
		
		
        	
        	
		
		return output ;
	}

 
}