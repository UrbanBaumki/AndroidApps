package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.maps.MapFactory;

/**
 * Created by Urban on 29.1.2017.
 */

public class EndPoint extends InteractiveTile {


    private MapFactory.MapType chapter;
    private int level;

    public EndPoint(World world, Rectangle rect, MapFactory.MapType chapter, int level){
        super(world, rect);
        fixture.setUserData(this);
        fixture.setSensor(true);
        this.chapter = chapter;
        this.level = level;
    }
    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }

    public MapFactory.MapType getChapter() {
        return chapter;
    }

    public int getLevel() {
        return level;
    }
}
