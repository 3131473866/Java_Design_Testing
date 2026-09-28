package com.example.diaop.services;

import com.example.diaop.interfaces.Middleware;

public class MiddlewareTomcat implements Middleware {
	@Override
	public String getType() {
		return "Tomcat Middleware";
	}
	
	@Override
	public boolean run() {
		System.out.println("running " + this.getType());
		
		// invoke services here if applicable
		
		return true;
	}
}
