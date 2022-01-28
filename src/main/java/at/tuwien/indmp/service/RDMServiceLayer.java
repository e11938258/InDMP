package at.tuwien.indmp.service;

import at.tuwien.indmp.dao.RDMServiceDao;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.Permission;
import at.tuwien.indmp.model.RDMService;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RDMServiceLayer {

    @Autowired
    private RDMServiceDao rdmServiceDao;

    @Autowired
    private PermissionService permissionService;

    /**
     * 
     * Create a new RDM service
     * 
     * @param rdmService
     */
    @Transactional
    public void create(RDMService rdmService) {
        Objects.requireNonNull(rdmService);
        // Create a new RDM service
        rdmServiceDao.persist(rdmService);
        // Create permissions
        for (Permission permission : rdmService.getPermissions()) {
            permission.setRDMService(rdmService);
            permissionService.create(permission);
        }
    }

    /**
     * 
     * Find the RDM service by id
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public RDMService find(Long id) {
        try {
            return rdmServiceDao.find(id);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new NotFoundException(" not found");
        }
    }

    /**
     * 
     * Find the RDM service by host
     * 
     * @param host
     * @return
     */
    @Transactional(readOnly = true)
    public RDMService findByHost(URI host) {
        return rdmServiceDao.findByHost(host);
    }

    /**
     * 
     * Get all RDM service
     * 
     * @param rdmService
     * @return
     */
    @Transactional(readOnly = true)
    public List<RDMService> getAllRDMServices() {
        return rdmServiceDao.findAll();
    }

    /**
     * 
     * Update the RDM service
     * 
     * @param RDM service
     */
    @Transactional
    public void update(RDMService rdmService) {
        Objects.requireNonNull(rdmService);
        rdmServiceDao.update(rdmService);
    }

    /**
     * 
     * Remove the RDM service by id
     * 
     * @param id
     * @return
     */
    @Transactional
    public void delete(Long id) {
        rdmServiceDao.delete(find(id));
    }
}
