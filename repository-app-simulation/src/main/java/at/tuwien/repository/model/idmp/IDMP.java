package at.tuwien.repository.model.idmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.repository.model.dmp.DMP_id;
import at.tuwien.repository.util.DMPConstants;

public class IDMP {

    /* Properties */
    @NotNull
    @JsonFormat(pattern = DMPConstants.DATE_TIME_FORMAT_ISO_8601)
    private Date created;

    @NotNull
    @JsonFormat(pattern = DMPConstants.DATE_TIME_FORMAT_ISO_8601)
    private Date modified;

    /* Nested data structure */
    @NotNull
    private DMP_id dmp_id;

    @NotNull
    private List<IdentifierUnit> identifier = new ArrayList<>();

    public IDMP() {
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return this.modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public DMP_id getDmp_id() {
        return this.dmp_id;
    }

    public void setDmp_id(DMP_id dmp_id) {
        this.dmp_id = dmp_id;
    }

    public List<IdentifierUnit> getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(List<IdentifierUnit> identifier) {
        this.identifier = identifier;
    }
}
