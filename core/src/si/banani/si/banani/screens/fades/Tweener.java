package si.banani.si.banani.screens.fades;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import si.banani.screens.MainMenu;
import si.banani.screens.ScreenEnums;
import si.banani.screens.ScreenManager;

/**
 * Created by Urban on 16. 10. 2016.
 */
public class Tweener {

    private static boolean isRunning = false;
    private static boolean finished = false;
    private static ScreenEnums change;
    private static boolean commitScreenChange = false;
    private static long timeStart;
    private static Sprite sprite;
    private static int currEffect = 0;
    private static ArrayList<Effect> effects = new ArrayList<Effect>();

    public static void setAnimForSprite(Sprite spriteImg){
        sprite = spriteImg;
    }

    public static void start(){
        if(isRunning) return;
        sprite.setAlpha(effects.get(currEffect).getStart());
        isRunning = true;
        timeStart = System.currentTimeMillis();
    }

    public static void update(long delta){
        if(!isRunning) return;

        Effect effect = effects.get(currEffect);
        float change = easeOutCubic(delta - timeStart, effect.getStart(), effect.getFinish(), effect.getTime());
        if(delta - timeStart <= effect.getTime())
            sprite.setAlpha(change);
        else
            nextEffect();

    }
    public static void nextEffect(){

        if(currEffect+1 >= effects.size()){
            isRunning = false;
            finished = true;
            currEffect= -1;
            if(commitScreenChange)
                changeScreen(change);
            return;
        }
        currEffect++;
        isRunning = false;
        start();
    }

    /*
        Sets the new Screen class to be displayed after effects queue is finished animating.
     */
    public static void setScreenChange(ScreenEnums screen){
        commitScreenChange = true;
        change = screen;

    }
    public static void fifoEffect(FadeEnums effect, float timeOfAnim){
        finished = false;

        switch (effect) {
            case FADE_IN:
                effects.add(new Effect(timeOfAnim, 0f, 1f));
                break;
            case FADE_OUT:
                effects.add(new Effect(timeOfAnim, 1f,-1f));
                break;
            case SLEEP:
                effects.add(new Effect(timeOfAnim, 1f, 0f));
                break;
        }
    }

    //Penner's easing equation
    public static float easeOutCubic(long t,float b,float c,float e){
        return (float) (c * (Math.pow(t / e - 1, 3) + 1) + b);
    }
    public static void changeScreen(ScreenEnums screen){
        if(isFinished())
            ScreenManager.getInstance().changeScreens(screen);
    }
    /*
        Function resets the settings/clears effects queue.
     */
    public static void reset(){
        effects = new ArrayList<Effect>();
        isRunning = false;
        finished = false;
        currEffect = 0;
    }
    public static boolean isFinished(){ return finished; }



}
