package persistence;

import exceptions.AppException;
import java.io.Serializable;
import java.util.List;
import model.Seat;
import model.Play;
import model.User;

public interface SeatDAO extends Serializable {

    public void addSeats(int amount, Play play) throws AppException;

    void changeAvailability(int index, String availability, User user)
            throws AppException;

    void cancelAvailability(int index, String availability) throws AppException;

    public List<Seat> findByShowID(int index) throws AppException;

    public Seat findById(Integer id) throws AppException;
}
