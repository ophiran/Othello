/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ophiran
 */
@Embeddable
public class MovesPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "gameId")
    private long gameId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numMove")
    private int numMove;

    public MovesPK() {
    }

    public MovesPK(long gameId, int numMove) {
        this.gameId = gameId;
        this.numMove = numMove;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getNumMove() {
        return numMove;
    }

    public void setNumMove(int numMove) {
        this.numMove = numMove;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) gameId;
        hash += (int) numMove;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovesPK)) {
            return false;
        }
        MovesPK other = (MovesPK) object;
        if (this.gameId != other.gameId) {
            return false;
        }
        if (this.numMove != other.numMove) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MovesPK[ gameId=" + gameId + ", numMove=" + numMove + " ]";
    }
    
}
