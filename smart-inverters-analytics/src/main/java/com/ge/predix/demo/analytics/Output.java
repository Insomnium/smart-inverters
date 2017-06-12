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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author predix -
 */
public class Output 
{
	Map<Long, Double> PVConsumption; 
	Map<Long, Double> networkConsumption;
	Map<Long, Double> pricePerHour;
	Double fullNetworkPrice ; 
	Double priceWithPV;
	Double batteryLevel ;
	
	public Output()
	{
		
		PVConsumption = new HashMap<Long, Double> ();
		networkConsumption = new HashMap<Long, Double> ();
		pricePerHour = new HashMap<Long, Double> ();
		fullNetworkPrice=0.0;
		priceWithPV= 0.0 ;
		batteryLevel=0.0;
	}
	
	public void Summary()
	{
		System.out.print("PV Consumption = ");
		for (Map.Entry<Long, Double> entry : PVConsumption.entrySet()) 
        {
			System.out.print(entry.getKey()+" - "+entry.getValue()+" || ");
        }
		System.out.println();
		
		System.out.print("Network Consumption = ");
		for (Map.Entry<Long, Double> entry : networkConsumption.entrySet()) 
        {
			System.out.print(entry.getKey()+" - "+entry.getValue()+" || ");
        }
		System.out.println();
		
		System.out.print("Price per Hour = ");
		for (Map.Entry<Long, Double> entry : pricePerHour.entrySet()) 
        {
			System.out.print(entry.getKey()+" - "+entry.getValue()+" || ");
        }
		System.out.println();
		System.out.println("fullNetworkPrice="+fullNetworkPrice);
		System.out.println("priceWithPV="+priceWithPV);
	}
}
