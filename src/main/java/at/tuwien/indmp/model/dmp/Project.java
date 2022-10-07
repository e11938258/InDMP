package at.tuwien.indmp.model.dmp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Project extends AbstractClassObject {

    /* Properties */
    private String description;

    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private LocalDate end;

    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private LocalDate start;

    @NotNull
    private String title;

    /* Nested data structure */
    private List<Funding> funding = new ArrayList<>();

    public Project() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEnd() {
        return this.end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public LocalDate getStart() {
        return this.start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Funding> getFunding() {
        return this.funding;
    }

    public void setFunding(List<Funding> funding) {
        this.funding = funding;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getDescription(),
                getEnd() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getEnd()) : null,
                getStart() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getStart()) : null,
                getTitle()
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "description",
                "end",
                "start",
                "title",
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getTitle();
    }

    @Override
    public List<Property> getPropertiesFromNestedObjects(DMP dmp, String atLocation, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();

        // ------------------------------------
        // Nested objects: Funding
        // ------------------------------------
        for (Funding i : getFunding()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
            properties.addAll(i.getPropertiesFromNestedObjects(dmp, getAtLocation(atLocation), rdmService));
        }

        return properties;
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getObjectType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "end", properties);
        setEnd(p != null ? LocalDate.parse(p.getValue()) : null);

        p = Functions.findPropertyInList(getObjectType(), "start", properties);
        setStart(p != null ? LocalDate.parse(p.getValue()) : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        p = Functions.findPropertyInList(getObjectType(), "title", properties);
        setTitle(p.getValue());

        // ------------------------------------
        // Nested objects: Funding
        // ------------------------------------
        for (Property property : propertyModule.findAllEntities(atLocation, "funding:identifier", true)) {
            final Funding i = new Funding();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            funding.add(i);
        }
    }
}
