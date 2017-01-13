package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.RayHandler;
import si.banani.learning.LearningGdx;
import si.banani.tween.ActorAccessor;

/**
 * Created by Urban on 6.1.2017.
 */

public class Levels extends BaseScreen {

    private Stage stage;
    private Table layout;
    private Label ch1, ch2, ch3, ch4, ch5;
    private BitmapFont font;
    private Label.LabelStyle style;
    private TweenManager tweenManager;

    public Levels(SpriteBatch sb) {
        super(sb);

        viewport = new FitViewport(LearningGdx.V_WIDTH, LearningGdx.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        layout = new Table();
        layout.setFillParent(true);
        layout.center();

        font = new BitmapFont(Gdx.files.internal("allura.fnt"));
        font.getData().setScale(0.35f);
        style = new Label.LabelStyle(font, Color.WHITE);

        ch1 = new Label("Chapter 1: Loneliness", style);
        ch2 = new Label("<--Zacasni Nazaj", style);
        ch3 = new Label("Chapter 3: Sadness", style);
        ch4 = new Label("Chapter 4: Suicide", style);
        ch5 = new Label("Chapter 5: The End", style);


        //listeners

        ch1.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Screen s = ScreenManager.getInstance().doesExist(ScreenEnums.PLAY);
                if(s != null)
                    ScreenManager.getInstance().set(s);
                else
                    ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.PLAY, batch);
            }
        });

        ch2.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().resumePreviousAndPause();
            }
        });

        layout.row().padTop(5);
        layout.add(ch1).expandX();

        layout.row().padTop(5);
        layout.add(ch2).expandX();

        layout.row().padTop(5);
        layout.add(ch3).expandX();

        layout.row().padTop(5);
        layout.add(ch4).expandX();

        layout.row().padTop(5);
        layout.add(ch5).expandX();

        stage.addActor(layout);

        tweenManager = new TweenManager();

        Tween.registerAccessor(Actor.class, new ActorAccessor());

    }

    @Override
    public void show() {

        Tween.from(layout, ActorAccessor.ALPHA, .5f).target(0).start(tweenManager);
        Tween.from(layout, ActorAccessor.X, .5f).target(LearningGdx.V_WIDTH/2).start(tweenManager);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        stage.act(delta);

        stage.draw();
        //layout.debug();
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
        super.dispose();
        font.dispose();
        layout = null;
        stage.dispose();

    }
}
