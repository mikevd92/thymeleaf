package model;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

    public static volatile SingularAttribute<User, Date> dateOfBirth;
    public static volatile SingularAttribute<User, String> address;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, Integer> idUser;
    public static volatile ListAttribute<User, Seat> places;
    public static volatile ListAttribute<User, Role> roles;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Boolean> role;

    private User_() {
    }
}
