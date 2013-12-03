/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ophiran
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerProfile {
    @XmlAttribute
    public List<String> gameList = new ArrayList<>();
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "wins")
    public int wins;
    @XmlElement(name = "loss")
    public int loss;
}
