package com.bobocode.context.service;

import com.bobocode.context.annotations.BoboComponent;

@BoboComponent
public class GreetingService {

    public void talk() {
        System.out.println("Hello from GreetingService!");
    }
}