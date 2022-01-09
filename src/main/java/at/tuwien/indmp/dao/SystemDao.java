package at.tuwien.indmp.dao;

import at.tuwien.indmp.model.System;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

@Repository
public class SystemDao extends AbstractDao<System> {

    public SystemDao() {
        super(System.class);
    }

    /**
     * 
     * Find all systems
     * 
     * @return
     */
    public List<System> findAll() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<System> criteriaQuery = criteriaBuilder.createQuery(System.class);
        final Root<System> root = criteriaQuery.from(System.class);
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
    public System findByHost(String host) {
        Objects.requireNonNull(host);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<System> criteriaQuery = criteriaBuilder.createQuery(System.class);
        final Root<System> root = criteriaQuery.from(System.class);
        criteriaQuery.select(root).distinct(true);

        // Conditions
        Predicate predicate = criteriaBuilder.equal(root.get("host"), host);
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
