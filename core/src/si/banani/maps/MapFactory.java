package si.banani.maps;

import java.util.Hashtable;


/**
 * Created by Urban on 28.1.2017.
 */

public class MapFactory {

    private static Hashtable<MapType, Map> _maps = new Hashtable<MapType, Map>();

    public static enum MapType{
        CHAPTER1,
        CHAPTER2,
        CHAPTER3,
        CHAPTER4
    }

    public static Map getMap(MapType mapType){
        Map map = null;

        switch (mapType){
            case CHAPTER1:
                map = _maps.get(MapType.CHAPTER1);
                if(map == null){
                    map = new LoneMap();
                    _maps.put(MapType.CHAPTER1, map);
                }
                break;


        }


        return map;
    }

}
