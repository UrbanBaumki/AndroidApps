package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.conversations.ConversationHolder;
import si.banani.world.CollisionBits;

/**
 * Created by Urban on 27.1.2017.
 */

public class DialogPoint extends InteractiveTile {

    private String chapter;

    public DialogPoint(World world, Rectangle rect, String chapter){
        super(world, rect);
        fixture.setUserData(this);
        fixture.setSensor(true);
        setCategoryFilter(CollisionBits.SENSOR_BIT);

        this.chapter = chapter;
    }

    public void setDialogToDisplay(){
        ConversationHolder.getInstance().setCurrent_chapter(chapter);

    }

    public void setExecuted(){
        setCategoryFilter(CollisionBits.NONCOLLISION_BIT);
    }
    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }
}
