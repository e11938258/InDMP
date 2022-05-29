package at.tuwien.indmp.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * https://www.w3.org/TR/2013/REC-prov-o-20130430/#Activity
 * 
 */
@javax.persistence.Entity
@Table(name = "activity")
public class Activity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, precision = 18, scale = 0)
    @JsonIgnore
    private Long id; // Just database identifier

    @Column(name = "started_at_time", nullable = false)
    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private Timestamp startedAtTime; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#startedAtTime

    @Column(name = "ended_at_time")
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private Timestamp endedAtTime; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#endedAtTime

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="was_associated_with", nullable = false)
    @NotNull
    private DataService wasAssociatedWith; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#wasAssociatedWith

    @OneToOne(mappedBy = "wasGeneratedBy")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Entity generated; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#generated

    public Activity() {
    }

    public Activity(Timestamp startedAtTime) {
        this.startedAtTime = startedAtTime;
    }

    @JsonIgnore
    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Entity getGenerated() {
        return this.generated;
    }

    public void setGenerated(Entity generated) {
        this.generated = generated;
    }
}
