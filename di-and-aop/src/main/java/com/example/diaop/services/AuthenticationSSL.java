package com.example.diaop.services;

import com.example.diaop.interfaces.Authentication;

public class AuthenticationSSL implements Authentication {
	@Override
	public String getType() {
		return "SSL Authentication";
	}
	
	@Override
	public boolean run() {
		System.out.println("running " + this.getType());
		
		// invoke services here if applicable
		
		return true;
	}
}
