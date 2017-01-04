package si.banani.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 26.12.2016.
 */

public class Switch extends InteractiveTile {

    private Animation switchAnimation;
    private float x, y;
    private float width, height;
    private float yOffset;
    private int dir = 1;
    private boolean isOn, hasBeenActivated, canBeSwitched ;
    private TileStates currentState, previousState;
    private float timeInCurrentState;
    private Door door;


    public Switch(World world, Rectangle rectangle, TextureRegion[] sprites, float frameSpeed){
        super(world, rectangle);
        fixture.setUserData(this);

        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();

        setCategoryFilter(CollisionBits.SWITCH_BIT);

        body.setType(BodyDef.BodyType.StaticBody);
        fixture.setSensor(true);

        switchAnimation = new Animation(sprites, frameSpeed);
        switchAnimation.setLooping(false);
        switchAnimation.changeDirection();

        this.yOffset = 0.5f;

        canBeSwitched = true;

        currentState = previousState = TileStates.OFF;
        timeInCurrentState = 0;
    }
    @Override
    public void render(SpriteBatch batch, float dt) {
        batch.draw(getCurrentFrame(dt), x  - dir * width /2/ LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width*1.2f / LearningGdx.PPM, height*1.2f / LearningGdx.PPM);
    }

    @Override
    public void update(float dt) {
        x = body.getPosition().x;
        y = body.getPosition().y;

    }
    private TextureRegion getCurrentFrame(float dt){
        currentState = getState();
        TextureRegion region = null;
        switch (currentState){
            case OFF:
            case ON:
                region = this.switchAnimation.getCurrentFrame();
                break;
            case SWITCHING:
                region = this.switchAnimation.getCurrentFrame();
                switchAnimation.update(dt);
                break;
        }
        timeInCurrentState = currentState == previousState ? timeInCurrentState + dt : 0;
        previousState = currentState;
        return region;
    }
    public void activate(){
        this.hasBeenActivated = true;
    }
    private TileStates getState(){


        if(switchAnimation.isFinished() ) {
            switchAnimation.resetFinished();
            canBeSwitched = true;
            if (!isOn) {
                isOn = true;
                //switch the doors
                if(door != null)
                    this.door.setOpen(true);
                return TileStates.ON;
            } else {
                isOn = false;
                //close the doors
                if(door != null)
                    this.door.setOpen(false);
                return TileStates.OFF;
            }
        }

        if(hasBeenActivated && canBeSwitched){
            hasBeenActivated = false;
            canBeSwitched = false;
            switchAnimation.changeDirection();
            return TileStates.SWITCHING;
        }
        return currentState;
    }
    public void addDoor(Door door){
        this.door = door;
    }

}
