package com.company.addonsdemo.screen.importcalendar;

import com.company.addonsdemo.buscalimport.dto.HolidaysDto;
import com.company.addonsdemo.buscalimport.service.HolidayApiImportService;
import com.company.addonsdemo.buscalimport.service.HolidayApiIntegrationService;
import io.jmix.businesscalendarui.screen.calendar.BusinessCalendarBrowse;
import io.jmix.core.Messages;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.ComponentsHelper;
import io.jmix.ui.component.TextField;
import io.jmix.ui.component.ValidationErrors;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("ImportCalendar")
@UiDescriptor("importcalendar.xml")
public class ImportCalendarScreen extends Screen {
    @Autowired
    private HolidayApiIntegrationService holidayApiIntegrationService;
    @Autowired
    private HolidayApiImportService importService;
    @Autowired
    private TextField<String> calendarNameFieldId;
    @Autowired
    private TextField<String> calendarCodeFieldId;
    @Autowired
    private TextField<String> apiKeyFieldId;
    @Autowired
    private TextField<String> yearFieldId;
    @Autowired
    private TextField<String> countryFieldId;
    @Autowired
    private Notifications notifications;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private ScreenValidation screenValidation;
    @Autowired
    private Messages messages;

    @Subscribe("commitAndCloseBtn")
    public void onCommitAndCloseBtnClick(Button.ClickEvent event) {
        ValidationErrors validationErrors =
                screenValidation.validateUiComponents(ComponentsHelper.getComponents(getWindow()));
        if (!validationErrors.isEmpty()) {
            screenValidation.showValidationErrors(this, validationErrors);
            return;
        }
        String calendarName = calendarNameFieldId.getValue();
        String calendarCode = calendarCodeFieldId.getValue();
        String holidayApiKey = apiKeyFieldId.getValue();
        String requestedYear = yearFieldId.getValue();
        String requestedCountry = countryFieldId.getValue();
        HolidaysDto holidays =
                holidayApiIntegrationService.getHolidaysInformation(holidayApiKey, requestedCountry,
                        requestedYear);
        importService.importCalendar(calendarName, calendarCode, holidays);
        notifications.create(Notifications.NotificationType.HUMANIZED)
                .withCaption(messages.getMessage(this.getClass(), "calendarImported"))
                .show();
        screenBuilders.screen(this)
                .withScreenClass(BusinessCalendarBrowse.class)
                .withOpenMode(OpenMode.THIS_TAB)
                .show();
    }

    @Subscribe("closeBtn")
    public void onCloseBtnClick(Button.ClickEvent event) {
        close(StandardOutcome.CLOSE);
    }
}