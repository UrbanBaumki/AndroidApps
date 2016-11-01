package si.banani.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Urban on 25.10.2016.
 */

public class TextureManager {

    private static TextureManager txm;
    private static HashMap<String, TextureAtlas> atlasFiles;
    private static HashMap<String, TextureRegion> regions;

    public static TextureManager getInstance(){
        if(txm == null) txm = new TextureManager();
        return txm;
    }
    public static void splitAtlasIntoRegions(){
        if(regions == null) regions = new HashMap<String, TextureRegion>();

        //loop the atlases
        Iterator it = atlasFiles.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            TextureAtlas reg = (TextureAtlas) pair.getValue();
            Array<TextureAtlas.AtlasRegion> tmpReg = reg.getRegions();
            for(int i = 0; i < tmpReg.size; i++){
                TextureRegion text = tmpReg.get(i);
                regions.put(tmpReg.get(i).name, text);
            }
            it.remove();
        }


    }
    /*
        This function is used for adding atlas/spritesheet files. It is used to store multiple atlas/spritesheet files
        and keeping their sliced regions
     */
    public static void addAtlas(String path, String name){
        if(atlasFiles == null)
            atlasFiles = new HashMap<String, TextureAtlas>();

        atlasFiles.put(name, new TextureAtlas(Gdx.files.internal(path)));
    }
    /*
        @param atlasName    Name of the atlas file specified in the addAtlas() function
        @param regionName   Name of the region from the .pack file (or the file name without the extention)
     */
    public static TextureRegion findRegionByName(String atlasName, String regionName){
        return atlasFiles.get(atlasName).findRegion(regionName);
    }

    public static TextureRegion getRegionByName(String regionName){
        return regions.get(regionName);
    }

    public static void disposeAll(){

        if(regions == null) return;
        Iterator it = regions.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            TextureRegion reg = (TextureRegion) pair.getValue();

            reg.getTexture().dispose();
        }

        if(atlasFiles == null) return;
        it = atlasFiles.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            TextureAtlas reg = (TextureAtlas) pair.getValue();

            reg.dispose();
        }
    }

}
