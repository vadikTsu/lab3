package ua.com.hastleuniversity.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
public class AbstractRepo<T> {

    Class<T> entity;

    @PersistenceContext
    protected EntityManager entityManager;

    public final void setEntity(final Class<T> entity) {
        this.entity = entity;
    }

    public Optional<T> findById(final long id) {
        return Optional.ofNullable(entityManager.find(entity, id));
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return entityManager.createQuery("from " + entity.getName()).getResultList();
    }

    public boolean existsById(final long id) {
        return entityManager.contains(entityManager.find(entity, id));
    }

    public T save(T entity) {
        if (entityManager.contains(entity)) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
        return entity;
    }

    public List<T> findAllById(Set<Long> ids) {
        String jpql = "SELECT e FROM " + entity.getSimpleName() + " e WHERE e.id IN :ids";
        return entityManager.createQuery(jpql, entity)
                .setParameter("ids", ids)
                .getResultList();
    }

    public void delete(final T entity) {
        entityManager.remove(entity);
    }

    public void deleteById(final long id) {
        final T entity = findById(id).orElseThrow(EntityNotFoundException::new);
        delete(entity);
    }

}
