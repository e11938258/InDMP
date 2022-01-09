package at.tuwien.indmp.service;

import at.tuwien.indmp.model.System;
import at.tuwien.indmp.dao.SystemDao;
import at.tuwien.indmp.util.Constants;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemService {

    @Autowired
    private SystemDao systemDao;

    /**
     * 
     * Create a new system
     * 
     * @param system
     */
    @Transactional
    public void create(System system) {
        Objects.requireNonNull(system);
        systemDao.persist(system);
    }

    /**
     * 
     * Update the system
     * 
     * @param system
     */
    @Transactional
    public void update(System system) {
        Objects.requireNonNull(system);
        systemDao.update(system);
    }

    /**
     * 
     * Get all systems
     * 
     * @param system
     * @return
     */
    @Transactional(readOnly = true)
    public List<System> getAllSystems() {
        return systemDao.findAll();
    }

    /**
     * 
     * Get all systems for DMP
     * 
     * @param dmp
     * @return
     */
    @Transactional(readOnly = true)
    public List<System> getSystems() {
        // TODO: Limit systems to concrete DMP 
        return systemDao.findAll();
    }

    /**
     * 
     * Find the system by host
     * 
     * @param host
     * @return
     */
    @Transactional(readOnly = true)
    public System findByHost(
            @Valid @NotNull @Size(min = Constants.SYSTEM_HOST_MIN, max = Constants.SYSTEM_HOST_MAX) @Pattern(regexp = Constants.SYSTEM_HOST_REGEX) String host) {
        return systemDao.findByHost(host);
    }

}
