package si.banani.screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import si.banani.learning.LearningGdx;
import si.banani.maps.Map;
import si.banani.maps.MapFactory;
import si.banani.sound.AudioManager;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;

/**
 * Created by Urban on 1.1.2017.
 */

public class DialogScreen extends BaseScreen implements AudioSubject{
    private Array<AudioObserver> _observers;
    private BitmapFont font;
    private float timeDisplayed,pauseTime, endPauseTime;
    boolean finished;
    private Table displayTable;
    private Stage stage;
    private Label displayLabel;
    private int textNum;
    Array<String> dialogs ;


    public DialogScreen(SpriteBatch spriteBatch, int textNum) {
        super(spriteBatch);

        _observers = new Array<AudioObserver>();
        addObserver(AudioManager.getInstance());
        this.textNum = textNum;
        dialogs = new Array<String>();

        this.viewport = new FitViewport(LearningGdx.V_WIDTH , LearningGdx.V_HEIGHT , new OrthographicCamera());

        this.stage = new Stage(viewport, batch);

        displayTable = new Table();

        displayTable.bottom();
        displayTable.setFillParent(true);
        displayTable.row().pad(15f);



        //Get text
        FileHandle file = Gdx.files.internal("bin/intro.txt");
        String introText = file.readString();

        FileHandle file2 = Gdx.files.internal("bin/ending.txt");
        String outroText = file2.readString();

        font = new BitmapFont(Gdx.files.internal("allura.fnt"));


        dialogs.add(introText);
        dialogs.add(outroText);


        timeDisplayed = 0f;

        endPauseTime = 3.5f;

        displayLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        displayLabel.setWrap(true);
        displayTable.add(displayLabel).fillX().align(Align.top).expandX();
        //displayTable.add(displayLabel).width(50f).align(Align.top).align(Align.left);

        //displayLabel.setWrap(true);
        stage.addActor(displayTable);

        loadMusic();

        if(textNum == 1)
            startEndMusic();
    }

    public void loadMusic(){
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_ENDING);
    }
    public void startEndMusic(){

        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_ENDING);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }
    private String getCurrentString(float dt){

        if(timeDisplayed >= dialogs.get(textNum).length()/10f){
            if(pauseTime >= endPauseTime)
                finished = true;
            pauseTime += dt;
            return dialogs.get(textNum);
        }
        int indexPercent = (int)Math.floor((timeDisplayed/(dialogs.get(textNum).length()/10f)) * dialogs.get(textNum).length());
        timeDisplayed += dt;


        return dialogs.get(textNum).substring(0, indexPercent);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(finished){
            finished = false;
            timeDisplayed = 0;
            if(textNum == 1){
                ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.CREDITS, batch);
            }else
                ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.PLAY, batch, MapFactory.MapType.CHAPTER1);
        }

        updateLabelString( getCurrentString(delta) );
        stage.draw();


    }
    private void updateLabelString(String string){
        ((Label) displayTable.getCells().get(0).getActor()).setText(string);
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
