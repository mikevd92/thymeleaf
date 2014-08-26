/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Mihai
 */
@Entity
@Table(name = "seat")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSeat", nullable = false)
    private Integer idSeat;
    @Size(max = 45)
    @Column(name = "availability", length = 45)
    private String availability;
    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seatNumber", nullable = false)
    private int seatNumber;
    @JoinColumn(name = "idPlay", referencedColumnName = "idPlay")
    @ManyToOne
    private Play idPlay;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne
    private User idUser;

    public Seat() {
    }

    public Seat(Integer idPlace) {
        this.idSeat = idPlace;
    }

    public Seat(Play play, Integer seatNumber, String availability) {
        this.idPlay = play;
        this.seatNumber = seatNumber;
        this.availability = availability;
    }

    public Seat(Integer idPlace, String name, String availability) {
        this.idSeat = idPlace;
        this.name = name;
        this.availability = availability;
    }

    public Integer getIdSeat() {
        return idSeat;
    }

    public void setIdSeat(Integer idPlace) {
        this.idSeat = idPlace;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getName() {
        if (idUser != null) {
            name = idUser.getName();
        } else {
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        this.idUser = new User(name);
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Play getIdPlay() {
        return idPlay;
    }

    public void setIdPlay(Play idPlay) {
        this.idPlay = idPlay;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSeat != null ? idSeat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seat)) {
            return false;
        }
        Seat other = (Seat) object;
        if ((this.idSeat == null && other.idSeat != null) || (this.idSeat != null && !this.idSeat.equals(other.idSeat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Seat[ idSeat=" + idSeat + ", name=" + name + ",seatNumher=" + seatNumber + ",availability=" + availability + " ]";
    }

}
