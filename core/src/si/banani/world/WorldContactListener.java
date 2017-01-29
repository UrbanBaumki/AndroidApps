package si.banani.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;

import com.badlogic.gdx.physics.box2d.Manifold;


import si.banani.entities.BasicPlayer;
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;

import si.banani.entities.RockEnemy;
import si.banani.entities.SpiderEnemy;
import si.banani.maps.MapFactory;
import si.banani.screens.Play;
import si.banani.tiles.Box;

import si.banani.tiles.DialogPoint;
import si.banani.tiles.EndPoint;
import si.banani.tiles.Ladder;
import si.banani.tiles.Potion;
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

            Player p = (Player) b.getUserData();
            p.setReset(true);
        }else if(a.getUserData() instanceof Player && b.getUserData() instanceof EndPoint){
            Play.nextMap = MapFactory.MapType.CHAPTER1;

        }
        else if(a.getUserData() instanceof FemalePlayer && b.getUserData() instanceof Spikes){
            FemalePlayer p = (FemalePlayer) a.getUserData();
            p.setReset(true);
        }
        else if(a.getUserData() != null && a.getUserData() instanceof Spikes && b.getUserData() != null && b.getUserData() instanceof Box){
            //spikes and a box

        }else if(a.getUserData() != null && a.getUserData() instanceof Player && b.getUserData() != null && b.getUserData().equals("stairs")){
            Player p = (Player)a.getUserData();

            Gdx.app.log("Stairs", "Stairs");
        }else if(a.getUserData() instanceof FemalePlayer && b.getUserData() instanceof Potion){ //female in energy potion
            Potion p = (Potion) b.getUserData();
            if(p.getPotionType() == Potion.PotionType.ENERGY)
                p.pickup();
            ((FemalePlayer) a.getUserData()).addEnergy(p.getValue());

        }else if(a.getUserData() instanceof Player && b.getUserData() instanceof Potion){
            Potion p = (Potion) b.getUserData();
            if(p.getPotionType() == Potion.PotionType.HEALTH)
                p.pickup();
            ((Player) a.getUserData()).addHealth(p.getValue());

        } else if(a.getUserData() instanceof Player && b.getUserData() instanceof DialogPoint){
            DialogPoint p = (DialogPoint) b.getUserData();
            p.setDialogToDisplay();
            p.setExecuted();
            ((Player)a.getUserData()).startDialog();

        } else if(a.getUserData() instanceof Player &&  b.getUserData() instanceof RockEnemy ){
            RockEnemy p = (RockEnemy) b.getUserData();
            p.dealDamageToTarget();
            Gdx.app.log("Dotik", "");

        }else if(a.getUserData() instanceof  BasicPlayer && a.isSensor() && b.getUserData() instanceof  Ladder){
            ((BasicPlayer)a.getUserData()).setCanClimb(true);

        }
        if(a.getUserData() instanceof BasicPlayer && a.isSensor() ){
            BasicPlayer p = (BasicPlayer) a.getUserData();
            p.increaseFootContacts(1);

        }




        if(reverse)
            collideChecker(b, a, false);
    }
    private void decollideChecker(Fixture a, Fixture b, boolean reverse){
       if(a.getUserData() instanceof BasicPlayer && a.isSensor() ){
            BasicPlayer p = (BasicPlayer) a.getUserData();
            p.increaseFootContacts(-1);

        }
        if(a.getUserData() instanceof  BasicPlayer && a.isSensor() && b.getUserData() instanceof  Ladder){
           ((BasicPlayer)a.getUserData()).setCanClimb(false);

       }




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
