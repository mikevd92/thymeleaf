package persistence;

import exceptions.AppException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import model.Play;

public interface PlayDAO extends Serializable {

    public void addPlay(Play play) throws AppException;

    public void removePlay(int index) throws AppException;

    public void updatePlay(Play play) throws AppException;

    public List<Play> displayPlays() throws AppException;

    public Play findById(Integer id) throws AppException;
}
