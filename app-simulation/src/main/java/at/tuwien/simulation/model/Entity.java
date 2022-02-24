package at.tuwien.simulation.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import at.tuwien.simulation.util.ModelConstants;

public class Entity {

    @Size(min = ModelConstants.ENTITY_CLASS_IDENTIFIER_MIN, max = ModelConstants.ENTITY_CLASS_IDENTIFIER_MAX)
    @Pattern(regexp = ModelConstants.ENTITY_CLASS_IDENTIFIER_REGEX)
    private String atLocation;

    @Size(min = ModelConstants.ENTITY_SPECIALIZATION_OF_MIN, max = ModelConstants.ENTITY_SPECIALIZATION_OF_MAX)
    @Pattern(regexp = ModelConstants.ENTITY_SPECIALIZATION_OF_REGEX)
    private String specializationOf;

    @Size(min = ModelConstants.ENTITY_VALUE_MIN, max = ModelConstants.ENTITY_VALUE_MAX)
    @Pattern(regexp = ModelConstants.ENTITY_VALUE_REGEX)
    private String value;

    private Activity wasGeneratedBy;

    public Entity(String atLocation, String specializationOf, String value) {
        this.atLocation = atLocation;
        this.specializationOf = specializationOf;
        this.value = value;
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

    public boolean hasSameValue(Entity entity) {
        return getValue().equals(entity.getValue());
    }

    @Override
    public String toString() {
        return "{" +
            " atLocation='" + getAtLocation() + "'" +
            ", specializationOf='" + getSpecializationOf() + "'" +
            ", value='" + getValue() + "'" +
            ", wasGeneratedBy='" + getWasGeneratedBy() + "'" +
            "}";
    }
}
