package si.banani.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.awt.event.ContainerListener;

import si.banani.screens.MainMenu;

/**
 * Created by Urban on 2.12.2016.
 */

public class WorldContactListener implements ContactListener {

    MainMenu m;
    public WorldContactListener(MainMenu m){
        this.m = m;
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        Object hit = a.getBody().getUserData();
        if(hit != null){
            String hitName = hit.toString();
            if(hitName.equals("spikes")){
                m.reset = true;
            }
        }

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
