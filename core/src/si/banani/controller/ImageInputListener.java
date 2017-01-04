package si.banani.controller;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Urban on 27.12.2016.
 */

public class ImageInputListener extends InputListener {

    public Image image;
    public float scale, scaleSmall;

    public ImageInputListener(Image image){
        super();
        this.image = image;
        this.scale = 1.3f;
        this.scaleSmall = 1.1f;
        image.setOrigin(image.getWidth()/2, image.getHeight()/2);
    }

}
