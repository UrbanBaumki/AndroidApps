package si.banani.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.awt.event.ContainerListener;

import si.banani.entities.Player;
import si.banani.screens.MainMenu;
import si.banani.tiles.Box;
import si.banani.tiles.InteractiveTile;
import si.banani.tiles.Spikes;

/**
 * Created by Urban on 2.12.2016.
 */

public class WorldContactListener implements ContactListener {


    public WorldContactListener(){

    }
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
        if(a.getUserData() != null && a.getUserData() instanceof Spikes && b.getUserData() != null && b.getUserData() instanceof Box){
            //spikes and a box
            Box box = (Box) b.getUserData();
            box.onSpikeHit();
        }
        if(reverse)
            collideChecker(b, a, false);
    }
    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
