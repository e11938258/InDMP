package at.tuwien.indmp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import at.tuwien.indmp.util.ModelConstants;

/**
 * 
 * Property class
 * 
 * https://www.w3.org/TR/2013/REC-prov-o-20130430/#Entity
 * 
 */
@Entity
@Table(name = "property")
public class Property extends AbstractEntity {

    @Column(name = "at_location", nullable = false)
    @Size(min = ModelConstants.PROPERTY_AT_LOCATION_MIN, max = ModelConstants.PROPERTY_AT_LOCATION_MAX)
    @Pattern(regexp = ModelConstants.PROPERTY_AT_LOCATION_REGEX)
    private String atLocation; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#atLocation

    @Column(name = "specialization_of", nullable = false)
    @Size(min = ModelConstants.PROPERTY_SPECIALIZATION_OF_MIN, max = ModelConstants.PROPERTY_SPECIALIZATION_OF_MAX)
    @Pattern(regexp = ModelConstants.PROPERTY_SPECIALIZATION_OF_REGEX)
    private String specializationOf; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#specializationOf

    @Column(nullable = false)
    @Size(min = ModelConstants.PROPERTY_VALUE_MIN, max = ModelConstants.PROPERTY_VALUE_MAX)
    @Pattern(regexp = ModelConstants.PROPERTY_VALUE_REGEX)
    private String value; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#value

    @OneToOne
    @JoinColumn(name = "was_generated_by", referencedColumnName = "id")
    private Activity wasGeneratedBy; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#wasGeneratedBy

    public Property() {
    }

    public Property(String specializationOf) {
        this.specializationOf = specializationOf;
    }

    public Property(String atLocation, String specializationOf, String value, Activity wasGeneratedBy) {
        this.atLocation = atLocation;
        this.specializationOf = specializationOf;
        this.value = value;
        this.wasGeneratedBy = wasGeneratedBy;
    }

    public String getSpecializationOf() {
        return this.specializationOf;
    }

    public void setSpecializationOf(String specializationOf) {
        this.specializationOf = specializationOf;
    }

    public String getAtLocation() {
        return this.atLocation;
    }

    public void setAtLocation(String atLocation) {
        this.atLocation = atLocation;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Activity getWasGeneratedBy() {
        return this.wasGeneratedBy;
    }

    public void setWasGeneratedBy(Activity wasGeneratedBy) {
        this.wasGeneratedBy = wasGeneratedBy;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", atLocation='" + getAtLocation() + "'" +
                ", specializationOf='" + getSpecializationOf() + "'" +
                ", value='" + getValue() + "'" +
                ", wasGeneratedBy='" + getWasGeneratedBy() + "'" +
                "}";
    }

    /* Non-standard */

    /**
     * 
     * Has the property same value?
     * 
     * @param entity
     * @return
     */
    public boolean hasSameValue(Property entity) {
        return getValue().equals(entity.getValue());
    }
}
