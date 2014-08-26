package persistence;

import java.util.List;
import exceptions.AppException;

public interface DAO<T, PK> {

    List<T> findAll() throws AppException;

    T findById(PK id) throws AppException;

    T save(T entity) throws AppException;

    void remove(T entity) throws AppException;

    void removeById(PK id) throws AppException;

    void flush() throws AppException;
}
