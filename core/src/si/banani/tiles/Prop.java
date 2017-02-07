package si.banani.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 25.1.2017.
 */

public class Prop extends InteractiveTile {


    private float x, y;
    private int width, height;
    private float yOffset;
    private TextureRegion region;

    public Prop(World world, Rectangle rect, float density){
        super(world, rect);

        fixture.setUserData(this);

        setCategoryFilter(CollisionBits.OBJECT_BIT);

        body.setType(BodyDef.BodyType.DynamicBody);
        fixture.setRestitution(0f);
        fixture.setDensity(density);
        fixture.setFriction(0.3f);
        body.resetMassData();






         region = new TextureRegion(new Texture(Gdx.files.internal("textures/props/swings.png")));


        this.width = region.getRegionWidth();
        this.height = region.getRegionHeight();



        this.destroyed = false;
        this.destroy = false;
        this.yOffset = 0.5f;


    }
    @Override
    public void render(SpriteBatch batch, float dt) {
        if(!destroyed) {
            float angle = (float)(body.getAngle() * 180 / Math.PI);
            batch.draw(region, x - width / 2 / LearningGdx.PPM, y - height / 2 / LearningGdx.PPM - yOffset / LearningGdx.PPM, width / 2 / LearningGdx.PPM, height / 2 / LearningGdx.PPM, width / LearningGdx.PPM, height / LearningGdx.PPM, 1, 1, angle, true);
        }
    }

    public void update(float dt){

        if(destroy && !destroyed){
            world.destroyBody(body);
        }else{
            x = body.getPosition().x;
            y = body.getPosition().y;

        }

    }
    public void onSpikeHit(){
        this.destroy = true;

    }

    public boolean isSetForDestruction(){
        return this.destroy;
    }
    public void setDestroyed(boolean set){
        this.destroyed = set;
    }
    public Body getBody(){ return this.body; }
}
