package si.banani.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;

import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import si.banani.entities.BasicPlayer;
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;

import si.banani.entities.RockEnemy;
import si.banani.entities.SpiderEnemy;
import si.banani.entities.ZombieEnemy;
import si.banani.maps.Map;
import si.banani.maps.MapFactory;
import si.banani.screens.Play;
import si.banani.tiles.Box;

import si.banani.tiles.CheckPoint;
import si.banani.tiles.CutscenePoint;
import si.banani.tiles.DialogPoint;
import si.banani.tiles.EndPoint;
import si.banani.tiles.Ladder;
import si.banani.tiles.Potion;
import si.banani.tiles.Spikes;
import si.banani.tiles.Switch;
import si.banani.tiles.TileStates;
import si.banani.water.FixturePair;
import si.banani.water.PolygonClipping;
import si.banani.water.Water;
import si.banani.water.WaterHandler;

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
        }else if(a.getUserData() instanceof Water && a.isSensor() && b.getBody().getType() == BodyDef.BodyType.DynamicBody){ //any dynamic body and water

            WaterHandler.getInstance().addFixturePair(b, a);

        }else if(a.getUserData() instanceof String && a.getUserData().equals("killSensor") && b.getUserData() instanceof ZombieEnemy){
            FemalePlayer female = (FemalePlayer)a.getBody().getFixtureList().get(0).getUserData();
            ZombieEnemy zombie = (ZombieEnemy) b.getUserData();
            //check wether zombie is too close and kill him
            if(Math.abs(female.getPosition().x - zombie.getPosition().x) <= 0.5f && Math.abs(female.getPosition().y - zombie.getPosition().y) <= 0.5f){

                zombie.setDead(true);
            }

        }else if(a.getUserData() instanceof Player && b.getUserData() instanceof CutscenePoint){
            Play.switchToCutscene = true;


        }else if(a.getUserData() instanceof Spikes && b.getUserData() instanceof RockEnemy){
            ((RockEnemy)b.getUserData()).setDead(true);
        }
        else if(a.getUserData() instanceof Player && b.getUserData() instanceof EndPoint){
            EndPoint end = (EndPoint) b.getUserData();
            Play.nextMap = end.getChapter();
            Play.nextLevel = end.getLevel();
            Play.chapFinished = true;
        }else if(a.getUserData() instanceof Player && (b.getUserData() instanceof RockEnemy && b.isSensor() || b.getUserData() instanceof ZombieEnemy && b.isSensor())){
         //"VIDNI" SENZOR ZA ROCK ENEMY in PLAYER KONTAKT
            ((BasicPlayer)b.getUserData()).setTarget((Player)a.getUserData());
        }
        else if(a.getUserData() instanceof Player && b.getUserData() instanceof CheckPoint){
            Player p = (Player) a.getUserData();
            p.setCheckpoint((CheckPoint)b.getUserData());
            //((CheckPoint)b.getUserData()).setAlreadyActivated();

        } else if(a.getUserData() instanceof FemalePlayer && b.getUserData() instanceof CheckPoint){
            FemalePlayer p = (FemalePlayer) a.getUserData();
            p.setCheckpoint((CheckPoint)b.getUserData());
            //((CheckPoint)b.getUserData()).setAlreadyActivated();

        }
        else if(a.getUserData() instanceof FemalePlayer && b.getUserData() instanceof Spikes){
            FemalePlayer p = (FemalePlayer) a.getUserData();
            p.setReset(true);
        }
        else if(a.getUserData() != null && a.getUserData() instanceof Spikes && b.getUserData() != null && b.getUserData() instanceof Box){
            //spikes and a box


        }else if(a.getUserData() instanceof FemalePlayer && b.getUserData() instanceof Potion){ //female in energy potion
            Potion p = (Potion) b.getUserData();
            if(p.getPotionType() == Potion.PotionType.ENERGY)
            {
                p.pickup();
                ((FemalePlayer) a.getUserData()).addEnergy(p.getValue());
            }


        }else if(a.getUserData() instanceof Player && b.getUserData() instanceof Potion){
            Potion p = (Potion) b.getUserData();
            if(p.getPotionType() == Potion.PotionType.HEALTH)
            {
                p.pickup();
                ((Player) a.getUserData()).addHealth(1);
            }


        } else if(a.getUserData() instanceof Player && b.getUserData() instanceof DialogPoint){
            DialogPoint p = (DialogPoint) b.getUserData();
            if(p.getChapter().equals("21")){
                p.setDialogToDisplay();
                p.setExecuted();
                ((Player)a.getUserData()).startDialog();
            }else{
                p.setDialogToDisplay();
                p.setExecuted();
                ((Player)a.getUserData()).startDialog();
            }



        } else if(a.getUserData() instanceof Player &&  b.getUserData() instanceof RockEnemy ){
            RockEnemy p = (RockEnemy) b.getUserData();
            p.setTarget((Player)a.getUserData());
            p.setDoAttack(true);


        }else if(a.getUserData() instanceof Player &&  b.getUserData() instanceof ZombieEnemy){
            ZombieEnemy p = (ZombieEnemy) b.getUserData();
            p.setTarget((Player)a.getUserData());
            p.setDoAttack(true);
        }else if(a.getUserData() instanceof  BasicPlayer && a.isSensor() && b.getUserData() instanceof  Ladder){
            ((BasicPlayer)a.getUserData()).setCanClimb(true);

        }
        if(a.getUserData() instanceof BasicPlayer && a.isSensor() ){
            if(a.getUserData() instanceof ZombieEnemy){
                if(a.getFilterData().maskBits == (CollisionBits.OBJECT_BIT | CollisionBits.DEFAULT_BIT | CollisionBits.DOORS_BIT)){
                    //the collision sensor
                    ((ZombieEnemy) a.getUserData()).switchDirection();
                }
            }else if(a.getUserData() instanceof Player){
                 if(!b.isSensor() ){
                    BasicPlayer p = (BasicPlayer) a.getUserData();
                    p.increaseFootContacts(1);
                }
            }else if(a.getUserData() instanceof FemalePlayer){
                if(b.getUserData() instanceof Water || !b.isSensor()){
                    BasicPlayer p = (BasicPlayer) a.getUserData();
                    p.increaseFootContacts(1);
                }
            }


        }




        if(reverse)
            collideChecker(b, a, false);
    }
    private void decollideChecker(Fixture a, Fixture b, boolean reverse){
       if(a.getUserData() instanceof BasicPlayer && a.isSensor()){


           if(a.getUserData() instanceof FemalePlayer && (b.getUserData() instanceof Water || !b.isSensor())){

                BasicPlayer p = (BasicPlayer) a.getUserData();
                p.increaseFootContacts(-1);
            }else if(a.getUserData() instanceof Player && !b.isSensor()){
               BasicPlayer p = (BasicPlayer) a.getUserData();
               p.increaseFootContacts(-1);
           }


        }
        if(a.getUserData() instanceof  BasicPlayer && a.isSensor() && b.getUserData() instanceof  Ladder){
           ((BasicPlayer)a.getUserData()).setCanClimb(false);

       }

        if(a.getUserData() instanceof Player && (b.getUserData() instanceof RockEnemy && b.isSensor()  || b.getUserData() instanceof ZombieEnemy && b.isSensor())){
            //"VIDNI" SENZOR ZA ROCK ENEMY in PLAYER KONTAKT
            if(b.getFilterData().maskBits != (CollisionBits.OBJECT_BIT | CollisionBits.DEFAULT_BIT | CollisionBits.DOORS_BIT)){
                //the collision sensor
                ((BasicPlayer)b.getUserData()).setTarget(null);
            }

        }
        if(a.getUserData() instanceof Water && a.isSensor() && b.getBody().getType() == BodyDef.BodyType.DynamicBody){
            WaterHandler.getInstance().removeFixtureContact(b);
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
