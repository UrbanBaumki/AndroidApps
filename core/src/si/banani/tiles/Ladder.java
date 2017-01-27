package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 26.1.2017.
 */

public class Ladder extends InteractiveTile {

    private float x, y;
    private int width, height;
    private int dir = 1;
    private float yOffset, xOffset;
    private TextureRegion textureRegion;

    public Ladder(World world, Rectangle rect, TextureRegion[] sprites){
        super(world, rect);

        yOffset = -3f;
        xOffset = -3f;

        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();
        textureRegion = sprites[0];

        fixture.setUserData(this);
        fixture.setSensor(true);

        x = body.getPosition().x;
        y = body.getPosition().y;

        setCategoryFilter(CollisionBits.OBJECT_BIT);
    }
    @Override
    public void render(SpriteBatch batch, float dt) {
        batch.draw(textureRegion, x  - dir * width /2/ LearningGdx.PPM + xOffset/LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width*1.2f / LearningGdx.PPM, height*1.2f / LearningGdx.PPM);
    }

    @Override
    public void update(float dt) {

    }
}
