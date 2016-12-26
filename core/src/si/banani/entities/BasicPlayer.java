package si.banani.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 6.12.2016.
 */

public abstract class BasicPlayer {

    protected Body body;
    protected BodyDef bdef;
    protected FixtureDef fdef;
    protected World world;
    protected PolygonShape shape;

    protected float x, y;

    protected float movementSpeed, maxMovSpeed, jumpSpeed;

    protected PlayerState previousState, currentState;
    protected float timeInCurrentState;

    protected int dir;
    protected int width, height;

    //movement booleans
    public boolean left, right, up, down;
    private boolean isJumping, isFalling;


    public BasicPlayer(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType){
        this.world = world;
        this.timeInCurrentState = 0;

        this.movementSpeed = 0.2f;
        this.maxMovSpeed = 1.5f;
        this.jumpSpeed = 4f;

        this.x = x;
        this.y = y;

        this.dir = 1;

        //creates the player by defining body , shape and fixture
        defineBody(x,y, bodyType);
        defineShape(width, height);
        defineFixture();
    }
    public void defineBody(int x, int y, BodyDef.BodyType bodyType){
        this.bdef = new BodyDef();
        bdef.position.set(x / LearningGdx.PPM, y / LearningGdx.PPM);
        bdef.type = bodyType;

        this.body = this.world.createBody(bdef);
    }
    public void defineShape(int width, int height){
        this.shape = new PolygonShape();
        shape.setAsBox(width / LearningGdx.PPM, height/ LearningGdx.PPM);
    }
    public void defineFixture(){
        this.fdef = new FixtureDef();
        fdef.shape = this.shape;
        //fdef.density = 1f;

        this.body.createFixture(fdef);
        this.body.setFixedRotation(true);

    }
    public void update(float dt){
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;

        if(getYvelocity() < 0)
        {
            isFalling = true;
            isJumping = false;
        }
        else
            isFalling = false;

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

        if(up && !isJumping && !isFalling && getYvelocity() <= jumpSpeed)
        {
            isJumping = true;
            this.body.applyLinearImpulse(new Vector2(0, jumpSpeed), body.getWorldCenter(), true);
        }
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

    abstract  void render(SpriteBatch sb, float dt);
    abstract PlayerState getState();


    public void setXYvelocity(float x, float y){
        this.body.setLinearVelocity(x,y);
    }
    public void setTransform(float x, float y, float z){
        this.body.setTransform(x,y, z);
    }
    public void setFriction(float f){
        this.body.getFixtureList().get(0).setFriction(f);
    }


    public float getXvelocity(){return this.body.getLinearVelocity().x;}
    public float getYvelocity(){return this.body.getLinearVelocity().y;}

}
