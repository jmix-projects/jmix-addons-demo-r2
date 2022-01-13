package com.company.addonsdemo.teams;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

@Component
public class TeamsClient {
    private static final ObjectMapper mapper = new
            ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Logger log = LoggerFactory.getLogger(TeamsClient.class);

    protected TeamsClient() {
    }

    public boolean sendMessage(String url, Card card) {
        String payload = toJson(card);
        ClientResponse response = Client
                .create()
                .resource(url)
                .accept("application/json")
                .post(ClientResponse.class, payload);
        log.debug("Request payload: {}", payload);
        String output = response.getEntity(String.class);
        log.debug("Response: {}", output);
        return response.getStatus() == 200;
    }

    private static String toJson(final Object pojo) {
        try {
            return mapper.writeValueAsString(pojo);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}