package si.banani.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
    private float startX, startY, width, height, yOffset;
    private float offsetX, offsetY;
    private float movingSpeed;
    private Texture platform;
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
        platform = new Texture(Gdx.files.internal("textures/ch4/platform.png"));

        this.width = platform.getWidth();
        this.height = platform.getHeight();
        yOffset = 0;
    }

    @Override
    public void render(SpriteBatch batch, float dt) {
        batch.draw(platform, body.getPosition().x  -  width /2/ LearningGdx.PPM, body.getPosition().y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , width / LearningGdx.PPM, height / LearningGdx.PPM);
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
