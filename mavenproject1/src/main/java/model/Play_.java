package model;

import java.sql.Date;
import java.sql.Time;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Play.class)
public abstract class Play_ {

    public static volatile SingularAttribute<Play, Time> startTime;
    public static volatile SingularAttribute<Play, Integer> idPlay;
    public static volatile SingularAttribute<Play, Date> startDate;
    public static volatile SingularAttribute<Play, Integer> ticketPrice;
    public static volatile ListAttribute<Play, Seat> seats;
    public static volatile SingularAttribute<Play, String> playName;
    public static volatile SingularAttribute<Play, Time> endTime;

    private Play_() {
    }

}
