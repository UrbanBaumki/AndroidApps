package si.banani.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 15.12.2016.
 */

public class RockEnemy extends BasicPlayer implements AudioSubject {

    private TextureRegion[] sprites;
    private Animation walk, attack;
    private float yOffset = 5f;
    private boolean dead = false;
    private TextureRegion deadTexture;
    private boolean destroy, destroyed;
    private boolean doAttack = false;
    Array<AudioObserver> _observers = new Array<AudioObserver>();
    public RockEnemy(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, float frameSpeed) {
        super(world, x, y, width, height, bodyType);

        this.currentState = PlayerState.SLEEPING;
        this.previousState = PlayerState.SLEEPING;

        this.x = x;
        this.y = y;
        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();


        this.sprites = sprites;

        this.dir = 1;
        this.movementSpeed = 0.7f;
        this.maxMovSpeed = 0.95f;
        this.jumpSpeed = 4f;


        this.walk = new Animation(this.sprites, frameSpeed);
        this.walk.setStartingFrame(1);


        Filter f = new Filter();
        f.categoryBits = CollisionBits.ENEMY_BIT;
        f.maskBits = CollisionBits.SPIKES_BIT |
                CollisionBits.DEFAULT_BIT |
                CollisionBits.GHOST_PATH_BIT |
        CollisionBits.PLAYER_BIT;

        ((body.getFixtureList()).get(0)).setFilterData(f);

        ((body.getFixtureList()).get(0)).setUserData(this);



        body.destroyFixture(footFixture);
        world.destroyJoint(body.getJointList().get(0).joint);
        circleBody.destroyFixture(circleFixture);

        //creating a sensor as a radar to detect our target

        PolygonShape radarShape = new PolygonShape();
        radarShape.setAsBox(200 / LearningGdx.PPM, 40 / LearningGdx.PPM);

        FixtureDef radarFixtureDef = new FixtureDef();
        radarFixtureDef.shape = radarShape;
        radarFixtureDef.isSensor = true;
        radarFixtureDef.density = 0f;

        f = new Filter();
        f.categoryBits = CollisionBits.SENSOR_BIT;
        f.maskBits =
                CollisionBits.PLAYER_BIT;

       Fixture radar =  body.createFixture(radarFixtureDef);
        radar.setFilterData(f);
        radar.setUserData(this);

        deadTexture = new TextureRegion(new Texture(Gdx.files.internal("textures/props/dead_rock.png")));
        TextureRegion[] attacks = new TextureRegion(new Texture(Gdx.files.internal("textures/props/rock_attack.png"))).split(45,37)[0];
        attack = new Animation(attacks, 1/3f);
        this.addObserver(AudioManager.getInstance());
        loadSounds();
    }
    public void dealDamageToTarget(){
        if(target == null)
            return;
        if(Math.abs(target.getPosition().x - x) <= 0.5f && Math.abs(target.getPosition().y - y) <= 0.5f)
            target.recieveDamage(1, dir);
        doAttack = false;
    }

    @Override
    public void update(float dt) {
        if(destroy && !destroyed)
            world.destroyBody(body);

        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;

        if(dir == -1 && getXvelocity() >= -maxMovSpeed && currentState == PlayerState.FOLLOWING)
        {
            this.body.applyForceToCenter(new Vector2(-movementSpeed, 0), true);

        }
        if(dir == 1 && getXvelocity() <= maxMovSpeed  && currentState == PlayerState.FOLLOWING)
        {
            this.body.applyForceToCenter(new Vector2(movementSpeed, 0), true);

        }

    }



    @Override
    public void render(SpriteBatch sb, float dt) {
        sb.draw(getCurrentFrame(dt), x  - dir * width /2/ LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width / LearningGdx.PPM, height / LearningGdx.PPM);
    }

    private TextureRegion getCurrentFrame(float dt){
        TextureRegion currFrame = null;
        this.currentState = getState();

        switch (currentState){
            case DEAD:
                currFrame = deadTexture;
                break;
            case SLEEPING:
                currFrame = this.walk.getFirstFrame();
                this.walk.reset();
                this.body.setLinearVelocity(0,this.getYvelocity());
                break;
            case FOLLOWING:
                currFrame = this.walk.getCurrentFrame();
                this.walk.update(dt);
                break;
            case ATTACKING:
                currFrame = attack.getCurrentFrame();
                attack.update(dt);
                break;
        }
        timeInCurrentState = currentState == previousState ? timeInCurrentState + dt : 0;
        previousState = currentState;
        return currFrame;
    }

    @Override
    public  PlayerState getState() {
        PlayerState state = PlayerState.SLEEPING;
        if(dead && timeInCurrentState >= 1.5f){
            state = PlayerState.DEAD;
            destroy = true;
        }else if(dead){
            state = PlayerState.DEAD;
        }else if(doAttack && timeInCurrentState >= 0.7f){
            dealDamageToTarget();
        }else if(doAttack ){
            state = PlayerState.ATTACKING;
        }
        else if(target != null) {
            if (this.x <= target.getPosition().x)
                dir = 1;
            else
                dir = -1;

            if (target.getDir() == dir) {
                state = PlayerState.FOLLOWING;
            } else if (this.getXvelocity() == 0) {
                state = PlayerState.SLEEPING;

            }
        }
        return state;
    }

    @Override
    public void doSwitch() {

    }


    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead; timeInCurrentState = 0f;
        playDeadSound();
    }

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDoAttack() {

        return doAttack;
    }

    public void setDoAttack(boolean doAttack) {
        this.doAttack = doAttack;
        timeInCurrentState = 0f;
    }

    public void playDeadSound(){
        notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_ZOMBIE_DIE);
    }
    public void loadSounds(){

        notify(AudioObserver.AudioCommand.SOUND_LOAD, AudioObserver.AudioTypeEvent.SOUND_ZOMBIE_DIE);
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
}
