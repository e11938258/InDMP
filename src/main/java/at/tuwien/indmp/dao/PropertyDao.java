package at.tuwien.indmp.dao;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import at.tuwien.indmp.model.Activity;
import at.tuwien.indmp.model.Property;

@Repository
public class PropertyDao extends AbstractDao<Property> {

    public PropertyDao() {
        super(Property.class);
    }

    /**
     * 
     * Find active property
     * 
     * @param atLocation
     * @param specializationOf
     * @param value
     * @return
     */
    public Property findProperty(String atLocation, String specializationOf, String value, boolean onlyActive) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        final Root<Property> root = criteriaQuery.from(Property.class);
        final Join<Property, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
        criteriaQuery.select(root).distinct(true);

        // ------------------------------------
        // Conditions
        // ------------------------------------
        Predicate predicate = criteriaBuilder.isNotNull(root.get("value"));

        if (atLocation != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("atLocation"), atLocation));
        }

        if (specializationOf != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("specializationOf"), specializationOf));
        }

        if (value != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("value"), value));
        }

        if (onlyActive) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(activity.get("endedAtTime")));
        }

        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * 
     * Find properties
     * 
     * @param atLocation
     * @param specializationOf
     * @param onlyActive
     * @return
     */
    public List<Property> findProperties(String atLocation, String specializationOf, String value, boolean onlyActive) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        final Root<Property> root = criteriaQuery.from(Property.class);
        final Join<Property, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
        criteriaQuery.select(root).distinct(true);

        // ------------------------------------
        // Conditions
        // ------------------------------------
        Predicate predicate = criteriaBuilder.isNotNull(root.get("value"));

        if (atLocation != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("atLocation"), atLocation));
        }

        if (specializationOf != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("specializationOf"), specializationOf));
        }

        if (value != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("value"), value));
        }

        if (onlyActive) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(activity.get("endedAtTime")));
        }

        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * 
     * Find properties with properties of nested objects
     * 
     * @param atLocation
     * @param specializationOf
     * @param onlyActive
     * @return
     */
    public List<Property> findAllProperties(String atLocation, String specializationOf, boolean onlyActive) {
        Objects.requireNonNull(atLocation);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        final Root<Property> root = criteriaQuery.from(Property.class);
        final Join<Property, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
        criteriaQuery.select(root).distinct(true);

        // ------------------------------------
        // Conditions
        // ------------------------------------
        Predicate predicate = criteriaBuilder.like(root.get("atLocation"), atLocation + "%");

        if (specializationOf != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("specializationOf"), specializationOf));
        }

        if (onlyActive) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(activity.get("endedAtTime")));
        }

        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}
