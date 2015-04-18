package persistence;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import exceptions.AppException;
import model.User;
import model.User_;
import org.springframework.transaction.annotation.Transactional;

@Repository("userDAO")
public class JpaUserDAO extends JpaDAO<User, Integer> implements UserDAO {

    public JpaUserDAO() throws AppException {
        super();
    }

    @Cacheable(value = "users", key = "name")
    @Override
    @Transactional
    public User findUserByNameAndPass(String name, String pass) throws AppException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(this.entityClass);
            Root<User> user = cq.from(this.entityClass);
            cq.where(cb.and(
                    cb.equal(user.get(User_.name), name),
                    cb.equal(user.get(User_.password), pass)
            )
            );
            cq.select(user);
            TypedQuery<User> q = entityManager.createQuery(cq);
            return q.getSingleResult();
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

    @Cacheable(value = "users", key = "name")
    @Override
    @Transactional
    public User findUserByName(String name) throws AppException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(this.entityClass);
            Root<User> user = cq.from(this.entityClass);
            cq.where(cb.equal(user.get(User_.name), name));
            cq.select(user);
            TypedQuery<User> q = entityManager.createQuery(cq);
            return q.getSingleResult();
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
    }

    @CacheEvict(value = "users", key = "name")
    @Override
    @Transactional
    public void createUser(User user) throws AppException {
        this.save(user);
    }

}
