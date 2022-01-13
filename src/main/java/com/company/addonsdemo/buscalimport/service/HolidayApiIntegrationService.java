package com.company.addonsdemo.buscalimport.service;

import com.company.addonsdemo.buscalimport.dto.HolidaysDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("project_HolidayApiIntegrationService")
public class HolidayApiIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(HolidayApiIntegrationService.class);

    /**
     * In order to communicate with holidayAPI it is necessary to provide apiKey, country and year
     */
    private static final String HOLIDAY_API_URL =
            "https://holidayapi.com/v1/holidays?key=%s&country=%s&year=%s";

    public HolidaysDto getHolidaysInformation(String apiKey, String countryCode, String requestedYear) {
        log.debug("Requested holidays information with parameters apiKey={} countryCode={} requestedYear={}", apiKey, countryCode, requestedYear);
        ResponseEntity<HolidaysDto> responseEntity;
        try {
            responseEntity = new RestTemplate().getForEntity(
                    String.format(HOLIDAY_API_URL, apiKey, countryCode, requestedYear),
                    HolidaysDto.class
            );
        } catch (Exception e) {
            log.warn("Unexpected exception occurred", e);
            throw new IllegalStateException(e);
        }

        log.debug("Response status {}", responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

}
