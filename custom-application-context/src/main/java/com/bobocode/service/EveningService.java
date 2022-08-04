package com.bobocode.service;

import com.bobocode.context.annotations.BoboComponent;

@BoboComponent
public class EveningService {

    public EveningService() {
    }

    public void talk() {
        System.out.println("EveningService wishes you a good evening!");
    }
}
