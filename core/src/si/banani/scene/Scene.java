package si.banani.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import si.banani.tiles.Box;
import si.banani.tiles.Door;
import si.banani.tiles.Switch;

/**
 * Created by Urban on 9.12.2016.
 */

public class Scene {

    public static ArrayList<Object> sceneObjects = new ArrayList<Object>();
    private static SpriteBatch batch;
    private static World world;

    public static void setSpriteBatch(SpriteBatch b){
        batch = b;
    }

    public static void addObjectToScene(Object o ){
        sceneObjects.add(o);
    }
    public static void setWorld(World w){
        world = w;
    }
    public static void update(float dt){

        for(int i = 0; i < sceneObjects.size(); i++){
            Object object = sceneObjects.get(i);

            if(object instanceof Box){
                Box b = (Box) object;
                b.update(dt);
                if(b.isSetForDestruction()){
                    b.setDestroyed(true);
                }
            }
            else if(object instanceof Switch){
                Switch s = (Switch) object;
                s.update(dt);
            }else if(object instanceof Door){
                Door s = (Door) object;
                s.update(dt);
            }
        }
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
            }

        }
    }

}
