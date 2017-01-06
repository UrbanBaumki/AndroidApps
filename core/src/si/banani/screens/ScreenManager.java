package si.banani.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.Stack;

import si.banani.si.banani.screens.fades.Tweener;

/**
 * Created by Urban on 25.10.2016.
 */

public class ScreenManager {

    private static ScreenManager scmInstance;
    private Game game;
    private static Stack<Screen> screens;
    private boolean commitScreenChange = false;

    private ScreenManager(){
        super();
    }

    /*  A singleton use of this class

        @return     Returns a ScreenManager instance, ment for a Singleton use in appi
     */
    public static ScreenManager getInstance(){
        if(scmInstance == null) {
            scmInstance = new ScreenManager();
            screens = new Stack<Screen>();
        }
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
    public void push(Screen screen){
        screens.push(screen);
    }
    public void pop(){screens.pop();}
    public Screen peek(){
        return screens.peek();
    }
    public void resumePreviousAndPause(){
        Screen curr = screens.pop();
        Screen prev = screens.pop();

        game.setScreen(prev);
        push(curr);
        push(prev);

    }
    public void set(Screen screen){
        if(screen == null)
            return;

        game.setScreen(screen);
        push(screen);
    }

    public Screen doesExist(ScreenEnums enums){
        Screen scr = null;
        for( Screen s : screens){

            if(enums == ScreenEnums.LEVELS && s instanceof Levels)
            {
                return s;
            }
            if(enums == ScreenEnums.SETTINGS && s instanceof Settings)
            {
                return s;
            }

        }
        return scr;
    }
    public void changeScreensAndPause(ScreenEnums screenenums, Object... params){
        Screen curr = game.getScreen();
        if(curr != null)
            push(curr);

        Screen newScreen = screenenums.getScreen(params);
        game.setScreen(newScreen);
        push(newScreen);


    }
    public void changeScreensAndDispose(ScreenEnums screenenums, Object... params){
        Screen curr = game.getScreen();

        if(curr != null)
            curr.dispose();

        Screen newScreen = screenenums.getScreen(params);
        game.setScreen(newScreen);


    }
}
