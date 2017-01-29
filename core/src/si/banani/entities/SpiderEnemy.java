package si.banani.entities;

import com.badlogic.gdx.Gdx;
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
 * Created by Urban on 16.12.2016.
 */

public class SpiderEnemy extends BasicPlayer {

    private Player target;
    private TextureRegion[] sprites;
    private float yOffset = 0.5f;
    private float climbOffset = 0.4f;
    private float triggerDistance = 0.3f;
    private float hideSpotY;
    private Animation walk, attack;

    public SpiderEnemy(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, TextureRegion[] sprites2, float frameSpeed, float frameSpeed2, Player player){
        super(world, x, y, width, height, bodyType);

        this.climbOffset += y/LearningGdx.PPM;
        this.target = player;

        this.currentState = PlayerState.SLEEPING;
        this.previousState = PlayerState.SLEEPING;

        this.x = x;
        this.y = y;
        this.hideSpotY = y / LearningGdx.PPM;
        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();


        this.sprites = sprites;

        this.dir = 1;
        this.movementSpeed = 1f;
        this.maxMovSpeed = 1.2f;
        this.jumpSpeed = 4f;

        this.walk = new Animation(this.sprites, frameSpeed);
        this.attack = new Animation(sprites2, frameSpeed2);

        Filter f = new Filter();
        f.categoryBits = CollisionBits.SPIDER_ENEMY_BIT;
        f.maskBits = CollisionBits.SPIKES_BIT;


        ((body.getFixtureList()).get(0)).setFilterData(f);
    }

    @Override
    public void update(float dt) {
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;


        if(currentState == PlayerState.FOLLOWING ){
            this.body.setLinearVelocity(new Vector2(0, movementSpeed));
        }else if(currentState == PlayerState.ATTACKING || currentState == PlayerState.SLEEPING){
            this.body.setLinearVelocity(new Vector2(0, 0));
        }else if(currentState == PlayerState.HIDDING ){
            this.body.setLinearVelocity(new Vector2(0, -movementSpeed));
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
            case HIDDING:
                currFrame = this.walk.getCurrentFrame();
                this.walk.update(dt);
                break;
            case ATTACKING:
                currFrame = this.attack.getCurrentFrame();
                this.attack.update(dt);
                break;

        }
        timeInCurrentState = currentState == previousState ? timeInCurrentState + dt : 0;
        previousState = currentState;
        return currFrame;
    }

    @Override
    public PlayerState getState() {

        PlayerState state = currentState;

        float tx = target.getPosition().x;
        float dist = Math.abs(this.body.getPosition().x - tx);

        if(this.currentState == PlayerState.SLEEPING){

            if(dist <= triggerDistance){
                state = PlayerState.FOLLOWING;
            }
        }else if(currentState == PlayerState.FOLLOWING){
            if(dist > triggerDistance)
                state = PlayerState.HIDDING;
            else
            {

                if(this.y >= this.climbOffset)
                {
                    state = PlayerState.ATTACKING;
                }

            }
        }else if(currentState == PlayerState.ATTACKING){

            if(this.timeInCurrentState >= 1.15f)
            {
                state = PlayerState.HIDDING;
            }


        }else if(currentState == PlayerState.HIDDING){
            if(this.y <= this.hideSpotY)
            {
                state = PlayerState.SLEEPING;
            }

        }

        return state;
    }

    @Override
    public void doSwitch() {

    }
}
