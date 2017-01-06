package si.banani.controller;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Urban on 6.1.2017.
 */

public class ButtonInputListener extends InputListener {

    private TextButton button;
    public ButtonInputListener(TextButton button){
        this.button = button;
    }
    public void setBgColor(){
        this.button.setColor(255,255,255,0.4f);
    }
    public void resetBgColor(){
        this.button.setColor(255, 255, 255, 1f);
    }
}
