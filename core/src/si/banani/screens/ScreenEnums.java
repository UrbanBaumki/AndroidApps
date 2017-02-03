package si.banani.screens;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Urban on 25.10.2016.
 */

public enum ScreenEnums {

    SPLASH {
        public Screen getScreen(Object... params){
            if(params == null)
                return null;
            return new Splash((SpriteBatch) params[0]);
        }
    },
    MAIN_MENU{
        @Override
        public Screen getScreen(Object... params) {
            if(params == null)
            return null;
            return new MainMenu((SpriteBatch)params[0]);
        }
    },
    LEVELS{
        @Override
        public Screen getScreen(Object... params) {
            if(params == null)
                return null;
            return new Levels((SpriteBatch)params[0]);
        }
    },
    SETTINGS{
        @Override
        public Screen getScreen(Object... params) {
            if(params == null)
                return null;
            return new Settings((SpriteBatch)params[0]);
        }
    },
    PLAY {

        public Screen getScreen(Object... params){
            if(params == null)
                return null;
            return new Play((SpriteBatch) params[0]);
        }
    },
    CUTSCENE{
                public Screen getScreen(Object... params){
                if(params == null)
                    return null;
                return new CutsceneScreen((SpriteBatch) params[0]);
            }
    },

    DIALOG{
        public Screen getScreen(Object... params){
            if(params == null)
                return null;
            return new DialogScreen((SpriteBatch) params[0]);
        }
    };

    public abstract Screen getScreen(Object... params);

}
