package si.banani.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.scenes.Hud;
import si.banani.screens.Play;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.textures.TextureManager;
import si.banani.tiles.Switch;
import si.banani.world.CollisionBits;


/**
 * Created by Urban on 6.12.2016.
 */

public class Player extends BasicPlayer implements AudioSubject {

    private int yOffset = -6;

    private Animation walkAnimation, climbAnimation;
    private TextureRegion[] sprites;
    private boolean switching;
    private int maxHealth = 3;



    private Hud hud;

    //audio observers
    Array<AudioObserver> _observers = new Array<AudioObserver>();
    private float stepTime = 0f;
    private float stepTimer = 0f;
    private AudioObserver.AudioTypeEvent steps[] = {AudioObserver.AudioTypeEvent.SOUND_STEP_GRASS_1, AudioObserver.AudioTypeEvent.SOUND_STEP_GRASS_2, AudioObserver.AudioTypeEvent.SOUND_STEP_GRASS_3, AudioObserver.AudioTypeEvent.SOUND_STEP_GRASS_4};
    Random r = new Random();
    OrthographicCamera camera;
    public Player(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, float frameSpeed, Hud hud, OrthographicCamera camera) {
        super(world, x, y, width, height, bodyType);
        ((body.getFixtureList()).get(0)).setUserData(this);

        startX = x;
        startY = y;

        this.camera = camera;
        Filter f = new Filter();
        f.categoryBits = CollisionBits.PLAYER_BIT;
        f.maskBits = CollisionBits.ENEMY_BIT |
                CollisionBits.SPIKES_BIT |
                CollisionBits.DEFAULT_BIT |
                CollisionBits.OBJECT_BIT |
                CollisionBits.SWITCH_BIT |
                CollisionBits.DOORS_BIT  |
                CollisionBits.GHOST_PATH_BIT;

        ((body.getFixtureList()).get(0)).setFilterData(f);
        ((body.getFixtureList()).get(1)).setFilterData(f);
        ((body.getFixtureList()).get(0)).setDensity(8f);
        ((body.getFixtureList()).get(0)).setFriction(1f);
        ((circleBody.getFixtureList()).get(0)).setFilterData(f);
        body.resetMassData();

        footFixture.setUserData(this);

        this.currentState = PlayerState.STANDING;
        this.previousState = PlayerState.STANDING;
        this.timeInCurrentState = 0;


        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();
        this.sprites = sprites;
        this.hud = hud;

        this.dir = 1;

        this.walkAnimation = new Animation(this.sprites, frameSpeed);
        walkAnimation.setStartingFrame(1);
        this.climbAnimation = new Animation(TextureManager.getRegionByName("climbing").split(15,50)[0], 1/7f);

        jumpSpeed = 1.6f;

        //sound init
        this.addObserver(AudioManager.getInstance());
        loadSounds();
        stepTime = frameSpeed* 3.3f;

        health = 3;

        isControlled = true;


    }

    public void addHealth(int h){
        health += h;
        if (health > maxHealth)
            health = maxHealth;
    }

    public void update(float dt){
        //Gdx.app.log("H", String.format("%f", health));
        if(reset){
            resetPlayer();
            reset = false;
        }
        this.switching = false;
        super.update(dt);

        //play steps
        if(currentState == PlayerState.WALKING  ){
            //we've made a step
            if(stepTimer >= stepTime){
                stepTimer -= stepTime;

                notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, steps[r.nextInt(steps.length)]);
            }else{
                stepTimer += dt;
            }

        }else
            stepTimer = stepTime;

    }
    public void render(SpriteBatch sb, float dt){
        sb.draw(getCurrentFrame(dt), x  - dir * width /2/ LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width / LearningGdx.PPM, height / LearningGdx.PPM);

    }

    public TextureRegion getCurrentFrame(float dt){
        currentState = getState();
        TextureRegion region = null;
        switch (currentState){
            case STANDING:
                region = this.walkAnimation.getFirstFrame();
                walkAnimation.reset();
                break;
            case WALKING:
                region = this.walkAnimation.getCurrentFrame();
                walkAnimation.update(dt);
                break;
            case CLIMBING:
                region = this.climbAnimation.getCurrentFrame();
                if(getXvelocity() != 0 || getYvelocity() != 0)
                    climbAnimation.update(dt);
                break;
        }
        timeInCurrentState = currentState == previousState ? timeInCurrentState + dt : 0;
        previousState = currentState;
        return region;
    }

    public void resetPlayer(){
        hud.decreaseLives();
        hud.update();
        setXYvelocity(0,0) ;
        setTransform(startX, startY, 0);
        //camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 + 150/ LearningGdx.PPM,0);


        if(hud.getNumLives() == 0){

            Play.setRunning(false);

            hud.showGameOver();
        }
    }

    public void recieveDamage(float dmg, int dir){
        this.health -= dmg;
        float force = dir * dmg * 15;
        body.applyForceToCenter(force, Math.abs(force/3), true);
    }
    public float getX(){return this.x;}
    public float getY(){return this.y;}
    public int getDir(){return this.dir; }

    public Vector2 getPosition(){ return this.body.getPosition(); }
    public void setMovementSpeed(float speed){this.movementSpeed = speed; }
    public void setMaxMovSpeed(float cap){this.maxMovSpeed = cap;}
    public void setJumpSpeed(float jumpSpeed){this.jumpSpeed = jumpSpeed;}

    public void doSwitch(){
        this.switching = true;
    }
    public boolean isSwitching(){ return this.switching; }
    public PlayerState getState(){

        if(isClimbing)
            return PlayerState.CLIMBING;
        else if(this.body.getLinearVelocity().x <= 0.09f && this.body.getLinearVelocity().x >= -0.09f )
            return PlayerState.STANDING;
        else
            return PlayerState.WALKING;
    }


    public void loadSounds(){
        notify(AudioObserver.AudioCommand.SOUND_LOAD, AudioObserver.AudioTypeEvent.SOUND_STEP_GRASS_1);
        notify(AudioObserver.AudioCommand.SOUND_LOAD, AudioObserver.AudioTypeEvent.SOUND_STEP_GRASS_2);
        notify(AudioObserver.AudioCommand.SOUND_LOAD, AudioObserver.AudioTypeEvent.SOUND_STEP_GRASS_3);
        notify(AudioObserver.AudioCommand.SOUND_LOAD, AudioObserver.AudioTypeEvent.SOUND_STEP_GRASS_4);
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
        for(AudioObserver observer : _observers)
            observer.onNotify(command, event);
    }

    public Vector3 getProjectedPosition(){
        return camera.project(new Vector3(getX(), getY(), 0));
    }
    public void startDialog(){
        hud.enableDialog(true);
    }
}
