package si.banani.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.Hashtable;

import si.banani.learning.LearningGdx;
import si.banani.sound.Utility;

/**
 * Created by Urban on 24.1.2017.
 */

public class MapManager {

    private static final String TAG =
            MapManager.class.getSimpleName();

    private Hashtable<String, String> _mapPaths;
    private Hashtable<String, Vector2> _mapStartLocation;

    //predefined map names
    public final static String CH1 = "Loneliness";
    public final static String CH2 = "FEAR";

    //layers
    private final static String MAP_COLLISION_LAYER = "COLLISION_LAYER";
    private final static String MAP_SPAWN_LAYER = "SPAWN_LAYER";

    private final static String PLAYER_START = "PLAYER_START";
    private Vector2 _playerStartPositionRect;
    private Vector2 _closestPlayerStartPosition;
    private Vector2 _convertedUnits;

    private Vector2 _playerStart;
    private TiledMap _currentMap = null;

    private String _currentMapName;
    private MapLayer _collisionLayer = null;
    private MapLayer _spawnsLayer = null;

    public final static float UNIT_SCALE = LearningGdx.PPM;

    public MapManager(){
        _playerStart = new Vector2(0,0);
        _mapPaths = new Hashtable();
        _mapPaths.put(CH1, "map.tmx");
        //_mapPaths.put(CH2, "");
        //_mapPaths.put(CASTLE_OF_DOOM, “maps/castle_of_doom.tmx”);
        _mapStartLocation = new Hashtable();
        _mapStartLocation.put(CH1, _playerStart.cpy());

        _playerStartPositionRect = new Vector2(0,0);
        _closestPlayerStartPosition = new Vector2(0,0);
        _convertedUnits = new Vector2(0,0);
    }

    public void loadMap(String mapName){
        _playerStart.set(0,0);
        String mapFullPath = _mapPaths.get(mapName);
        if( mapFullPath == null || mapFullPath.equals("") ) {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }
        if( _currentMap != null ){
            _currentMap.dispose();
        }
        Utility.loadMapAsset(mapFullPath);
        if( Utility.isAssetLoaded(mapFullPath) ) {
            _currentMap = Utility.getMapAsset(mapFullPath);
            _currentMapName = mapName;
        }else{
            Gdx.app.debug(TAG, "Map not loaded");
            return;
        }
        _collisionLayer =
                _currentMap.getLayers().get(MAP_COLLISION_LAYER);
        if( _collisionLayer == null ){
            Gdx.app.debug(TAG, "No collision layer!");
        }

        _spawnsLayer =
                _currentMap.getLayers().get(MAP_SPAWN_LAYER);
        if( _spawnsLayer == null ){
            Gdx.app.debug(TAG, "No spawn layer!");
        }else{
            Vector2 start =
                    _mapStartLocation.get(_currentMapName);
            if( start.isZero() ){
                setClosestStartPosition(_playerStart);
                start = _mapStartLocation.get(_currentMapName);
            }
            _playerStart.set(start.x, start.y);
        }
        Gdx.app.debug(TAG, "Player Start: (" + _playerStart.x + ","
        + _playerStart.y + ")");
    }

    public TiledMap getCurrentMap(){
        if( _currentMap == null ) {
            _currentMapName = CH1;
            loadMap(_currentMapName);
        }
        return _currentMap;
    }
    public MapLayer getCollisionLayer(){
        return _collisionLayer;
    }

    public Vector2 getPlayerStartUnitScaled(){
        Vector2 playerStart = _playerStart.cpy();
        playerStart.set(_playerStart.x / UNIT_SCALE, _playerStart.y
                / UNIT_SCALE);
        return playerStart;
    }

    private void setClosestStartPosition(final Vector2 position){
        //Get last known position on this map
        _playerStartPositionRect.set(0,0);
        _closestPlayerStartPosition.set(0,0);
        float shortestDistance = 0f;
        //Go through all player start positions and choose closest to
        //last known position
        for( MapObject object: _spawnsLayer.getObjects()){
            if( object.getName().equalsIgnoreCase(PLAYER_START) ){

                ((RectangleMapObject)object).getRectangle().getPosition(_playerStartPositionRect);
                float distance = position.dst2(_playerStartPositionRect);
                if( distance < shortestDistance ||
                        shortestDistance == 0 ){
                    _closestPlayerStartPosition.set(
                            _playerStartPositionRect);
                    shortestDistance = distance;
                }
            }
        }
        _mapStartLocation.put(
                _currentMapName, _closestPlayerStartPosition.cpy());
    }
    public MapLayer getMapLayer(String layerName){
        MapLayer layer = null;
        if(_currentMap != null)
            layer = _currentMap.getLayers().get(layerName);

        return layer;
    }
    public void setClosestStartPositionFromScaledUnits(
            Vector2 position){
        if( UNIT_SCALE <= 0 )
            return;
        _convertedUnits.set(position.x*UNIT_SCALE,
                position.y*UNIT_SCALE);
        setClosestStartPosition(_convertedUnits);
    }
}


