package at.tuwien.indmp.dao;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

public abstract class AbstractDao<T extends Serializable> {

    protected final Class<T> type;

    @PersistenceContext
    protected EntityManager entityManager;

    protected AbstractDao(Class<T> clazz) {
        this.type = clazz;
    }

    /**
     *
     * Create a new entity
     *
     * @param entity to create
     */
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        try {
            entityManager.persist(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     *
     * Find entity by id
     *
     * @param id of entity
     * @return entity
     */
    public T find(Long id) {
        Objects.requireNonNull(id);
        return entityManager.find(type, id);
    }

    /**
     *
     * Update the entity
     *
     * @param entity to update
     * @return updated object
     */
    public T update(T entity) {
        Objects.requireNonNull(entity);
        try {
            return entityManager.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     *
     * Delete the entity
     *
     * @param entity to delete
     */
    public void delete(T entity) {
        Objects.requireNonNull(entity);
        try {
            final T toRemove = entityManager.merge(entity);
            if (toRemove != null) {
                entityManager.remove(toRemove);
            }
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
