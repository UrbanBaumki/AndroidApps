package si.banani.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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

        //setting UPPER HUD
        topTable = new Table();
        topTable.top();
        topTable.setFillParent(true);


        topTable.row().padTop(10f);
        topTable.add().expandX();
        topTable.add().expandX();
        topTable.add().expandX();
        topTable.add().expandX();

        for(int i = 0; i < fullHearts.size; i++){
            topTable.add(fullHearts.get(i)).size(fullHearts.get(i).getWidth(), fullHearts.get(i).getHeight()).padRight(5f);
        }

        topTable.add().expandX();

        stage.addActor(topTable);

    }
    public void update(){
        //clearing hearts
        for(int i = maxLives; i > numLives; i--)
            topTable.getCells().get(3+i).clearActor().setActor(emptyHearts.get(i-1));


    }
    public void showGameOver(){
        topTable.clear();
        topTable.add(new Label("GAME OVER!", new Label.LabelStyle(new BitmapFont(), Color.BLACK))).expandX();

    }
    public void decreaseLives(){
        this.numLives-=1;
    }
    public Integer getNumLives(){
        return numLives;
    }

}