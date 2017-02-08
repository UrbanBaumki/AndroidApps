package si.banani.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.controller.PlayerMovementController;
import si.banani.learning.LearningGdx;
import si.banani.scenes.Hud;
import si.banani.screens.Play;
import si.banani.tiles.CheckPoint;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 27.12.2016.
 */

public class FemalePlayer extends BasicPlayer {

    private float yOffset = 0f;
    private float offDir = 1f;
    private Animation floatAnimation;
    private float energyLevel = 100f;
    private float maxEnergyLevel = 100f;
    private float energyDrainSpeed = 1f;
    private Hud hud;
    private OrthographicCamera camera;
    private boolean switching = false;
    private float lastCheckpointX, lastCheckpointY;

    public FemalePlayer(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, float frameSpeed, Hud hud, OrthographicCamera camera) {
        super(world, x, y, width, height, bodyType);
        this.hud = hud;
        this.camera = camera;
        ((body.getFixtureList()).get(0)).setUserData(this);

        startX = x;
        startY = y;
        lastCheckpointX = x;
        lastCheckpointY = y;


        Filter f = new Filter();
        f.categoryBits = CollisionBits.GHOST_BIT;
        f.maskBits = CollisionBits.ENEMY_BIT |
                CollisionBits.SPIKES_BIT |
                CollisionBits.DEFAULT_BIT |
                CollisionBits.SWITCH_BIT |
                CollisionBits.OBJECT_BIT|
                CollisionBits.DOORS_BIT | CollisionBits.BOX_BIT;

        ((body.getFixtureList()).get(0)).setFilterData(f);
        ((body.getFixtureList()).get(1)).setFilterData(f);
        ((body.getFixtureList()).get(0)).setDensity(8f);
        ((body.getFixtureList()).get(0)).setFriction(1f);
        ((circleBody.getFixtureList()).get(0)).setDensity(6f);
        ((circleBody.getFixtureList()).get(0)).setFilterData(f);
        body.resetMassData();

        footFixture.setUserData(this);

        this.currentState = PlayerState.STANDING;
        this.previousState = PlayerState.STANDING;
        this.timeInCurrentState = 0;

        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();
        dir = 1;

        floatAnimation = new Animation(sprites, frameSpeed);
        jumpSpeed = 1.6f;

        //defining another sensor for killing
        FixtureDef circle = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(50f/LearningGdx.PPM);
        circle.shape = circleShape;
        circle.isSensor = true;
        body.createFixture(circle).setUserData("killSensor");


    }
    @Override
    public void update(float dt){
        if(reset){
            resetPlayer();
            reset = false;
        }
        this.switching = false;
        super.update(dt);

        if(isControlled){
            this.energyLevel -= energyDrainSpeed * dt;
            if(energyLevel < 0){
                PlayerMovementController.getInstance().switchPlayer();
            }else
                hud.setEnergyLevel(energyLevel);
        }

        //Gdx.app.log("Falling", String.format("%d", numFootContants));
    }

    public void setCheckpoint(CheckPoint checkpoint){
        this.lastCheckpointX = checkpoint.spawnX;
        this.lastCheckpointY = checkpoint.spawnY;
    }
    public void resetPlayer(){

        setXYvelocity(0,0) ;
        setTransform(lastCheckpointX, lastCheckpointY, 0);
        //camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 + 150/ LearningGdx.PPM,0);



    }
    public void addEnergy(float energy){
        this.energyLevel +=energy;
        if(energyLevel > maxEnergyLevel)
            energyLevel = maxEnergyLevel;
    }
    @Override
    public void render(SpriteBatch sb, float dt) {

        //just a hardcoded float offset animation, to save cpu time from calculating sin function :)
        if(yOffset > 1f)
            offDir = -1f;
        else if(yOffset < -4f)
            offDir = 1f;

        yOffset += dt * offDir * 5f;

        sb.draw(getCurrentFrame(dt), x  - dir * width /2/ LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width / LearningGdx.PPM, height / LearningGdx.PPM);
    }
    private TextureRegion getCurrentFrame(float dt){
        PlayerState currentState = getState();
        TextureRegion region = null;
        switch (currentState){
            case STANDING:
                region = floatAnimation.getCurrentFrame();
                floatAnimation.update(dt);
                break;
            case FOLLOWING:
            case WALKING:
                region = floatAnimation.getCurrentFrame();
                floatAnimation.update(dt);
                break;
        }
        return region;
    }
    public Body getBody(){
        return this.body;
    }

    @Override
    PlayerState getState() {
        return currentState;
    }

    public Vector3 getProjectedPosition(){
        return camera.project(new Vector3(body.getPosition().x, body.getPosition().y, 0));
    }
    public void doSwitch(){
        this.switching = true;
    }
    public boolean isSwitching(){ return this.switching; }

    public float getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(float energyLevel) {
        this.energyLevel = energyLevel;
        hud.setEnergyLevel(energyLevel);
    }
}
