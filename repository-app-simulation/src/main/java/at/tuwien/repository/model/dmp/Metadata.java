package at.tuwien.repository.model.dmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.repository.util.DMPConstants;

public class Metadata {

    /* Properties */
    private String description;

    @NotNull
    @Pattern(regexp = DMPConstants.REGEX_ISO_639_3)
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
}
