package si.banani.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.camera.CameraEffects;
import si.banani.camera.Parallaxer;
import si.banani.controller.PlayerMovementController;
import si.banani.entities.EnemyManager;
import si.banani.entities.EntityFactory;
import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.shaders.OpacityShader;
import si.banani.shaders.ShaderFactory;
import si.banani.shaders.WaterShader;
import si.banani.sound.AudioObserver;
import si.banani.tiles.Tiles;
import si.banani.water.WaterHandler;
import si.banani.world.WorldCollideListener;
import si.banani.world.WorldContactListener;
import si.banani.world.WorldCreator;

/**
 * Created by Urban on 28.1.2017.
 */

public class LoneMap extends Map {

    private static String _mapPath [] = {"maps/ch1/ch1_lvl1.tmx"};

    public LoneMap(World world, int level) {
        super(MapFactory.MapType.CHAPTER1, _mapPath[level], world);


        super.level = level;
        overlaper = new WorldCollideListener(world);


        worldCreator = new WorldCreator(world, _currentMap);
        parallaxer = new Parallaxer(camera);
        parallaxer.addTexture("textures/ch1/wood_bg.png",-LearningGdx.V_WIDTH/2, 0, LearningGdx.V_WIDTH * 1.2f, LearningGdx.V_HEIGHT * 1.2f, 0.4f, 0.4f);

        world.setContactListener(new WorldContactListener());

        EntityFactory.giveCamera(camera);

        EntityFactory.giveWorld(world);

        Scene.clearCachedObjects();

        //map specifics
        worldCreator.createTileFixtures("Floor", Tiles.FLOOR);
        worldCreator.createTileFixtures("Spikes", Tiles.SPIKES);
        worldCreator.createTileFixtures("Boxes", Tiles.BOX);

        worldCreator.createTileFixtures("Switches", Tiles.SWITCHES);
        worldCreator.createTileFixtures("Doors", Tiles.DOORS);
        worldCreator.createTileFixtures("GhostPath", Tiles.GHOST_PATH);
        worldCreator.createTileFixtures("Props", Tiles.PROPS);
        worldCreator.createTileFixtures("Swings", Tiles.SWINGS);
        worldCreator.createTileFixtures("Ladders", Tiles.LADDERS);
        worldCreator.createTileFixtures("Dialogs", Tiles.DIALOG);
        worldCreator.createTileFixtures("Potions", Tiles.POTION);
        worldCreator.createTileFixtures("Checkpoints", Tiles.CHECKPOINT);
        worldCreator.createTileFixtures("Cutscene", Tiles.CUTSCENE);

        worldCreator.createTileFixtures("Start", Tiles.START);
        worldCreator.createTileFixtures("End", Tiles.END);
        worldCreator.createTileFixtures("Enemies", Tiles.ENEMIES);
        worldCreator.createTileFixtures("Water", Tiles.WATER);
        worldCreator.createTileFixtures("Platforms", Tiles.PLATFORM);
        worldCreator.createTileFixtures("Bridge", Tiles.GHOSTBRIDGE);

        CameraEffects.setTarget(EntityFactory.getEntity(EntityFactory.EntityType.PLAYER));
        PlayerMovementController.getInstance().clearPlayers();
        PlayerMovementController.getInstance().addPlayer(EntityFactory.getEntity(EntityFactory.EntityType.PLAYER));
        PlayerMovementController.getInstance().addPlayer(EntityFactory.getEntity(EntityFactory.EntityType.FEMALE));
        PlayerMovementController.getInstance().setPlayer(0);

        WaterHandler.getInstance().clearPairs();


    }

    @Override
    public void update(float dt, OrthogonalTiledMapRenderer mapRenderer) {
        world.step(1/60f, 6, 2);
        overlaper.update();

        //WaterHandler.getInstance().update();

        mapRenderer.setView(camera);

        EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).update(dt);
        EntityFactory.getEntity(EntityFactory.EntityType.FEMALE).update(dt);

        //Player's reset if out fallen of the map. It decreases life aswell
        if(EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).getY() < 0){
            EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).setReset(true);
        }
        if(EntityFactory.getEntity(EntityFactory.EntityType.FEMALE).getY() < 0){
            EntityFactory.getEntity(EntityFactory.EntityType.FEMALE).setReset(true);
        }

        //we update objects and enemies
        Scene.update(dt);
        EnemyManager.getInstance().updateEnemies(dt,  EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).getPosition().x);




    }

    @Override
    public void render(SpriteBatch batch, float dt , OrthogonalTiledMapRenderer mapRenderer) {

        //here we render everything, but each map renders in specific order or specific shader
        //maps parallaxed bg texture
        renderBackground(batch, dt);

        //tiled map background images
        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
        mapRenderer.getBatch().begin();
        mapRenderer.renderTileLayer((TiledMapTileLayer)_currentMap.getLayers().get("Bg"));
        mapRenderer.getBatch().end();

        batch.setProjectionMatrix(camera.combined);
        //render the map also
        //mapRenderer.render(bg);
        batch.begin();

        //render the scene with objects
        Scene.render(dt);

        (EntityFactory.getEntity(EntityFactory.EntityType.PLAYER)).render(batch, dt);

        //e.render(batch, delta);
        EnemyManager.getInstance().renderEnemies(batch, dt);
        //s.render(batch, delta);

        batch.end();

        //Scene.renderWater(batch,dt, camera);


        //maps foreground or main layer
        mapRenderer.getBatch().begin();
        mapRenderer.renderTileLayer((TiledMapTileLayer)_currentMap.getLayers().get("Fg"));
        mapRenderer.getBatch().end();

        if(PlayerMovementController.getInstance().getCurrent_player() == 1)
        {
            ((OpacityShader)ShaderFactory.getShader(ShaderFactory.ShaderType.OPACITY_SHADER)).setDirection(1);
            Scene.showBridge(true);

        }else{
            Scene.showBridge(false);
            ((OpacityShader)ShaderFactory.getShader(ShaderFactory.ShaderType.OPACITY_SHADER)).setDirection(-1);

        }

        ShaderFactory.getShader(ShaderFactory.ShaderType.OPACITY_SHADER).render(batch, camera, dt);
        mapRenderer.getBatch().begin();
        mapRenderer.getBatch().setShader((ShaderFactory.getShader(ShaderFactory.ShaderType.OPACITY_SHADER)).getShaderProgram());

        mapRenderer.renderTileLayer((TiledMapTileLayer)_currentMap.getLayers().get("Paths"));
        mapRenderer.getBatch().end();

        mapRenderer.getBatch().setShader(  (ShaderFactory.getShader(ShaderFactory.ShaderType.DEFAULT_SHADER)).getShaderProgram());


    }

    @Override
    public void renderForCutscene(SpriteBatch batch, float dt, OrthogonalTiledMapRenderer mapRenderer) {
        mapRenderer.setView(camera);
        //maps parallaxed bg texture
        renderBackground(batch, dt);

        //tiled map background images
        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
        mapRenderer.getBatch().begin();
        mapRenderer.renderTileLayer((TiledMapTileLayer)_currentMap.getLayers().get("Bg"));
        mapRenderer.getBatch().end();

        batch.setProjectionMatrix(camera.combined);


        //maps foreground or main layer
        mapRenderer.getBatch().begin();
        mapRenderer.renderTileLayer((TiledMapTileLayer)_currentMap.getLayers().get("Fg"));
        mapRenderer.getBatch().end();
    }

    public void renderBackground(SpriteBatch batch, float dt){
        //bg
        parallaxer.render(batch);
    }

    @Override
    public void unloadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_ONE);
    }

    @Override
    public void loadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_ONE);
        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_ONE);
    }
}
