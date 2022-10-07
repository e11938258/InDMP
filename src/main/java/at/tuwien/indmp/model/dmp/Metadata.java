package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Metadata extends AbstractClassObject {

    /* Properties */
    private String description;

    @NotNull
    @Pattern(regexp = ModelConstants.REGEX_ISO_639_3)
    private String language;

    /* Nested data structure */
    @NotNull
    private Metadata_standard_id metadata_standard_id;

    public Metadata() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Metadata_standard_id getMetadata_standard_id() {
        return this.metadata_standard_id;
    }

    public void setMetadata_standard_id(Metadata_standard_id metadata_standard_id) {
        this.metadata_standard_id = metadata_standard_id;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getDescription(),
                getLanguage(),
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "description",
                "language",
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getMetadata_standard_id().getObjectIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String atLocation, RDMService rdmService) {
        return getMetadata_standard_id().getProperties(dmp, atLocation, rdmService);
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findProperties(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getSpecializationOf("description"), properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("language"), properties);
        setLanguage(p != null ? p.getValue() : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        final Property identifier = Functions.findPropertyInList(getSpecializationOf("identifier"), properties);
        final Property type = Functions.findPropertyInList(getSpecializationOf("type"), properties);
        metadata_standard_id = new Metadata_standard_id(identifier.getValue(), type.getValue());
    }
}
