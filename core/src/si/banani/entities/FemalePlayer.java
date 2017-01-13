package si.banani.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 27.12.2016.
 */

public class FemalePlayer extends BasicPlayer {

    private int yOffset = 6;

    private Animation floatAnimation;

    public FemalePlayer(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, float frameSpeed) {
        super(world, x, y, width, height, bodyType);

        ((body.getFixtureList()).get(0)).setUserData(this);

        Filter f = new Filter();
        f.categoryBits = CollisionBits.GHOST_BIT;
        f.maskBits = CollisionBits.ENEMY_BIT |
                CollisionBits.SPIKES_BIT |
                CollisionBits.DEFAULT_BIT |
                CollisionBits.OBJECT_BIT |
                CollisionBits.SWITCH_BIT;

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
        dir = 1;

        floatAnimation = new Animation(sprites, frameSpeed);
    }

    @Override
    public void render(SpriteBatch sb, float dt) {
        sb.draw(getCurrentFrame(dt), x  - dir * width /2/ LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width / LearningGdx.PPM, height / LearningGdx.PPM);
    }
    private TextureRegion getCurrentFrame(float dt){
        PlayerState currentState = getState();
        TextureRegion region = null;
        switch (currentState){
            case STANDING:
                region = floatAnimation.getFirstFrame();
                break;
            case FOLLOWING:
            case WALKING:
                region = floatAnimation.getCurrentFrame();
                floatAnimation.update(dt);
                break;
        }
        return region;
    }
    public Body getBody(){
        return this.body;
    }

    @Override
    PlayerState getState() {
        return currentState;
    }
}
