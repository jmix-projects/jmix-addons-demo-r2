package com.company.addonsdemo.teams;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageCard implements Card {
    private final String summary;
    private final List<Section> sections;

    protected MessageCard(Builder builder) {
        this.sections = builder.sections;
        this.summary = builder.summary;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSummary() {
        return summary;
    }

    public List<Section> getSections() {
        return sections;
    }

    public static class Builder {
        private final List<Section> sections = new ArrayList<>();
        private String summary;

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder addSection(Section section) {
            this.sections.add(section);
            return this;
        }

        public MessageCard build() {
            return new MessageCard(this);
        }
    }
}