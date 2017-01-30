package si.banani.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import si.banani.maps.MapFactory;

/**
 * Created by Urban on 28.1.2017.
 */

public class ShaderFactory {

    public static enum ShaderType{
        WATER_SHADER,
        BLACK_AND_WHITE_SHADER,
        DEFAULT_SHADER
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
            case DEFAULT_SHADER:
                shader = _shaders.get(ShaderType.DEFAULT_SHADER);
                if(shader == null)
                {
                    shader = new DefaultShader();
                    _shaders.put(ShaderType.DEFAULT_SHADER, shader);
                }
                break;
            case BLACK_AND_WHITE_SHADER:
                shader = _shaders.get(ShaderType.BLACK_AND_WHITE_SHADER);
                if(shader == null)
                {
                    shader = new BlackAndWhiteShader();
                    _shaders.put(ShaderType.BLACK_AND_WHITE_SHADER, shader);
                }
                break;


        }

        return shader;
    }
    public static void disposeShaders(){
        Set<ShaderType> keys = _shaders.keySet();

        //Obtaining iterator over set entries
        Iterator<ShaderType> itr = keys.iterator();

        //Displaying Key and value pairs
        ShaderType str;
        while (itr.hasNext()) {

            str = itr.next();

            _shaders.get(str).dispose();
        }

        _shaders.clear();
    }
}
