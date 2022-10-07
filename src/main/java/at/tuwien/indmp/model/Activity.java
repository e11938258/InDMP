package at.tuwien.indmp.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import at.tuwien.indmp.util.ModelConstants;

/**
 * 
 * Activity class
 * 
 * https://www.w3.org/TR/2013/REC-prov-o-20130430/#Activity
 * 
 */
@Entity
@Table(name = "activity")
public class Activity extends AbstractEntity {

    @Column(name = "started_at_time", nullable = false)
    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private Timestamp startedAtTime; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#startedAtTime

    @Column(name = "ended_at_time")
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private Timestamp endedAtTime; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#endedAtTime

    @OneToOne(mappedBy = "wasGeneratedBy")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Property generated; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#generated

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="was_started_by", nullable = false)
    @NotNull
    private RDMService wasStartedBy; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#wasStartedBy 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="was_ended_by", nullable = false)
    @NotNull
    private RDMService wasEndedBy; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#wasEndedBy

    public Activity() {
    }

    public Activity(Timestamp startedAtTime) {
        this.startedAtTime = startedAtTime;
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

    @JsonIgnore
    public Property getGenerated() {
        return this.generated;
    }

    public void setGenerated(Property generated) {
        this.generated = generated;
    }

    public RDMService getWasStartedBy() {
        return this.wasStartedBy;
    }

    public void setWasStartedBy(RDMService wasStartedBy) {
        this.wasStartedBy = wasStartedBy;
    }

    public RDMService getWasEndedBy() {
        return this.wasEndedBy;
    }

    public void setWasEndedBy(RDMService wasEndedBy) {
        this.wasEndedBy = wasEndedBy;
    }

}
