package si.banani.si.banani.screens.fades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Urban on 16. 10. 2016.
 */
public class Tweener {

    private static float alpha = 0.01f;
    private static boolean ascend = true;
    private static double speed = 1;
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
        if(alpha >= 1f) ascend = false;
        alpha += (alpha * delta) * speed;
        spriteImage.setAlpha(alpha);
    }
    public static void fadeOut(Sprite spriteImage, float delta){
        if(alpha <= 0f) ascend = true;
        alpha -= (alpha * delta) * speed;
        spriteImage.setAlpha(alpha);
    }
}
