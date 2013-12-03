/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import session.GameListInfo;

/**
 *
 * @author ophiran
 */
@Entity
@Table(name = "games")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Games.findAll", query = "SELECT g FROM Games g"),
    @NamedQuery(name = "Games.findById", query = "SELECT g FROM Games g WHERE g.id = :id"),
    @NamedQuery(name = "Games.findByGameName", query = "SELECT g FROM Games g WHERE g.gameName = :gameName"),
    @NamedQuery(name = "Games.findByStartDate", query = "SELECT g FROM Games g WHERE g.startDate = :startDate"),
    @NamedQuery(name = "Games.findByEndDate", query = "SELECT g FROM Games g WHERE g.endDate = :endDate"),
    @NamedQuery(name = "Games.findByPassword", query = "SELECT g FROM Games g WHERE g.password = :password"),
    @NamedQuery(name = "Games.findByState", query = "SELECT g FROM Games g WHERE g.state = :state"),
    @NamedQuery(name = "Games.findByUnique", query = "SELECT g FROM Games g WHERE g.gameName = :gameName AND g.startDate = :startDate"),
    @NamedQuery(name = "Games.findWithPlayer",query = "SELECT g FROM Games g WHERE g.player1 = :player OR g.player2 = :player")})
public class Games implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "gameName")
    private String gameName;
    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Size(max = 40)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "player2", referencedColumnName = "Id")
    @ManyToOne
    private Players player2;
    @JoinColumn(name = "player1", referencedColumnName = "Id")
    @ManyToOne
    private Players player1;
    @JoinColumn(name = "state", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Gamestate state;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "games")
    private Collection<Moves> movesCollection;

    public Games() {
    }

    public Games(Long id) {
        this.id = id;
    }

    public Games(Long id, String gameName) {
        this.id = id;
        this.gameName = gameName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Players getPlayer2() {
        return player2;
    }

    public void setPlayer2(Players player2) {
        this.player2 = player2;
    }

    public Players getPlayer1() {
        return player1;
    }

    public void setPlayer1(Players player1) {
        this.player1 = player1;
    }

    public Gamestate getState() {
        return state;
    }

    public void setState(Gamestate state) {
        this.state = state;
    }

    @XmlTransient
    public Collection<Moves> getMovesCollection() {
        return movesCollection;
    }

    public void setMovesCollection(Collection<Moves> movesCollection) {
        this.movesCollection = movesCollection;
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
        if (!(object instanceof Games)) {
            return false;
        }
        Games other = (Games) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Games[ id=" + id + " ]";
    }
    
    public GameListInfo getGameInfo() {
        GameListInfo gameList = new GameListInfo();
        gameList.gameId = getId();
        gameList.gameName = gameName;
        if(getPlayer1() != null)
            gameList.player1 = getPlayer1().getNickname();
        if(getPlayer2() != null)
            gameList.player2 = getPlayer2().getNickname();
        gameList.startDate = getStartDate().toString();
        return gameList;
    }
}
