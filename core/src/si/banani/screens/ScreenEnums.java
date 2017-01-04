package si.banani.screens;


import com.badlogic.gdx.Screen;

/**
 * Created by Urban on 25.10.2016.
 */

public enum ScreenEnums {

    SPLASH {
        public Screen getScreen(Object... params){
            return new Splash();
        }
    },

    MAIN_MENU {
        public Screen getScreen(Object... params){
            return new MainMenu();
        }
    },
    DIALOG{
        public Screen getScreen(Object... params){
            return new DialogScreen();
        }
    };

    public abstract Screen getScreen(Object... params);

}
