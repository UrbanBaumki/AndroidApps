package si.banani.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 1.12.2016.
 */

public class Hud {

    private Viewport viewport;
    public Stage stage;
    public Table topTable, bottomTable;

    private Integer numLives;
    Label lifeLabel, livesLabel, bottomHud;

    public Hud(SpriteBatch batch){
        this.viewport = new FitViewport(LearningGdx.V_WIDTH, LearningGdx.V_HEIGHT, new OrthographicCamera());
        this.stage = new Stage(viewport, batch);

        this.numLives = 5;
        //setting UPPER HUD
        topTable = new Table();
        topTable.top();
        topTable.setFillParent(true);

        BitmapFont bmp = new BitmapFont();


        lifeLabel = new Label("Todo hud.. ", new Label.LabelStyle(bmp , Color.BLACK));
        livesLabel = new Label(String.format("Lives: %d", this.numLives), new Label.LabelStyle(bmp , Color.BLACK));

        topTable.add(lifeLabel).expandX();
        topTable.add(livesLabel).expandX();

        //lower HUD
        bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom();

        bottomHud = new Label("Todo bottom hud:" , new Label.LabelStyle(bmp, Color.BLACK));

        bottomTable.add(bottomHud).expandX();

        stage.addActor(topTable);
        stage.addActor(bottomTable);

    }

}
