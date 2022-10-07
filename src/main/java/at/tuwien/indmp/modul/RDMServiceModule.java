package at.tuwien.indmp.modul;

import at.tuwien.indmp.dao.DataServiceDao;
import at.tuwien.indmp.exception.ConflictException;
import at.tuwien.indmp.exception.NotFoundException;
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
public class RDMServiceModule {

    @Autowired
    private DataServiceDao dataServiceDao;

    private final Logger log = LoggerFactory.getLogger(RDMServiceModule.class);

    /**
     * 
     * Create a new service
     * 
     * @param dataService
     */
    @Transactional
    public void persist(RDMService dataService) {
        Objects.requireNonNull(dataService);
        if (!existsByAccessRights(dataService.getAccessRights())) {
            dataServiceDao.persist(dataService);
        } else {
            throw new ConflictException("This client id is already registered.");
        }
    }

    @Transactional(readOnly = true)
    private boolean existsByAccessRights(String accessRights) {
        try {
            if (dataServiceDao.findByAccessRights(accessRights) != null) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return false;
        }
    }

    /**
     * 
     * Find the service by access rights
     * 
     * @param accessRights
     * @return
     */
    @Transactional(readOnly = true)
    public RDMService findByAccessRights(String accessRights) {
        try {
            return dataServiceDao.findByAccessRights(accessRights);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            log.error("Service not found by client id");
            throw new NotFoundException("Service not found by client id");
        }
    }

    /**
     * 
     * Get all services
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<RDMService> getAllDataServices() {
        return dataServiceDao.findAll();
    }

    /**
     * 
     * Update service
     * 
     */
    @Transactional
    public void update(RDMService dataService) {
        dataServiceDao.update(dataService);
    }
}
