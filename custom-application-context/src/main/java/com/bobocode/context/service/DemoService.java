package com.bobocode.context.service;

import com.bobocode.context.annotations.BoboComponent;

@BoboComponent(name = "demoService")
public class DemoService {

    public void talk() {
        System.out.println("[DemoService] - What's up?");
    }
}
