package at.tuwien.indmp.dao;

import at.tuwien.indmp.model.RDMService;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

@Repository
public class RDMServiceDao extends AbstractDao<RDMService> {

    public RDMServiceDao() {
        super(RDMService.class);
    }

    /**
     * 
     * Find all systems
     * 
     * @return
     */
    public List<RDMService> findAll() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<RDMService> criteriaQuery = criteriaBuilder.createQuery(RDMService.class);
        final Root<RDMService> root = criteriaQuery.from(RDMService.class);
        criteriaQuery.select(root).distinct(true);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * 
     * Find the system by host
     * 
     * @param host
     * @return
     */
    public RDMService findByHost(URI host) {
        Objects.requireNonNull(host);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<RDMService> criteriaQuery = criteriaBuilder.createQuery(RDMService.class);
        final Root<RDMService> root = criteriaQuery.from(RDMService.class);
        criteriaQuery.select(root).distinct(true);

        // Conditions
        Predicate predicate = criteriaBuilder.equal(root.get("host"), host.toString());
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
