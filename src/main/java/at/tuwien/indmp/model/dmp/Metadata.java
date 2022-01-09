package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.dmp.GrantIdentifierType;
import at.tuwien.indmp.util.dmp.Language;
import at.tuwien.indmp.util.var.ServiceType;

public class Metadata extends ClassEntity {

    /* Properties */
    private String description;

    @NotNull
    private Language language;

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

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
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
    public String[] getValueNames() {
        return new String[] {
                "description",
                "language",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getMetadata_standard_id().getClassIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        return getMetadata_standard_id().getProperties(dmp, reference, system);
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, System system) {
        return null;
    }

    @Override
    public boolean hasRightsToUpdate(System system) {
        return Functions.isServiceTypeInArray(new ServiceType[] {
                ServiceType.DMP_APP,
                ServiceType.REPOSITORY_STORE,
                ServiceType.IT_RESOURCE,
                ServiceType.REPOSITORY_INGESTOR,
        }, system.getType());
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "license", classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("language", properties);
        setLanguage(p != null ? Language.valueOf(p.getValue()) : null);

        // Set identifier
        final List<Property> identifierProperties = propertyService.findProperties(dmpIdentifier, "metadata_standard_id",
                classIdentifier, null, null, null);
        final Property identifier = Functions.findPropertyInList("identifier", identifierProperties);
        final Property type = Functions.findPropertyInList("type", identifierProperties);
        metadata_standard_id = new Metadata_standard_id(identifier.getValue(), GrantIdentifierType.valueOf(type.getValue()));
    }
}
