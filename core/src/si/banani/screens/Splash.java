package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import si.banani.si.banani.screens.fades.FadeEnums;
import si.banani.si.banani.screens.fades.Tweener;

/**
 * Created by Urban on 16. 10. 2016.
 */
public class Splash implements Screen {

    private SpriteBatch batch;
    private Sprite img;

    @Override
    public void show() {
        //First method called when screen opened
        this.batch = new SpriteBatch();

        this.img = new Sprite(new Texture(Gdx.files.internal("splash.jpg")));
        this.img.setSize( Gdx.graphics.getWidth() , Gdx.graphics.getHeight() );

        Tweener.setAnimForSprite(this.img);
        Tweener.fifoEffect(FadeEnums.FADE_IN, 1500);
        Tweener.fifoEffect(FadeEnums.SLEEP, 2000);
        Tweener.fifoEffect(FadeEnums.FADE_OUT, 1500);
        Tweener.setScreenChange(ScreenEnums.MAIN_MENU);
        Tweener.start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        img.draw(batch);
        Tweener.update(System.currentTimeMillis());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        this.batch.dispose();
        this.img.getTexture().dispose();
    }
}
