package com.bobocode;

import com.bobocode.context.AnnotationConfigApplicationContext;
import com.bobocode.context.ApplicationContext;
import com.bobocode.service.DemoService;
import com.bobocode.service.EveningService;
import com.bobocode.service.GreetingService;

import java.util.Map;

public class DemoApp {

    private static final String DEFAULT_PACKAGE = "com.bobocode";

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(DEFAULT_PACKAGE);
        final EveningService eveningService = context.getBean(EveningService.class);
        eveningService.talk();

        final GreetingService greetingService = context.getBean(GreetingService.class);
        greetingService.talk();

        final GreetingService gsByName = context.getBean("greetingService", GreetingService.class);
        gsByName.talk();

        final EveningService esByName = context.getBean("eveningService", EveningService.class);
        esByName.talk();

        final Map<String, DemoService> allBeans = context.getAllBeans(DemoService.class);
        System.out.println(allBeans);
    }
}
