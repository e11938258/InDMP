package at.tuwien.indmp.service.impl;

import at.tuwien.indmp.dao.DataServiceDao;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.service.DataServiceService;

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
public class DataServiceServiceImpl implements DataServiceService {

    @Autowired
    private DataServiceDao dataServiceDao;

    private final Logger log = LoggerFactory.getLogger(DataServiceServiceImpl.class);

    /**
     * 
     * Create a new service
     * 
     * @param dataService
     */
    @Transactional
    public void persist(DataService dataService) {
        Objects.requireNonNull(dataService);
        dataServiceDao.persist(dataService);
    }

    /**
     * 
     * Find the service by access rights
     * 
     * @param accessRights
     * @return
     */
    @Transactional(readOnly = true)
    public DataService findByClientId(String accessRights) {
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
    public List<DataService> getAllDataServices() {
        return dataServiceDao.findAll();
    }

    /**
     * 
     * Update service
     * 
     */
    @Transactional
    public void update(DataService dataService) {
        dataServiceDao.update(dataService);
    }
}
