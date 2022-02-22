package at.tuwien.indmp.dao;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import at.tuwien.indmp.model.Entity;

@Repository
public class EntityDao extends AbstractDao<Entity> {

    public EntityDao() {
        super(Entity.class);
    }

    /**
     * 
     * Find property
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

        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * 
     * Find properties (using equal)
     * 
     * @param atLocation
     * @param specializationOf
     * @return
     */
    public List<Entity> findEntities(String atLocation, String specializationOf) {
        Objects.requireNonNull(atLocation);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(Entity.class);
        final Root<Entity> root = criteriaQuery.from(Entity.class);
        criteriaQuery.select(root).distinct(true);

        // Conditions
        Predicate predicate = criteriaBuilder.equal(root.get("atLocation"), atLocation);
        criteriaQuery.where(predicate);

        if (specializationOf != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("specializationOf"), specializationOf));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * 
     * Find all properties (with nested, using LIKE)
     * 
     * @param atLocation
     * @return
     */
    public List<Entity> findAllEntities(String atLocation, String specializationOf) {
        Objects.requireNonNull(atLocation);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(Entity.class);
        final Root<Entity> root = criteriaQuery.from(Entity.class);
        criteriaQuery.select(root).distinct(true);

        // Conditions
        Predicate predicate = criteriaBuilder.like(root.get("atLocation"), atLocation);

        if (specializationOf != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("specializationOf"), specializationOf));
        }

        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    // /**
    // *
    // * Find identifiers
    // *
    // * @param dmpIdentifier
    // * @param classType
    // * @param propertyName
    // * @return
    // */
    // public List<Property> findIdentifiers(String dmpIdentifier, String classType,
    // String propertyName) {
    // Objects.requireNonNull(dmpIdentifier);

    // final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    // final CriteriaQuery<Property> criteriaQuery =
    // criteriaBuilder.createQuery(Property.class);
    // final Root<Property> root = criteriaQuery.from(Property.class);
    // criteriaQuery.select(root).distinct(true);

    // // Conditions
    // Predicate predicate = criteriaBuilder.equal(root.get("dmpIdentifier"),
    // dmpIdentifier);

    // // Class type
    // if (classType != null) {
    // predicate = criteriaBuilder.and(predicate,
    // criteriaBuilder.equal(root.get("classType"), classType));
    // }

    // // Property name
    // if (propertyName != null) {
    // predicate = criteriaBuilder.and(predicate,
    // criteriaBuilder.equal(root.get("propertyName"), propertyName));
    // }
    // criteriaQuery.where(predicate);

    // return entityManager.createQuery(criteriaQuery).getResultList();
    // }

}
