package si.banani.screens;

import com.badlogic.gdx.Gdx;


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
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;
import si.banani.entities.RockEnemy;
import si.banani.entities.SpiderEnemy;
import si.banani.learning.LearningGdx;
import si.banani.maps.MapFactory;
import si.banani.maps.MapManager;
import si.banani.scene.Scene;
import si.banani.scenes.Hud;
import si.banani.shaders.DefaultShader;
import si.banani.shaders.ShaderFactory;
import si.banani.shaders.WaterShader;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.textures.TextureManager;
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
        LOADING
    }

    private static GameState gameState;

    private Array<AudioObserver> _observers = new Array<AudioObserver>();

    public static boolean reset = false;
    private Hud hud;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //box2d
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private int levelW, levelH;

    //my fixture creator
    private WorldCreator worldCreator;

    //for player

    Player male;
    FemalePlayer female;

    RockEnemy e;
    SpiderEnemy s;

    int [] fg = {1,2,3,4,5,6};
    int [] paths = {10};


    //Parallax

    ParallaxCamera parallaxCameraFG;

    Texture  foreground;
    Parallaxer parallaxer;
    int [] parallaxBgIndex = {0};
    int [] parallaxFgIndex = {1};

    //Testing lights
    RayHandler handler;
    PointLight pointLight;

    //overlaping
    WorldCollideListener overlaper;


    //Final map manager
    MapManager mapManager;

    public Play(SpriteBatch spriteBatch) {
        super(spriteBatch);

        Scene.setSpriteBatch(batch);

        mapManager = new MapManager();

        gameState = GameState.RUNNING;

        //MUSIC OBSERVER AND LOADING
        //this.addObserver(AudioManager.getInstance());
        //loadMusic();

        //MAP LOADING
        //the texture manager

        TextureManager.addAtlas("content.pack", "contentAtlas");
        TextureManager.splitAtlasIntoRegions();


        this.hud = new Hud(this.batch);



        MapProperties properties = mapManager.getCurrentTiledMap().getProperties();
        levelW = properties.get("width", Integer.class);
        levelH = properties.get("height", Integer.class);


        parallaxCameraFG = new ParallaxCamera(LearningGdx.V_WIDTH/2, LearningGdx.V_HEIGHT/2, mapManager.getCurrentCamera());

        parallaxer = new Parallaxer( mapManager.getCurrentCamera());
        parallaxer.addTexture("wood_bg.png", -LearningGdx.V_WIDTH/2, -LearningGdx.V_HEIGHT/2, LearningGdx.V_WIDTH * 1.2f, LearningGdx.V_HEIGHT * 1.2f, 0.0025f, 0.6f);



        box2DDebugRenderer = new Box2DDebugRenderer();

        this.male = new Player(mapManager.getCurrentWorld(), 150, 250, 8, 24, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerMale").split(32,64)[0], 1/7f, hud, camera);
        male.setFirstAnimationFrame(1);
        this.female = new FemalePlayer(mapManager.getCurrentWorld(), 110, 250, 8, 24, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerFemale").split(32,64)[0], 1/7f, hud, camera);

        mapManager.setMale(male);
        mapManager.setGhost(female);


        mapManager.getCurrentWorld().setContactListener(new WorldContactListener());



        //first give a batch to scene

        // Scene.setSpriteBatch(this.batch);
       // Scene.setWorld(world);


        //bg = new Texture(Gdx.files.internal("wood_bg.png"));
        foreground = new Texture(Gdx.files.internal("wood_fg.png"));




        CameraCoordinates c = new CameraCoordinates(male, female, mapManager.getCurrentCamera());
        hud.setCameraCoordinates(c);

        //e = new RockEnemy(world, 300, 300, 16, 10, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("rockEnemy").split(44,37)[0], 1/4f, male);
        //s = new SpiderEnemy(world, 355, 150, 10, 28, BodyDef.BodyType.KinematicBody, TextureManager.getRegionByName("spiderEnemy").split(14,61)[0],  TextureManager.getRegionByName("spiderAttacking").split(23,61)[0] , 1/6f,1/3f, male);



        this.inputController = new InputController(this.batch);

        PlayerMovementController.getInstance().addPlayer(this.male);
        PlayerMovementController.getInstance().addPlayer(this.female);


        //RayHandler.useDiffuseLight(true);

        handler = new RayHandler(mapManager.getCurrentWorld());

        handler.setAmbientLight(1f);
        pointLight = new PointLight(handler, 100, Color.WHITE, 128/LearningGdx.PPM, 0,0);
        pointLight.attachToBody(female.getBody());
        pointLight.setIgnoreAttachedBody(true);



        CameraEffects.setTarget(male);
    }


    @Override
    public void show() {

        //First method called when screen opened
        inputController.resetInputProcessor();

        if(mapRenderer == null){
            mapRenderer = new OrthogonalTiledMapRenderer(mapManager.getCurrentTiledMap(), 1/ LearningGdx.PPM );
        }
    }

    public void update(float delta){
        if(!running) return;


        mapManager.getCurrentWorld().step(1/60f, 6, 2);
        mapManager.getCurrentOverlaper().update();

        male.update(delta);
        female.update(delta);

        //update the scene with its objects
        Scene.update(delta); //important that is AFTER THE WORLD.STEP !!!
        //e.update(delta);
        //s.update(delta);

       //camera update
        CameraEffects.updateCamera(delta);

        float sX = mapManager.getCurrentCamera().viewportWidth/2;
        float sY =  mapManager.getCurrentCamera().viewportHeight/2;

        CameraEffects.boundCamera(sX, sY, levelW * 64 - sX *2, levelH * 64 - sY * 2);
        // ---- end camera update

        //for the map renderer
        mapRenderer.setView(mapManager.getCurrentCamera());

        if(male.getY() < 0){
            male.resetPlayer();
        }else if(reset){
            male.resetPlayer();
            reset = false;
        }
        //

        handler.setCombinedMatrix(mapManager.getCurrentCamera());

    }

    @Override
    public void render(float delta) {

        if(gameState == GameState.GAME_OVER){
            //konec igre
        }

        if(gameState == GameState.PAUSED){
            return;
        }
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(mapManager.hasMapChanged()){
            mapRenderer.setMap(mapManager.getCurrentTiledMap());

            mapManager.set_mapChanged(false);
        }

        //bg
        parallaxer.render(batch, parallaxBgIndex);

        batch.setProjectionMatrix(mapManager.getCurrentCamera().combined);
        //render the map also
        //mapRenderer.render(bg);
        batch.begin();

        //render the scene with objects
        Scene.render(delta);

        male.render(batch, delta);

        //e.render(batch, delta);
        //s.render(batch, delta);

        batch.end();



        //render water
        ((WaterShader)ShaderFactory.getShader(ShaderFactory.ShaderType.WATER_SHADER)).render(batch, mapManager.getCurrentCamera(), delta);



        //fg

        mapRenderer.render(fg);


        ((DefaultShader) ShaderFactory.getShader(ShaderFactory.ShaderType.DEFAULT_SHADER)).render(batch,mapManager.getCurrentCamera(), delta);

        batch.setProjectionMatrix(parallaxCameraFG.calculateParallaxMatrix(1.2f, 0.5f));
        batch.begin();
        batch.enableBlending();


        float par = parallaxCameraFG.position.x + parallaxCameraFG.getViewportWidth();
        int ind = (int) (par / foreground.getWidth()) + 1;
        if(ind-1 == 0){
            batch.draw(foreground,(ind-1) * foreground.getWidth(), -LearningGdx.V_HEIGHT/7);
            batch.draw(foreground,ind * foreground.getWidth(), -LearningGdx.V_HEIGHT/7);
        }else{
            batch.draw(foreground,(ind-2) * foreground.getWidth(), -LearningGdx.V_HEIGHT/7);
            batch.draw(foreground,(ind-1) * foreground.getWidth(), -LearningGdx.V_HEIGHT/7);
            batch.draw(foreground,ind * foreground.getWidth(), -LearningGdx.V_HEIGHT/7);
        }

        batch.end();

        if(PlayerMovementController.getInstance().getCurrent_player() == 1)
            mapRenderer.render(paths);



        handler.updateAndRender();
        batch.begin();
        batch.setProjectionMatrix(mapManager.getCurrentCamera().combined);
        female.render(batch,delta);



        batch.end();
        /////



        //batch.setShader(defaulShader);
        batch.setProjectionMatrix(this.hud.stage.getCamera().combined);

        hud.render(delta);

        this.inputController.draw();


        //debuger
        //box2DDebugRenderer.render(world, camera.combined);

    }
    public static void setRunning(boolean b){ running = b; }

    @Override
    public void dispose() {
        super.dispose();
        TextureManager.disposeAll();
        world.dispose();
        handler.dispose();
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }




}
