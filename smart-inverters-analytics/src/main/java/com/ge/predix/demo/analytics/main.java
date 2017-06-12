/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
 
package com.ge.predix.demo.analytics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author predix -
 */

public class main {

	/**
	 * @param args -
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		// TODO Auto-input._generatedPower method stub
		Input input = new Input();
		
		input.price.put("0", 20.68);
		input.price.put("1", 19.9);
		input.price.put("2", 18.45);
		input.price.put("3", 17.85);
		input.price.put("4", 17.2);
		input.price.put("5", 17.31);
		input.price.put("6", 21.66);
		input.price.put("7", 30.57);
		input.price.put("8", 37.32);
		input.price.put("9", 41.91);
		input.price.put("10", 44.45);
		input.price.put("11", 47.28);
		input.price.put("12", 48.11);
		input.price.put("13", 47.62);
		input.price.put("14", 47.86);
		input.price.put("15", 45.97);
		input.price.put("16", 46.38);
		input.price.put("17", 46.00);
		input.price.put("18", 46.59);
		input.price.put("19", 45.23);
		input.price.put("20", 46.42);
		input.price.put("21", 48.53);
		input.price.put("22", 46.43);
		input.price.put("23", 38.29);
		
		input.powerDemand.put("0", 20.5);
		input.powerDemand.put("1", 20.5);
		input.powerDemand.put("2", 20.5);
		input.powerDemand.put("3", 20.5);
		input.powerDemand.put("4", 20.5);
		input.powerDemand.put("5", 20.5);
		input.powerDemand.put("6", 50.5);
		input.powerDemand.put("7", 70.5);
		input.powerDemand.put("8", 70.5);
		input.powerDemand.put("9", 60.5);
		input.powerDemand.put("10", 30.5);
		input.powerDemand.put("11", 30.5);
		input.powerDemand.put("12", 30.5);
		input.powerDemand.put("13", 30.5);
		input.powerDemand.put("14", 30.5);
		input.powerDemand.put("15", 30.5);
		input.powerDemand.put("16", 30.5);
		input.powerDemand.put("17", 50.5);
		input.powerDemand.put("18", 60.5);
		input.powerDemand.put("19", 70.5);
		input.powerDemand.put("20", 70.5);
		input.powerDemand.put("21", 70.5);
		input.powerDemand.put("22", 60.5);
		input.powerDemand.put("23", 40.5);
		
		input.generatedPower.put("0", 0.0);
		input.generatedPower.put("1", 0.0);
		input.generatedPower.put("2", 0.0);
		input.generatedPower.put("3", 0.0);
		input.generatedPower.put("4", 0.0);
		input.generatedPower.put("5", 0.0);
		input.generatedPower.put("6", 40.0);
		input.generatedPower.put("7", 80.5);
		input.generatedPower.put("8", 120.5);
		input.generatedPower.put("9", 160.5);
		input.generatedPower.put("10", 180.5);
		input.generatedPower.put("11", 200.5);
		input.generatedPower.put("12", 220.5);
		input.generatedPower.put("13", 220.5);
		input.generatedPower.put("14", 200.5);
		input.generatedPower.put("15", 180.5);
		input.generatedPower.put("16", 160.5);
		input.generatedPower.put("17", 120.5);
		input.generatedPower.put("18", 80.5);
		input.generatedPower.put("19", 40.5);
		input.generatedPower.put("20", 0.0);
		input.generatedPower.put("21", 0.0);
		input.generatedPower.put("22", 0.0);
		input.generatedPower.put("23", 0.0);
		
		
		input.batteryCapacity=5000;
		
		String jsonInput;
		ObjectMapper mapper = new ObjectMapper();
		jsonInput=mapper.writeValueAsString(input);
		
		System.out.println(jsonInput);

		String output = new OptimalRoiForSolarSystem().calculate(jsonInput);
		System.out.println(output);


	}

}
