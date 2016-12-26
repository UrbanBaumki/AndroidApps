package si.banani.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 15.12.2016.
 */

public class RockEnemy extends BasicPlayer {

    private TextureRegion[] sprites;
    private Player target;
    private Animation walk;
    private float yOffset = 0.5f;


    public RockEnemy(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, float frameSpeed, Player player) {
        super(world, x, y, width, height, bodyType);

        this.target = player;

        this.currentState = PlayerState.SLEEPING;
        this.previousState = PlayerState.SLEEPING;

        this.x = x;
        this.y = y;
        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();


        this.sprites = sprites;

        this.dir = 1;
        this.movementSpeed = 0.1f;
        this.maxMovSpeed = 0.95f;
        this.jumpSpeed = 4f;

        this.walk = new Animation(this.sprites, frameSpeed);
        this.walk.setStartingFrame(1);


        Filter f = new Filter();
        f.categoryBits = CollisionBits.ROCK_ENEMY_BIT;
        f.maskBits = CollisionBits.SPIKES_BIT |
                CollisionBits.DEFAULT_BIT ;

        ((body.getFixtureList()).get(0)).setFilterData(f);


    }

    @Override
    public void update(float dt) {
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;

        if(dir == -1 && getXvelocity() >= -maxMovSpeed && currentState == PlayerState.FOLLOWING)
        {
            this.body.applyLinearImpulse(new Vector2(-movementSpeed, 0), body.getWorldCenter(), true);

        }
        if(dir == 1 && getXvelocity() <= maxMovSpeed  && currentState == PlayerState.FOLLOWING)
        {
            this.body.applyLinearImpulse(new Vector2(movementSpeed, 0), body.getWorldCenter(), true);

        }

    }

    @Override
    public void render(SpriteBatch sb, float dt) {
        sb.draw(getCurrentFrame(dt), x  - dir * width /2/ LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width / LearningGdx.PPM, height / LearningGdx.PPM);
    }

    private TextureRegion getCurrentFrame(float dt){
        TextureRegion currFrame = null;
        this.currentState = getState();

        switch (currentState){
            case SLEEPING:
                currFrame = this.walk.getFirstFrame();
                this.walk.reset();
                this.body.setLinearVelocity(0,this.getYvelocity());
                break;
            case FOLLOWING:
                currFrame = this.walk.getCurrentFrame();
                this.walk.update(dt);
                break;
        }
        timeInCurrentState = currentState == previousState ? timeInCurrentState + dt : 0;
        previousState = currentState;
        return currFrame;
    }

    @Override
    public  PlayerState getState() {
        PlayerState state = PlayerState.SLEEPING;

        if(this.x <= target.getPosition().x)
            dir = 1;
        else
            dir = -1;

        if(target.getDir() == dir) {
            state = PlayerState.FOLLOWING;
        }else if(this.getXvelocity() == 0)
        {
            state = PlayerState.SLEEPING;

        }

        return state;
    }
}
