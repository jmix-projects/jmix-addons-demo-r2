package com.company.addonsdemo.screen.buscalapi;

import io.jmix.businesscalendar.model.BusinessCalendar;
import io.jmix.businesscalendar.repository.BusinessCalendarRepository;
import io.jmix.ui.component.*;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
@UiController("BuscalApiScreen")
@UiDescriptor("buscal-api-screen.xml")
public class BuscalApiScreen extends Screen {
    @Autowired
    protected BusinessCalendarRepository businessCalendarRepository;
    @Autowired
    private ComboBox<String> businessCalendarNameFieldId;
    @Autowired
    private DateField<LocalDateTime> currentDateTimeFieldId;
    @Autowired
    private ComboBox<ChronoUnit> timeUnitFieldId;
    @Autowired
    private TextField<Long> timeAmountFieldId;
    @Autowired
    private ComboBox<String> timeOperationFieldId;
    @Autowired
    private ScreenValidation screenValidation;
    @Autowired
    private Label<String> resultLabelId;
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        Map<String, String> buscalOptionsMap =
                businessCalendarRepository.getAllBusinessCalendars().stream()
                        .collect(Collectors.toMap(BusinessCalendar::getName,
                                BusinessCalendar::getCode));
        businessCalendarNameFieldId.setOptionsMap(buscalOptionsMap);
        Map<String, ChronoUnit> timeUnitOptionsMap = new LinkedHashMap<>();
        timeUnitOptionsMap.put("Minutes", ChronoUnit.MINUTES);
        timeUnitOptionsMap.put("Hours", ChronoUnit.HOURS);
        timeUnitOptionsMap.put("Days", ChronoUnit.DAYS);
        timeUnitFieldId.setOptionsMap(timeUnitOptionsMap);
        timeUnitFieldId.setValue(ChronoUnit.MINUTES);
        timeOperationFieldId.setOptionsList(Arrays.asList("Plus", "Minus"));
        timeOperationFieldId.setValue("Plus");
    }
    @Subscribe("calculate")
    @SuppressWarnings("all")
    public void onCalculateClick(Button.ClickEvent event) {
        ValidationErrors validationErrors =
                screenValidation.validateUiComponents(ComponentsHelper.getComponents(getWindow()));
        if (!validationErrors.isEmpty()) {
            screenValidation.showValidationErrors(this, validationErrors);
            return;
        }
        LocalDateTime sourceLocalDateTime = currentDateTimeFieldId.getValue();
        Long timeAmount = timeAmountFieldId.getValue();
        BusinessCalendar businessCalendar =
                businessCalendarRepository.getBusinessCalendarByCode(businessCalendarNameFieldId.getValue());
        LocalDateTime result;
        if ("plus".equalsIgnoreCase(timeOperationFieldId.getValue())) {
            result = businessCalendar.plus(sourceLocalDateTime, Duration.of(timeAmount,
                    timeUnitFieldId.getValue()));
        } else {
            result = businessCalendar.minus(sourceLocalDateTime,
                    Duration.of(timeAmount, timeUnitFieldId.getValue()));
        }
        resultLabelId.setVisible(true);
        resultLabelId.setValue("<br/><b>Result is :</b> " + result);
    }
    @Subscribe("closeBtn")
    public void onCloseBtnClick(Button.ClickEvent event) {
        close(StandardOutcome.CLOSE);
    }
}