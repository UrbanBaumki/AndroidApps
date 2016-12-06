package si.banani.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;


/**
 * Created by Urban on 6.12.2016.
 */

public class Player extends BasicPlayer {

    private float x, y;
    private int yOffset = 6;
    private float movementSpeed, maxMovSpeed, jumpSpeed;

    private int dir = 1;
    private int width, height;

    private Animation walkAnimation;
    private TextureRegion[] sprites;

    private boolean left, right, up, down;
    private boolean isJumping, isFalling;

    public Player(World world, TextureRegion[] sprites, float frameSpeed) {
        this(world, 0, 0, sprites, frameSpeed);
    }

    public Player(World world, int x, int y, TextureRegion[] sprites, float frameSpeed) {
        this(world, x, y , 20, 20, BodyDef.BodyType.DynamicBody, sprites, frameSpeed);
    }

    public Player(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, float frameSpeed) {
        super(world, x, y, width, height, bodyType);
        this.x = x;
        this.y = y;
        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();
        this.sprites = sprites;
        this.movementSpeed = 0.2f;
        this.maxMovSpeed = 1.5f;
        this.jumpSpeed = 4f;
        this.walkAnimation = new Animation(this.sprites, frameSpeed);
    }

    public void update(float dt){
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;

        //push the body according to the set direction of movement and also check speed constraints
        if(left && getXvelocity() >= -maxMovSpeed)
        {
            this.body.applyLinearImpulse(new Vector2(-movementSpeed, 0), body.getWorldCenter(), true);
            dir = -1;
        }

        if(right && getXvelocity() <= maxMovSpeed)
        {
            this.body.applyLinearImpulse(new Vector2(movementSpeed, 0), body.getWorldCenter(), true);
            dir = 1;
        }

        if(up)
            this.body.applyLinearImpulse(new Vector2(0, jumpSpeed), body.getWorldCenter(), true);

        
    }
    public void render(SpriteBatch sb, float dt){

        sb.draw(getCurrentFrame(), x  - dir * width /2/ LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width / LearningGdx.PPM, height / LearningGdx.PPM);

        if(left || right)
            walkAnimation.update(dt);
        else
            walkAnimation.reset();
    }

    public void setXYvelocity(float x, float y){
        this.body.setLinearVelocity(x,y);
    }
    public void setTransform(float x, float y, float z){
        this.body.setTransform(x,y, z);
    }
    public void setFriction(float f){
        this.body.getFixtureList().get(0).setFriction(f);
    }
    public void setMovementSpeed(float speed){this.movementSpeed = speed; }
    public void setMaxMovSpeed(float cap){this.maxMovSpeed = cap;}
    public void setJumpSpeed(float jumpSpeed){this.jumpSpeed = jumpSpeed;}

    public float getXvelocity(){return this.body.getLinearVelocity().x;}
    public float getYvelocity(){return this.body.getLinearVelocity().y;}
    public TextureRegion getCurrentFrame(){
        return this.walkAnimation.getCurrentFrame();
    }
    public void goLeft(){this.left = true;}
    public void goRight(){this.right = true;}
    public void goUp(){this.up = true;}
    public void goDown(){this.down = true;}
    public void stopLeft(){this.left = false;}
    public void stopRight(){this.right = false;}
    public void stopUp(){this.up = false;}
    public void stopDown(){
        this.down = false;
    }
    public float getX(){return this.x;}
    public float getY(){return this.y;}
}
