package si.banani.scenes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

import si.banani.screens.ScreenEnums;
import si.banani.screens.ScreenManager;
import si.banani.sound.Utility;

/**
 * Created by Urban on 26.1.2017.
*/
public class DialogUI extends Window {

    private Label dialog;
    private String full;
    private float timeDisplayed, timeToAnimate,pauseTime, endPauseTime;
    boolean finished;

    public DialogUI() {
        super("ME:", Utility.DIALOGUI_SKIN);

        full = "";

        timeDisplayed = 0f;
        timeToAnimate = 5f;
        endPauseTime = 1.8f;

        setKeepWithinStage(false);

        setPosition(120, 150);
        setMovable(false);
        getTitleTable().clearChildren();

        bottom();

        dialog = new Label("", Utility.DIALOGUI_SKIN);


        dialog.setFontScale(0.65f);


        dialog.setWrap(true);
        row().fill();
        add(dialog).fillX().align(Align.left).expandX().fill();

        setSize(200, 35);
        finished = true;

    }

    public void setNextText(String t, float timeToAnimate){
        full = t;
        this.timeToAnimate = timeToAnimate;
    }

    public boolean render(float dt){

        if(finished){
            finished = false;
            timeDisplayed = 0;
            pauseTime = 0;
            return true;
        }

        updateLabelString( getCurrentString(dt) );
        return false;
    }
    private void updateLabelString(String string){
        dialog.setText(string);
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
}
