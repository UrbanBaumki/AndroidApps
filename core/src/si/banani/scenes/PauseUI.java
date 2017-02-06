package si.banani.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

import si.banani.learning.LearningGdx;
import si.banani.screens.Levels;
import si.banani.screens.Play;
import si.banani.screens.ScreenEnums;
import si.banani.screens.ScreenManager;
import si.banani.sound.Utility;

/**
 * Created by Urban on 6.2.2017.
 */

public class PauseUI extends Window {

    private Label resume, toMainMenu, reset;


    public PauseUI() {
        super("|PAUSE|", Utility.DIALOGUI_SKIN);

        setMovable(false);
        getTitleLabel().setFontScale(0.7f);
        getTitleLabel().setAlignment(Align.center);

        setKeepWithinStage(false);

        resume = new Label("Resume", Utility.DIALOGUI_SKIN);
        reset = new Label("Restart", Utility.DIALOGUI_SKIN);
        toMainMenu = new Label("Main Menu", Utility.DIALOGUI_SKIN);

        resume.setFontScale(0.65f);
        resume.setAlignment( Align.center);

        reset.setFontScale(0.65f);
        reset.setAlignment( Align.center);

        toMainMenu.setFontScale(0.65f);

        toMainMenu.setAlignment(Align.center);
        toMainMenu.setHeight(10);


        resume.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Play.gameState = Play.GameState.RESUME;
            }
        });

        reset.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Play.gameState = Play.GameState.RESET;
            }
        });

        toMainMenu.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.MAIN_MENU, LearningGdx.getSpriteBatch());
            }
        });

        bottom();
        defaults().expand().fill();
        add();
        row();
        add(resume).padTop(10f).expandX();
        row();
        add(reset).expand();
        row();
        add(toMainMenu).expandX();
        row();


        setSize(150, 150);

        setVisible(false);
        pack();

    }
    public Vector2 getPosition(){
        return  new Vector2(getX(), getY());
    }

    public void showMe(){
        setVisible(true);
        getTitleLabel().setText("|PAUSE|");
        resume.setVisible(true);
        Gdx.input.setInputProcessor(this.getStage());

    }
    public void showGameOver(){
        getTitleLabel().setText("DIED..");
        Gdx.input.setInputProcessor(this.getStage());
        resume.setVisible(false);
        setVisible(true);
    }

}
