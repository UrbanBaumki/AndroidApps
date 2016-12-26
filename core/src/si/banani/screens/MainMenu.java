package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.controller.InputController;
import si.banani.controller.PlayerMovementController;
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

public class MainMenu extends BaseScreen {


    public boolean reset = false;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //box2d
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    //my fixture creator
    private WorldCreator worldCreator;

    //for player

    Player male;
    RockEnemy e;
    SpiderEnemy s;
    int [] bg = {0};
    int [] fg = {1,2,3,4,5,6};



    public MainMenu() {
        super();
        this.hud = new Hud(this.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ LearningGdx.PPM);
        this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 + 150/ LearningGdx.PPM, 0);


        //the texture manager
        TextureManager.addAtlas("content.pack", "contentAtlas");
        TextureManager.splitAtlasIntoRegions();
        //box2d
        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        worldCreator = new WorldCreator(world, map);

        world.setContactListener(new WorldContactListener());
        //creating a test box
        //first give a batch to scene
        Scene.setSpriteBatch(this.batch);
        Scene.setWorld(world);

    }
    public void resetPlayer(){
        hud.decreaseLives();
        hud.update();
        male.setXYvelocity(0,0) ;
        male.setTransform(150 / LearningGdx.PPM, 230 / LearningGdx.PPM, 0);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 + 150/ LearningGdx.PPM,0);

        reset = false;
        if(hud.getNumLives() == 0){
            running = false;
            hud.showGameOver();
        }
    }

    @Override
    public void show() {

        //First method called when screen opened
        this.male = new Player(world, 150, 250, 8, 24, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerMale").split(32,64)[0], 1/7f);
        male.setFriction(0.5f);
        male.setFirstAnimationFrame(1);

        e = new RockEnemy(world, 300, 250, 16, 10, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("rockEnemy").split(32,25)[0], 1/4f, male);
        //s = new SpiderEnemy(world, 355, 150, 10, 28, BodyDef.BodyType.KinematicBody, TextureManager.getRegionByName("spiderEnemy").split(14,61)[0],  TextureManager.getRegionByName("spiderAttacking").split(23,61)[0] , 1/6f,1/3f, male);

        this.inputController = new InputController(this.batch);
        PlayerMovementController.getInstance().addPlayer(this.male);
    }
    public void input(float dt){

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) )
        {
            //camera.position.x += 100 / LearningGdx.PPM  * dt ;

        }


    }
    public void update(float delta){
        if(!running) return;
        input(delta);

        world.step(1/60f, 6, 2);

        male.update(delta);

        //update the scene with its objects
        Scene.update(delta); //important that is AFTER THE WORLD.STEP !!!
        e.update(delta);
        //s.update(delta);
        if(male.getX() >= LearningGdx.V_WIDTH / 2 / LearningGdx.PPM)
            this.camera.position.x = male.getX();
        this.camera.position.y = male.getY();

        this.camera.update();
        //for the map renderer
        mapRenderer.setView(this.camera);

        if(male.getY() < 0){
            resetPlayer();
        }else if(reset){
            resetPlayer();
        }

    }

    @Override
    public void render(float delta) {


        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.setProjectionMatrix(camera.combined);

        //render the map also
        mapRenderer.render(bg);
        batch.begin();

        male.render(batch, delta);

        //render the scene with objects
        Scene.render(delta);

        e.render(batch, delta);
       //s.render(batch, delta);

        //batch.draw(TextureManager.getRegionByName("playerFemale"), 0 ,0);
        batch.end();

        mapRenderer.render(fg);

        batch.setProjectionMatrix(this.hud.stage.getCamera().combined);
        hud.stage.draw();

        this.inputController.draw();
        //debuger
        box2DDebugRenderer.render(world, camera.combined);

    }

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
}
