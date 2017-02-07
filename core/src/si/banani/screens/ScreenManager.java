package si.banani.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;


/**
 * Created by Urban on 25.10.2016.
 */

public class ScreenManager {

    private static ScreenManager scmInstance;
    private Game game;
    private static Stack<Screen> screens;
    private static Hashtable<ScreenEnums, Screen> _screens;
    private static Screen currentScreen, previousScreen;

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
            _screens = new Hashtable<ScreenEnums, Screen>();
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
        if(previousScreen == null)
            return;
        Screen prev = previousScreen;
        Screen curr = currentScreen;
        Screen tmp = curr;

        curr = prev;
        prev = tmp;

        tmp = null;

        previousScreen = prev;
        currentScreen = curr;

        game.setScreen(curr);


    }
    public void set(Screen screen){
        if(screen == null)
            return;

        if(currentScreen != null)
            previousScreen = currentScreen;
        currentScreen = screen;
        game.setScreen(screen);

    }
    public void disposeAll(){
        Set<ScreenEnums> set = _screens.keySet();
        Iterator<ScreenEnums> it = set.iterator();

        while (it.hasNext()){
            ScreenEnums screenEnums = it.next();
            _screens.get(screenEnums).dispose();
        }
        _screens.clear();
    }
    public void quitApplication(){
        disposeAll();
        Gdx.app.exit();
    }
    public Screen getScreen(ScreenEnums screenEnums){
        return _screens.get(screenEnums);
    }

    public void changeScreensAndPause(ScreenEnums screenenums, Object... params){
        Screen screenToChange = getScreen(screenenums);

        if(screenToChange == null)
        {
            screenToChange = screenenums.getScreen(params);
            _screens.put(screenenums, screenToChange);
        }

        if(currentScreen != null)
            previousScreen = currentScreen;

        currentScreen = screenToChange;

        game.setScreen(currentScreen);

    }
    public void changeScreensAndDispose(ScreenEnums screenenums, Object... params){
        Screen curr = game.getScreen();

        if(curr != null)
        {
            screens.remove(curr);
            curr.dispose();
        }

        Screen newScreen = getScreen(screenenums);

        if(newScreen == null){
            newScreen = screenenums.getScreen(params);
            _screens.put(screenenums, newScreen);
        }

        previousScreen = null;
        currentScreen = newScreen;

        game.setScreen(newScreen);

    }
}
