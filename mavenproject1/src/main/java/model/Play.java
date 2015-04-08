/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

/**
 *
 * @author Mihai
 */
@Entity
@Table(name = "play")
@NamedQueries({
    @NamedQuery(name = "Play.findAll", query = "SELECT p FROM Play p"),
    @NamedQuery(name = "Play.findByIdPlay", query = "SELECT p FROM Play p WHERE p.idPlay = :idPlay"),
    @NamedQuery(name = "Play.findByEndTime", query = "SELECT p FROM Play p WHERE p.endTime = :endTime"),
    @NamedQuery(name = "Play.findByPlayName", query = "SELECT p FROM Play p WHERE p.playName = :playName"),
    @NamedQuery(name = "Play.findByStartDate", query = "SELECT p FROM Play p WHERE p.startDate = :startDate"),
    @NamedQuery(name = "Play.findByStartTime", query = "SELECT p FROM Play p WHERE p.startTime = :startTime"),
    @NamedQuery(name = "Play.findByTicketPrice", query = "SELECT p FROM Play p WHERE p.ticketPrice = :ticketPrice")})
public class Play implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPlay", nullable = false)
    private Integer idPlay;
    @Column(name = "endTime")
    private Time endTime;
    @Size(max = 45)
    @Column(name = "playName", length = 45)
    private String playName;
    @Column(name = "startDate")
    @Future
    private Date startDate;
    @Column(name = "startTime")
    private Time startTime;
    @Basic(optional = false)
    @Column(name = "ticketPrice", nullable = false)
    @Digits(integer = 6, fraction = 0)
    private int ticketPrice;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idPlay")
    private List<Seat> seats;

    public Play() {
    }

    public Play(Integer idPlay) {
        this.idPlay = idPlay;
    }

    public Play(Integer idPlay, int ticketPrice) {
        this.idPlay = idPlay;
        this.ticketPrice = ticketPrice;
    }

    public Play(Integer idPlay, Date startDate, Time startTime, Time endTime, int ticketPrice) {
        this.idPlay = idPlay;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
    }

    public Integer getIdPlay() {
        return idPlay;
    }

    public void setIdPlay(Integer idPlay) {
        this.idPlay = idPlay;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public List<Seat> getSeatList() {
        return seats;
    }

    public void setSeatList(List<Seat> placeList) {
        this.seats = placeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlay != null ? idPlay.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Play other = (Play) obj;
        if (!Objects.equals(this.endTime, other.endTime)) {
            return false;
        }
        if (!Objects.equals(this.playName, other.playName)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        if (!Objects.equals(this.startTime, other.startTime)) {
            return false;
        }
        if (this.ticketPrice != other.ticketPrice) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Play{" + "idPlay=" + idPlay + ", endTime=" + endTime + ", playName=" + playName + ", startDate=" + startDate + ", startTime=" + startTime + ", ticketPrice=" + ticketPrice + ", placeList=" + seats + '}';
    }

}
