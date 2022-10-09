package at.tuwien.indmp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonView;

import at.tuwien.indmp.util.Views;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, precision = 18, scale = 0)
    @JsonView(Views.Extended.class)
    private Long id; // Just database identifier

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}