package at.tuwien.indmp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import at.tuwien.indmp.model.Activity;
import at.tuwien.indmp.model.Entity;

@Repository
public class EntityDao extends AbstractDao<Entity> {

    @Autowired
    private ActivityDao activityDao;

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
        Predicate predicate = criteriaBuilder.like(root.get("atLocation"), atLocation + "%");

        if (specializationOf != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("specializationOf"), specializationOf));
        }

        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     *
     * Find identifiers in history table
     *
     * @param id
     * @return
     */
    public List<Entity> findInHistory(Long id) {
        Objects.requireNonNull(id);
        final List<Entity> entities = new ArrayList<>();

        // Run query
        final Query query = entityManager.createNativeQuery(
                "SELECT e.at_location, e.specialization_of, e.value, e.was_generated_by from entity_history e WHERE ID = ? ORDER BY e.ID ASC");
        query.setParameter(1, id);

        // Create a new entity for each instance
        List<Object[]> returnList = query.getResultList();
        for (Object[] entity : returnList) {
            final Activity activity = activityDao.find(Long.valueOf(String.valueOf(entity[3])));
            entities.add(new Entity(String.valueOf(entity[0]), String.valueOf(entity[1]), String.valueOf(entity[2]),
                    activity));
        }

        return entities;
    }

}
