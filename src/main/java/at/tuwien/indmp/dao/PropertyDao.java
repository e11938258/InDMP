package at.tuwien.indmp.dao;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import at.tuwien.indmp.model.Property;

import org.springframework.stereotype.Repository;

@Repository
public class PropertyDao extends AbstractDao<Property> {

    public PropertyDao() {
        super(Property.class);
    }

    /**
     * 
     * Find property
     * 
     * @param dmpIdentifier
     * @param classType
     * @param classIdentifier
     * @param propertyName
     * @param value
     * @param reference
     * @return
     */
    public List<Property> findProperty(String dmpIdentifier, String classType, String classIdentifier,
            String propertyName, String value, String reference) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        final Root<Property> root = criteriaQuery.from(Property.class);
        criteriaQuery.select(root).distinct(true);

        // Conditions
        Predicate predicate = null;

        // DMP identifier
        if (dmpIdentifier != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("dmpIdentifier"), dmpIdentifier));
        }

        // Class name
        if (classType != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("classType"), classType));
        }

        // Class identifier
        if (classIdentifier != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("classIdentifier"), classIdentifier));
        }

        // Property name
        if (propertyName != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("propertyName"), propertyName));
        }

        // Value
        if (value != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("value"), value));
        }

        // Reference
        if (reference != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("reference"), reference));
        }

        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * 
     * Find identifiers
     * 
     * @param dmpIdentifier
     * @param classType
     * @param propertyName
     * @return
     */
    public List<Property> findIdentifiers(String dmpIdentifier, String classType, String propertyName) {
        Objects.requireNonNull(dmpIdentifier);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Property> criteriaQuery = criteriaBuilder.createQuery(Property.class);
        final Root<Property> root = criteriaQuery.from(Property.class);
        criteriaQuery.select(root).distinct(true);

        // Conditions
        Predicate predicate = criteriaBuilder.equal(root.get("dmpIdentifier"), dmpIdentifier);

        // Class type
        if (classType != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("classType"), classType));
        }

        // Property name
        if (propertyName != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("propertyName"), propertyName));
        }
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}
