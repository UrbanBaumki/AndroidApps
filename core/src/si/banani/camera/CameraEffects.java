package si.banani.camera;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import si.banani.entities.BasicPlayer;
import si.banani.learning.LearningGdx;
import si.banani.tiles.InteractiveTile;

/**
 * Created by Urban on 27.12.2016.
 */

public class CameraEffects {

    private static OrthographicCamera camera;
    private static BasicPlayer target;
    private static InteractiveTile tileTarget = null;
    private static float tileDisplayTime, timeDisplaying = 0f;
    private static float lerp = 0.08f;
    public static float offsetX = LearningGdx.V_WIDTH/6;
    private static float camZoom = 1.25f;
    private static float initialZoom;
    private static boolean zooming;
    private static int zoomDir = 1;
    private static float timeAnimating = 0f;
    private static float timeOfZooming = 0.5f;
    public static int currTarDir = 1;

    public static void updateCamera(float dt){

        Vector3 camPos = camera.position;
        currTarDir = target.getDir();

        if(tileTarget != null){
            float offset = LearningGdx.V_WIDTH/2/LearningGdx.PPM;
            float tmpLerp = lerp;
            camPos.x = camera.position.x + (tileTarget.getPosition().x + offset / LearningGdx.PPM - camera.position.x) * tmpLerp;
            camPos.y = camera.position.y + (tileTarget.getPosition().y - camera.position.y) * tmpLerp * 2;
            timeDisplaying += dt;
            if(timeDisplaying >= tileDisplayTime){
                timeDisplaying= 0f;
                tileTarget = null;
                zooming = true;
                return;
            }
        }else {

            float offset = offsetX * target.getDir();
            float tmpLerp = lerp;

            camPos.x = camera.position.x + (target.getPosition().x + offset / LearningGdx.PPM - camera.position.x) * tmpLerp;
            camPos.y = camera.position.y + (target.getPosition().y - camera.position.y) * tmpLerp * 2;
        }
        //zooming
        if(zooming){

            if(zoomDir == 1){

                zoomIn(dt);
            }else{
                if(zoomOut(dt))
                {
                    zoomDir = 1;
                }
            }

        }

        camera.position.set(camPos);
        camera.update();

    }

    public static boolean zoomOut(float dt){
        float offset = (camZoom  - camera.zoom) * lerp;
        camera.zoom += offset;
        timeAnimating += dt;
        if(timeAnimating >= timeOfZooming)
        {
            timeAnimating = 0;
            return true;
        }
        return false;
    }
    public static boolean zoomIn(float dt){
        float offset = (initialZoom - camera.zoom) * lerp;
        camera.zoom += offset;
        timeAnimating += dt;
        if(timeAnimating >= timeOfZooming)
        {
            timeAnimating = 0;
            return true;
        }
        return false;
    }


    public static void boundCamera(float sX, float sY, float width, float height){
        Vector3 pos = camera.position;

        if(pos.x < sX)
            pos.x = sX;

        if(pos.y < sY)
            pos.y = sY;

        if(pos.x > sX + width)
            pos.x = sX + width;

        if(pos.y > sY + height)
            pos.y = sY + height;

        camera.position.set(pos);
        camera.update();
    }

    public static void setZooming(boolean b){ zooming = b;}
    public static void setZoomOffset(float off){ camZoom = off; }
    public static void setCamera(OrthographicCamera cam){ camera = cam; initialZoom = camera.zoom; }
    public static void setTarget(BasicPlayer tar) {
        target = tar;
        if(zooming){
            timeAnimating = 0;
            zoomDir = -zoomDir;
        }

    }
    public static void setTileTarget(InteractiveTile tile, float displayTime){
        tileTarget = tile;
        tileDisplayTime = displayTime;
        if(zooming){
            timeAnimating = 0;
            zoomDir = -zoomDir;
        }
        zooming = false;
    }
}
