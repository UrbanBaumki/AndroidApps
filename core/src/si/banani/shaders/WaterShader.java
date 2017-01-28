package si.banani.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 28.1.2017.
 */

public class WaterShader extends Shader implements ShaderInterface {

    private Texture water, perlin;
    private float time;
    public WaterShader(){

        ShaderProgram.pedantic = false;

        this.VERTEX_SHADER_PATH = "bin/shaders/water.vsh";
        this.FRAGMENT_SHADER_PATH = "bin/shaders/water.fsh";

        shaderProgram = new ShaderProgram(Gdx.files.internal(VERTEX_SHADER_PATH) ,Gdx.files.internal(FRAGMENT_SHADER_PATH));

        water = new Texture(Gdx.files.internal("water.png"));
        perlin = new Texture(Gdx.files.internal("perlin.jpg"));

        time = 0;
    }


    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera, float delta) {
        //water shader
        time += delta;
        time = time % 10f;
        float angle = time * (2 * MathUtils.PI);
        if (angle > (2 * MathUtils.PI))
            angle -= (2 * MathUtils.PI);


        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_projTrans", camera.combined, false);
        shaderProgram.setUniformf("u_deltatime", angle);
        shaderProgram.setUniformi("u_texture_perlin", 1);
        shaderProgram.setUniformf("u_playerposition", camera.position.x);
        //wS.setUniformf("u_speed", 0.1f);
        shaderProgram.end();

        //Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
        perlin.bind(1);

        batch.setShader(shaderProgram);
        batch.begin();
        water.bind(0);
        //Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
        batch.draw(water, 200/ LearningGdx.PPM, 80/LearningGdx.PPM, water.getWidth()/LearningGdx.PPM, water.getHeight()/LearningGdx.PPM);
        batch.end();

    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
        water.dispose();
        perlin.dispose();
    }
}
