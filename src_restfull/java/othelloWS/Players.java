/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package othelloWS;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ophiran
 */
@Entity
@Table(name = "players")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Players.findAll", query = "SELECT p FROM Players p"),
    @NamedQuery(name = "Players.findById", query = "SELECT p FROM Players p WHERE p.id = :id"),
    @NamedQuery(name = "Players.findByNickname", query = "SELECT p FROM Players p WHERE p.nickname = :nickname"),
    @NamedQuery(name = "Players.findByPassword", query = "SELECT p FROM Players p WHERE p.password = :password"),
    @NamedQuery(name = "Players.findByWins", query = "SELECT p FROM Players p WHERE p.wins = :wins"),
    @NamedQuery(name = "Players.findByLost", query = "SELECT p FROM Players p WHERE p.lost = :lost")})
public class Players implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nickname")
    private String nickname;
    @Size(max = 40)
    @Column(name = "password")
    private String password;
    @Column(name = "wins")
    private Integer wins;
    @Column(name = "lost")
    private Integer lost;
    @OneToMany(mappedBy = "player2")
    private Collection<Games> gamesCollection;
    @OneToMany(mappedBy = "player1")
    private Collection<Games> gamesCollection1;

    public Players() {
    }

    public Players(Long id) {
        this.id = id;
    }

    public Players(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLost() {
        return lost;
    }

    public void setLost(Integer lost) {
        this.lost = lost;
    }

    @XmlTransient
    public Collection<Games> getGamesCollection() {
        return gamesCollection;
    }

    public void setGamesCollection(Collection<Games> gamesCollection) {
        this.gamesCollection = gamesCollection;
    }

    @XmlTransient
    public Collection<Games> getGamesCollection1() {
        return gamesCollection1;
    }

    public void setGamesCollection1(Collection<Games> gamesCollection1) {
        this.gamesCollection1 = gamesCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Players)) {
            return false;
        }
        Players other = (Players) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "othelloWS.Players[ id=" + id + " ]";
    }
    
}
