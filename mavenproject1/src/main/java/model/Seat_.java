package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Seat.class)
public abstract class Seat_ {

    public static volatile SingularAttribute<Seat, String> name;
    public static volatile SingularAttribute<Seat, Play> play;
    public static volatile SingularAttribute<Seat, Integer> seatNumber;
    public static volatile SingularAttribute<Seat, Integer> idSeat;
    public static volatile SingularAttribute<Seat, User> user;
    public static volatile SingularAttribute<Seat, String> availability;

}
