package at.tuwien.simulation.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.simulation.util.ModelConstants;

public class Activity {

    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private Timestamp startedAtTime;

    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private Timestamp endedAtTime;

    @NotNull
    private DataService wasAssociatedWith;

    public Activity() {
    }

    public Activity(Date startedAtTime) {
        this.startedAtTime = new Timestamp(startedAtTime.getTime());
    }

    public Timestamp getStartedAtTime() {
        return this.startedAtTime;
    }

    public void setStartedAtTime(Timestamp startedAtTime) {
        this.startedAtTime = startedAtTime;
    }

    public Timestamp getEndedAtTime() {
        return this.endedAtTime;
    }

    public void setEndedAtTime(Timestamp endedAtTime) {
        this.endedAtTime = endedAtTime;
    }

    public DataService getWasAssociatedWith() {
        return this.wasAssociatedWith;
    }

    public void setWasAssociatedWith(DataService wasAssociatedWith) {
        this.wasAssociatedWith = wasAssociatedWith;
    }
}
