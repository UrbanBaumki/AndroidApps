package si.banani.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.camera.CameraEffects;
import si.banani.entities.EntityFactory;
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;
import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.world.WorldCollideListener;

/**
 * Created by Urban on 28.1.2017.
 */

public class MapManager {

    private Map _currentMap;
    private boolean _mapChanged = false;
    private OrthographicCamera currCamera;

    private Player male;
    private FemalePlayer ghost;

    public MapManager(){

    }

    public void loadMap(MapFactory.MapType mapType){
        EntityFactory.clearEntities();
        MapFactory.clearCurrentWorld();
        Map map = MapFactory.getMap(mapType);

        if(map == null)
            return;

        if(_currentMap != null){
            _currentMap.unloadMusic();
            clearCurrentMap();

        }

        map.loadMusic();

        _currentMap = map;
        currCamera = _currentMap.getCamera();

        _mapChanged = true;

        CameraEffects.setCamera(currCamera);

        CameraEffects.setZooming(true);



    }
    public void renderCurrentMap(SpriteBatch batch, float dt, OrthogonalTiledMapRenderer mapRenderer){
        if(_currentMap != null)
            _currentMap.render(batch, dt, mapRenderer);
    }
    public void renderCurrentMapsBg(SpriteBatch batch, float dt){
        if(_currentMap != null)
            _currentMap.renderBackground(batch, dt);
    }
    public void updateCurrentMap(float dt, OrthogonalTiledMapRenderer mapRenderer){
        if(_currentMap != null)
            _currentMap.update(dt, mapRenderer);
    }
    public void clearCurrentMap(){
        _currentMap.dispose();
    }

    public MapFactory.MapType getCurrentMapType(){ return _currentMap.mapType; }

    public void disableCurrentMapMusic(){
        _currentMap.unloadMusic();
    }
    public void enableCurrentMapMusic(){
        _currentMap.loadMusic();
    }
    public TiledMap getCurrentTiledMap(){
        if( _currentMap == null ) {
            loadMap(MapFactory.MapType.CHAPTER5);
        }
        return _currentMap.get_currentMap();
    }
    public boolean hasMapChanged(){
        return _mapChanged;
    }
    public void set_mapChanged(boolean change){
        this._mapChanged = change;
    }
    public void setMale(Player p){
        this.male = p;
    }
    public void setGhost(FemalePlayer g){
        this.ghost = g;
    }
    public OrthographicCamera getCurrentCamera(){
        return currCamera;
    }
    public WorldCollideListener getCurrentOverlaper(){
        return _currentMap.getOverlaper();
    }
    public World getCurrentWorld(){
        return _currentMap.getWorld();
    }
    public Viewport getCurrentViewport(){
        return _currentMap.viewport;
    }
}
