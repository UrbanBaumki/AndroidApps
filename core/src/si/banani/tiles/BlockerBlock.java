package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.world.CollisionBits;

/**
 * Created by Urban on 8.2.2017.
 */

public class BlockerBlock extends InteractiveTile {


    public BlockerBlock(World world, Rectangle rectangle) {
        super(world, rectangle);
        fixture.setUserData(this);
    }

    public void unblock(){
        setCategoryFilter(CollisionBits.NONCOLLISION_BIT);
    }
    public void block(){
        setCategoryFilter(CollisionBits.DEFAULT_BIT);
    }
    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }
}
