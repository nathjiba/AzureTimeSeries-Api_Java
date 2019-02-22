package com.timeseries.model;

public class Environment {

    private String displayName;
    private String environmentFqdn;
    private String environmentId;
    private String resourceId;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEnvironmentFqdn() {
        return environmentFqdn;
    }

    public void setEnvironmentFqdn(String environmentFqdn) {
        this.environmentFqdn = environmentFqdn;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}