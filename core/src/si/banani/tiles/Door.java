package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.camera.CameraEffects;
import si.banani.learning.LearningGdx;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 29.12.2016.
 */

public class Door extends InteractiveTile {

    private Animation doorAnimation;
    private int width, height;
    private float yOffset;
    private int dir;
    private TileStates currentState, previousState;
    private boolean open;
    private float timeInCurrentState;

    public Door(World world, Rectangle rectangle, TextureRegion[] sprites, float frameSpeed) {
        super(world, rectangle);
        fixture.setUserData(this);

        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();


        setCategoryFilter(CollisionBits.DOORS_BIT);

        doorAnimation = new Animation(sprites, frameSpeed);

        this.yOffset = 0.5f;
        dir = 1;

        currentState = previousState = TileStates.CLOSED;
        timeInCurrentState = 0;

    }
    @Override
    public void render(SpriteBatch batch, float dt) {
        batch.draw(getCurrentFrame(dt), body.getPosition().x  - dir * width /2/ LearningGdx.PPM, body.getPosition().y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width / LearningGdx.PPM, height / LearningGdx.PPM);
    }

    @Override
    public void update(float dt) {

    }

    private TextureRegion getCurrentFrame(float dt){
        currentState = getState();
        TextureRegion region  = null;
        switch (currentState){
            case CLOSED:
                region = doorAnimation.getFirstFrame();
                break;
            case OPENED:
                region = doorAnimation.getLastFrame();
                break;
        }
        timeInCurrentState = currentState == previousState ? timeInCurrentState + dt : 0;
        previousState = currentState;
        return region;
    }

    public void setOpen(boolean b){

        this.open = b;

        if(open)
            setCategoryFilter(CollisionBits.NONCOLLISION_BIT);
        else
            setCategoryFilter(CollisionBits.DOORS_BIT);

    }

    private TileStates getState(){
        TileStates state = TileStates.CLOSED;

        if(open)
            state = TileStates.OPENED;

        return state;
    }
}
