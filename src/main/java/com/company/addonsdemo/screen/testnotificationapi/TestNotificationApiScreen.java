package com.company.addonsdemo.screen.testnotificationapi;

import com.company.addonsdemo.screen.notification.NotificationScreen;
import io.jmix.notifications.NotificationManager;
import io.jmix.notifications.channel.impl.UiNotificationChannel;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.Button;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("TestNotificationApiScreen")
@UiDescriptor("test-notification-api-screen.xml")
public class TestNotificationApiScreen extends Screen {

    @Autowired
    NotificationManager notificationManager;

    @Autowired
    UiNotificationChannel uiNotificationChannel;

    @Autowired
    private ScreenBuilders screenBuilders;

    @Subscribe("send")
    public void onSendClick(Button.ClickEvent event) {
        NotificationManager.NotificationRequestBuilder notificationRequestBuilder = (NotificationManager.NotificationRequestBuilder) notificationManager.createNotification();
        notificationRequestBuilder.withSubject("Key test").withRecipientUsernames("notification_user").toChannels(uiNotificationChannel).withBody("my test body").withTypeName("info").send();
    }

    @Subscribe("show")
    public void onShowClick(Button.ClickEvent event) {
        screenBuilders.screen(this)
                .withScreenClass(NotificationScreen.class)
                .build()
                .show();
    }
}