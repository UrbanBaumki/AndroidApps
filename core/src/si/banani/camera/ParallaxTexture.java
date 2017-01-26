package si.banani.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Urban on 26.1.2017.
 */

public class ParallaxTexture {
    private Texture texture;
    private float x, y, pX, pY;
    private ParallaxCamera parallaxCamera;

    public ParallaxTexture(Texture t, float x, float y , float viewportWidth, float viewportHeight , float parallaxX, float parallaxY, OrthographicCamera camera){
        this.texture = t;
        this.x = x;
        this.y = y;
        parallaxCamera = new ParallaxCamera( viewportWidth,  viewportHeight, camera);
        this.pX = parallaxX;
        this.pY = parallaxY;
    }


    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getpX() {
        return pX;
    }

    public void setpX(float pX) {
        this.pX = pX;
    }

    public float getpY() {
        return pY;
    }

    public void setpY(float pY) {
        this.pY = pY;
    }

    public ParallaxCamera getParallaxCamera() {
        return parallaxCamera;
    }

    public void setParallaxCamera(ParallaxCamera parallaxCamera) {
        this.parallaxCamera = parallaxCamera;
    }
}
