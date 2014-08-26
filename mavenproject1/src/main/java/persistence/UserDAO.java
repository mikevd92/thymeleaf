package persistence;

import exceptions.AppException;
import java.io.Serializable;
import model.User;

public interface UserDAO extends Serializable{

    User findUserByNameAndPass(String name, String pass) throws AppException;

    void createUser(User user) throws AppException;

    User findUserByName(String name) throws AppException;
}
