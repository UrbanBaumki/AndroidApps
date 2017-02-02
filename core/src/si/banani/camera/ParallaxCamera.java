package si.banani.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 2.1.2017.
 */

public class ParallaxCamera extends OrthographicCamera {
    Matrix4 parallaxView;
    Matrix4 parallaxCombined;
    Vector3 tmpVec;
    Vector3 tmpVec2;
    OrthographicCamera givenCamera;

    public ParallaxCamera(float viewportW, float viewportH, OrthographicCamera cam){
        super(viewportW, viewportH);
        parallaxView = new Matrix4();
        parallaxCombined = new Matrix4();
        tmpVec = new Vector3();
        tmpVec2 = new Vector3();
        givenCamera = cam;

    }
    public Matrix4 calculateParallaxMatrix(float parX, float parY){
        position.set(new Vector3(givenCamera.position ).scl(LearningGdx.PPM));
        zoom = givenCamera.zoom;
        update();
        tmpVec.set(position);
        tmpVec.x *= parX;
        tmpVec.y *= parY;

        parallaxView.setToLookAt(tmpVec, tmpVec2.set(tmpVec).add(direction), up);
        parallaxCombined.set(projection);
        Matrix4.mul(parallaxCombined.val, parallaxView.val);
        return parallaxCombined;
    }
    public float getTmpVecX(){ return tmpVec.x;}
    public float getViewportWidth(){ return this.viewportWidth; }
    public float getViewportHeight(){ return this.viewportHeight; }
}
