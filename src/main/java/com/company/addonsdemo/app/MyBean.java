package com.company.addonsdemo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value = "demo_MyBean")
public class MyBean {

    private static final Logger log = LoggerFactory.getLogger(MyBean.class);

    public void first(String inp1, String inp2) {
        log.info(inp1 + inp2);
    }

    public String newLastName(String lastName) {
        return lastName + " (modified)";
    }
}