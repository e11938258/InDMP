package at.tuwien.indmp.service;

import at.tuwien.indmp.dao.RDMServiceDao;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.Permission;
import at.tuwien.indmp.model.RDMService;

import java.util.List;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(DMPService.class);

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
     * Find the RDM service by client id
     * 
     * @param clientId
     * @return
     */
    @Transactional(readOnly = true)
    public RDMService findByClientId(String clientId) {
        try {
            return rdmServiceDao.findByClientId(clientId);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            log.error("RDM service not found by client id");
            throw new NotFoundException("RDM service not found by client id");
        }
    }

    @Transactional(readOnly = true)
    private RDMService find(Long id) {
        try {
            return rdmServiceDao.find(id);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            log.error("RDM service not found by id");
            throw new NotFoundException("RDM service not found by id");
        }
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
