package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import si.banani.learning.LearningGdx;
import si.banani.maps.Map;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.sound.Utility;

/**
 * Created by Urban on 8.2.2017.
 */

public class CreditsScreen extends BaseScreen implements AudioSubject{
    private static String CREDITS_PATH = "licenses/credits.txt";
    private Stage stage;
    private ScrollPane _scrollPane;
    private BitmapFont font;
    private Array<AudioObserver> _observers;
    public CreditsScreen(final SpriteBatch batch) {
        super(batch);
        _observers = new Array<AudioObserver>();
        addObserver(AudioManager.getInstance());

        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //Get text
        FileHandle file = Gdx.files.internal(CREDITS_PATH);
        String textString = file.readString();

        font = new BitmapFont(Gdx.files.internal("allura.fnt"));
        Label text = new Label(textString, new Label.LabelStyle(font, Color.WHITE));
        text.setAlignment(Align.top | Align.center);
        text.setWrap(true);

        _scrollPane = new ScrollPane(text);
        _scrollPane.addListener(new ClickListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        return true;
                                    }

                                    @Override
                                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                        _scrollPane.setScrollY(0);
                                        _scrollPane.updateVisualScroll();
                                       stopMusic();
                                        ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.MAIN_MENU, batch);

                                    }
                                }
        );

        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.defaults().width(Gdx.graphics.getWidth());
        table.add(_scrollPane);

        stage.addActor(table);


    }
    public void stopMusic(){
        notify(AudioObserver.AudioCommand.MUSIC_STOP_ALL, AudioObserver.AudioTypeEvent.MUSIC_ENDING);
    }

    @Override
    public void show() {
        _scrollPane.setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if( delta == 0){
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        _scrollPane.setScrollY(_scrollPane.getScrollY()+delta*20);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void addObserver(AudioObserver audioObserver) {
        _observers.add(audioObserver);
    }

    @Override
    public void removeObserver(AudioObserver audioObserver) {
        _observers.removeValue(audioObserver, true);
    }

    @Override
    public void removeAllObservers() {
        _observers.removeAll(_observers, true);
    }

    @Override
    public void notify(AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event) {
        for(AudioObserver observer: _observers){
            observer.onNotify(command, event);
        }
    }
}
