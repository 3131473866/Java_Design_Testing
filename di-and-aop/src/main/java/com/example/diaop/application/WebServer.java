package com.example.diaop.application;

import com.example.diaop.interfaces.Frontend;
import com.example.diaop.interfaces.Middleware;
import com.example.diaop.interfaces.Persistence;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class WebServer {
	private final Frontend frontend;
	private final Middleware middleware;
	private final Persistence persistence;

	@Inject
	public WebServer(Frontend frontend, Middleware middleware, Persistence persistence) {
		this.frontend = frontend;
		this.middleware = middleware;
		this.persistence = persistence;
	}
	
	public String getType() {
		return "WebServer";
	}

	public boolean run() {
		System.out.println("running " + this.getType());
		
		// invoke services here if applicable
		frontend.run();
		middleware.run();
		persistence.run();
		
		return true;
	}
}
