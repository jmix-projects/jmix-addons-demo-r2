package com.company.addonsdemo.teams;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Section {
    protected final String activityTitle;
    protected final String activitySubtitle;
    protected final List<Fact> facts;

    protected Section(Builder builder) {
        this.activityTitle = builder.title;
        this.activitySubtitle = builder.subtitle;
        this.facts = builder.facts;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public String getActivitySubtitle() {
        return activitySubtitle;
    }

    public List<Fact> getFacts() {
        return facts;
    }

    public static class Builder {
        private String title;
        private String subtitle;
        private final List<Fact> facts = new ArrayList<>();

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder addFact(Fact fact) {
            if (fact != null) {
                this.facts.add(fact);
            }
            return this;
        }

        public Section build() {
            return new Section(this);
        }
    }
}
