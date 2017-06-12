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
	public Map<Long, Double> PVConsumptionSmart; 
	public Map<Long, Double> PVConsumptionDumb; 
	public Map<Long, Double> networkConsumptionSmart;
	public 	Map<Long, Double> networkConsumptionDumb;
	public 	Map<Long, Double> pricePerHourSmart;
	public 	Map<Long, Double> pricePerHourDumb;
	public 	Double fullNetworkPrice ; 
	public 	Double priceWithPVSmart;
	public 	Double priceWithPVDumb;
	public 	Double batteryLevelSmart ;
	public 	Double batteryLevelDumb;
	
	public Output()
	{
		
		PVConsumptionSmart = new HashMap<Long, Double> ();
		networkConsumptionSmart = new HashMap<Long, Double> ();
		pricePerHourSmart = new HashMap<Long, Double> ();
		fullNetworkPrice=0.0;
		priceWithPVSmart= 0.0 ;
		batteryLevelSmart=0.0;
		
		PVConsumptionDumb = new HashMap<Long, Double> ();
		networkConsumptionDumb = new HashMap<Long, Double> ();
		pricePerHourDumb = new HashMap<Long, Double> ();
		
		priceWithPVDumb= 0.0 ;
		batteryLevelDumb=0.0;
	}
	
	public void Summary()
	{
		//TODO: add the rest 
		System.out.print("PV Consumption = ");
		for (Map.Entry<Long, Double> entry : PVConsumptionSmart.entrySet()) 
        {
			System.out.print(entry.getKey()+" - "+entry.getValue()+" || ");
        }
		System.out.println();
		
		System.out.print("Network Consumption = ");
		for (Map.Entry<Long, Double> entry : networkConsumptionSmart.entrySet()) 
        {
			System.out.print(entry.getKey()+" - "+entry.getValue()+" || ");
        }
		System.out.println();
		
		System.out.print("Price per Hour = ");
		for (Map.Entry<Long, Double> entry : pricePerHourSmart.entrySet()) 
        {
			System.out.print(entry.getKey()+" - "+entry.getValue()+" || ");
        }
		System.out.println();
		System.out.println("fullNetworkPrice="+fullNetworkPrice);
		System.out.println("priceWithPV="+priceWithPVSmart);
	}
}
