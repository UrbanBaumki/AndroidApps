package si.banani.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.world.CollisionBits;

/**
 * Created by Urban on 8.12.2016.
 */

public class Spikes extends InteractiveTile {


    public Spikes(World world, Rectangle rectangle) {
        super(world, rectangle);
        fixture.setUserData(this);
        setCategoryFilter(CollisionBits.SPIKES_BIT);
    }


    public void onHit() {
        Gdx.app.log("Spike", "collide");
    }
}
