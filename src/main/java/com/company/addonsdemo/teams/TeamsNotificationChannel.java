package com.company.addonsdemo.teams;

import com.company.addonsdemo.entity.User;
import com.google.common.base.Strings;
import io.jmix.notifications.Notification;
import io.jmix.notifications.channel.NotificationChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TeamsNotificationChannel implements NotificationChannel {
    public static final String NAME = "teams";
    private static final Logger log =
            LoggerFactory.getLogger(TeamsNotificationChannel.class);
    @Autowired
    protected TeamsClient teamsClient;
    @Autowired
    protected ProjectNotificationsProperties projectNotificationsProperties;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean send(Notification notification) {
        log.debug("Send message to teams channel");
        String hookUrl = getHookUrl(notification.getRecipient());
        if (Strings.isNullOrEmpty(hookUrl)) {
            log.error("Teams webhook url is not set");
            return false;
        }
        String subject = notification.getSubject();
        String body = notification.getBody();
        UserDetails recipient = notification.getRecipient();
        User user = (User) recipient;
        String userDisplayName = user.getDisplayName();
        MessageCard card = MessageCard.builder()
                .summary(subject)
                .addSection(
                        Section.builder()
                                .title(subject)
                                .subtitle(body)
                                .addFact(Fact.of("User", userDisplayName))
                                .build()
                ).build();
        return teamsClient.sendMessage(hookUrl, card);
    }

    protected String getHookUrl(UserDetails user) {
        //Resolve webhook url by user if necessary
        return projectNotificationsProperties.getTeamsHookUrl();
    }
}
