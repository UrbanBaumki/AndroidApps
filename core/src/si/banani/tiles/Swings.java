package si.banani.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 29.1.2017.
 */

public class Swings {

    private float x1,y1,x2,y2, yOffset = 7.5f;
    private TextureRegion swing, circle;
    private float width, height;
    Body body, circleBody;


    public Swings(World world, Polygon rect){

        swing = new TextureRegion(new Texture(Gdx.files.internal("textures/props/swings.png")));
        circle = new TextureRegion(new Texture(Gdx.files.internal("textures/props/swingsCircle.png")));

        width = swing.getRegionWidth();
        height = swing.getRegionHeight();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();


        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set( (rect.getX()  ) / LearningGdx.PPM , (rect.getY()  ) / LearningGdx.PPM);



        body = world.createBody(bdef);


        //swing fixture
        PolygonShape pS = new PolygonShape();

        float [] v2 = rect.getVertices();


        for( int i = 0;i < v2.length; i++){

            v2[i]= v2[i] / LearningGdx.PPM;


        }
        pS.set(v2);


        fdef.shape = pS;
        fdef.friction = 1f;
        fdef.density = 1f;

        fdef.restitution = 0f;


        body.createFixture(fdef);


        //the circle
        bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set((rect.getX()  ) / LearningGdx.PPM, (rect.getY()  ) / LearningGdx.PPM - 20/LearningGdx.PPM);

         circleBody = world.createBody(bdef);


        //fixture definition for the circle fixture
        FixtureDef fdef2 = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(8f/LearningGdx.PPM);

        fdef2.shape = circleShape;
        fdef2.density = 200f;
        fdef2.restitution = 0f;
        fdef2.friction = 2f;

        circleBody.createFixture(fdef2);
        circleBody.setFixedRotation(true);


        //the revolute joint def
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = body;
        revoluteJointDef.bodyB = circleBody;
        revoluteJointDef.localAnchorA.set(rect.getBoundingRectangle().getWidth()/2, -10/LearningGdx.PPM);

        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.maxMotorTorque = 1;
        revoluteJointDef.motorSpeed = 0;

        world.createJoint(revoluteJointDef);

        pS.dispose();


    }

    public void update(float dt){
        x1 = body.getPosition().x;
        y1 = body.getPosition().y;
    }

    public void render(SpriteBatch batch, float dt){
        float angle = (float)(body.getAngle() * 180 / Math.PI) ;
        batch.draw(swing, circleBody.getPosition().x - width/2/LearningGdx.PPM, circleBody.getPosition().y + yOffset/LearningGdx.PPM, width / 2 / LearningGdx.PPM, height / 2 / LearningGdx.PPM, width / LearningGdx.PPM, height / LearningGdx.PPM, 1, 1, angle, true);
        batch.draw(circle, circleBody.getPosition().x - circle.getRegionWidth()/2/LearningGdx.PPM , circleBody.getPosition().y - circle.getRegionHeight()/2/LearningGdx.PPM, circle.getRegionWidth()/LearningGdx.PPM, circle.getRegionHeight()/LearningGdx.PPM);
    }

}
