package at.tuwien.indmp.dao;

import at.tuwien.indmp.model.RDMService;

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
     * Find the system by client id
     * 
     * @param clientId
     * @return
     */
    public RDMService findByClientId(String clientId) {
        Objects.requireNonNull(clientId);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<RDMService> criteriaQuery = criteriaBuilder.createQuery(RDMService.class);
        final Root<RDMService> root = criteriaQuery.from(RDMService.class);
        criteriaQuery.select(root).distinct(true);

        // Conditions
        Predicate predicate = criteriaBuilder.equal(root.get("clientId"), clientId);
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
