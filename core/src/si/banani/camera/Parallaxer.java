package si.banani.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import si.banani.learning.LearningGdx;
import si.banani.sound.Utility;

/**
 * Created by Urban on 26.1.2017.
 */

public class Parallaxer {


    private Array<ParallaxTexture> textures;
    private OrthographicCamera originalCamera;

    public Parallaxer(OrthographicCamera camera){
        textures = new Array<ParallaxTexture>();
        originalCamera = camera;
    }

    public void addTexture(String fileName, float x, float y,float viewportWidth, float viewportHeight , float parallaxX, float parallaxY){
        Utility.loadTextureAsset(fileName);
        Texture tmp = Utility.getTextureAsset(fileName);
        textures.add(new ParallaxTexture(tmp, x, y , viewportWidth,  viewportHeight ,  parallaxX,  parallaxY, originalCamera));
    }
    public void addTexture(Texture texture, float x, float y, float viewportWidth, float viewportHeight, float parallaxX, float parallaxY){

        textures.add(new ParallaxTexture(texture, x, y , viewportWidth,  viewportHeight ,  parallaxX,  parallaxY, originalCamera));
    }
    public void render(SpriteBatch batch){


        batch.begin();
        batch.enableBlending();

        for(ParallaxTexture texture : textures)
        {
            batch.setProjectionMatrix(texture.getParallaxCamera().calculateParallaxMatrix(texture.getpX(), texture.getpY()));
            batch.draw(texture.getTexture(), texture.getX(), texture.getY());
            batch.draw(texture.getTexture(), texture.getX() + texture.getTexture().getWidth(), texture.getY());
        }

        batch.end();
    }
    public void render(SpriteBatch batch, int [] indicies){


        batch.begin();
        batch.enableBlending();

        for(int i = indicies[0]; i <= indicies[indicies.length-1]; i++)
        {
            ParallaxTexture p = textures.get(i);
            batch.setProjectionMatrix(p.getParallaxCamera().calculateParallaxMatrix(p.getpX(), p.getpY()));
            batch.draw(p.getTexture(), p.getX(), p.getY());
        }

        batch.end();
    }

}
