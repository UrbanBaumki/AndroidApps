package si.banani.shaders;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by Urban on 28.1.2017.
 */

public abstract class Shader {

    protected ShaderProgram shaderProgram;
    protected String VERTEX_SHADER_PATH;
    protected String FRAGMENT_SHADER_PATH;
    public abstract ShaderProgram getShaderProgram();
    public abstract void dispose();
    public abstract void render(SpriteBatch batch, OrthographicCamera camera, float dt);


}
