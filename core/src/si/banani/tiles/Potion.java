package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import si.banani.learning.LearningGdx;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 27.1.2017.
 */

public class Potion extends InteractiveTile implements AudioSubject{

    Array<AudioObserver> _observers = new Array<AudioObserver>();

    public void loadSounds(){

        notify(AudioObserver.AudioCommand.SOUND_LOAD, AudioObserver.AudioTypeEvent.SOUND_POTION);
    }
    @Override
    public void addObserver(AudioObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(AudioObserver observer) {
        _observers.removeValue(observer, true);
    }

    @Override
    public void removeAllObservers() {
        _observers.removeAll(_observers, true);
    }

    @Override
    public void notify(AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event) {
        for(AudioObserver observer : _observers)
            observer.onNotify(command, event);
    }
    public void playGulp(){
        notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_POTION);
    }

    public enum PotionType{
        HEALTH,
        ENERGY
    }
    private float offDir = 1f;
    private float x, y;
    private int width, height;
    private int dir = 1;
    private float yOffset, xOffset;
    private TextureRegion textureRegion;
    private PotionType type;
    private int value = 20;

    public Potion(World world, Rectangle rect, TextureRegion[] sprites , PotionType type) {
        super(world, rect);

        this.type = type;

        yOffset = -3f;
        xOffset = -3f;

        this.width = sprites[0].getRegionWidth();
        this.height = sprites[0].getRegionHeight();
        textureRegion = sprites[0];

        fixture.setUserData(this);
        fixture.setSensor(true);

        x = body.getPosition().x;
        y = body.getPosition().y;

        this.addObserver(AudioManager.getInstance());
        loadSounds();

    }

    public int getValue(){return this.value; }
    @Override
    public void render(SpriteBatch batch, float dt) {
        if (!destroyed) {
            if(yOffset > 1f)
                offDir = -1f;
            else if(yOffset < -1f)
                offDir = 1f;

            yOffset += dt * offDir * 8f;

            batch.draw(textureRegion, x - dir * width / 2 / LearningGdx.PPM, y - height / 2 / LearningGdx.PPM + yOffset / LearningGdx.PPM, dir * width / LearningGdx.PPM, height / LearningGdx.PPM);
        }
    }

    @Override
    public void update(float dt) {
        if (destroy && !destroyed) {
            world.destroyBody(body);
        }else{
            x = body.getPosition().x;
            y = body.getPosition().y;
        }


    }
    public void pickup(){
        this.destroy = true;
        playGulp();
    }
    public PotionType getPotionType(){
        return type;
    }

    public boolean isSetForDestruction(){
        return this.destroy;
    }
    public void setDestroyed(boolean set){
        this.destroyed = set;
    }
    public Body getBody(){ return this.body; }
}