package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 1.12.2016.
 */

public class BaseScreen implements Screen{

    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected SpriteBatch batch;

    /*
        Every custom screen class that extends my BaseScreen class, has to override the show and render methods in order to
        really customize the behaviour, since these implementations in this class are blank.
     */

    public BaseScreen(){
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(LearningGdx.V_WIDTH, LearningGdx.V_HEIGHT, camera);
        this.batch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width,height);
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

    }
}
