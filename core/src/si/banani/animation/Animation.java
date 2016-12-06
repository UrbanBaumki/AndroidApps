package si.banani.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import si.banani.textures.TextureManager;

/**
 * Created by Urban on 30.10.2016.
 */

public class Animation {

    private TextureRegion[] sprites;
    private int startingFrame;
    private int currentFrame;
    private float frameSpeed;
    private float timer;
    private int direction;
    private boolean yoyoEnabled;
    public Animation(TextureRegion[] sprites, float frameSpeed){
        this(sprites, frameSpeed, 0);
    }
    public Animation(TextureRegion[] sprites, float frameSpeed, int startingFrame){
        this.sprites = sprites;
        this.frameSpeed = frameSpeed;
        this.startingFrame = startingFrame;
        this.currentFrame = startingFrame; //usually 0 :)
        this.direction = 1;
        this.yoyoEnabled = false;

    }
    public void setStartingFrame(int i ){
        this.startingFrame = i;
        this.currentFrame = startingFrame;
    }
    public void update(float delta){
        timer += delta;
        if(timer >= frameSpeed){
            nextFrame();
        }
    }
    public void nextFrame(){
        currentFrame += 1* direction;
        if(currentFrame == sprites.length)
            currentFrame = startingFrame;
        timer -= frameSpeed;
    }
    public void reset(){
        currentFrame = startingFrame;
    }
    public void changeDirection(){
        this.direction *= -1;
    }
    public void setYoyoEnabled(boolean set){ this.yoyoEnabled = set; }
    public void setFrame(int frameNum){
        this.currentFrame = frameNum;
    }

    public TextureRegion getCurrentFrame(){
        return this.sprites[currentFrame];
    }

}
