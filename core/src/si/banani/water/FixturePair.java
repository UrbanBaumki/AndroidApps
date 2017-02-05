package si.banani.water;

import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by Urban on 4.2.2017.
 */

public class FixturePair {
    private Fixture a;
    private Fixture b;
    public FixturePair(Fixture a, Fixture b){
        this.a = a;
        this.b = b;
    }

    public Fixture getA() {
        return a;
    }

    public Fixture getB() {
        return b;
    }
}
