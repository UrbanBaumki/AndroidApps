package si.banani.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Hashtable;

import si.banani.scenes.Hud;
import si.banani.textures.TextureManager;

/**
 * Created by Urban on 29.1.2017.
 */

public class EntityFactory {
    private static Hashtable<EntityType, BasicPlayer> entities = new Hashtable<EntityType, BasicPlayer>();
    private static Hud hud;
    private static OrthographicCamera camera;
    private static World world;

    public static Hud getHud() {
        return hud;
    }

    public static enum EntityType{
        PLAYER,
        FEMALE,
        ROCK,
        ZOMBIE,
        SPIDER
    }

    public static BasicPlayer getEntity(EntityType entityType){
        BasicPlayer p = null;
        switch (entityType){
            case PLAYER:
                p = entities.get(EntityType.PLAYER);
                if(p == null){
                    p = new Player(world, 150, 250, 8, 18, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerMale").split(15,48)[0], 1/7f, hud, camera);
                    entities.put(EntityType.PLAYER, p);
                }

                break;
            case FEMALE:
                p = entities.get(EntityType.FEMALE);
                if(p == null){
                    p = new FemalePlayer(world, 110, 250, 8, 18, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerFemale").split(15,50)[0], 1/6f, hud, camera);
                    entities.put(EntityType.FEMALE, p);
                }

                break;


        }

        return p;
    }
    public static BasicPlayer createEnemy(EntityType entityType, Rectangle rect, World world){
        BasicPlayer p = null;

        switch (entityType){
            case ROCK:
                p = new RockEnemy(world, (int)rect.getX(), (int)rect.getY(), 16, 10, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("rockEnemy").split(44,37)[0], 1/4f);
                break;
            case ZOMBIE:
                p = new ZombieEnemy(world, (int)rect.getX(), (int)rect.getY(), 16, 10, BodyDef.BodyType.DynamicBody, new TextureRegion(new Texture(Gdx.files.internal("textures/props/zombie.png"))).split(20,48)[0], 1/4f);
                break;
            default:

        }
        return p;
    }
    public static void giveHud(Hud hudd){
        hud = hudd;
    }
    public static void giveCamera(OrthographicCamera cameraa){
        camera = cameraa;
    }
    public static void giveWorld(World wor){
        world = wor;
    }
    public static void clearEntities(){
        entities.clear();
    }
}
