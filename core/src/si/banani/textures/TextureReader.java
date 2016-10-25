package si.banani.textures;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by Urban on 25.10.2016.
 */

public class TextureReader {

    private HashMap<String, Texture> textures;

    public TextureReader(){
        textures = new HashMap<String, Texture>();
    }

    public void loadTexture(String key, String path){
        Texture loaded = new Texture(path);
        if(loaded != null){
            textures.put(key, loaded);
        }
    }

    public Texture getTexture(String key){
        return textures.get(key);
    }

    public void disposeTexture(String key){
        if(textures.get(key) != null)
            textures.get(key).dispose();
    }

}
