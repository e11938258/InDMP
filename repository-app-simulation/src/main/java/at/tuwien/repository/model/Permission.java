package at.tuwien.repository.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Permission {

    /* Properties */
    @NotNull
    @JsonProperty("class_type")
    private String classType;

    @NotNull
    private boolean allowed;

    /* Nested data structure */
    private RDMService rdmService;

    public Permission() {
    }

    public Permission(String classType, boolean allowed) {
        this.classType = classType;
        this.allowed = allowed;
    }

    public String getClassType() {
        return this.classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    public boolean getAllowed() {
        return this.allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public RDMService getRDMService() {
        return this.rdmService;
    }

    public void setRDMService(RDMService rdmService) {
        this.rdmService = rdmService;
    }
}
