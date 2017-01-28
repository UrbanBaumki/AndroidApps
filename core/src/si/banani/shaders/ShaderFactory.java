package si.banani.shaders;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Urban on 28.1.2017.
 */

public class ShaderFactory {

    public static enum ShaderType{
        WATER_SHADER,
        BLACK_AND_WHITE_SHADER
    }
    private static Hashtable<ShaderType, Shader> _shaders = new Hashtable<ShaderType, Shader>();

    public static Shader getShader(ShaderType shaderType){
        Shader shader = null;

        switch (shaderType){
            case WATER_SHADER:
                shader = _shaders.get(ShaderType.WATER_SHADER);
                if(shader == null)
                {
                    shader = new WaterShader();
                    _shaders.put(ShaderType.WATER_SHADER, shader);
                }
                break;


        }

        return shader;
    }
}
