package at.tuwien.simulation.model.test;

import java.util.Date;

import at.tuwien.simulation.model.dmp.DMPScheme;

public class TestCaseEntity {

    private DMPScheme dmpScheme;

    private final Date startDate;

    private Date endDate;

    public TestCaseEntity() {
        this.startDate = new Date();
    }

    public DMPScheme getDmpScheme() {
        return this.dmpScheme;
    }

    public void setDmpScheme(DMPScheme dmpScheme) {
        this.dmpScheme = dmpScheme;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void generateNewModifiedDate() {
        dmpScheme.getDmp().setModified(new Date());
    }

}
