package com.company.addonsdemo;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("TestDelegate")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TestDelegate implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(TestDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("test completed");
    }
}