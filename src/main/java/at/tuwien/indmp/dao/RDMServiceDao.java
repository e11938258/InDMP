package at.tuwien.indmp.dao;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import at.tuwien.indmp.model.RDMService;

@Repository
public class RDMServiceDao extends AbstractDao<RDMService> {

    public RDMServiceDao() {
        super(RDMService.class);
    }

    /**
     * 
     * Find all rdm services
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
     * Find the rdm service by access rights
     * 
     * @param accessRights
     * @return
     */
    public RDMService findByAccessRights(String accessRights) {
        Objects.requireNonNull(accessRights);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<RDMService> criteriaQuery = criteriaBuilder.createQuery(RDMService.class);
        final Root<RDMService> root = criteriaQuery.from(RDMService.class);
        criteriaQuery.select(root).distinct(true);

        // ------------------------------------
        // Conditions
        // ------------------------------------
        Predicate predicate = criteriaBuilder.equal(root.get("accessRights"), accessRights);
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
