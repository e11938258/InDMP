package at.tuwien.indmp.model.dmp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Project extends AbstractClassEntity {

    /* Properties */
    private String description;

    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private Date end;

    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
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
                getEnd() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getEnd()) : null,
                getStart() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getStart()) : null,
                getTitle()
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "description",
                "end",
                "start",
                "title",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getTitle();
    }

    @Override
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location, DataService dataService) {
        final List<Entity> properties = new ArrayList<>();

        // Funding
        for (Funding i : getFunding()) {
            properties.addAll(i.getProperties(dmp, getLocation(location), dataService));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, getLocation(location), dataService));
        }

        return properties;
    }

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null);

        Entity p = Functions.findPropertyInList(getClassType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "end", properties);
        try {
            setEnd(p != null ? ModelConstants.DATE_FORMATTER_ISO_8601.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList(getClassType(), "start", properties);
        try {
            setStart(p != null ? ModelConstants.DATE_FORMATTER_ISO_8601.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set identifier
        p = Functions.findPropertyInList(getClassType(), "title", properties);
        setTitle(p.getValue());

        // Nested classes
        // Funding
        for (Entity property : entityService.findAllEntities(location, "funding:identifier")) {
            final Funding i = new Funding();
            i.build(entityService, location + "/" + property.getValue());
            funding.add(i);
        }
    }
}
