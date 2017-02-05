package si.banani.water;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.learning.LearningGdx;
import si.banani.shaders.ShaderFactory;
import si.banani.tiles.InteractiveTile;

/**
 * Created by Urban on 4.2.2017.
 */

public class Water extends InteractiveTile {

    private TextureRegion region;
    private float width, height, x, y;
    private int dir = 1;
    private float yOffset, xOffset;
    private OrthographicCamera cam;
    public Water(World world, Rectangle rectangle) {
        super(world, rectangle);

        yOffset = 54f;
        xOffset = -3f;

        fixture.setUserData(this);
        fixture.setSensor(true);
        fixture.setDensity(10f);

        region = new TextureRegion(new Texture(Gdx.files.internal("water.png")));
        region.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        region.setRegion(0,0, (int) rectangle.getWidth(), (int)rectangle.getHeight());
        this.width = region.getRegionWidth();
        this.height = region.getRegionHeight();

        x = body.getPosition().x;
        y = body.getPosition().y;
    }

    @Override
    public void render(SpriteBatch batch, float dt) {
        ShaderFactory.getShader(ShaderFactory.ShaderType.WATER_SHADER).render(batch, cam, dt);
        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
        batch.begin();
        batch.draw(region, x  - dir * width /2/ LearningGdx.PPM + xOffset/LearningGdx.PPM, y - height/2/LearningGdx.PPM  + yOffset / LearningGdx.PPM , dir * width / LearningGdx.PPM, height / LearningGdx.PPM);
        batch.end();

        ShaderFactory.getShader(ShaderFactory.ShaderType.DEFAULT_SHADER).render(batch,cam,dt);
    }

    @Override
    public void update(float dt) {

    }
    public void setCamera(OrthographicCamera cam){
        this.cam = cam;
    }
}
