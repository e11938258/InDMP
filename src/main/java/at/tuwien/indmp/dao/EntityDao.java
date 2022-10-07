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
public class EntityDao extends AbstractDao<Property> {

    public EntityDao() {
        super(Property.class);
    }

    /**
     * 
     * Find entity which is active
     * 
     * @param atLocation
     * @param specializationOf
     * @param value
     * @return
     */
    public Property findEntity(String atLocation, String specializationOf, String value) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        final Root<Property> root = criteriaQuery.from(Property.class);
        final Join<Property, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
        criteriaQuery.select(root).distinct(true);

        // Conditions
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

        // Only active entity
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(activity.get("endedAtTime")));

        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * 
     * Find entities
     * 
     * @param atLocation
     * @param specializationOf
     * @param onlyActive
     * @return
     */
    public List<Property> findEntities(String atLocation, String specializationOf, String value, boolean onlyActive) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        final Root<Property> root = criteriaQuery.from(Property.class);
        final Join<Property, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
        criteriaQuery.select(root).distinct(true);

        // Conditions
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
     * Find entities with nested ones
     * 
     * @param atLocation
     * @param specializationOf
     * @param onlyActive
     * @return
     */
    public List<Property> findAllEntities(String atLocation, String specializationOf, boolean onlyActive) {
        Objects.requireNonNull(atLocation);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        final Root<Property> root = criteriaQuery.from(Property.class);
        final Join<Property, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
        criteriaQuery.select(root).distinct(true);

        // Conditions
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
