/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import exceptions.AppException;
import java.util.Arrays;
import model.Role;
import model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mihai
 */
@Repository("rolePlayDAO")
public class JpaRoleDAO extends JpaDAO<Role,Integer> implements RoleDAO{
    @CacheEvict(value = "users", key = "name")
    @Override
    @Transactional
    public void addRole(User user) throws AppException {
        Role role=new Role(1);
        role.setName("user");
        user.setRoles(Arrays.asList(new Role(1)));
    }
}
