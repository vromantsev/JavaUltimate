package com.bobocode.service;

import com.bobocode.context.annotations.BoboComponent;
import com.bobocode.context.annotations.Inject;

@BoboComponent(name = "demoService")
public class DemoService {

    @Inject
    private EveningService eveningService;

    @Inject
    private GreetingService greetingService;

    public DemoService() {
    }

    public void talk() {
        System.out.println("[DemoService] - What's up?");
        System.out.print("[Injected EveningService] - ");
        eveningService.talk();
        System.out.print("[Injected GreetingService] - ");
        greetingService.talk();
    }
}
