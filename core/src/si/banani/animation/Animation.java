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
    private boolean isLooping;
    private boolean finished;
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
        this.isLooping = true;

    }
    public void setStartingFrame(int i ){
        this.startingFrame = i;
        this.currentFrame = startingFrame;
    }
    public void update(float delta){
        if(frameSpeed == 0)
            return;

        timer += delta;
        if(timer >= frameSpeed){
            nextFrame();
        }
    }
    public void nextFrame(){
        currentFrame += 1* direction;

        if(direction == 1) {

            if (currentFrame == sprites.length) {

                if(isLooping){

                    if (yoyoEnabled) {
                        this.changeDirection();
                        currentFrame += 1*direction;
                    }
                    else
                        currentFrame = startingFrame;

                }else{
                    currentFrame = sprites.length - 1;
                    finished = true;
                    }
                }
            }
        else
        {
            if (currentFrame == -1 )
                if(isLooping)
                {
                    if(yoyoEnabled){
                        this.changeDirection();
                        currentFrame += 1*direction;
                    }else
                        currentFrame = sprites.length - 1;
                }
                else {
                    currentFrame = 0;
                    finished = true;
                }
        }
        timer -= frameSpeed;
    }
    public TextureRegion getFirstFrame(){ return this.sprites[0]; }
    public TextureRegion getLastFrame(){ return this.sprites[sprites.length-1]; }
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
    public void setLooping(boolean looping){ this.isLooping = looping; }
    public boolean isFinished(){return this.finished; }
    public void resetFinished(){ this.finished = false; }
    public TextureRegion getCurrentFrame(){
        return this.sprites[currentFrame];
    }

}
