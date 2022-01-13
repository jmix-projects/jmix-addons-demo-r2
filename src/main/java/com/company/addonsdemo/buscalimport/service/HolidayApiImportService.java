package com.company.addonsdemo.buscalimport.service;

import com.company.addonsdemo.buscalimport.dto.HolidayDto;
import com.company.addonsdemo.buscalimport.dto.HolidaysDto;
import io.jmix.businesscalendar.entities.CalendarEntity;
import io.jmix.businesscalendar.entities.HolidayEntity;
import io.jmix.businesscalendar.model.*;
import io.jmix.businesscalendar.util.BusinessCalendarConstants;
import io.jmix.core.UnconstrainedDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service("project_HolidaysApiCalendarImporter")
public class HolidayApiImportService {

    @Autowired
    private UnconstrainedDataManager dataManager;

    @Transactional
    public void importCalendar(String bcName, String bcCode, HolidaysDto holidays) {
        BusinessCalendarImpl calendar = new BusinessCalendarImpl(bcName, bcCode, BusinessCalendarSource.DATABASE);
        for (HolidayDto holiday : holidays.getHolidays()) {
            FixedDayHoliday fixedDayHoliday = new FixedDayHoliday(LocalDate.parse(holiday.getObserved()));
            fixedDayHoliday.setDescription(holiday.getName());
            calendar.addHoliday(fixedDayHoliday);
        }
        saveCalendar(calendar);
    }

    private void saveCalendar(BusinessCalendarImpl businessCalendar) {
        CalendarEntity calendarEntity = dataManager.create(CalendarEntity.class);
        calendarEntity.setName(businessCalendar.getName());
        calendarEntity.setCode(businessCalendar.getCode());
        dataManager.save(calendarEntity);

        //holidaysAPI provides only information about holidays
        businessCalendar.getHolidays().forEach(
                h -> {
                    HolidayEntity holidayEntity = dataManager.create(HolidayEntity.class);
                    holidayEntity.setCalendar(calendarEntity);
                    holidayEntity.setDescription(h.getDescription());
                    switch (h.getType()) {
                        case BusinessCalendarConstants.WEEKLY_CRON_EXPRESSION:
                        case BusinessCalendarConstants.CRON_EXPRESSION:
                            holidayEntity.setCronExpression(((CronHoliday) h).getCronExpression());
                            holidayEntity.setWeekly(BusinessCalendarConstants.WEEKLY_CRON_EXPRESSION.equals(h.getType()));
                            break;
                        case BusinessCalendarConstants.FIXED_DAY:
                            holidayEntity.setFixedDate(((FixedDayHoliday) h).getLocalDate());
                            break;
                        case BusinessCalendarConstants.REPEATABLE_DATE:
                            holidayEntity.setMonthValue(((FixedYearlyHoliday) h).getMonth().getValue());
                            holidayEntity.setDayOfMonth(((FixedYearlyHoliday) h).getDayOfMonth());
                            break;
                        default:
                            throw new IllegalArgumentException("Unexpected holiday type " + h.getType());
                    }

                    dataManager.save(holidayEntity);
                }
        );
    }
}