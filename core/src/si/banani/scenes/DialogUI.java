package si.banani.scenes;


import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

import si.banani.sound.Utility;

/**
 * Created by Urban on 26.1.2017.
*/
public class DialogUI extends Window {

    private Label dialog;

    public DialogUI() {
        super("ME:", Utility.DIALOGUI_SKIN);

        setKeepWithinStage(false);

        setPosition(120, 150);
        setMovable(false);
        getTitleTable().clearChildren();

        bottom();

        dialog = new Label("I loved you for ever.", Utility.DIALOGUI_SKIN);


        dialog.setFontScale(0.65f);


        dialog.setWrap(true);
        row().fill();
        add(dialog).fillX().align(Align.left).expandX().fill();

        setSize(200, 40);



    }
}
