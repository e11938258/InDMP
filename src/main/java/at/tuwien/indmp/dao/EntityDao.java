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
import at.tuwien.indmp.model.Entity;

@Repository
public class EntityDao extends AbstractDao<Entity> {

    public EntityDao() {
        super(Entity.class);
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
    public Entity findEntity(String atLocation, String specializationOf, String value) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(Entity.class);
        final Root<Entity> root = criteriaQuery.from(Entity.class);
        final Join<Entity, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
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
    public List<Entity> findEntities(String atLocation, String specializationOf, String value, boolean onlyActive) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(Entity.class);
        final Root<Entity> root = criteriaQuery.from(Entity.class);
        final Join<Entity, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
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
    public List<Entity> findAllEntities(String atLocation, String specializationOf, boolean onlyActive) {
        Objects.requireNonNull(atLocation);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(Entity.class);
        final Root<Entity> root = criteriaQuery.from(Entity.class);
        final Join<Entity, Activity> activity = root.join("wasGeneratedBy", JoinType.INNER);
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
