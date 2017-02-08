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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import si.banani.controller.ButtonInputListener;
import si.banani.learning.LearningGdx;
import si.banani.serialization.Serializer;
import si.banani.si.banani.screens.fades.Tweener;
import si.banani.tween.ActorAccessor;

/**
 * Created by Urban on 5.1.2017.
 */

public class MainMenu extends BaseScreen {

    private Table layoutTable;
    private TextButton play, settings, exit;
    private Label header;
    private Skin skin;
    private BitmapFont font;
    private Stage stage;
    private TextButton.TextButtonStyle buttonStyle;
    private TweenManager tweenManager;

    public MainMenu(SpriteBatch spriteBatch){
        super(spriteBatch);


        this.viewport = new FitViewport(LearningGdx.V_WIDTH , LearningGdx.V_HEIGHT , new OrthographicCamera());
        this.stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        layoutTable = new Table();

        font = new BitmapFont(Gdx.files.internal("allura.fnt"));
        font.getData().setScale(0.5f);
        header = new Label(LearningGdx.TITLE, new Label.LabelStyle(font, Color.WHITE));
        header.setFontScale(1.5f);

        buttonStyle = new TextButton.TextButtonStyle(null, null, null, font );

        play = new TextButton("Play",buttonStyle );

        settings = new TextButton("Credits", buttonStyle);
        exit = new TextButton("Exit", buttonStyle);

        //adding listeners
        play.addListener( new ButtonInputListener(play){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setBgColor();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resetBgColor();

                ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.LEVELS, batch);
            }
        });



        settings.addListener( new ButtonInputListener(settings){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setBgColor();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resetBgColor();

                ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.CREDITS, batch, null);
            }
        });

        exit.addListener( new ButtonInputListener(exit){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setBgColor();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resetBgColor();
                ScreenManager.getInstance().quitApplication();
            }
        });

        layoutTable.setFillParent(true);
        layoutTable.center();

        layoutTable.row().pad(2f);
        layoutTable.add(header).expandX();

        layoutTable.row().align(8).padLeft(25f);
        layoutTable.add(play).expandX();

        layoutTable.row().align(8).padLeft(25f);
        layoutTable.add(settings).expandX();

        layoutTable.row().align(8).padLeft(25f);
        layoutTable.add(exit).expandX();
        //layoutTable.pack();

        stage.addActor(layoutTable);


        //animations
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());


    }

    @Override
    public void show() {
        Timeline.createSequence().beginSequence()
                .push(Tween.set(play, ActorAccessor.ALPHA).target(0))
                .push(Tween.set(settings, ActorAccessor.ALPHA).target(0))
                .push(Tween.set(exit, ActorAccessor.ALPHA).target(0))
                .push(Tween.from(header, ActorAccessor.ALPHA, .5f).target(0))
                .push(Tween.to(play, ActorAccessor.ALPHA, .3f).target(1))
                .push(Tween.to(settings, ActorAccessor.ALPHA, .3f).target(1))
                .push(Tween.to(exit, ActorAccessor.ALPHA, .3f).target(1))
                .end().start(tweenManager);

        //table anim
        Tween.from(layoutTable, ActorAccessor.X, .4f).target(LearningGdx.V_WIDTH / 8).start(tweenManager);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        stage.act(delta);

        stage.draw();
        //layoutTable.debug();
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
    public void dispose(){

        stage.dispose();
        font.dispose();

    }
    public void resize (int width, int height){
        viewport.update(width, height);
    }

}
