package at.tuwien.indmp.dao;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import at.tuwien.indmp.model.DataService;

@Repository
public class DataServiceDao extends AbstractDao<DataService> {

    public DataServiceDao() {
        super(DataService.class);
    }

    /**
     * 
     * Find all services
     * 
     * @return
     */
    public List<DataService> findAll() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<DataService> criteriaQuery = criteriaBuilder.createQuery(DataService.class);
        final Root<DataService> root = criteriaQuery.from(DataService.class);
        criteriaQuery.select(root).distinct(true);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * 
     * Find the service by access rights
     * 
     * @param accessRights
     * @return
     */
    public DataService findByAccessRights(String accessRights) {
        Objects.requireNonNull(accessRights);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<DataService> criteriaQuery = criteriaBuilder.createQuery(DataService.class);
        final Root<DataService> root = criteriaQuery.from(DataService.class);
        criteriaQuery.select(root).distinct(true);

        // Conditions
        Predicate predicate = criteriaBuilder.equal(root.get("accessRights"), accessRights);
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
