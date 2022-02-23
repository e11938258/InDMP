package at.tuwien.repository.model.dmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.repository.util.ModelConstants;

public class Project {

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
}
