/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package othelloWS;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ophiran
 */
@Entity
@Table(name = "moves")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Moves.findAll", query = "SELECT m FROM Moves m"),
    @NamedQuery(name = "Moves.findByGameId", query = "SELECT m FROM Moves m WHERE m.movesPK.gameId = :gameId"),
    @NamedQuery(name = "Moves.findByNumMove", query = "SELECT m FROM Moves m WHERE m.movesPK.numMove = :numMove"),
    @NamedQuery(name = "Moves.findByPlayer", query = "SELECT m FROM Moves m WHERE m.player = :player"),
    @NamedQuery(name = "Moves.findByPosX", query = "SELECT m FROM Moves m WHERE m.posX = :posX"),
    @NamedQuery(name = "Moves.findByPosY", query = "SELECT m FROM Moves m WHERE m.posY = :posY"),
    @NamedQuery(name = "Moves.findByDateMove", query = "SELECT m FROM Moves m WHERE m.dateMove = :dateMove")})
public class Moves implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MovesPK movesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "player")
    private long player;
    @Basic(optional = false)
    @NotNull
    @Column(name = "posX")
    private int posX;
    @Basic(optional = false)
    @NotNull
    @Column(name = "posY")
    private int posY;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateMove")
    @Temporal(TemporalType.DATE)
    private Date dateMove;
    @JoinColumn(name = "gameId", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Games games;
    @JoinColumn(name = "color", referencedColumnName = "colorId")
    @ManyToOne(optional = false)
    private Colors color;

    public Moves() {
    }

    public Moves(MovesPK movesPK) {
        this.movesPK = movesPK;
    }

    public Moves(MovesPK movesPK, long player, int posX, int posY, Date dateMove) {
        this.movesPK = movesPK;
        this.player = player;
        this.posX = posX;
        this.posY = posY;
        this.dateMove = dateMove;
    }

    public Moves(long gameId, int numMove) {
        this.movesPK = new MovesPK(gameId, numMove);
    }

    public MovesPK getMovesPK() {
        return movesPK;
    }

    public void setMovesPK(MovesPK movesPK) {
        this.movesPK = movesPK;
    }

    public long getPlayer() {
        return player;
    }

    public void setPlayer(long player) {
        this.player = player;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Date getDateMove() {
        return dateMove;
    }

    public void setDateMove(Date dateMove) {
        this.dateMove = dateMove;
    }

    public Games getGames() {
        return games;
    }

    public void setGames(Games games) {
        this.games = games;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movesPK != null ? movesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Moves)) {
            return false;
        }
        Moves other = (Moves) object;
        if ((this.movesPK == null && other.movesPK != null) || (this.movesPK != null && !this.movesPK.equals(other.movesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "othelloWS.Moves[ movesPK=" + movesPK + " ]";
    }
    
}
