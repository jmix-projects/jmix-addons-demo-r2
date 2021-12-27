package com.company.addonsdemo;

import com.company.addonsdemo.entity.User;
import com.company.addonsdemo.security.FullAccessRole;
import com.google.common.collect.Sets;
import io.jmix.bpmui.security.role.BpmAdminRole;
import io.jmix.bpmui.security.role.BpmProcessActorRole;
import io.jmix.core.SaveContext;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.entity.EntityValues;
import io.jmix.core.querycondition.PropertyCondition;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import io.jmix.securityui.role.UiFilterRole;
import io.jmix.securityui.role.UiMinimalRole;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class StartupUsersCreator {

    private static final Logger log = LoggerFactory.getLogger(StartupUsersCreator.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UnconstrainedDataManager dataManager;

    @Autowired
    private AddonsDemoProperties addonsDemoProperties;

    @Autowired
    private SystemAuthenticator authenticator;

    @EventListener(ApplicationStartedEvent.class)
    public void createDemoUsers(ApplicationStartedEvent event) {

        authenticator.withSystem(() -> {
            overrideUsersPasswords();
            createMainAdminUser();

            //main admin
            createUser(addonsDemoProperties.getAdmin(),
                    addonsDemoProperties.getPassword(),
                    Sets.newHashSet(FullAccessRole.CODE));

            createUser("bpmadmin",
                    addonsDemoProperties.getPassword(),
                    Sets.newHashSet(FullAccessRole.CODE, BpmAdminRole.CODE));

            createUser("bpmuser",
                    addonsDemoProperties.getPassword(),
                    Sets.newHashSet(UiMinimalRole.CODE, UiFilterRole.CODE, BpmProcessActorRole.CODE));

            createUser("taskuser",
                    addonsDemoProperties.getPassword(),
                    Sets.newHashSet(UiMinimalRole.CODE, UiFilterRole.CODE, BpmProcessActorRole.CODE));


            Map<String, Object> attributes = new HashMap<>();
            attributes.put("firstName", "first");
            attributes.put("email", "first@gmail.com");

            createUser("first",
                    addonsDemoProperties.getPassword(),
                    attributes,
                    Sets.newHashSet(FullAccessRole.CODE));

            attributes.clear();
            attributes.put("firstName", "second");
            attributes.put("lastName", "testdata");
            attributes.put("email", "second@gmail.com");

            createUser("second",
                    addonsDemoProperties.getPassword(),
                    attributes,
                    Sets.newHashSet(FullAccessRole.CODE));

            attributes.clear();
            attributes.put("firstName", "third");
            attributes.put("lastName", "testdata");
            attributes.put("email", "third@gmail.com");

            createUser("third",
                    addonsDemoProperties.getPassword(),
                    attributes,
                    Sets.newHashSet(FullAccessRole.CODE));

            return null;
        });
    }

    private void createMainAdminUser() {
        User adminUser = dataManager.load(User.class)
                .condition(PropertyCondition.equal("username", addonsDemoProperties.getAdmin()))
                .optional()
                .orElse(null);
        if (adminUser == null) {
            User newUser = dataManager.create(User.class);
            newUser.setUsername(addonsDemoProperties.getAdmin());
            newUser.setPassword(addonsDemoProperties.getPassword());
            newUser.setActive(true);

            RoleAssignmentEntity assignment = dataManager.create(RoleAssignmentEntity.class);
            assignment.setUsername(addonsDemoProperties.getAdmin());
            assignment.setRoleCode(FullAccessRole.CODE);
            assignment.setRoleType(RoleAssignmentRoleType.RESOURCE);

            dataManager.save(newUser, assignment);
        }
    }

    private void overrideUsersPasswords() {
        List<User> users = dataManager.load(User.class)
                .all()
                .list();
        SaveContext saveContext = new SaveContext();
        for (User user : users) {
            user.setPassword(addonsDemoProperties.getPassword());
            saveContext.saving(user);
        }
        dataManager.save(saveContext);
    }

    private void createUser(String username, String password, Set<String> resourceRoleCodes) {
        createUser(username, password, new HashMap<>(), resourceRoleCodes);
    }

    private void createUser(String username, String password, Map<String, Object> attributes, Set<String> resourceRoleCodes) {
        User user = dataManager.load(User.class)
                .condition(PropertyCondition.equal("username", username))
                .optional()
                .orElse(null);
        if (user == null) {
            SaveContext saveContext = new SaveContext();
            User newUser = dataManager.create(User.class);
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setActive(true);


            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                EntityValues.setValue(newUser, entry.getKey(), entry.getValue());
            }

            saveContext.saving(newUser);

            for (String roleCode : resourceRoleCodes) {
                RoleAssignmentEntity assignment = dataManager.create(RoleAssignmentEntity.class);
                assignment.setUsername(username);
                assignment.setRoleCode(roleCode);
                assignment.setRoleType(RoleAssignmentRoleType.RESOURCE);
                saveContext.saving(assignment);
            }

            dataManager.save(saveContext);
            log.info("User {} has been created", username);
        }
    }

}
