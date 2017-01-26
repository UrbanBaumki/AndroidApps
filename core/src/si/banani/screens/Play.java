package si.banani.screens;

import com.badlogic.gdx.Gdx;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
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
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;
import si.banani.entities.RockEnemy;
import si.banani.entities.SpiderEnemy;
import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.scenes.Hud;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.textures.TextureManager;
import si.banani.world.MapManager;
import si.banani.world.WorldCollideListener;
import si.banani.world.WorldContactListener;
import si.banani.world.WorldCreator;


/**
 * Created by Urban on 18.10.2016.
 */

public class Play extends BaseScreen implements AudioSubject{

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
    //int [] bg = {0};
    int [] fg = {1,2,3,4,5,6};
    int [] paths = {10};

    BitmapFont font;

    //Parallax
    ParallaxCamera parallaxCameraFG, parallaxCameraBG;
    Texture  foreground;
    Parallaxer parallaxer;
    int [] parallaxBgIndex = {0};
    int [] parallaxFgIndex = {1};

    //Testing lights
    RayHandler handler;
    PointLight pointLight;

    //overlaping
    WorldCollideListener overlaper;

    //bwShader test
    ShaderProgram bwShader, defaulShader, wS;
    Texture water, perlin;
    float time = 1f;



    public Play(SpriteBatch spriteBatch) {
        super(spriteBatch);


        this.addObserver(AudioManager.getInstance());
        loadMusic();

        //MAP LOADING
        //the texture manager
        TextureManager.addAtlas("content.pack", "contentAtlas");
        TextureManager.splitAtlasIntoRegions();

        world = new World(new Vector2(0, -10), true);

        worldCreator = new WorldCreator(world, MapManager.CH1);

        map = worldCreator.getCurrentMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ LearningGdx.PPM);


        //this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 + 150/ LearningGdx.PPM, 0);
        MapProperties properties = map.getProperties();
        levelW = properties.get("width", Integer.class);
        levelH = properties.get("height", Integer.class);

       // parallaxCameraBG = new ParallaxCamera(LearningGdx.V_WIDTH * 1.2f, LearningGdx.V_HEIGHT * 1.2f, camera);
        parallaxCameraFG = new ParallaxCamera(LearningGdx.V_WIDTH/2, LearningGdx.V_HEIGHT/2, camera);

        parallaxer = new Parallaxer( camera);
        parallaxer.addTexture("wood_bg.png", -LearningGdx.V_WIDTH/2, -LearningGdx.V_HEIGHT/2, LearningGdx.V_WIDTH * 1.2f, LearningGdx.V_HEIGHT * 1.2f, 0.0025f, 0.6f);



        this.hud = new Hud(this.batch);


        box2DDebugRenderer = new Box2DDebugRenderer();



        world.setContactListener(new WorldContactListener());
        //creating a test box
        //first give a batch to scene
        Scene.setSpriteBatch(this.batch);
        Scene.setWorld(world);
        font = new BitmapFont(Gdx.files.internal("allura.fnt"));

        //bg = new Texture(Gdx.files.internal("wood_bg.png"));
        foreground = new Texture(Gdx.files.internal("wood_fg.png"));

        this.male = new Player(world, 150, 250, 8, 24, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerMale").split(32,64)[0], 1/7f, hud);
        male.setFirstAnimationFrame(1);

        this.female = new FemalePlayer(world, 110, 250, 8, 24, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerFemale").split(32,64)[0], 1/7f, hud);

        //e = new RockEnemy(world, 300, 300, 16, 10, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("rockEnemy").split(44,37)[0], 1/4f, male);
        //s = new SpiderEnemy(world, 355, 150, 10, 28, BodyDef.BodyType.KinematicBody, TextureManager.getRegionByName("spiderEnemy").split(14,61)[0],  TextureManager.getRegionByName("spiderAttacking").split(23,61)[0] , 1/6f,1/3f, male);

        CameraEffects.setCamera(camera);
        CameraEffects.setTarget(male);
        CameraEffects.setZooming(true);

        this.inputController = new InputController(this.batch);

        PlayerMovementController.getInstance().addPlayer(this.male);
        PlayerMovementController.getInstance().addPlayer(this.female);


        //RayHandler.useDiffuseLight(true);
        handler = new RayHandler(world);

        handler.setAmbientLight(1f);
        pointLight = new PointLight(handler, 100, Color.WHITE, 128/LearningGdx.PPM, 0,0);
        pointLight.attachToBody(female.getBody());
        pointLight.setIgnoreAttachedBody(true);

        overlaper = new WorldCollideListener(world);

        //bwShader



        ShaderProgram.pedantic = false;
        bwShader = new ShaderProgram(Gdx.files.internal("bin/shaders/earthquake.vsh") ,Gdx.files.internal("bin/shaders/bw.fsh") );
        Gdx.app.log(bwShader.isCompiled() ? "Compiled" : bwShader.getLog(), "");
        //bwShader.setUniformMatrix("u_projTrans", camera.combined, false);

        defaulShader = batch.createDefaultShader();

        water = new Texture(Gdx.files.internal("water.png"));
        perlin = new Texture(Gdx.files.internal("perlin.jpg"));

        wS = new ShaderProgram(Gdx.files.internal("bin/shaders/water.vsh") ,Gdx.files.internal("bin/shaders/water.fsh"));
        Gdx.app.log(bwShader.isCompiled() ? "Water compiled" : bwShader.getLog(), "");


    }

    void loadMusic(){
        this.notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_ONE);
        this.notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_ONE);
    }

    @Override
    public void show() {

        //First method called when screen opened
        inputController.resetInputProcessor();

    }

    public void update(float delta){
        if(!running) return;


        world.step(1/60f, 6, 2);
        overlaper.update();

        male.update(delta);
        female.update(delta);

        //update the scene with its objects
        Scene.update(delta); //important that is AFTER THE WORLD.STEP !!!
        //e.update(delta);
        //s.update(delta);

       //camera update
        CameraEffects.updateCamera(delta);

        float sX = camera.viewportWidth/2;
        float sY = camera.viewportHeight/2;

        CameraEffects.boundCamera(sX, sY, levelW * 64 - sX *2, levelH * 64 - sY * 2);
        // ---- end camera update

        //for the map renderer
        mapRenderer.setView(this.camera);

        if(male.getY() < 0){
            male.resetPlayer();
        }else if(reset){
            male.resetPlayer();
            reset = false;
        }
        //

        handler.setCombinedMatrix(camera);

    }

    @Override
    public void render(float delta) {


        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //bg
        parallaxer.render(batch, parallaxBgIndex);


        batch.setProjectionMatrix(camera.combined);
        //render the map also
        //mapRenderer.render(bg);
        batch.begin();

        male.render(batch, delta);


        //render the scene with objects
        Scene.render(delta);


        //e.render(batch, delta);
        //s.render(batch, delta);

        batch.end();



        //water shader
        time += delta;
        time = time % 10f;
        float angle = time * (2 * MathUtils.PI);
        if (angle > (2 * MathUtils.PI))
            angle -= (2 * MathUtils.PI);


        wS.begin();
        wS.setUniformMatrix("u_projTrans", camera.combined, false);
        wS.setUniformf("u_deltatime", angle);
        wS.setUniformi("u_texture_perlin", 1);
        wS.setUniformf("u_playerposition", camera.position.x);
        //wS.setUniformf("u_speed", 0.1f);
        wS.end();

        //Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
        perlin.bind(1);

        batch.setShader(wS);
        batch.begin();
        water.bind(0);
        //Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
        batch.draw(water, 200/LearningGdx.PPM, 80/LearningGdx.PPM, water.getWidth()/LearningGdx.PPM, water.getHeight()/LearningGdx.PPM);
        batch.end();










        //fg
        mapRenderer.render(fg);

        batch.setShader(defaulShader);
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
        batch.setProjectionMatrix(camera.combined);
        female.render(batch,delta);



        batch.end();
        /////



        //batch.setShader(defaulShader);
        batch.setProjectionMatrix(this.hud.stage.getCamera().combined);

        hud.stage.draw();

        this.inputController.draw();


        //debuger
        box2DDebugRenderer.render(world, camera.combined);

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
        int gutterW = viewport.getLeftGutterWidth();
        int gutterH = viewport.getTopGutterHeight();
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


    @Override
    public void addObserver(AudioObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(AudioObserver observer) {
        _observers.removeValue(observer, true);
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
}
