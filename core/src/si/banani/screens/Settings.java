package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import si.banani.learning.LearningGdx;
import si.banani.serialization.Serializer;
import si.banani.serialization.SettingsDesriptor;
import si.banani.textures.TextureManager;
import si.banani.tween.ActorAccessor;

/**
 * Created by Urban on 6.1.2017.
 */

public class Settings extends BaseScreen {

    private Stage stage;
    private Table layout;
    private static Label s1, s2, volumeLabel, back, difficultyLabel;
    private BitmapFont font;
    private Label.LabelStyle style;
    private TweenManager tweenManager;
    private static Image l1, r1;
    private static int volume = 100;
    private static Table volumeTable, diffTable;
    private static int dir = 1;
    private static boolean changeVolume;
    private static int difficultyInd = 0;
    private static String [] dificulties = {"Normal", "Hard"};
    private SettingsDesriptor desriptor;
    private String saveName = "settings";
    public Settings(SpriteBatch sb) {
        super(sb);

        desriptor = Serializer.getInstance().loadDescriptor(SettingsDesriptor.class, saveName);
        if(desriptor == null)
        {
            desriptor = new SettingsDesriptor();
            desriptor.diff = difficultyInd;
            desriptor.vol = volume;
            Serializer.getInstance().saveObject(saveName, desriptor, true);
        }
        volume = desriptor.vol;
        difficultyInd = desriptor.diff;

        viewport = new FitViewport(LearningGdx.V_WIDTH, LearningGdx.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        layout = new Table();
        layout.setFillParent(true);
        layout.center();
        layout.setBounds(0,0, viewport.getScreenWidth(), viewport.getScreenHeight());


        font = new BitmapFont(Gdx.files.internal("allura.fnt"));
        font.getData().setScale(0.35f);
        style = new Label.LabelStyle(font, Color.WHITE);

        s1 = new Label("Difficulty: ", style);
        s2 = new Label("Volume: ", style);
        volumeLabel = new Label(Integer.toString(volume)+"%", style);

        difficultyLabel = new Label(dificulties[difficultyInd], style);
        difficultyLabel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                difficultyInd = 1 - difficultyInd;
                updateControlls();
                return true;
            }
        });
        back = new Label("Back", style);

        back.addListener(new InputListener(){
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
        layout.add(s1).expandX();

        diffTable = new Table();

        layout.add(diffTable).expandX();
        diffTable.row();

        diffTable.add(difficultyLabel);


        layout.row().padTop(5);
        layout.add(s2).expandX();

        //volume controlls
        if(!TextureManager.getInstance().doesRegionExist("left_arrow")){
            TextureManager.addAtlas("content.pack", "contentAtlas");
            TextureManager.splitAtlasIntoRegions();
        }
        l1 = new Image(TextureManager.getRegionByName("left_arrow"));
        r1 = new Image(TextureManager.getRegionByName("right_arrow"));
        l1.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    dir = -1;
                    changeVolume = true;


                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeVolume = false;
            }
        });
        r1.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    dir = 1;
                    changeVolume = true;


                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeVolume = false;
            }
        });
        volumeTable = new Table();

        layout.add(volumeTable);

        //volumeTable.setFillParent(true);
        volumeTable.setSize(volumeTable.getParent().getWidth(), volumeTable.getParent().getHeight());
        volumeTable.left().bottom();

        volumeTable.row();
        volumeTable.add(l1);
        volumeTable.add(volumeLabel);
        volumeTable.add(r1);

        layout.row().padTop(5);
        layout.add(back).expandX();
        layout.add().expandX();


        //END volume controlls


        stage.addActor(layout);

        tweenManager = new TweenManager();

        Tween.registerAccessor(Actor.class, new ActorAccessor());


    }
    public static void updateControlls(){
        Label diff = (Label) diffTable.getCells().get(0).getActor();
        diff.setText(dificulties[difficultyInd]);
        Label vol = (Label) volumeTable.getCells().get(1).getActor();
        vol.setText(Integer.toString(volume)+ "%");
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

        if(changeVolume)
        {
            if(dir == 1 && volume < 100 || dir == -1 && volume > 0)
                volume += dir;
            updateControlls();
        }
        stage.draw();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        desriptor = new SettingsDesriptor();
        desriptor.diff = difficultyInd;
        desriptor.vol = volume;
        Serializer.getInstance().saveObject(saveName, desriptor, true);
    }
    @Override
    public void dispose(){
        super.dispose();
        font.dispose();
        layout = null;
        stage.dispose();

    }
}
