package si.banani.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Urban on 2.2.2017.
 */

public class OpacityShader extends Shader implements ShaderInterface {


    private float time, timeToAnimate;
    private int dir;
    public OpacityShader(){

        ShaderProgram.pedantic = false;
        VERTEX_SHADER_PATH = "bin/shaders/passtrough.vsh";
        FRAGMENT_SHADER_PATH = "bin/shaders/opacity.fsh";

        shaderProgram = new ShaderProgram(Gdx.files.internal(VERTEX_SHADER_PATH), Gdx.files.internal(FRAGMENT_SHADER_PATH));

        time = 0f;
        timeToAnimate = 2f;
        dir = 1;

    }

    public void resetTimer(){
        time = 0f;
    }
    public void setDirection(int dir ) {
        this.dir = dir;
    }
    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera, float delta) {

        time += dir*delta;

        time = MathUtils.clamp(time, 0f, timeToAnimate);

        float percentage = time/timeToAnimate;


        shaderProgram.begin();
        shaderProgram.setUniformf("u_percent", percentage);
        shaderProgram.end();

    }

    @Override
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }
}
