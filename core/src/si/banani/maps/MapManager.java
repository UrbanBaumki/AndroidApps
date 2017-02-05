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
import si.banani.serialization.Chapter;
import si.banani.serialization.ChapterDescriptor;
import si.banani.serialization.ProfileObserver;
import si.banani.serialization.ProgressDescriptor;
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



    private int currentLevel;
    private MapFactory.MapType currentMapType;

    private ChapterDescriptor chapterDescriptor;
    private Chapter chapter;
    private boolean finished;

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
    public void loadMap(MapFactory.MapType mapType, int level){
        EntityFactory.clearEntities();
        MapFactory.clearCurrentWorld();
        EnemyManager.getInstance().clearCachedEnemies();
        if(_currentMap != null){
            _currentMap.unloadMusic();
            finished = _currentMap.isChapterFinished();
            clearCurrentMap();
        }
        Map map = MapFactory.getMap(mapType, level);

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

        currentMapType = _currentMap.mapType;
        currentLevel = _currentMap.getLevel();

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
            loadMap(MapFactory.MapType.CHAPTER5, currentLevel);
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
                ProgressDescriptor progressDescriptor = profileManager.getCurrentProgress();

                MapFactory.MapType mt = profileManager.getMapTypeToLoad();
                Chapter chap = profileManager.getChapter(mt.toString());

                if(chap == null){
                    chap = new Chapter();
                    chap.setLastGhostEnergy(100);
                    chap.setLastPlayerHealth(3);
                    chap.setLastX(1);
                    chap.setLastY(5);
                    chap.setFinished(false);
                    chap.setLastLevelPlayed(0);
                    currentLevel = 0;
                    currentMapType = mt;
                    profileManager.addChapter(currentMapType.toString(), chap);
                }else{
                    currentMapType = mt;
                    currentLevel = chap.getLastLevelPlayed();

                }
                loadMap(currentMapType, currentLevel);


                setMale((Player) EntityFactory.getEntity(EntityFactory.EntityType.PLAYER));
                setGhost((FemalePlayer) EntityFactory.getEntity(EntityFactory.EntityType.FEMALE));
                male.setTransform(chap.getLastX(), chap.getLastY(), 0);
                ghost.setTransform(chap.getLastX() - 0.2f, chap.getLastY(), 0);
                ghost.setEnergyLevel(chap.getLastGhostEnergy());
                //missing loading player health
                CameraCoordinates c = new CameraCoordinates(male, ghost, currCamera);
                hud.setCameraCoordinates(c);

                break;
            case SAVING_PROFILE:

                profileManager.getChapter(currentMapType.toString()).setLastPlayerHealth(male.getHealth());
                profileManager.getChapter(currentMapType.toString()).setLastGhostEnergy(ghost.getEnergyLevel());
                profileManager.getChapter(currentMapType.toString()).setLastX(male.getLastCheckpointX());
                profileManager.getChapter(currentMapType.toString()).setLastY(male.getLastCheckpointY());
                profileManager.getChapter(currentMapType.toString()).setLastLevelPlayed(currentLevel);
                profileManager.getChapter(currentMapType.toString()).setFinished(finished);

                break;
        }
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
}
