/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import exceptions.AppException;
import java.io.Serializable;
import model.Role;
import model.User;

/**
 *
 * @author Mihai
 */
public interface RoleDAO extends Serializable {
   public void addRole(User user) throws AppException; 
}
