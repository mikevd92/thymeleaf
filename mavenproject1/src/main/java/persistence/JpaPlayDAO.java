package persistence;

import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import model.Seat;
import model.Play;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import exceptions.AppException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository("userPlayDAO")
public class JpaPlayDAO extends JpaDAO<Play, Integer> implements
        PlayDAO {

    public JpaPlayDAO() throws AppException {
        super();
    }

    @Override
    @CacheEvict(value = "plays", key = "index")
    @Transactional
    public void removePlay(int index) throws AppException {
        try {
            Optional<Play> playOpt = Optional.ofNullable(this.findById(index));
            Play play = playOpt.orElseThrow(IllegalArgumentException::new);
            this.remove(play);
        } catch (AppException e) {
            throw new AppException("DB Exception: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updatePlay(Play play)
            throws AppException {
        Date date;
        Time sTime;
        Time eTime;
        Play otherPlay;
        try {
            Optional<Play> playOpt = Optional.ofNullable(this.findById(play.getIdPlay()));
            otherPlay = playOpt.orElseThrow(NullPointerException::new);
            Optional<String> opt = Optional.ofNullable(play.getPlayName());
            opt.filter(p -> !p.equals("")).ifPresent(p -> otherPlay.setPlayName(p));
            Optional<Integer> optPrice = Optional.ofNullable(otherPlay.getTicketPrice());
            optPrice.filter(p -> p != null).ifPresent(p -> otherPlay.setTicketPrice(play.getTicketPrice()));
            Optional<Date> optDate = Optional.ofNullable(play.getStartDate());
            optDate.filter(p -> p != null).ifPresent(p -> otherPlay.setStartDate(play.getStartDate()));
            Optional<Time> optTime = Optional.ofNullable(play.getStartTime());
            optTime.filter(p -> p != null).ifPresent(p -> otherPlay.setStartTime(play.getStartTime()));
            optTime = Optional.ofNullable(play.getEndTime());
            optTime.filter(p -> p != null).ifPresent(p -> otherPlay.setEndTime(play.getEndTime()));
            this.entityManager.merge(otherPlay);
        } catch (IllegalArgumentException ex) {
            throw new AppException("DB Exception : Illegal Argument " + ex.getMessage());
        } catch (AppException e) {
            throw new AppException("DB Exception : Play does not exist");
        }
    }

    @Cacheable(value = "places", key = "index")
    @Transactional
    public List<Seat> findByShowID(int index) throws AppException {
        Play play = entityManager.find(Play.class, index);
        return play.getSeatList();
    }

    @Override
    @CacheEvict(value = "plays", key = "playName")
    @Transactional
    public void addPlay(Play play) throws AppException {
        Date date = play.getStartDate();
        Time sTime = play.getStartTime();
        Time eTime = play.getEndTime();
        this.save(play);
    }

    @Override
    @Cacheable(value = "plays")
    @Transactional
    public List<Play> displayPlays() throws AppException {
        List<Play> plays = this.findAll();
        return plays;
    }
}
