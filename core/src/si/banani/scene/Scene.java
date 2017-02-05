package si.banani.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import si.banani.tiles.Box;
import si.banani.tiles.Door;
import si.banani.tiles.Ladder;
import si.banani.tiles.Potion;
import si.banani.tiles.Prop;
import si.banani.tiles.Swings;
import si.banani.tiles.Switch;
import si.banani.water.Water;

/**
 * Created by Urban on 9.12.2016.
 */

public class Scene {

    public static ArrayList<Object> sceneObjects = new ArrayList<Object>();
    public static ArrayList<Object> removables = new ArrayList<Object>();
    private static SpriteBatch batch;

    public static void setSpriteBatch(SpriteBatch b){
        batch = b;
    }

    public static void addObjectToScene(Object o ){
        sceneObjects.add(o);
    }

    public static void update(float dt){


        for(int i = 0; i < sceneObjects.size(); i++){
            Object object = sceneObjects.get(i);
            Object pleaseRemove = null;

            if(object instanceof Box){
                Box b = (Box) object;
                b.update(dt);
                if(b.isSetForDestruction()){
                    b.setDestroyed(true);
                    pleaseRemove = b;
                }
            }else if(object instanceof Potion){
                Potion b = (Potion) object;
                b.update(dt);
                if(b.isSetForDestruction()){
                    b.setDestroyed(true);
                    pleaseRemove = b;
                }
            }
            else if(object instanceof Switch){
                Switch s = (Switch) object;
                s.update(dt);
            }else if(object instanceof Door){
                Door s = (Door) object;
                s.update(dt);
            }else if(object instanceof Prop){
                Prop p = (Prop) object;
                p.update(dt);
            }

            removables.add(pleaseRemove);
        }

        //remove destroyed objects here
        for(Object obj : removables){
            sceneObjects.remove(obj);
        }
        removables.clear();
    }
    public static void render(float dt){

        for(int i = 0; i < sceneObjects.size(); i++){
            Object object = sceneObjects.get(i);

            if(object instanceof Box){
                Box b = (Box) object;
                b.render(batch, dt);
            }
            else if(object instanceof Switch){
                Switch s = (Switch) object;
                s.render(batch, dt);
            }else if(object instanceof Door){
                Door s = (Door) object;
                s.render(batch, dt);
            }else if(object instanceof Prop){
                Prop p = (Prop) object;
                p.render(batch, dt);
            }else if(object instanceof Ladder){
                Ladder p = (Ladder) object;
                p.render(batch, dt);
            }else if(object instanceof Potion){
                Potion b = (Potion) object;
                b.render(batch, dt);
            }else if(object instanceof Swings){
                Swings s = (Swings) object;
                s.render(batch,dt);
            }

        }
    }
    public static void renderWater(SpriteBatch batch, float dt, OrthographicCamera camera){
        for(Object o: sceneObjects){
            if(o instanceof Water){
                Water w = (Water)o;
                w.setCamera(camera);
                w.render(batch, dt);
            }
        }
    }
    public static void clearCachedObjects(){
        sceneObjects.clear();
    }


}
