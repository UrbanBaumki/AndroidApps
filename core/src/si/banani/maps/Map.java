package si.banani.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.camera.Parallaxer;
import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.sound.Utility;
import si.banani.world.WorldCollideListener;
import si.banani.world.WorldCreator;

/**
 * Created by Urban on 28.1.2017.
 */

public abstract class Map implements AudioSubject{

    private Array<AudioObserver> _observers;

    protected final static String CHECKPOINT_LAYER = "Checkpoints";
    protected final static String NEXT_LEVEL_LAYER = "Next";

    protected Json _json;

    protected TiledMap _currentMap = null;
    protected Vector2 _playerStart;

    protected World world;
    protected WorldCreator worldCreator;

    protected WorldCollideListener overlaper;

    protected Parallaxer parallaxer;

    protected MapLayer _checkPointLayer = null;
    public MapFactory.MapType mapType;

    protected OrthographicCamera camera;
    protected Viewport viewport;

    protected SpriteBatch batch = LearningGdx.getSpriteBatch();

    public Map(MapFactory.MapType mapType, String mapPath, World world){
        _observers = new Array<AudioObserver>();
        this.mapType = mapType;
        _playerStart = new Vector2(0,0);
        this.world = world;

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(LearningGdx.V_WIDTH / LearningGdx.PPM, LearningGdx.V_HEIGHT / LearningGdx.PPM, camera);

        Utility.loadMapAsset(mapPath);
        if(Utility.isAssetLoaded(mapPath)){
            _currentMap = Utility.getMapAsset(mapPath);
        }

        _checkPointLayer = _currentMap.getLayers().get(CHECKPOINT_LAYER);

        addObserver(AudioManager.getInstance());
    }
    abstract public void update(float dt);
    abstract public void render(SpriteBatch batch, float dt);
    abstract public void renderBackground(SpriteBatch batch, float dt);
    abstract public void unloadMusic();
    abstract public void loadMusic();

    public World getWorld(){ return this.world; }

    @Override
    public void addObserver(AudioObserver audioObserver) {
        _observers.add(audioObserver);
    }

    @Override
    public void removeObserver(AudioObserver audioObserver) {
        _observers.removeValue(audioObserver, true);
    }

    @Override
    public void removeAllObservers() {
        _observers.removeAll(_observers, true);
    }

    @Override
    public void notify(AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event) {
        for(AudioObserver observer: _observers){
            observer.onNotify(command, event);
        }
    }
    public TiledMap get_currentMap(){
        return this._currentMap;
    }
    public OrthographicCamera getCamera(){
        return this.camera;
    }
    public WorldCollideListener getOverlaper(){
        return overlaper;
    }
    public void dispose(){

        MapFactory.remove(mapType);
    }
}
