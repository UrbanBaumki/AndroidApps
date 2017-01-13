package si.banani.screens;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.controller.InputController;
import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 1.12.2016.
 */

public abstract class BaseScreen implements Screen{

    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected SpriteBatch batch;
    protected InputController inputController;
    protected static boolean running;

    /*
        Every custom screen class that extends my BaseScreen class, has to override the show and render methods in order to
        really customize the behaviour, since these implementations in this class are blank.
     */

    public BaseScreen(SpriteBatch batch){
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(LearningGdx.V_WIDTH / LearningGdx.PPM, LearningGdx.V_HEIGHT / LearningGdx.PPM, camera);
        this.batch = batch;
        this.running = true;
    }

    @Override
    public abstract void show() ;

    @Override
    public abstract void render(float delta) ;

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width,height);
    }

    @Override
    public abstract void pause();

    @Override
    public abstract void resume() ;

    @Override
    public abstract void hide() ;

    @Override
    public void dispose() {
        //this.batch.dispose();

    }
}
