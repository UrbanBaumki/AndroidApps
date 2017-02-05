package si.banani.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.camera.CameraEffects;
import si.banani.entities.CameraCoordinates;
import si.banani.entities.EnemyManager;
import si.banani.entities.EntityFactory;
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;
import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.scenes.Hud;
import si.banani.serialization.ProfileObserver;
import si.banani.serialization.SaveGameDescriptor;
import si.banani.serialization.Serializer;
import si.banani.world.WorldCollideListener;

/**
 * Created by Urban on 28.1.2017.
 */

public class MapManager implements ProfileObserver{

    private Map _currentMap;
    private boolean _mapChanged = false;
    private OrthographicCamera currCamera;

    private Player male;
    private FemalePlayer ghost;
    private Hud hud;

    public MapManager(){

    }

    public void dispose(){
        EnemyManager.getInstance().clearCachedEnemies();
        EntityFactory.clearEntities();
        MapFactory.clearCurrentWorld();
        if(_currentMap != null){
            _currentMap.unloadMusic();
            clearCurrentMap();
        }
    }
    public void loadMap(MapFactory.MapType mapType){
        EntityFactory.clearEntities();
        MapFactory.clearCurrentWorld();
        EnemyManager.getInstance().clearCachedEnemies();
        if(_currentMap != null){
            _currentMap.unloadMusic();
            clearCurrentMap();
        }
        Map map = MapFactory.getMap(mapType);

        if(map == null)
            return;



        map.loadMusic();

        _currentMap = map;
        currCamera = _currentMap.getCamera();

        _mapChanged = true;

        CameraEffects.setCamera(currCamera);

        CameraEffects.setZooming(true);

        setMale((Player) EntityFactory.getEntity(EntityFactory.EntityType.PLAYER));
        setGhost((FemalePlayer) EntityFactory.getEntity(EntityFactory.EntityType.FEMALE));

    }
    public void renderCurrentMap(SpriteBatch batch, float dt, OrthogonalTiledMapRenderer mapRenderer){
        if(_currentMap != null)
            _currentMap.render(batch, dt, mapRenderer);
    }
    public void renderCurrentMapForCutscene(SpriteBatch batch, float dt, OrthogonalTiledMapRenderer mapRenderer){
        if(_currentMap != null)
            _currentMap.renderForCutscene(batch, dt, mapRenderer);
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

    @Override
    public void onNotify(Serializer profileManager, ProfileEvent event) {
        switch (event){
            case PROFILE_LOADED:
                SaveGameDescriptor loadedSave = profileManager.getCurrentSave();
                if(loadedSave == null)
                    loadMap(MapFactory.MapType.CHAPTER5);
                else {
                    loadMap(MapFactory.MapType.valueOf(loadedSave.getMapType()));
                    setMale((Player) EntityFactory.getEntity(EntityFactory.EntityType.PLAYER));
                    setGhost((FemalePlayer) EntityFactory.getEntity(EntityFactory.EntityType.FEMALE));
                    male.setTransform(loadedSave.getLastX(), loadedSave.getLastY(), 0);
                    ghost.setTransform(loadedSave.getLastX() - 0.2f, loadedSave.getLastY(), 0);
                    ghost.setEnergyLevel(loadedSave.getLastGhostEnergy());
                    CameraCoordinates c = new CameraCoordinates(male, ghost, currCamera);
                    hud.setCameraCoordinates(c);
                }
                break;
            case SAVING_PROFILE:
                SaveGameDescriptor curSave = new SaveGameDescriptor();
                curSave.setPlayerHealth(male.getHealth());
                curSave.setNumFinishedChapter(1);
                curSave.setMapType(getCurrentMapType().toString());
                curSave.setLastGhostEnergy(ghost.getEnergyLevel());
                curSave.setLastX(male.getLastCheckpointX());
                curSave.setLastY(male.getLastCheckpointY());
                profileManager.setCurrentSave(curSave);
                break;
        }
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }
}
