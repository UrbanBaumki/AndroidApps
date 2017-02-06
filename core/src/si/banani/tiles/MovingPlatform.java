package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 6.2.2017.
 */

public class MovingPlatform extends InteractiveTile {

    private  int dir;
    private float startX, startY;
    private float offsetX, offsetY;
    private float movingSpeed;
    public MovingPlatform(World world, Rectangle rectangle, int offsetX, int offsetY, int dir, float movingSpeed) {
        super(world, rectangle);
        body.setType(BodyDef.BodyType.KinematicBody);

        startX = rectangle.getX()/LearningGdx.PPM;
        startY = rectangle.getY()/LearningGdx.PPM;
        this.dir = dir;
        this.offsetX = offsetX/ LearningGdx.PPM;
        this.offsetY = offsetY/ LearningGdx.PPM;
        this.movingSpeed = movingSpeed;
        fixture.setUserData(this);
    }

    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

        if(dir == 1){
            body.setLinearVelocity(movingSpeed, 0);
            if(body.getPosition().x >= startX + offsetX){
                body.setTransform(new Vector2(startX+offsetX, body.getPosition().y), 0);
                dir = -dir;
            }
            if(body.getPosition().y >= startY + offsetY){
                body.setTransform(new Vector2(body.getPosition().x, startY+offsetY), 0);
            }
        }else{
            body.setLinearVelocity(-movingSpeed, 0);
            if(body.getPosition().x <= startX - offsetX){
                body.setTransform(new Vector2(startX-offsetX, body.getPosition().y), 0);
                dir = -dir;
            }
            if(body.getPosition().y <= startY - offsetY){
                body.setTransform(new Vector2(body.getPosition().x, startY-offsetY), 0);
            }
        }
    }
}
