package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Metadata extends AbstractClassEntity {

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
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location, DataService dataService) {
        return getMetadata_standard_id().getProperties(dmp, location, dataService);
    }

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null, null, true);

        Entity p = Functions.findPropertyInList(getClassType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "language", properties);
        setLanguage(p != null ? p.getValue() : null);

        // Set identifier
        final Entity identifier = Functions.findPropertyInList(getClassType(), "identifier", properties);
        final Entity type = Functions.findPropertyInList(getClassType(), "type", properties);
        metadata_standard_id = new Metadata_standard_id(identifier.getValue(), type.getValue());
    }
}
