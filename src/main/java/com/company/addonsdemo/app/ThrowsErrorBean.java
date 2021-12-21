package com.company.addonsdemo.app;

import org.springframework.stereotype.Component;

@Component(value = "demo_ThrowsErrorBean")
public class ThrowsErrorBean {
    public void throwError(boolean isError) {
        throw new RuntimeException("this is a process error");
    }
}