package persistence;

import exceptions.AppException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class JpaDAO<E, PK> implements DAO<E, PK> {

    protected Class<E> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public JpaDAO() throws AppException {
        try {
            ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
            this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public List<E> findAll() throws AppException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> cq = cb.createQuery(this.entityClass);
            Root<E> root = cq.from(this.entityClass);
            cq.select(root);
            TypedQuery<E> q = entityManager.createQuery(cq);
            List<E> list = q.getResultList();
            return list;
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

    @Transactional
    public E findById(PK id) throws AppException {
        try {
            return entityManager.find(entityClass, id);
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

    @Transactional
    public void flush() throws AppException {
        try {
            entityManager.flush();
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

    @Transactional
    public void remove(E entity) throws AppException {
        try {
            entityManager.remove(entity);
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

    @Transactional
    public void removeById(PK id) throws AppException {
        entityManager.remove(findById(id));
    }

    @Transactional
    public E save(E entity) throws AppException {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

    public String getEntityName() throws AppException {
        try {
            return entityClass.getName();
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

}
