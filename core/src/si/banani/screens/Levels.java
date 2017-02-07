package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.RayHandler;
import si.banani.learning.LearningGdx;
import si.banani.maps.MapFactory;
import si.banani.serialization.Chapter;
import si.banani.serialization.ChapterDescriptor;
import si.banani.serialization.Serializer;
import si.banani.tween.ActorAccessor;

/**
 * Created by Urban on 6.1.2017.
 */

public class Levels extends BaseScreen {

    private Stage stage;
    private Table layout;
    private Label selectChapter, ch1, ch2, ch3, ch4, ch5;
    private BitmapFont font;
    private Label.LabelStyle style;
    private TweenManager tweenManager;

    private int numOfCompletedChapters = 0;
    public Levels(SpriteBatch sb) {
        super(sb);

        ChapterDescriptor progress = Serializer.getInstance().loadDescriptor(ChapterDescriptor.class, "chapterProgress");
        MapFactory.MapType [] types = MapFactory.MapType.values();

        if(progress!= null){

            for(int i = 0; i < types.length; i++){
                MapFactory.MapType type = types[i];
                Chapter tmpChap = progress.getChapter(type.toString());

                if(tmpChap != null)
                {
                    if(tmpChap.isFinished() == 1)
                        numOfCompletedChapters++;
                    else
                        break;
                }
                else
                    break;

            }

        }

        viewport = new FitViewport(LearningGdx.V_WIDTH, LearningGdx.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        layout = new Table();
        layout.setFillParent(true);
        layout.center();

        font = new BitmapFont(Gdx.files.internal("allura.fnt"));
        font.getData().setScale(0.35f);
        style = new Label.LabelStyle(font, Color.WHITE);

        selectChapter = new Label("Select a chapter:", style);
        ch1 = new Label("Chapter 1: Loneliness", style);
        ch2 = new Label("Chapter 2: Fear", style);
        ch3 = new Label("Chapter 3: Sadness", style);
        ch4 = new Label("Chapter 4: Suicide", style);
        ch5 = new Label("Chapter 5: The End", style);


        layout.add(selectChapter).expandX();
        //listeners

        ch1.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.PLAY, batch, MapFactory.MapType.CHAPTER1);


            }
        });

        layout.row().padTop(5);
        layout.add(ch1).expandX();


        if(numOfCompletedChapters >= 1) {
            ch2.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.PLAY, batch, MapFactory.MapType.CHAPTER2);
                }
            });
            layout.row().padTop(5);
            layout.add(ch2).expandX();
        }



        if(numOfCompletedChapters >= 2) {
            ch3.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                    ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.PLAY, batch, MapFactory.MapType.CHAPTER3);


                }
            });

            layout.row().padTop(5);
            layout.add(ch3).expandX();
        }

        if(numOfCompletedChapters >= 3){

            ch4.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                    ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.PLAY, batch, MapFactory.MapType.CHAPTER4);


                }
            });

            layout.row().padTop(5);
            layout.add(ch4).expandX();
        }

        if(numOfCompletedChapters >= 4){

            ch5.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                    ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.PLAY, batch, MapFactory.MapType.CHAPTER5);


                }
            });

            layout.row().padTop(5);
            layout.add(ch5).expandX();
        }



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

        font.dispose();
        layout = null;
        stage.dispose();

    }
}
