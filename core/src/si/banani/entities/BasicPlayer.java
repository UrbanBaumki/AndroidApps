package si.banani.entities;

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


    public BasicPlayer(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType){
        this.world = world;
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

        this.body.createFixture(fdef);

    }

}
