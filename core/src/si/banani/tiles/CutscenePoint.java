package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.maps.MapFactory;

/**
 * Created by Urban on 7.2.2017.
 */

public class CutscenePoint extends InteractiveTile {


    public CutscenePoint(World world, Rectangle rectangle) {
        super(world, rectangle);

        fixture.setSensor(true);
        fixture.setUserData(this);
    }

    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }


}
