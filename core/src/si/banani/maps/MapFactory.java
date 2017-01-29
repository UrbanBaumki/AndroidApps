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
        CHAPTER4,
        CHAPTER5
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
            case CHAPTER3:
                map = _maps.get(MapType.CHAPTER3);
                if(map == null){
                    map = new SadMap();
                    _maps.put(MapType.CHAPTER3, map);
                }
                break;

            case CHAPTER5:
                map = _maps.get(MapType.CHAPTER5);
                if(map == null){
                    map = new DrownMap();
                    _maps.put(MapType.CHAPTER5, map);
                }
                break;

        }


        return map;
    }

}
