package persistence;

import exceptions.AppException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.Query;
import model.Seat;
import model.Play;
import model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("userSeatDAO")
public class JpaSeatDAO extends JpaDAO<Seat, Integer> implements SeatDAO {

    public JpaSeatDAO() throws AppException {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Seat> findByShowID(int index) throws AppException {
        Query q;
        try {
            Play play = entityManager.find(Play.class, index);
            q = entityManager.createQuery("SELECT X FROM " + entityClass.getName() + " X WHERE X.idPlay=:index");
            q.setParameter("index", play);
        } catch (Exception ex) {
            throw new AppException("DB Exception: " + ex.getMessage());
        }
        return q.getResultList();
    }

    @CacheEvict(value = "places", allEntries = true)
    @Override
    @Transactional
    public void addSeats(int amount, Play play) throws AppException {
        List<Seat> seats = IntStream.range(1, amount + 1).<Seat>mapToObj(p -> {
            Seat place = null;
            try {
                place = new Seat(play, p, "available");
                this.save(place);
            } catch (AppException e) {
                throw new AppException("Exception : An Error Occured");
            }
            return place;
        }).collect(Collectors.toList());
        play.setSeatList(seats);
    }

    @Override
    @Transactional
    public void changeAvailability(int index, String availability, User user) throws AppException {
        try {
            Optional<Seat> opt = Optional.ofNullable(this.findById(index));
            Seat place = opt.orElseThrow(IllegalArgumentException::new);
            place.setAvailability(availability);
            place.setIdUser(user);
            this.entityManager.merge(place);
        } catch (AppException e) {
            throw new AppException("DB Exception: Inexistent place");
        }
    }

    @Override
    @Transactional
    public void cancelAvailability(int index, String availability) throws AppException {
        try {
            Optional<Seat> opt = Optional.ofNullable(this.findById(index));
            Seat place = opt.orElseThrow(IllegalArgumentException::new);
            place.setAvailability(availability);
            place.setIdUser(null);
            this.entityManager.merge(place);
        } catch (AppException e) {
            throw new AppException("DB Exception: Inexistent place");
        }
    }
}
