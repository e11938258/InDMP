package at.tuwien.indmp.model.idmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.dmp.Dmp_id;

public class IDMP {

    /* Properties */
    @NotNull
    private Date created;

    @NotNull
    private Date modified;

    /* Nested data structure */
    @NotNull
    private Dmp_id dmp_id;

    @NotNull
    private List<Identifier> identifier = new ArrayList<>();

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

    public Dmp_id getDmp_id() {
        return this.dmp_id;
    }

    public void setDmp_id(Dmp_id dmp_id) {
        this.dmp_id = dmp_id;
    }

    public List<Identifier> getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }
}
