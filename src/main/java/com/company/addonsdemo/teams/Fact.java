package com.company.addonsdemo.teams;

public class Fact {
    protected String name;
    protected String value;

    protected Fact(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Fact of(String name, String value) {
        return new Fact(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}