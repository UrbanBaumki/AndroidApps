package si.banani.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by Urban on 28.1.2017.
 */

public class DefaultShader extends Shader implements ShaderInterface {

    public DefaultShader(){
        ShaderProgram.pedantic = false;
        VERTEX_SHADER_PATH = "bin/shaders/passtrough.vsh";
        FRAGMENT_SHADER_PATH = "bin/shaders/passtrough.fsh";

        shaderProgram = new ShaderProgram(Gdx.files.internal(VERTEX_SHADER_PATH), Gdx.files.internal(FRAGMENT_SHADER_PATH));

    }

    public ShaderProgram getShaderProgram(){
        return shaderProgram;
    }
    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera, float delta) {
        batch.setShader(shaderProgram);
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }
}
