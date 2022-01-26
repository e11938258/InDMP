package at.tuwien.indmp.model.dmp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.DMPConstants;
import at.tuwien.indmp.util.Functions;

public class Project extends ClassEntity {

    /* Properties */
    private String description;

    @JsonFormat(pattern = DMPConstants.DATE_FORMAT_ISO_8601)
    private Date end;

    @JsonFormat(pattern = DMPConstants.DATE_FORMAT_ISO_8601)
    private Date start;

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

    public Date getEnd() {
        return this.end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return this.start;
    }

    public void setStart(Date start) {
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
                getEnd() != null ? DMPConstants.DATE_FORMATTER_ISO_8601.format(getEnd()) : null,
                getStart() != null ? DMPConstants.DATE_FORMATTER_ISO_8601.format(getStart()) : null
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "description",
                "end",
                "start",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getTitle();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();

        if (hasRightsToUpdate(rdmService)) {
            final Property property = new Property(dmp.getClassIdentifier(), getClassType(), getClassIdentifier(),
                    "title", getClassIdentifier(), reference);
            properties.add(property);
        }

        return properties;
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, RDMService system) {
        final List<Property> properties = new ArrayList<>();

        // Funding
        for (Funding i : getFunding()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, system));
        }

        return properties;
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Nested classes
        // Funding
        for (Property property : propertyService.findProperties(dmpIdentifier, "funder_id", null, "identifier", null,
                classIdentifier)) {
            final Funding i = new Funding();
            i.build(propertyService, dmpIdentifier, property.getValue());
            funding.add(i);
        }

        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, getClassType(), classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("end", properties);
        try {
            setEnd(p != null ? DMPConstants.DATE_FORMATTER_ISO_8601.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList("start", properties);
        try {
            setStart(p != null ? DMPConstants.DATE_FORMATTER_ISO_8601.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set identifier
        p = Functions.findPropertyInList("title", properties);
        setTitle(p.getValue());
    }
}
