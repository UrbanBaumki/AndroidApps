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
import com.badlogic.gdx.utils.viewport.FitViewport;

import si.banani.learning.LearningGdx;

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
    private GlyphLayout layout;

    public DialogScreen(SpriteBatch spriteBatch){
        super(spriteBatch);
        layout = new GlyphLayout();

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
    private String addLinesToText(String text){
        String [] words = text.split(" ");
        String fixed = "";

        int accWidth = 0;
        for(String word : words){
            layout.setText(font, word);
            accWidth += layout.width;

            if(accWidth >= LearningGdx.V_WIDTH * 5/9){
                accWidth = 0;
                fixed+= "\n";
            }
            fixed += word+" ";
        }
        return fixed;
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
            return full;
        }
        int indexPercent = (int)Math.floor((timeDisplayed/timeToAnimate) * full.length());
        timeDisplayed += dt;


       return full.substring(0, indexPercent);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(finished){
            finished = false;
            timeDisplayed = 0;
            ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.MAIN_MENU, batch);
        }

        updateLabelString( getCurrentString(delta) );
        stage.draw();
        Gdx.app.log("Dialog zaslon", "render");

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
        Gdx.app.log("Dialog zaslon", "skrivamse");
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}