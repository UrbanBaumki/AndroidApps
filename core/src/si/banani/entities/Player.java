package si.banani.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.scenes.Hud;
import si.banani.screens.Play;
import si.banani.tiles.Switch;
import si.banani.world.CollisionBits;


/**
 * Created by Urban on 6.12.2016.
 */

public class Player extends BasicPlayer {

    private int yOffset = 1;

    private Animation walkAnimation;
    private TextureRegion[] sprites;
    private boolean fixedFriction, switching;

    private Hud hud;

    public Player(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, float frameSpeed, Hud hud) {
        super(world, x, y, width, height, bodyType);
        ((body.getFixtureList()).get(0)).setUserData(this);

        Filter f = new Filter();
        f.categoryBits = CollisionBits.PLAYER_BIT;
        f.maskBits = CollisionBits.ENEMY_BIT |
                CollisionBits.SPIKES_BIT |
                CollisionBits.DEFAULT_BIT |
                CollisionBits.OBJECT_BIT |
                CollisionBits.SWITCH_BIT |
                CollisionBits.DOORS_BIT;
        ((body.getFixtureList()).get(0)).setFilterData(f);
        ((body.getFixtureList()).get(0)).setDensity(6f);
        ((body.getFixtureList()).get(0)).setFriction(1f);
        ((circleBody.getFixtureList()).get(0)).setFilterData(f);
        body.resetMassData();

        this.currentState = PlayerState.STANDING;
        this.previousState = PlayerState.STANDING;
        this.timeInCurrentState = 0;




        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();
        this.sprites = sprites;
        this.hud = hud;

        this.dir = 1;

        this.walkAnimation = new Animation(this.sprites, frameSpeed);

        jumpSpeed = 1.6f;
    }

    public void update(float dt){

        this.switching = false;
        super.update(dt);

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
        }
        timeInCurrentState = currentState == previousState ? timeInCurrentState + dt : 0;
        previousState = currentState;
        return region;
    }

    public void resetPlayer(){
        hud.decreaseLives();
        hud.update();
        setXYvelocity(0,0) ;
        setTransform(150 / LearningGdx.PPM, 230 / LearningGdx.PPM, 0);
        //camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 + 150/ LearningGdx.PPM,0);


        if(hud.getNumLives() == 0){

            Play.setRunning(false);

            hud.showGameOver();
        }
    }

    public float getX(){return this.x;}
    public float getY(){return this.y;}
    public int getDir(){return this.dir; }
    public void setFirstAnimationFrame(int i){
        this.walkAnimation.setStartingFrame(1);
    }
    public Vector2 getPosition(){ return this.body.getPosition(); }
    public void setMovementSpeed(float speed){this.movementSpeed = speed; }
    public void setMaxMovSpeed(float cap){this.maxMovSpeed = cap;}
    public void setJumpSpeed(float jumpSpeed){this.jumpSpeed = jumpSpeed;}

    public void doSwitch(){
        this.switching = true;
    }
    public boolean isSwitching(){ return this.switching; }
    public PlayerState getState(){

        if(this.body.getLinearVelocity().x <= 0.09f && this.body.getLinearVelocity().x >= -0.09f )
            return PlayerState.STANDING;
        else
            return PlayerState.WALKING;
    }

}
