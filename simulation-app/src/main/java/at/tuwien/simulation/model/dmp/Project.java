package at.tuwien.simulation.model.dmp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.util.ModelConstants;

public class Project extends AbstractClassEntity {

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

    public Project(String description, LocalDate end, LocalDate start, String title, List<Funding> funding) {
        this.description = description;
        this.end = end;
        this.start = start;
        this.title = title;
        this.funding = funding;
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
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location) {
        final List<Entity> properties = new ArrayList<>();

        // Funding
        for (Funding i : getFunding()) {
            properties.addAll(i.getProperties(dmp, getLocation(location)));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, getLocation(location)));
        }

        return properties;
    }
}
