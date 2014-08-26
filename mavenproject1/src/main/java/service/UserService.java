package service;

import exceptions.AppException;
import java.io.Serializable;
import java.util.List;
import model.Play;
import model.Seat;
import model.User;

public interface UserService extends Serializable {

    public void addUser(User user) throws AppException;

    List<Seat> findByShowID(int index) throws AppException;

    public void changeAvailability(int index, String availability, String name) throws AppException;

    public void cancelAvailability(int index, String availability) throws AppException;

    List<Play> loadPlays() throws AppException;

    public Play getPlay(int index) throws AppException;

    public Seat getPlace(int index) throws AppException;

    public void addPlay(Play play) throws AppException;

    public void removePlay(int index) throws AppException;

    public void updatePlay(Play play) throws AppException;
    
    public User loadUserByName(String name);

    public String getPlaceName(int index) throws AppException;

}
