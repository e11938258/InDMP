package at.tuwien.indmp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import at.tuwien.indmp.util.Constants;
import at.tuwien.indmp.util.Views;

@Entity
@Table(name = "property", indexes = {
        @Index(name = "idx_id", columnList = "id", unique = true) })
public class Property extends AbstractEntity {

    @Column(length = Constants.PROPERTY_DMP_IDENTIFIER_MAX, name = "dmp_identifier", nullable = false)
    @Size(min = Constants.PROPERTY_DMP_IDENTIFIER_MIN, max = Constants.PROPERTY_DMP_IDENTIFIER_MAX)
    @Pattern(regexp = Constants.PROPERTY_DMP_IDENTIFIER_REGEX)
    @JsonView(Views.Basic.class)
    @JsonProperty("dmp_identifier")
    private String dmpIdentifier;

    @Column(length = Constants.PROPERTY_CLASS_NAME_MAX, name = "class_name", nullable = false)
    @Size(min = Constants.PROPERTY_CLASS_NAME_MIN, max = Constants.PROPERTY_CLASS_NAME_MAX)
    @Pattern(regexp = Constants.PROPERTY_CLASS_NAME_REGEX)
    @JsonView(Views.Basic.class)
    @JsonProperty("class_name")
    private String className;

    @Column(length = Constants.PROPERTY_CLASS_IDENTIFIER_MAX, name = "class_identifier", nullable = false)
    @Size(min = Constants.PROPERTY_CLASS_IDENTIFIER_MIN, max = Constants.PROPERTY_CLASS_IDENTIFIER_MAX)
    @Pattern(regexp = Constants.PROPERTY_CLASS_IDENTIFIER_REGEX)
    @JsonView(Views.Basic.class)
    @JsonProperty("class_identifier")
    private String classIdentifier;

    @Column(length = Constants.PROPERTY_NAME_MAX, name = "property_name", nullable = false)
    @Size(min = Constants.PROPERTY_NAME_MIN, max = Constants.PROPERTY_NAME_MAX)
    @Pattern(regexp = Constants.PROPERTY_NAME_REGEX)
    @JsonView(Views.Basic.class)
    @JsonProperty("property_name")
    private String propertyName;

    @Column(length = Constants.PROPERTY_VALUE_MAX, nullable = false)
    @Size(min = Constants.PROPERTY_VALUE_MIN, max = Constants.PROPERTY_VALUE_MAX)
    @Pattern(regexp = Constants.PROPERTY_VALUE_REGEX)
    @JsonView(Views.Basic.class)
    private String value;

    @Column(length = Constants.PROPERTY_CLASS_IDENTIFIER_MAX)
    @Size(min = Constants.PROPERTY_CLASS_IDENTIFIER_MIN, max = Constants.PROPERTY_CLASS_IDENTIFIER_MAX)
    @Pattern(regexp = Constants.PROPERTY_CLASS_IDENTIFIER_REGEX)
    @JsonView(Views.Basic.class)
    private String reference;

    @Column(name = "valid_from", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(Views.Basic.class)
    @JsonProperty("valid_from")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date validFrom;

    @Column(name = "valid_until")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(Views.Basic.class)
    @JsonProperty("valid_until")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date validUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviceid")
    @JsonIgnore
    private System system;

    public Property() {
        this.system = null;
    }

    public Property(String dmpIdentifier, String className, String classIdentifier, String propertyName,
            String value, String reference, Date validFrom, System system) {
        this.dmpIdentifier = dmpIdentifier;
        this.className = className;
        this.classIdentifier = classIdentifier;
        this.propertyName = propertyName;
        this.value = value;
        this.reference = reference;
        this.validFrom = validFrom;
        this.system = system;
    }

    public Property(Property property, String value, Date validFrom, System system) {
        this.dmpIdentifier = property.getDmpIdentifier();
        this.className = property.getClassName();
        this.classIdentifier = property.getClassIdentifier();
        this.propertyName = property.getPropertyName();
        this.value = value;
        this.reference = property.getReference();
        this.validFrom = validFrom;
        this.system = system;
    }

    @JsonIgnore
    public String getDmpIdentifier() {
        return this.dmpIdentifier;
    }

    public void setDmpIdentifier(String dmpIdentifier) {
        this.dmpIdentifier = dmpIdentifier;
    }

    @JsonIgnore
    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @JsonIgnore
    public String getClassIdentifier() {
        return this.classIdentifier;
    }

    public void setClassIdentifier(String classIdentifier) {
        this.classIdentifier = classIdentifier;
    }

    @JsonIgnore
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

    public Date getValidFrom() {
        return this.validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return this.validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    @JsonIgnore
    public System getSystem() {
        return this.system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public boolean hasSameValue(Property property) {
        return getValue().equals(property.getValue());
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "{" +
            " dmpIdentifier='" + getDmpIdentifier() + "'" +
            ", className='" + getClassName() + "'" +
            ", classIdentifier='" + getClassIdentifier() + "'" +
            ", propertyName='" + getPropertyName() + "'" +
            ", value='" + getValue() + "'" +
            ", reference='" + getReference() + "'" +
            ", validFrom='" + getValidFrom() + "'" +
            "}";
    }
}
