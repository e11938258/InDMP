package at.tuwien.simulation.model.test;

import java.time.LocalDateTime;

import at.tuwien.simulation.model.dmp.DMPScheme;

public class TestCaseEntity {

    private DMPScheme dmpScheme;

    private String statusCode, body;

    private final LocalDateTime startDate;

    public TestCaseEntity() {
        this.startDate = LocalDateTime.now();
    }

    public DMPScheme getDmpScheme() {
        return this.dmpScheme;
    }

    public void setDmpScheme(DMPScheme dmpScheme) {
        this.dmpScheme = dmpScheme;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void generateNewModifiedDate() {
        dmpScheme.getDmp().setModified(LocalDateTime.now());
    }

}
