package si.banani.maps;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Hashtable;


/**
 * Created by Urban on 28.1.2017.
 */

public class MapFactory {

    private static Hashtable<MapType, Map> _maps = new Hashtable<MapType, Map>();

    private static World world ;
    public static enum MapType{
        CHAPTER1,
        CHAPTER2,
        CHAPTER3,
        CHAPTER4,
        CHAPTER5
    }

    public static Map getMap(MapType mapType){
        Map map = null;

        world = new World(new Vector2(0,-10), true);
        switch (mapType){
            case CHAPTER1:
                map = _maps.get(MapType.CHAPTER1);
                if(map == null){
                    map = new LoneMap(world);
                    _maps.put(MapType.CHAPTER1, map);
                }
                break;
            case CHAPTER3:
                map = _maps.get(MapType.CHAPTER3);
                if(map == null){
                    map = new SadMap(world);
                    _maps.put(MapType.CHAPTER3, map);
                }
                break;

            case CHAPTER5:
                map = _maps.get(MapType.CHAPTER5);
                if(map == null){
                    map = new DrownMap(world);
                    _maps.put(MapType.CHAPTER5, map);
                }
                break;

        }


        return map;
    }
    public static void remove(MapType mapType){
        _maps.remove(mapType);
    }
    public static void setWorld(World w){
        world = w;
    }
}
