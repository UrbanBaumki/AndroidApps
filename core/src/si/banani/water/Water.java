package si.banani.water;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.tiles.InteractiveTile;

/**
 * Created by Urban on 4.2.2017.
 */

public class Water extends InteractiveTile {

    public Water(World world, Rectangle rectangle) {
        super(world, rectangle);

        fixture.setUserData(this);
        fixture.setSensor(true);
        fixture.setDensity(10f);
    }

    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }
}
