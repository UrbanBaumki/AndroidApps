package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.world.CollisionBits;

/**
 * Created by Urban on 1.2.2017.
 */

public class CheckPoint extends InteractiveTile {

    public float spawnX, spawnY;
    public CheckPoint(World world, Rectangle rectangle) {
        super(world, rectangle);
        fixture.setUserData(this);
        fixture.setSensor(true);
        spawnX = body.getPosition().x;
        spawnY = body.getPosition().y;
    }
    public void setAlreadyActivated(){
        setCategoryFilter(CollisionBits.NONCOLLISION_BIT);
    }
    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }
}
