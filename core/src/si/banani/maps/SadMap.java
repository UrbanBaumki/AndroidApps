package si.banani.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.camera.CameraEffects;
import si.banani.camera.Parallaxer;
import si.banani.controller.PlayerMovementController;
import si.banani.entities.EntityFactory;
import si.banani.entities.Player;
import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.shaders.OpacityShader;
import si.banani.shaders.ShaderFactory;
import si.banani.shaders.WaterShader;
import si.banani.sound.AudioObserver;
import si.banani.tiles.Tiles;
import si.banani.world.WorldCollideListener;
import si.banani.world.WorldContactListener;
import si.banani.world.WorldCreator;

/**
 * Created by Urban on 29.1.2017.
 */

public class SadMap extends Map {


    private static String _mapPath[] = {"maps/ch3/ch3_lvl0.tmx", "maps/ch3/ch3_lvl1.tmx"};
    private Texture [] rain = {new Texture(Gdx.files.internal("textures/ch3/rain1.png")),new Texture(Gdx.files.internal("textures/ch3/rain2.png")), new Texture(Gdx.files.internal("textures/ch3/rain3.png")), new Texture(Gdx.files.internal("textures/ch3/rain4.png"))};
    private float rainTimer = 0f;
    private float rainAnimate=0.1f;
    private int currRain = 0;
    public SadMap(World world, int level) {
        super(MapFactory.MapType.CHAPTER3, _mapPath[level], world);
        super.level = level;

        overlaper = new WorldCollideListener(world);


        worldCreator = new WorldCreator(world, _currentMap);
        parallaxer = new Parallaxer(camera);
        parallaxer.addTexture("textures/ch3/ch3_bg.png",-LearningGdx.V_WIDTH/2, 0, LearningGdx.V_WIDTH * 1.2f, LearningGdx.V_HEIGHT * 1.2f, 0.4f, 0.4f);

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

        CameraEffects.setTarget(EntityFactory.getEntity(EntityFactory.EntityType.PLAYER));
        PlayerMovementController.getInstance().clearPlayers();
        PlayerMovementController.getInstance().addPlayer(EntityFactory.getEntity(EntityFactory.EntityType.PLAYER));
        PlayerMovementController.getInstance().addPlayer(EntityFactory.getEntity(EntityFactory.EntityType.FEMALE));
        PlayerMovementController.getInstance().setPlayer(0);


    }

    @Override
    public void update(float dt, OrthogonalTiledMapRenderer mapRenderer) {

        //each map defines its own update method, so we get same/similar or totally different behaviour
        //based on the map needs.
        world.step(1/60f, 6, 2);
        overlaper.update();

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

        Scene.update(dt);

    }

    @Override
    public void render(SpriteBatch batch, float dt, OrthogonalTiledMapRenderer mapRenderer) {

        //here we render everything, but each map renders in specific order or specific shader

        ShaderFactory.getShader(ShaderFactory.ShaderType.BLACK_AND_WHITE_SHADER).render(batch, camera, dt);
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
        //s.render(batch, delta);

        batch.end();


        mapRenderer.getBatch().begin();
        mapRenderer.renderTileLayer((TiledMapTileLayer)_currentMap.getLayers().get("Fg"));
        mapRenderer.getBatch().end();

        if(PlayerMovementController.getInstance().getCurrent_player() == 1)
        {
            ((OpacityShader)ShaderFactory.getShader(ShaderFactory.ShaderType.OPACITY_SHADER)).setDirection(1);

        }else{
            ((OpacityShader)ShaderFactory.getShader(ShaderFactory.ShaderType.OPACITY_SHADER)).setDirection(-1);

        }

        ShaderFactory.getShader(ShaderFactory.ShaderType.OPACITY_SHADER).render(batch, camera, dt);
        mapRenderer.getBatch().begin();
        mapRenderer.getBatch().setShader((ShaderFactory.getShader(ShaderFactory.ShaderType.OPACITY_SHADER)).getShaderProgram());

        mapRenderer.renderTileLayer((TiledMapTileLayer)_currentMap.getLayers().get("Paths"));
        mapRenderer.getBatch().end();

        mapRenderer.getBatch().setShader(  (ShaderFactory.getShader(ShaderFactory.ShaderType.BLACK_AND_WHITE_SHADER)).getShaderProgram());


        //drawing the rain
        rainTimer += dt;
        if(rainTimer >= rainAnimate)
        {
            rainTimer -= rainAnimate;
            currRain = (currRain+1)% rain.length;
        }

        batch.begin();
        batch.draw(rain[currRain], camera.position.x - rain[currRain].getWidth()/2/LearningGdx.PPM,camera.position.y - rain[currRain].getHeight()/2/LearningGdx.PPM, rain[currRain].getWidth()/LearningGdx.PPM, rain[currRain].getHeight()/LearningGdx.PPM);
        batch.end();
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
        notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_TWO);
    }

    @Override
    public void loadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_TWO);
        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_TWO);
    }
}
