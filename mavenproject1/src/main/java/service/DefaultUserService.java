 package service;

import exceptions.AppException;
import java.util.List;
import java.util.Optional;
import model.Play;
import model.Seat;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.SeatDAO;
import persistence.PlayDAO;
import persistence.UserDAO;

@Service("userService")
public class DefaultUserService implements
        UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PlayDAO userPlayDAO;
    @Autowired
    private SeatDAO userSeatDAO;

    @Transactional
    @Override
    public User loadUserByName(String name){
        User user=userDAO.findUserByName(name);
        return user;
    }

    @Transactional
    @Override
    public void addUser(User user) throws AppException {
        userDAO.createUser(user);
    }

    @Transactional
    @Override
    public void addPlay(Play play) throws AppException {
        userPlayDAO.addPlay(play);
        userSeatDAO.addSeats(200, play);
    }

    @Transactional
    @Override
    public void removePlay(int index) throws AppException {
        userPlayDAO.removePlay(index);
    }

    @Transactional
    @Override
    public void updatePlay(Play play)
            throws AppException {
        userPlayDAO.updatePlay(play);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Play> loadPlays() throws AppException {
        List<Play> plays = userPlayDAO.displayPlays();
        return plays;
    }

    @Transactional(readOnly = true)
    @Override
    public String getPlaceName(int index) throws AppException {
        Optional<Seat> place = Optional.ofNullable(userSeatDAO.findById(index));
        String name = place.map(p -> p.getName()).orElse("");
        return name;
    }

    @Transactional
    @Override
    public Seat getPlace(int index) throws AppException {
        Seat place = Optional.ofNullable(userSeatDAO.findById(index)).orElseThrow(() -> new AppException("No such Element"));
        return place;
    }

    @Transactional
    @Override
    public Play getPlay(int index) throws AppException {
        Play play = userPlayDAO.findById(index);
        return play;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Seat> findByShowID(int index) throws AppException {
        List<Seat> places = userSeatDAO.findByShowID(index);
        return places;
    }

    @Transactional
    @Override
    public void changeAvailability(int index, String availability, String name)
            throws AppException {
        availability = "unavailable";
        User user = (User) this.userDAO.findUserByName(name);
        this.userSeatDAO.changeAvailability(index, availability, user);
    }

    @Transactional
    @Override
    public void cancelAvailability(int index, String availability)
            throws AppException {
        availability = "available";
        this.userSeatDAO.cancelAvailability(index, availability);
    }

}
