package at.tuwien.simulation.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.util.ModelConstants;

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

    public Metadata(String description, String language, Metadata_standard_id metadata_standard_id) {
        this.description = description;
        this.language = language;
        this.metadata_standard_id = metadata_standard_id;
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
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location) {
        return getMetadata_standard_id().getProperties(dmp, location);
    }
}
