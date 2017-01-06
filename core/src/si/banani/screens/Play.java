package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.camera.CameraEffects;
import si.banani.camera.ParallaxCamera;
import si.banani.controller.InputController;
import si.banani.controller.PlayerMovementController;
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;
import si.banani.entities.RockEnemy;
import si.banani.entities.SpiderEnemy;
import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.scenes.Hud;
import si.banani.textures.TextureManager;
import si.banani.world.WorldContactListener;
import si.banani.world.WorldCreator;


/**
 * Created by Urban on 18.10.2016.
 */

public class Play extends BaseScreen {


    public static boolean reset = false;
    private Hud hud;

    private TmxMapLoader mapLoader;
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

    BitmapFont font;

    //Parallax
    ParallaxCamera parallaxCameraFG, parallaxCameraBG;
    Texture bg, foreground;

    public Play(SpriteBatch spriteBatch) {
        super(spriteBatch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ LearningGdx.PPM);
        this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 + 150/ LearningGdx.PPM, 0);
        MapProperties properties = map.getProperties();
        levelW = properties.get("width", Integer.class);
        levelH = properties.get("height", Integer.class);


        parallaxCameraBG = new ParallaxCamera(LearningGdx.V_WIDTH * 1.2f, LearningGdx.V_HEIGHT * 1.2f, camera);
        parallaxCameraFG = new ParallaxCamera(LearningGdx.V_WIDTH/2, LearningGdx.V_HEIGHT/2, camera);
        //the texture manager
        TextureManager.addAtlas("content.pack", "contentAtlas");
        TextureManager.splitAtlasIntoRegions();

        this.hud = new Hud(this.batch);

        //box2d
        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        worldCreator = new WorldCreator(world, map);

        world.setContactListener(new WorldContactListener());
        //creating a test box
        //first give a batch to scene
        Scene.setSpriteBatch(this.batch);
        Scene.setWorld(world);
        font = new BitmapFont(Gdx.files.internal("allura.fnt"));

        bg = new Texture(Gdx.files.internal("wood_bg.png"));
        foreground = new Texture(Gdx.files.internal("wood_fg.png"));

        this.male = new Player(world, 150, 250, 8, 24, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerMale").split(32,64)[0], 1/7f, hud);
        male.setFirstAnimationFrame(1);

        this.female = new FemalePlayer(world, 110, 250, 8, 24, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerFemale").split(32,64)[0], 1/7f);

        //e = new RockEnemy(world, 500, 250, 16, 10, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("rockEnemy").split(44,37)[0], 1/4f, male);
        //s = new SpiderEnemy(world, 355, 150, 10, 28, BodyDef.BodyType.KinematicBody, TextureManager.getRegionByName("spiderEnemy").split(14,61)[0],  TextureManager.getRegionByName("spiderAttacking").split(23,61)[0] , 1/6f,1/3f, male);

        CameraEffects.setCamera(camera);
        CameraEffects.setTarget(male);
        CameraEffects.setZooming(true);

        this.inputController = new InputController(this.batch);

        PlayerMovementController.getInstance().addPlayer(this.male);
        PlayerMovementController.getInstance().addPlayer(this.female);
    }



    @Override
    public void show() {

        //First method called when screen opened

    }

    public void update(float delta){
        if(!running) return;

        world.step(1/60f, 6, 2);

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

    }

    @Override
    public void render(float delta) {


        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //bg
        batch.setProjectionMatrix(parallaxCameraBG.calculateParallaxMatrix(0.025f, 0.6f));
        batch.begin();
        batch.enableBlending();
        batch.draw(bg, -LearningGdx.V_WIDTH/2, -LearningGdx.V_HEIGHT/2);

        batch.end();




        batch.setProjectionMatrix(camera.combined);
        //render the map also
        //mapRenderer.render(bg);
        batch.begin();



        male.render(batch, delta);
        female.render(batch,delta);

        //render the scene with objects
        Scene.render(delta);

        //e.render(batch, delta);
        //s.render(batch, delta);

        batch.end();

        mapRenderer.render(fg);

        //fg
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
        /////

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
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        inputController.resize(width,height);
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
