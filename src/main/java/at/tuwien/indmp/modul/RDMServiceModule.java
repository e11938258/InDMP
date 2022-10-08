package at.tuwien.indmp.modul;

import at.tuwien.indmp.dao.RDMServiceDao;
import at.tuwien.indmp.exception.ConflictException;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.util.RDMServiceState;

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
public class RDMServiceModule {

    @Autowired
    private RDMServiceDao rdmServiceDao;

    private final Logger log = LoggerFactory.getLogger(RDMServiceModule.class);

    /**
     * 
     * Create a new RDM service
     * 
     * @param rdmService
     */
    @Transactional
    public void persist(RDMService rdmService) {
        Objects.requireNonNull(rdmService);
        if (!existsByAccessRights(rdmService.getAccessRights())) {
            rdmServiceDao.persist(rdmService);
        } else {
            log.error("The service client id is already registered.");
            throw new ConflictException("The service client id is already registered.");
        }
    }

    /**
     * 
     * Fint the RDM service by id
     * 
     * @param id
     * @return
     */
    public RDMService find(Long id) {
        Objects.requireNonNull(id);
        return rdmServiceDao.find(id);
    }

    /**
     * 
     * UC8: Identification of the RDM service.
     * 
     * @param accessRights
     * @return
     */
    @Transactional(readOnly = true)
    public RDMService findByAccessRights(String accessRights) {
        try {
            // 1. The integration service tries to find the RDM service by the access
            // rights.
            return rdmServiceDao.findByAccessRights(accessRights);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            // 2. If the RDM service was not found
            // 2.1 The integration service returns an error message to the RDM service and
            // terminates the process.
            log.error("Service not found by client id.");
            throw new NotFoundException("Service not found by client id.");
        }
    }

    /**
     * 
     * Get all RDM services
     * 
     * @param onlyActive
     * @return
     */
    @Transactional(readOnly = true)
    public List<RDMService> getRDMServices(boolean onlyActive) {
        return rdmServiceDao.findAll(onlyActive);
    }

    /**
     * 
     * Update the RDM service
     * 
     */
    public void update(RDMService rdmService) {
        Objects.requireNonNull(rdmService);
        rdmServiceDao.update(rdmService);
    }

    /**
     * 
     * Set the state of the RDM service
     * 
     * @param state
     */
    public void setState(RDMService rdmService, RDMServiceState state) {
        Objects.requireNonNull(rdmService);
        Objects.requireNonNull(state);
        rdmService.setState(state);
        update(rdmService);
    }

    /* Private */

    @Transactional(readOnly = true)
    private boolean existsByAccessRights(String accessRights) {
        try {
            if (rdmServiceDao.findByAccessRights(accessRights) != null) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return false;
        }
    }
}
