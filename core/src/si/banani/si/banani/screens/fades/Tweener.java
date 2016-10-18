package si.banani.si.banani.screens.fades;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import si.banani.screens.MainMenu;

/**
 * Created by Urban on 16. 10. 2016.
 */
public class Tweener {

    private static float alpha = 0.01f;
    private static boolean ascend = true;
    private static double speed;

    public Tweener(){

    }
    public static void fadeInFadeOut(Sprite spriteImage, float delta, double speedOfAnim){
        speed = speedOfAnim;
        if(ascend){
            fadeIn(spriteImage, delta);
        }else {
            fadeOut(spriteImage, delta);
        }
    }

    public static void fadeIn(Sprite spriteImage, float delta){
        if(alpha >= 1f) {
            ascend = false;
        }else{
            alpha += speed * delta;
            spriteImage.setAlpha(alpha);
        }

    }
    public static void fadeOut(Sprite spriteImage, float delta){
        if(alpha <= 0f) ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
        alpha -= speed * delta;
        spriteImage.setAlpha(alpha);
    }
}
