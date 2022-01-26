package at.tuwien.indmp.service;

import at.tuwien.indmp.dao.RDMServiceDao;
import at.tuwien.indmp.model.RDMService;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RDMServiceLayer {

    @Autowired
    private RDMServiceDao rdmServiceDao;

    /**
     * 
     * Create a new RDM service
     * 
     * @param rdmService
     */
    @Transactional
    public void create(RDMService rdmService) {
        Objects.requireNonNull(rdmService);
        rdmServiceDao.persist(rdmService);
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
     * Update the RDM service
     * 
     * @param RDM service
     */
    @Transactional
    public void update(RDMService rdmService) {
        Objects.requireNonNull(rdmService);
        rdmServiceDao.update(rdmService);
    }

}
