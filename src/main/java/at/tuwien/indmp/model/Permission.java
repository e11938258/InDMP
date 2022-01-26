package at.tuwien.indmp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.util.DMPConstants;

@Entity
@Table(name = "permission")
public class Permission extends AbstractEntity {

    /* Properties */
    @Column(name = "class_type", nullable = false)
    @Pattern(regexp = DMPConstants.REGEX_DMP_CLASS_TYPE)
    private String classType;

    @Column(nullable = false)
    private boolean allowed;

    /* Nested data structure */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rdm_service_id")
    private RDMService rdmService;

    public Permission() {
    }

    public Permission(String classType, boolean allowed) {
        this.classType = classType;
        this.allowed = allowed;
    }

    public String getClassType() {
        return this.classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    public boolean getAllowed() {
        return this.allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public RDMService getRDMService() {
        return this.rdmService;
    }

    public void setRDMService(RDMService rdmService) {
        this.rdmService = rdmService;
    }
}
