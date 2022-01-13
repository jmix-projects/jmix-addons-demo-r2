package com.company.addonsdemo.teams;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("project.notifications")
public class ProjectNotificationsProperties {
    protected final String teamsHookUrl;

    public ProjectNotificationsProperties(String teamsHookUrl) {
        this.teamsHookUrl = teamsHookUrl;
    }

    public String getTeamsHookUrl() {
        return teamsHookUrl;
    }
}