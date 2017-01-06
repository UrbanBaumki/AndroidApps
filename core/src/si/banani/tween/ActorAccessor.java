package si.banani.tween;

import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Urban on 6.1.2017.
 */

public class ActorAccessor implements TweenAccessor<Actor>{

    public static final int X = 0, ALPHA = 1;

    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch (tweenType){
            case X:
                returnValues[0] = target.getX();
                return 1;
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType){
            case X:
                target.setX(newValues[0]);
                break;
            case ALPHA:
                target.getColor().a = newValues[0];
                break;
            default:
                assert false;
        }
    }
}
