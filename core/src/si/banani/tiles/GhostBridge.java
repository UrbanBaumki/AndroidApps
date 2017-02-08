package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.world.CollisionBits;

/**
 * Created by Urban on 8.2.2017.
 */

public class GhostBridge extends InteractiveTile {
    public GhostBridge(World world, Rectangle rectangle) {
        super(world, rectangle);
        fixture.setUserData(this);
        setCategoryFilter(CollisionBits.NONCOLLISION_BIT);
    }

    public void setTouchable(){
        setCategoryFilter(CollisionBits.DEFAULT_BIT);
    }
    public void setUntouchable(){
        setCategoryFilter(CollisionBits.NONCOLLISION_BIT);
    }

    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }
}
