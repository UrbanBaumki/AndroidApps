package si.banani.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.camera.CameraEffects;
import si.banani.conversations.Conversation;
import si.banani.conversations.ConversationHolder;
import si.banani.entities.BasicPlayer;
import si.banani.entities.CameraCoordinates;
import si.banani.entities.EntityFactory;
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;
import si.banani.learning.LearningGdx;
import si.banani.textures.TextureManager;

/**
 * Created by Urban on 1.12.2016.
 */

public class Hud {

    private Viewport viewport;
    public Stage stage;
    public Table topTable;

    private Integer numLives, maxLives;

    private Image heartFull, heartEmpty;
    private Array<Image> fullHearts;
    private Array<Image> emptyHearts;

    private Image energyBar;
    private Image energyBarBg;

    private float ghostEnergy = 100f;
    private float energyBarWidth;

    private DialogUI _dialogUI;
    private PauseUI pauseUI;
    private CameraCoordinates cameraCoordinates;

    private boolean dialogRunning = false;
    private boolean dialogCutscene = false;

    private OrthographicCamera givenCamera;

    public Hud(SpriteBatch batch){
        this.viewport = new FitViewport(LearningGdx.V_WIDTH , LearningGdx.V_HEIGHT , new OrthographicCamera());
        this.stage = new Stage(viewport, batch);


        this.numLives = maxLives = 3;


        fullHearts = new Array<Image>();
        emptyHearts = new Array<Image>();

        for(int i = 0; i < maxLives; i++){
            heartFull = new Image( TextureManager.getRegionByName("full_heart").split(7,6)[0][0] );
            heartFull.setOrigin(heartFull.getWidth()/2, heartFull.getHeight()/2);
            heartFull.setScale(2f,2f);

            heartEmpty = new Image( TextureManager.getRegionByName("empty_heart").split(7,6)[0][0] );
            heartEmpty.setOrigin(heartFull.getWidth()/2, heartFull.getHeight()/2);
            heartEmpty.setScale(2f,2f);

            fullHearts.add(heartFull);
            emptyHearts.add(heartEmpty);
        }


        energyBar = new Image(new Texture(Gdx.files.internal("skins/energyBar.png")));
        energyBarBg = new Image(new Texture(Gdx.files.internal("skins/energyBarBg.png")));

        energyBarWidth = energyBar.getWidth();

        energyBar.setSize(ghostEnergy/100 * energyBarWidth, energyBar.getHeight());

        WidgetGroup group = new WidgetGroup();
        group.addActor(energyBarBg);
        group.addActor(energyBar);

        //setting UPPER HUD
        topTable = new Table();
        topTable.top();
        topTable.setFillParent(true);


        topTable.row().padTop(10f);
        topTable.add(group).size(energyBarBg.getWidth(), energyBarBg.getHeight()).padTop(10f).expandX();
        topTable.add().expandX();
        topTable.add().expandX();
        topTable.add().expandX();
        topTable.add().expandX();

        for(int i = 0; i < fullHearts.size; i++){
            topTable.add(fullHearts.get(i)).size(fullHearts.get(i).getWidth(), fullHearts.get(i).getHeight()).padRight(5f);
        }

        topTable.add().expandX();

        stage.addActor(topTable);

        _dialogUI = new DialogUI();
        _dialogUI.setVisible(false);
        _dialogUI.setKeepWithinStage(false);
        pauseUI = new PauseUI();
        pauseUI.setKeepWithinStage(false);
        pauseUI.setVisible(false);

        pauseUI.setPosition(stage.getWidth()/2 - pauseUI.getWidth()/2,stage.getHeight()/2 - pauseUI.getHeight()/2);
        stage.addActor(_dialogUI);
        stage.addActor(pauseUI);

    }
    public void render(float dt){
        stage.draw();

        if(dialogRunning){

            int currSpeaker = ConversationHolder.getCurrentSpeaker();
            Vector3 pPos;
            if(currSpeaker == 0){
                pPos = new Vector3(EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).getPosition().x, EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).getPosition().y, 0);

            }else{
                pPos = new Vector3(EntityFactory.getEntity(EntityFactory.EntityType.FEMALE).getPosition().x, EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).getPosition().y, 0);
            }
            Vector3 windowCoords = givenCamera.project(pPos);
            viewport.unproject(windowCoords);

            _dialogUI.setPosition(windowCoords.x - CameraEffects.offsetX, 150 );

            if(_dialogUI.render(dt)){

                ConversationHolder.getInstance().next();
                String next = ConversationHolder.getInstance().getCurrentText();

                if (next == null)
                    enableDialog(false);
                else
                    _dialogUI.setNextText(next, next.length()/10f);
            }

        }



    }
    public void update(){
        //clearing hearts
        for(int i = maxLives; i > numLives; i--)
            topTable.getCells().get(4+i).clearActor().setActor(emptyHearts.get(i-1));


    }
    public void updateLives(){
        for(int i = numLives; i > 0; i--)
            topTable.getCells().get(4+i).clearActor().setActor(fullHearts.get(i-1));
    }
    public DialogUI get_dialogUI(){ return _dialogUI; }
    public void showGameOver(){
        pauseUI.showGameOver();

    }
    public void setEnergyLevel(float level) {
        ghostEnergy = level;
        updateEnergy();
    }
    private void updateEnergy(){
        energyBar.setSize(ghostEnergy/100 * energyBarWidth, energyBar.getHeight());
    }
    public void decreaseLives(){
        this.numLives-=1;
    }
    public void setNumLives(int num){
        numLives = num;
    }
    public Integer getNumLives(){
        return numLives;
    }
    public void enableDialog(boolean b){

        if(b){
            String next = ConversationHolder.getInstance().getCurrentText();
            _dialogUI.setNextText(next, next.length()/10f);
            _dialogUI.setFinished(false);
        }
        _dialogUI.setVisible(b);
        dialogRunning = b;

    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height);
        _dialogUI.getStage().getViewport().update(width,height);
        pauseUI.getStage().getViewport().update(width,height);

    }
    public void setCameraCoordinates(CameraCoordinates c){
        this.cameraCoordinates = c;
    }
    public void showPause(){
        pauseUI.showMe();
    }
    public void hidePause(){
        pauseUI.setVisible(false);
    }

    public OrthographicCamera getGivenCamera() {
        return givenCamera;
    }

    public void setGivenCamera(OrthographicCamera givenCamera) {
        this.givenCamera = givenCamera;
    }
}
