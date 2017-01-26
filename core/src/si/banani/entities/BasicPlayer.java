package si.banani.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import si.banani.learning.LearningGdx;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 6.12.2016.
 */

public abstract class BasicPlayer {

    protected Body body, circleBody;
    protected BodyDef bdef;
    protected FixtureDef fdef;
    protected World world;
    protected PolygonShape shape;
    protected RevoluteJoint revoluteJoint;
    protected RevoluteJointDef revoluteJointDef;
    protected Fixture footFixture;
    protected boolean hasFloor = true;

    protected float x, y;

    protected float movementSpeed, maxMovSpeed, jumpSpeed;

    protected PlayerState previousState, currentState;
    protected float timeInCurrentState;

    protected int dir;
    protected int width, height;

    //movement booleans
    public boolean left, right, up, down;
    protected boolean isJumping, isFalling;

    protected BasicPlayer target = null;
    protected float damage = 1f;

    protected float health = 100f;

    protected int numFootContants = 0;
    protected  int jumpTimeout = 0;

    protected boolean isControlled = false;

    public BasicPlayer(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType){
        this.world = world;
        this.timeInCurrentState = 0;

        this.movementSpeed = 10f;
        this.maxMovSpeed = 1.65f;
        this.jumpSpeed = 4f;

        this.width = width;
        this.height = height;

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

        //defining the circle body
        bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x / LearningGdx.PPM, y / LearningGdx.PPM - 100/LearningGdx.PPM);
        circleBody = world.createBody(bdef);
    }
    public void defineShape(int width, int height){
        this.shape = new PolygonShape();
        shape.setAsBox(width / LearningGdx.PPM, height/ LearningGdx.PPM);
    }
    public void defineFixture(){
        this.fdef = new FixtureDef();
        fdef.shape = this.shape;
        fdef.restitution = 0f;
        fdef.density = 1f;


        //fixture definition for the circle fixture
        FixtureDef fdef2 = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(10f/LearningGdx.PPM);

        fdef2.shape = circleShape;
        fdef2.density = 2f;
        fdef2.restitution = 0f;
        fdef2.friction = 2f;




        this.body.createFixture(fdef);
        this.body.setFixedRotation(true);

        shape = new PolygonShape();
        //shape.setAsBox(6f/LearningGdx.PPM, 4f /LearningGdx.PPM);
        float [] verticies = {-3,-34,3,-34,3,-29,-3,-29 };
        for(int i = 0; i < verticies.length; i++)
            verticies[i] = verticies[i] /LearningGdx.PPM;
        shape.set(verticies);

        FixtureDef footSensor = new FixtureDef();
        footSensor.shape = shape;
        footSensor.isSensor = true;



        Filter f = new Filter();
        f.categoryBits = CollisionBits.SENSOR_BIT;
        f.maskBits = CollisionBits.ENEMY_BIT |
                CollisionBits.SPIKES_BIT |
                CollisionBits.DEFAULT_BIT |
                CollisionBits.OBJECT_BIT |
                CollisionBits.GHOST_PATH_BIT;

        footFixture = body.createFixture(footSensor);
        footFixture.setFilterData(f);

        f.categoryBits = CollisionBits.PLAYER_BIT;


        this.circleBody.createFixture(fdef2).setFilterData(f);
        circleBody.setFixedRotation(false);
        //the revolute joint def
        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = body;
        revoluteJointDef.bodyB = circleBody;
        revoluteJointDef.localAnchorA.set(0, -20/LearningGdx.PPM);

        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.maxMotorTorque = 200;
        revoluteJointDef.motorSpeed = 0;

        revoluteJoint = (RevoluteJoint) world.createJoint(revoluteJointDef);

    }
    public void update(float dt){
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;

        jumpTimeout --;

        if(numFootContants > 0)
        {
            isFalling= false;
            isJumping = false;
        }else if(getYvelocity() > 0){
            isJumping = true;
            isFalling = false;
        }else {
            isFalling = true;
            isJumping = false;
        }


        //push the body according to the set direction of movement and also check speed constraints
        if(left && getXvelocity() >= -maxMovSpeed)
        {

            //this.body.applyLinearImpulse(new Vector2(-movementSpeed, 0), body.getWorldCenter(), true);
            this.body.applyForceToCenter(new Vector2(-movementSpeed/2, 0), true );
            revoluteJoint.setMotorSpeed(movementSpeed);
            dir = -1;
        }else if(!right){
            revoluteJoint.setMotorSpeed(0);
        }

        if(right && getXvelocity() <= maxMovSpeed)
        {
            //this.body.applyLinearImpulse(new Vector2(movementSpeed, 0), body.getWorldCenter(), true);
            this.body.applyForceToCenter(new Vector2(movementSpeed/2, 0), true );
            revoluteJoint.setMotorSpeed(-movementSpeed);
            dir = 1;
        }else if(!left){
            revoluteJoint.setMotorSpeed(0);
        }

        if(up && !isFalling && !isJumping && jumpTimeout <= 0 && getYvelocity() <= jumpSpeed)
        {
            jumpTimeout = 15;
            this.body.applyLinearImpulse(new Vector2(0, jumpSpeed*1.3f), body.getWorldCenter(), true);

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

    public abstract  void render(SpriteBatch sb, float dt);
    abstract PlayerState getState();


    public void setXYvelocity(float x, float y){
        this.body.setLinearVelocity(x,y);
    }
    public void setTransform(float x, float y, float z){
        this.body.setTransform(x,y, z);
        this.circleBody.setTransform(x,y,z);
    }
    public void setFriction(float f){
        this.body.getFixtureList().get(0).setFriction(f);
    }


    public float getXvelocity(){return this.body.getLinearVelocity().x;}
    public float getYvelocity(){return this.body.getLinearVelocity().y;}
    public Vector2 getPosition(){ return this.body.getPosition(); }
    public int getDir(){return this.dir;}
    public void increaseFootContacts(int num){

        this.numFootContants += num;}
    public void setActive(boolean b ){
        this.isControlled = b;
    }
}
