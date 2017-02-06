package si.banani.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import si.banani.animation.Animation;
import si.banani.learning.LearningGdx;
import si.banani.textures.TextureManager;

/**
 * Created by Urban on 6.2.2017.
 */

public class CutSceneAnimated extends Image{

    private Animation animation;

    private boolean animate = true;
    public CutSceneAnimated(String regionName, int splitWidth, int splitHeight, float frameSpeed){
        super();
        animation = new Animation(TextureManager.findRegionByName("contentAtlas", regionName).split(splitWidth, splitHeight)[0], frameSpeed);

        this.setDrawable(new TextureRegionDrawable());
        this.setSize(splitWidth/LearningGdx.PPM, splitHeight/LearningGdx.PPM);
    }

    @Override
    public void act(float delta){
        Drawable drawable = this.getDrawable();
        if( drawable == null ) {

            return;
        }
        if(animate)
            animation.update(delta);
        TextureRegion region = animation.getCurrentFrame();

        ((TextureRegionDrawable) drawable).setRegion(region);
        super.act(delta);
    }
    public void setAnimate(boolean b ){
        animate = b;
    }
}
