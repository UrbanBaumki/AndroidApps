package si.banani.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;

import com.badlogic.gdx.physics.box2d.Manifold;




import si.banani.entities.Player;

import si.banani.tiles.Box;

import si.banani.tiles.Spikes;
import si.banani.tiles.Switch;
import si.banani.tiles.TileStates;

/**
 * Created by Urban on 2.12.2016.
 */

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        collideChecker(a, b, true);
    }

    private void collideChecker(Fixture a, Fixture b, boolean reverse){

        if(a.getUserData() != null && a.getUserData() instanceof Spikes && b.getUserData() != null && b.getUserData() instanceof Player){
            //player has collided with the spikes
            Spikes spike = (Spikes) a.getUserData();
            spike.onHit();
        }
        else if(a.getUserData() != null && a.getUserData() instanceof Spikes && b.getUserData() != null && b.getUserData() instanceof Box){
            //spikes and a box

        }else if(a.getUserData() != null && a.getUserData() instanceof Player && b.getUserData() != null && b.getUserData().equals("stairs")){
            Player p = (Player)a.getUserData();

            Gdx.app.log("Stairs", "Stairs");
        }
        if(reverse)
            collideChecker(b, a, false);
    }
    private void decollideChecker(Fixture a, Fixture b, boolean reverse){

        if(reverse)
            decollideChecker(b, a, false);
    }
    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        decollideChecker(a, b, true);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
