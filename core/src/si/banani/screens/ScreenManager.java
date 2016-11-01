package si.banani.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import si.banani.si.banani.screens.fades.Tweener;

/**
 * Created by Urban on 25.10.2016.
 */

public class ScreenManager {

    private static ScreenManager scmInstance;
    private Game game;
    private boolean commitScreenChange = false;

    private ScreenManager(){
        super();
    }

    /*  A singleton use of this class

        @return     Returns a ScreenManager instance, ment for a Singleton use in appi
     */
    public static ScreenManager getInstance(){
        if(scmInstance == null) scmInstance = new ScreenManager();
        return scmInstance;
    }
    /*
        Initializer, to retrieve the main Game class for screen manipulation
        @param  game    Main Game class on which to perform screen changes
     */
    public void bindWithMainGameClass(Game game){
        this.game = game;
    }

    /*  Method for changing screens using enums
        @param screenenums an Enum representation of a specific Screen "state"
     */
    public void changeScreens(ScreenEnums screenenums, Object... params){
        Screen curr = game.getScreen();

        if(curr != null)
            curr.dispose();

        Screen newScreen = screenenums.getScreen(params);
        game.setScreen(newScreen);


    }
}
