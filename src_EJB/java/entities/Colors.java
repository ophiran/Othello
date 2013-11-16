/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author ophiran
 */
@Entity
@Table(name = "colors")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Colors.findAll", query = "SELECT c FROM Colors c"),
    @NamedQuery(name = "Colors.findByColorId", query = "SELECT c FROM Colors c WHERE c.colorId = :colorId"),
    @NamedQuery(name = "Colors.findByName", query = "SELECT c FROM Colors c WHERE c.name = :name")})
public class Colors implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "colorId")
    private Long colorId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "color")
    private Collection<Moves> movesCollection;

    public Colors() {
    }

    public Colors(Long colorId) {
        this.colorId = colorId;
    }

    public Colors(Long colorId, String name) {
        this.colorId = colorId;
        this.name = name;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        hash += (colorId != null ? colorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Colors)) {
            return false;
        }
        Colors other = (Colors) object;
        if ((this.colorId == null && other.colorId != null) || (this.colorId != null && !this.colorId.equals(other.colorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Colors[ colorId=" + colorId + " ]";
    }
    
}
