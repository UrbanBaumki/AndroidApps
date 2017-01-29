package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 8.12.2016.
 */

public abstract class InteractiveTile {
    protected BodyDef bdef;
    protected PolygonShape shape ;
    protected FixtureDef fdef;
    protected Fixture fixture;
    protected Rectangle rectangle;
    protected Body body;
    protected World world;
    protected boolean destroyed, destroy;

    public InteractiveTile(World world, Rectangle rectangle){
        this.world = world;
        this.rectangle = rectangle;
        bdef = new BodyDef();
        shape = new PolygonShape();
        fdef = new FixtureDef();
        createFixtures();
    }

    private void createFixtures(){
        Rectangle rect = rectangle;
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set( (rect.getX() + rect.getWidth()/2 ) / LearningGdx.PPM , (rect.getY() + rect.getHeight()/2 ) / LearningGdx.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(rect.getWidth()/2 / LearningGdx.PPM, rect.getHeight()/2 / LearningGdx.PPM);
        //fdef.density = 1f;
        fdef.shape = shape;

        fixture = body.createFixture(fdef);
    }
    public void setCategoryFilter(short filter){
        Filter f = new Filter();
        f.categoryBits = filter;
        fixture.setFilterData(f);
    }
    public abstract void render(SpriteBatch batch, float dt);
    public abstract void update(float dt);

    public Vector2 getPosition(){
        return body.getPosition();
    }
}
