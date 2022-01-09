package at.tuwien.indmp.model.dmp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.var.ServiceType;

public class Project extends ClassEntity {

    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    /* Properties */
    private String description;

    @JsonFormat(pattern = DATE_FORMAT)
    private Date end;

    @JsonFormat(pattern = DATE_FORMAT)
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
                getEnd() != null ? DATE_FORMATTER.format(getEnd()) : null,
                getStart() != null ? DATE_FORMATTER.format(getStart()) : null
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
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        final List<Property> properties = new ArrayList<>();

        final Property property = new Property(dmp.getClassIdentifier(), getClassName(), getClassIdentifier(), "title",
        getClassIdentifier(), reference, dmp.getModified(), system);
        properties.add(property);
        system.add(property);

        return properties;
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, System system) {
        final List<Property> properties = new ArrayList<>();

        // Funding
        for (Funding i : getFunding()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, system));
        }

        return properties;
    }

    @Override
    public boolean hasRightsToUpdate(System system) {
        return Functions.isServiceTypeInArray(new ServiceType[] {
                ServiceType.DMP_APP,
                ServiceType.ADMINISTRATIVE_DATA_COLLECTOR,
                ServiceType.FUNDER_SYSTEM,
        }, system.getType());
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
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "project", classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("end", properties);
        try {
            setEnd(p != null ? DATE_FORMATTER.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList("start", properties);
        try {
            setStart(p != null ? DATE_FORMATTER.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set identifier
        p = Functions.findPropertyInList("title", properties);
        setTitle(p.getValue());
    }
}
