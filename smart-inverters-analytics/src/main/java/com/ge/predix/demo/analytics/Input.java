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
public class Input 
{
    public Map<String, Number> generatedPower ;
    public Number batteryCapacity ;
    public Map<String, Number> powerDemand ;
    public Map<String, Number> price;
    
    public Input()
    {
    	generatedPower=new HashMap<String, Number>(); 
    	powerDemand=new HashMap<String, Number>(); 
    	price=new HashMap<String, Number>(); 
    	
    }
	
}
