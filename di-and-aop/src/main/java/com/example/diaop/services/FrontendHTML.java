package com.example.diaop.services;

import com.example.diaop.interfaces.Authentication;
import com.example.diaop.interfaces.Frontend;

import javax.inject.Inject;

public class FrontendHTML implements Frontend {
    private final Authentication authentication;

    @Inject
    public FrontendHTML(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public String getType() {
        return "HTML Frontend";
    }

    @Override
    public boolean run() {
        System.out.println("running " + this.getType());
        authentication.run();
        return true;
    }
}
