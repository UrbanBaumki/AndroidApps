package si.banani.screens;

import com.badlogic.gdx.Gdx;

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
import si.banani.maps.MapFactory;

/**
 * Created by Urban on 1.1.2017.
 */

public class DialogScreen extends BaseScreen {

    private BitmapFont font;
    private String full;
    private float timeDisplayed, timeToAnimate,pauseTime, endPauseTime;
    boolean finished;
    private Table displayTable;
    private Stage stage;
    private Label displayLabel;
    private int textNum;
    Array<String> dialogs ;
    private MapFactory.MapType mapToChange;

    public DialogScreen(SpriteBatch spriteBatch, int textNum, MapFactory.MapType mapType){
        super(spriteBatch);

        this.textNum = textNum;
        mapToChange = mapType;

        dialogs = new Array<String>();

        this.viewport = new FitViewport(LearningGdx.V_WIDTH , LearningGdx.V_HEIGHT , new OrthographicCamera());

        this.stage = new Stage(viewport, batch);

        displayTable = new Table();

        displayTable.bottom();
        displayTable.setFillParent(true);
        displayTable.row().pad(15f);


        font = new BitmapFont(Gdx.files.internal("allura.fnt"));
        full = "Ljubila ga je kljub storjenim napakam, kljub temu, da je zopet opit sedel za volan in ju jezno odpeljal proti domu." +
                "Ljubila ga je z vsem srcem, ceprav je postal agresiven in zacel glasen prepir." +
                "Nikdar ga ni nehala ljubiti, cetudi je zdaj peljal proti nevarnemu ovinku z mocno preveliko hitrostjo.";

        dialogs.add(full);

        timeDisplayed = 0f;
        timeToAnimate = 8f;
        endPauseTime = 3.5f;

        displayLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        displayLabel.setWrap(true);
        displayTable.add(displayLabel).fillX().align(Align.top).expandX();
        //displayTable.add(displayLabel).width(50f).align(Align.top).align(Align.left);

        //displayLabel.setWrap(true);
        stage.addActor(displayTable);

    }

    @Override
    public void show() {
        Gdx.app.log("Dialog zaslon", "show");
    }
    private String getCurrentString(float dt){

        if(timeDisplayed >= timeToAnimate){
            if(pauseTime >= endPauseTime)
                finished = true;
            pauseTime += dt;
            return dialogs.get(textNum);
        }
        int indexPercent = (int)Math.floor((timeDisplayed/timeToAnimate) * dialogs.get(textNum).length());
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
            ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.PLAY, batch, mapToChange);
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
}
