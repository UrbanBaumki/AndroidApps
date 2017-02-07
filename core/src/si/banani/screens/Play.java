package si.banani.screens;

import com.badlogic.gdx.Gdx;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


import box2dLight.PointLight;
import box2dLight.RayHandler;
import si.banani.camera.CameraEffects;
import si.banani.camera.ParallaxCamera;
import si.banani.camera.Parallaxer;
import si.banani.controller.InputController;
import si.banani.controller.PlayerMovementController;
import si.banani.entities.CameraCoordinates;
import si.banani.entities.EntityFactory;
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;
import si.banani.entities.RockEnemy;
import si.banani.entities.SpiderEnemy;
import si.banani.entities.ZombieEnemy;
import si.banani.learning.LearningGdx;
import si.banani.maps.MapFactory;
import si.banani.maps.MapManager;
import si.banani.scene.Scene;
import si.banani.scenes.Hud;
import si.banani.serialization.Serializer;
import si.banani.shaders.DefaultShader;
import si.banani.shaders.ShaderFactory;
import si.banani.shaders.WaterShader;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.textures.TextureManager;
import si.banani.water.WaterHandler;
import si.banani.world.CollisionBits;
import si.banani.world.WorldCollideListener;
import si.banani.world.WorldContactListener;
import si.banani.world.WorldCreator;


/**
 * Created by Urban on 18.10.2016.
 */

public class Play extends BaseScreen {

    public static enum GameState{
        RUNNING,
        PAUSED,
        GAME_OVER,
        SAVING,
        LOADING,
        RESUME,
        RESET,
        RESTART
    }
    public static MapFactory.MapType nextMap = null;
    public static Integer nextLevel = null;
    public static boolean chapFinished = false;
    public static MapFactory.MapType mapToLoad;
    public static boolean switchToCutscene= false;
    public static boolean switchToDialogScreen = false;
    public static Integer cutsceneNumber;

    public static GameState gameState;

    private Array<AudioObserver> _observers = new Array<AudioObserver>();

    public static boolean reset = false;
    protected Hud hud;


    protected OrthogonalTiledMapRenderer mapRenderer;

    //box2d

    private Box2DDebugRenderer box2DDebugRenderer;
    private int levelW, levelH;



    //for player

    Player male;
    FemalePlayer female;

    ZombieEnemy e;
    SpiderEnemy s;


    //Parallax



    Texture  foreground;

    //Testing lights
    RayHandler handler;
    PointLight pointLight;

    //Final map manager
   protected MapManager mapManager;

    CameraCoordinates c;
    MapProperties properties;

    public Play(SpriteBatch spriteBatch, MapFactory.MapType typeToLoad) {
        super(spriteBatch);

        Scene.setSpriteBatch(batch);
        mapToLoad = typeToLoad;


        gameState = GameState.RUNNING;


        //the texture manager
        TextureManager.addAtlas("content.pack", "contentAtlas");
        TextureManager.splitAtlasIntoRegions();


        //the player hud
        this.hud = new Hud(this.batch);
        EntityFactory.giveHud(hud);
        mapManager = new MapManager();

        mapManager.setHud(hud);

        //properties
        properties = mapManager.getCurrentTiledMap().getProperties();
        levelW = properties.get("width", Integer.class);
        levelH = properties.get("height", Integer.class);

        camera = mapManager.getCurrentCamera();



        box2DDebugRenderer = new Box2DDebugRenderer();


        this.male = (Player)EntityFactory.getEntity(EntityFactory.EntityType.PLAYER);

        this.female = (FemalePlayer) EntityFactory.getEntity(EntityFactory.EntityType.FEMALE);


        foreground = new Texture(Gdx.files.internal("wood_fg.png"));




         c = new CameraCoordinates(male, female, mapManager.getCurrentCamera());
        hud.setCameraCoordinates(c);


        //s = new SpiderEnemy(world, 355, 150, 10, 28, BodyDef.BodyType.KinematicBody, TextureManager.getRegionByName("spiderEnemy").split(14,61)[0],  TextureManager.getRegionByName("spiderAttacking").split(23,61)[0] , 1/6f,1/3f, male);


        //controller
        this.inputController = new InputController(this.batch);

        PlayerMovementController.getInstance().addPlayer(this.male);
        PlayerMovementController.getInstance().addPlayer(this.female);


        //RayHandler.useDiffuseLight(true);


        //BOX2d lights
        handler = new RayHandler(mapManager.getCurrentWorld());

        handler.setAmbientLight(1f);
        pointLight = new PointLight(handler, 100, Color.WHITE, 128/LearningGdx.PPM, 0,0);
        pointLight.attachToBody(female.getBody());
        pointLight.setIgnoreAttachedBody(true);
        Filter f = new Filter();
        f.categoryBits = CollisionBits.SENSOR_BIT;
        f.maskBits =
                        CollisionBits.OBJECT_BIT |
                                CollisionBits.ENEMY_BIT|
                        CollisionBits.DEFAULT_BIT |
                        CollisionBits.PLAYER_BIT |
                                CollisionBits.DOORS_BIT;

        pointLight.setContactFilter(f);



        //to start with male
        CameraEffects.setTarget(male);
    }


    @Override
    public void show() {

        Serializer.getInstance().addObserver(mapManager);
        Serializer.getInstance().setMapTypeToLoad(mapToLoad);
        Serializer.getInstance().loadSaveGameAndProgress();
        //First method called when screen opened
        inputController.resetInputProcessor();

        if(mapRenderer == null){
            mapRenderer = new OrthogonalTiledMapRenderer(mapManager.getCurrentTiledMap(), 1/ LearningGdx.PPM );
            camera = mapManager.getCurrentCamera();
            EntityFactory.giveWorld(mapManager.getCurrentWorld());

        }

    }

    public void input(){


        //should be removed for finished game

        if( Gdx.input.isKeyPressed(Input.Keys.D)){
            PlayerMovementController.getInstance().movePlayerRigth(true);
        }else{
            PlayerMovementController.getInstance().movePlayerRigth(false);
        }

        if( Gdx.input.isKeyPressed(Input.Keys.A)){
            PlayerMovementController.getInstance().movePlayerLeft(true);
        }else{
            PlayerMovementController.getInstance().movePlayerLeft(false);
        }

        if( Gdx.input.isKeyPressed(Input.Keys.W)){
            PlayerMovementController.getInstance().movePlayerUp(true);
        }else{
            PlayerMovementController.getInstance().movePlayerUp(false);
        }
        if( Gdx.input.isKeyPressed(Input.Keys.S)){
            PlayerMovementController.getInstance().movePlayerDown(true);
        }else{
            PlayerMovementController.getInstance().movePlayerDown(false);
        }
        if( Gdx.input.isKeyJustPressed(Input.Keys.E)){
            PlayerMovementController.getInstance().doSwitch();
        }

        if( Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            PlayerMovementController.getInstance().switchPlayer();
        }



    }
    public void update(float delta){
        input();
        if(!running) return;

        mapManager.updateCurrentMap(delta, mapRenderer);


        //s.update(delta);

       //CAMERA UPDATE---
        CameraEffects.updateCamera(delta);

        float sX = mapManager.getCurrentCamera().viewportWidth/2;
        float sY =  mapManager.getCurrentCamera().viewportHeight/2;

        CameraEffects.boundCamera(sX, sY, levelW * 64 - sX *2, levelH * 64 - sY * 2);
        // ---- end camera update


        //point handler
        handler.setCombinedMatrix(mapManager.getCurrentCamera());


        if(nextMap != null && !switchToCutscene){
            mapManager.setFinished(chapFinished);
            chapFinished = false;
            Serializer.getInstance().saveGame();
            mapManager.loadMap(nextMap, nextLevel);
            nextMap = null;
            nextLevel = null;

            properties = mapManager.getCurrentTiledMap().getProperties();
            levelW = properties.get("width", Integer.class);
            levelH = properties.get("height", Integer.class);


            //we give both players to the map manager, so it could create SaveDescriptor

        }
    }

    @Override
    public void render(float delta) {

        if(gameState == GameState.GAME_OVER){

            hud.showGameOver();
            //hud.render(delta);
        }
        if(gameState == GameState.RESET){
            //reset the whole map
            MapFactory.MapType typ = mapManager.getCurrentMapType();
            int lvl = mapManager.getCurrentLevel();
            mapManager.loadMap(typ, lvl);
            mapManager.getMale().setMaxHealth(3);
            mapManager.getGhost().setEnergyLevel(100);
            gameState = GameState.RESUME;
        }

        if(gameState == GameState.RESUME){
            gameState = GameState.RUNNING;
            hud.hidePause();
            inputController.resetInputProcessor();
        }
        if(gameState == GameState.PAUSED){
            hud.showPause();
            //hud.render(delta);

        }
        if(gameState == GameState.RUNNING)
            update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(mapManager.hasMapChanged()){
            mapRenderer.setMap(mapManager.getCurrentTiledMap());
            camera = mapManager.getCurrentCamera();
            mapRenderer.setView(camera);

            mapManager.set_mapChanged(false);

            //light render
            handler.setCombinedMatrix(camera);
            handler.setWorld(mapManager.getCurrentWorld());

            pointLight.attachToBody(EntityFactory.getEntity(EntityFactory.EntityType.FEMALE).getBody());

            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }



        //RENDERING THE MAP -- map takes care of render order, since it is map-specific
        mapManager.renderCurrentMap(batch, delta, mapRenderer);


        //female render with her light
        handler.updateAndRender();
        batch.begin();
        batch.setProjectionMatrix(mapManager.getCurrentCamera().combined);
        EntityFactory.getEntity(EntityFactory.EntityType.FEMALE).render(batch,delta);

        batch.end();





        //sets the correct projection matrix for hud
        batch.setProjectionMatrix(this.hud.stage.getCamera().combined);

        ShaderFactory.getShader(ShaderFactory.ShaderType.DEFAULT_SHADER).render(batch,camera,delta);
        hud.render(delta);

        this.inputController.draw();

        //debuger
       box2DDebugRenderer.render(mapManager.getCurrentWorld(), camera.combined);
        if(switchToCutscene){
            switchToCutscene = false;
            ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.CUTSCENE, batch, cutsceneNumber);
            return;
        }else if(switchToDialogScreen){
            switchToDialogScreen = false;
            ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.DIALOG, batch, cutsceneNumber, nextMap);
            return;
        }

    }
    public static void setRunning(boolean b){ running = b; }

    @Override
    public void dispose() {

        //TextureManager.disposeAll();
        //handler.dispose();
        mapManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        inputController.resize(width,height);
        hud.resize(width,height);
        mapManager.getCurrentViewport().update(width, height);
        int gutterW = mapManager.getCurrentViewport().getLeftGutterWidth();
        int gutterH = mapManager.getCurrentViewport().getTopGutterHeight();
        int rhWidth = width - (2 * gutterW);
        int rhHeight = height - (2 * gutterH);
        handler.useCustomViewport(gutterW, gutterH, rhWidth , rhHeight);

    }

    @Override
    public void pause() {
        if(gameState != GameState.GAME_OVER)
            Serializer.getInstance().saveGame();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

        if(gameState != GameState.GAME_OVER)
            Serializer.getInstance().saveGame();
    }




}
