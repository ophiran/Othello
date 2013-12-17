/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package othelloWS.service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import othelloWS.Games;

/**
 *
 * @author Ophiran
 */
@Stateless
@Path("othellows.games")
public class GamesFacadeREST extends AbstractFacade<Games> {
    @PersistenceContext(unitName = "OthelloRestFullPU")
    private EntityManager em;

    public GamesFacadeREST() {
        super(Games.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Games entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Long id, Games entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Games find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Games> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Games> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }
    
    @GET
    @Path("ended={State}")
    @Produces({"application/xml", "application/json"})
    public List<Games> findStateGame(@PathParam("State") String id){
        System.out.println("DEBUG");
        List<Games> retList = new ArrayList<Games>();
        for(Games item : super.findAll()) {
            if(item.getState().getName().contains(id)){
                retList.add(item);
            }
        }
        return retList;
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
