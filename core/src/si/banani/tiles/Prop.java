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

    private Animation box;
    private float x, y;
    private int width, height;
    private float yOffset;

    public Prop(World world, Rectangle rect){
        super(world, rect);

        fixture.setUserData(this);

        setCategoryFilter(CollisionBits.OBJECT_BIT);

        body.setType(BodyDef.BodyType.DynamicBody);
        fixture.setRestitution(0f);
        fixture.setDensity(0.8f);
        fixture.setFriction(0.3f);
        body.resetMassData();

        Texture b = new Texture(Gdx.files.internal("box.png"));
        b.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        TextureRegion reg = new TextureRegion(b, 0,0, 20,150);
        TextureRegion[] sprites = {reg};

        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();

        box = new Animation(sprites, 0);

        this.destroyed = false;
        this.destroy = false;
        this.yOffset = 0.5f;


    }
    @Override
    public void render(SpriteBatch batch, float dt) {
        if(!destroyed) {
            float angle = (float)(body.getAngle() * 180 / Math.PI) + 90;
            batch.draw(box.getCurrentFrame(), x - width / 2 / LearningGdx.PPM, y - height / 2 / LearningGdx.PPM - yOffset / LearningGdx.PPM, width / 2 / LearningGdx.PPM, height / 2 / LearningGdx.PPM, width / LearningGdx.PPM, height / LearningGdx.PPM, 1, 1, angle, true);
        }
    }

    public void update(float dt){

        if(destroy && !destroyed){
            world.destroyBody(body);
        }else{
            x = body.getPosition().x;
            y = body.getPosition().y;

            box.update(dt);
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
