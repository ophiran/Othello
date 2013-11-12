/*
 * To change this template, choose Tools | Templates
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
 * @author Ophiran
 */
@Entity
@Table(name = "gamestate")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gamestate.findAll", query = "SELECT g FROM Gamestate g"),
    @NamedQuery(name = "Gamestate.findById", query = "SELECT g FROM Gamestate g WHERE g.id = :id"),
    @NamedQuery(name = "Gamestate.findByName", query = "SELECT g FROM Gamestate g WHERE g.name = :name")})
public class Gamestate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "state")
    private Collection<Games> gamesCollection;

    public Gamestate() {
    }

    public Gamestate(Long id) {
        this.id = id;
    }

    public Gamestate(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Games> getGamesCollection() {
        return gamesCollection;
    }

    public void setGamesCollection(Collection<Games> gamesCollection) {
        this.gamesCollection = gamesCollection;
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
        if (!(object instanceof Gamestate)) {
            return false;
        }
        Gamestate other = (Gamestate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Gamestate[ id=" + id + " ]";
    }
    
}
