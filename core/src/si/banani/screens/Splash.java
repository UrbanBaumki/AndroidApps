package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    BitmapFont font = new BitmapFont();

    public Splash(){
        super();
    }

    @Override
    public void show() {
        //First method called when screen opened


        this.img = new Sprite(new Texture(Gdx.files.internal("splash.jpg")));
        this.img.setSize(LearningGdx.V_WIDTH, LearningGdx.V_HEIGHT );
        img.setPosition(-LearningGdx.V_WIDTH/2, -LearningGdx.V_HEIGHT/2);

        Tweener.reset();
        Tweener.setAnimForSprite(this.img);
        Tweener.fifoEffect(FadeEnums.FADE_IN, 1500);
        Tweener.fifoEffect(FadeEnums.SLEEP, 2000);
        Tweener.fifoEffect(FadeEnums.FADE_OUT, 1500);
        Tweener.setScreenChange(ScreenEnums.MAIN_MENU);
        Tweener.start();
        font.setColor(Color.WHITE);
        font.getData().setScale(4f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        img.draw(batch);
        Tweener.update(System.currentTimeMillis());
        font.draw(batch, String.format("Alpha: %f", img.getColor().a) , 10 ,50);
        batch.end();
    }


    @Override
    public void dispose() {
        super.dispose();
        this.img.getTexture().dispose();
    }
}
