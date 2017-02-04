package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import si.banani.learning.LearningGdx;
import si.banani.si.banani.screens.fades.FadeEnums;
import si.banani.si.banani.screens.fades.Tweener;

/**
 * Created by Urban on 16. 10. 2016.
 */
public class Splash extends BaseScreen {

    private Sprite img;


    public Splash(SpriteBatch sb){
        super(sb);
    }

    @Override
    public void show() {
        //First method called when screen opened


        this.img = new Sprite(new Texture(Gdx.files.internal("splash.jpg")));
        //this.img.setPosition(-img.getWidth()/2/LearningGdx.PPM, - img.getHeight()/2/LearningGdx.PPM);
        this.img.setSize(LearningGdx.V_WIDTH/LearningGdx.PPM, LearningGdx.V_HEIGHT/LearningGdx.PPM);
        this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2 / LearningGdx.PPM + LearningGdx.V_HEIGHT/2/LearningGdx.PPM, 0);
        //img.setPosition(-LearningGdx.V_WIDTH/2, -LearningGdx.V_HEIGHT/2);

        Tweener.reset();
        Tweener.setAnimForSprite(this.img);
        Tweener.fifoEffect(FadeEnums.FADE_IN, 1500);
        Tweener.fifoEffect(FadeEnums.SLEEP, 2000);
        Tweener.fifoEffect(FadeEnums.FADE_OUT, 1500);
        Tweener.setScreenChange(ScreenEnums.PLAY);
        Tweener.start();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        img.draw(batch);
        Tweener.update(System.currentTimeMillis());

        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {

        this.img.getTexture().dispose();
    }
}
