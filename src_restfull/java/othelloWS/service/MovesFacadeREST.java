/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package othelloWS.service;

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
import javax.ws.rs.core.PathSegment;
import othelloWS.Moves;
import othelloWS.MovesPK;

/**
 *
 * @author Ophiran
 */
@Stateless
@Path("othellows.moves")
public class MovesFacadeREST extends AbstractFacade<Moves> {
    @PersistenceContext(unitName = "OthelloRestFullPU")
    private EntityManager em;

    private MovesPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;gameId=gameIdValue;numMove=numMoveValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        othelloWS.MovesPK key = new othelloWS.MovesPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> gameId = map.get("gameId");
        if (gameId != null && !gameId.isEmpty()) {
            key.setGameId(new java.lang.Long(gameId.get(0)));
        }
        java.util.List<String> numMove = map.get("numMove");
        if (numMove != null && !numMove.isEmpty()) {
            key.setNumMove(new java.lang.Integer(numMove.get(0)));
        }
        return key;
    }

    public MovesFacadeREST() {
        super(Moves.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Moves entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") PathSegment id, Moves entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        othelloWS.MovesPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Moves find(@PathParam("id") PathSegment id) {
        othelloWS.MovesPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Moves> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Moves> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
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
