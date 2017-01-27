package si.banani.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.animation.Animation;
import si.banani.controller.PlayerMovementController;
import si.banani.learning.LearningGdx;
import si.banani.scenes.Hud;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 27.12.2016.
 */

public class FemalePlayer extends BasicPlayer {

    private float yOffset = 6f;
    private float offDir = 1f;
    private Animation floatAnimation;
    private float energyLevel = 100f;
    private float energyDrainSpeed = 1f;
    private Hud hud;


    public FemalePlayer(World world, int x, int y, int width, int height, BodyDef.BodyType bodyType, TextureRegion[] sprites, float frameSpeed, Hud hud) {
        super(world, x, y, width, height, bodyType);
        this.hud = hud;
        ((body.getFixtureList()).get(0)).setUserData(this);

        Filter f = new Filter();
        f.categoryBits = CollisionBits.GHOST_BIT;
        f.maskBits = CollisionBits.ENEMY_BIT |
                CollisionBits.SPIKES_BIT |
                CollisionBits.DEFAULT_BIT |
                CollisionBits.OBJECT_BIT;

        ((body.getFixtureList()).get(0)).setFilterData(f);
        ((body.getFixtureList()).get(1)).setFilterData(f);
        ((body.getFixtureList()).get(0)).setDensity(6f);
        ((body.getFixtureList()).get(0)).setFriction(1f);
        ((circleBody.getFixtureList()).get(0)).setDensity(6f);
        ((circleBody.getFixtureList()).get(0)).setFilterData(f);
        body.resetMassData();

        footFixture.setUserData(this);

        this.currentState = PlayerState.STANDING;
        this.previousState = PlayerState.STANDING;
        this.timeInCurrentState = 0;

        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();
        dir = 1;

        floatAnimation = new Animation(sprites, frameSpeed);
        jumpSpeed = 1.6f;

    }
    @Override
    public void update(float dt){
        super.update(dt);

        if(isControlled){
            this.energyLevel -= energyDrainSpeed * dt;
            if(energyLevel < 0){
                PlayerMovementController.getInstance().switchPlayer();
            }else
                hud.setEnergyLevel(energyLevel);
        }

        //Gdx.app.log("Falling", String.format("%d", numFootContants));
    }

    @Override
    public void render(SpriteBatch sb, float dt) {

        //just a hardcoded float offset animation, to save cpu time from calculating sin function :)
        if(yOffset > 6f)
            offDir = -1f;
        else if(yOffset < 1f)
            offDir = 1f;

        yOffset += dt * offDir * 5f;

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
