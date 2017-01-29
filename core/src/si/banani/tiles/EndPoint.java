package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Urban on 29.1.2017.
 */

public class EndPoint extends InteractiveTile {




    public EndPoint(World world, Rectangle rect){
        super(world, rect);
        fixture.setUserData(this);
        fixture.setSensor(true);
    }
    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }
}
