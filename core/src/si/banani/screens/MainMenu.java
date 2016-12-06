package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.entities.Player;
import si.banani.learning.LearningGdx;
import si.banani.scenes.Hud;
import si.banani.textures.TextureManager;
import si.banani.world.WorldContactListener;


/**
 * Created by Urban on 18.10.2016.
 */

public class MainMenu extends BaseScreen {


    private Animation animation;
    private boolean camFollow = false;
    private float x = 0, y = 0, speed = 0;
    int dir = 1;
    public boolean reset = false;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //box2d
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    //for player

    Player male;

    public MainMenu() {
        super();
        this.hud = new Hud(this.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/ LearningGdx.PPM);
        this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 + 150/ LearningGdx.PPM, 0);


        //box2d
        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object: map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set( (rect.getX() + rect.getWidth()/2 ) / LearningGdx.PPM , (rect.getY() + rect.getHeight()/2 ) / LearningGdx.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / LearningGdx.PPM, rect.getHeight()/2 / LearningGdx.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set( (rect.getX() + rect.getWidth()/2 ) / LearningGdx.PPM , (rect.getY() + rect.getHeight()/2 ) / LearningGdx.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / LearningGdx.PPM, rect.getHeight()/2 / LearningGdx.PPM);
            fdef.shape = shape;

            body.setUserData("spikes");
            body.createFixture(fdef);
        }

        world.setContactListener(new WorldContactListener(this));
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

        TextureManager.addAtlas("content.pack", "contentAtlas");
        TextureManager.splitAtlasIntoRegions();

        this.male = new Player(world, 150, 250, 8, 24, BodyDef.BodyType.DynamicBody, TextureManager.getRegionByName("playerMale").split(32,64)[0], 1/6f);
        male.setFriction(0.5f);
    }
    public void input(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) )
        {
            //camera.position.x += 100 / LearningGdx.PPM  * dt ;

        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            male.goRight();
        }else{
            male.stopRight();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) ){
            male.goLeft();
        }else{
            male.stopLeft();
        }
        if(Gdx.input.isTouched()){
            int x = Gdx.input.getX();
            if( x >= Gdx.graphics.getWidth()/2 ){
                male.goRight();}
            else{
                male.stopRight();
                }
            if(x < Gdx.graphics.getWidth()/2 ){
                male.goLeft();
            }else{
                male.stopLeft();
            }
        }
    }
    public void update(float delta){
        if(!running) return;
        input(delta);

        world.step(1/60f, 6, 2);

        male.update(delta);

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
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //debuger
        box2DDebugRenderer.render(world, camera.combined);

        batch.setProjectionMatrix(this.hud.stage.getCamera().combined);
        hud.stage.draw();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        male.render(batch, delta);

        //batch.draw(TextureManager.getRegionByName("playerFemale"), 0 ,0);
        batch.end();
        //render the map also
        mapRenderer.render();


    }

    @Override
    public void dispose() {
        super.dispose();
        TextureManager.disposeAll();
    }
}
