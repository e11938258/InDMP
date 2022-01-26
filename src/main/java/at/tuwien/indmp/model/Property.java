package at.tuwien.indmp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.tuwien.indmp.util.DMPConstants;
import at.tuwien.indmp.util.ModelConstants;

@Entity
@Table(name = "property")
public class Property extends AbstractEntity {

    @Column(name = "dmp_identifier", nullable = false)
    @Size(min = ModelConstants.PROPERTY_DMP_IDENTIFIER_MIN, max = ModelConstants.PROPERTY_DMP_IDENTIFIER_MAX)
    @Pattern(regexp = ModelConstants.PROPERTY_DMP_IDENTIFIER_REGEX)
    @JsonProperty("dmp_identifier")
    private String dmpIdentifier;

    @Column(name = "class_type", nullable = false)
    @Pattern(regexp = DMPConstants.REGEX_DMP_CLASS_TYPE)
    @JsonProperty("class_type")
    private String classType;

    @Column(name = "class_identifier", nullable = false)
    @Size(min = ModelConstants.PROPERTY_CLASS_IDENTIFIER_MIN, max = ModelConstants.PROPERTY_CLASS_IDENTIFIER_MAX)
    @Pattern(regexp = ModelConstants.PROPERTY_CLASS_IDENTIFIER_REGEX)
    @JsonProperty("class_identifier")
    private String classIdentifier;

    @Column(name = "property_name", nullable = false)
    @Size(min = ModelConstants.PROPERTY_NAME_MIN, max = ModelConstants.PROPERTY_NAME_MAX)
    @Pattern(regexp = ModelConstants.PROPERTY_NAME_REGEX)
    @JsonProperty("property_name")
    private String propertyName;

    @Column(nullable = false)
    @Size(min = ModelConstants.PROPERTY_VALUE_MIN, max = ModelConstants.PROPERTY_VALUE_MAX)
    @Pattern(regexp = ModelConstants.PROPERTY_VALUE_REGEX)
    private String value;

    @Column
    @Size(min = ModelConstants.PROPERTY_CLASS_IDENTIFIER_MIN, max = ModelConstants.PROPERTY_CLASS_IDENTIFIER_MAX)
    @Pattern(regexp = ModelConstants.PROPERTY_CLASS_IDENTIFIER_REGEX)
    private String reference;

    @Column(insertable = false, updatable = false, name = "sys_start_time")
    private LocalDateTime sysStartTime;

    @Column(insertable = false, updatable = false, name = "sys_end_time")
    private LocalDateTime sysEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rdm_service_id")
    private RDMService rdmService;

    public Property() {
        this.rdmService = null;
    }

    public Property(String dmpIdentifier, String classType, String classIdentifier, String propertyName,
            String value, String reference) {
        this.dmpIdentifier = dmpIdentifier;
        this.classType = classType;
        this.classIdentifier = classIdentifier;
        this.propertyName = propertyName;
        this.value = value;
        this.reference = reference;
    }

    public String getDmpIdentifier() {
        return this.dmpIdentifier;
    }

    public void setDmpIdentifier(String dmpIdentifier) {
        this.dmpIdentifier = dmpIdentifier;
    }

    public String getClassType() {
        return this.classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getClassIdentifier() {
        return this.classIdentifier;
    }

    public void setClassIdentifier(String classIdentifier) {
        this.classIdentifier = classIdentifier;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDateTime getSysStartTime() {
        return this.sysStartTime;
    }

    public LocalDateTime getSysEndTime() {
        return this.sysEndTime;
    }

    public RDMService getRDMService() {
        return this.rdmService;
    }

    public void setRDMService(RDMService rdmService) {
        this.rdmService = rdmService;
    }

    public boolean hasSameValue(Property property) {
        return getValue().equals(property.getValue());
    }

    @Override
    public String toString() {
        return "{" +
                " dmpIdentifier='" + getDmpIdentifier() + "'" +
                ", classType='" + getClassType() + "'" +
                ", classIdentifier='" + getClassIdentifier() + "'" +
                ", propertyName='" + getPropertyName() + "'" +
                ", value='" + getValue() + "'" +
                ", reference='" + getReference() + "'" +
                "}";
    }
}
