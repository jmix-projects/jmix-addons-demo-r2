package com.company.addonsdemo.security;

import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityui.role.annotation.MenuPolicy;
import io.jmix.securityui.role.annotation.ScreenPolicy;

@ResourceRole(name = "Notification API screen", code = NotificationAPIScreenRole.CODE)
public interface NotificationAPIScreenRole {

    String CODE = "ntfScreen";

    @MenuPolicy(menuIds = "TestNotificationApiScreen")
    @ScreenPolicy(screenIds = {"NotificationScreen", "TestNotificationApiScreen"})
    void screens();
}