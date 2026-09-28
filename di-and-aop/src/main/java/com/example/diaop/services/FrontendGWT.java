package com.example.diaop.services;

import com.example.diaop.interfaces.Authentication;
import com.example.diaop.interfaces.Frontend;

import javax.inject.Inject;

public class FrontendGWT implements Frontend {
    private final Authentication authentication;

    @Inject
    public FrontendGWT(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public String getType() {
        return "GWT Frontend";
    }

    @Override
    public boolean run() {
        System.out.println("running " + this.getType());
        authentication.run();
        return true;
    }
}
