package si.banani.sound;

/**
 * Created by Urban on 20.1.2017.
 */



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public final class Utility {
    public static final AssetManager _assetManager = new AssetManager();
    private static final String TAG = Utility.class.getSimpleName();
    private static InternalFileHandleResolver _filePathResolver =  new InternalFileHandleResolver();


    private final static String DIALOGUI_ATLAS_PATH = "skins/dialogUi.pack";
    private final static String DIALOGUI_SKIN_PATH = "skins/dialogUi.json";

    public static TextureAtlas DIALOGUI_ATLAS = new TextureAtlas(DIALOGUI_ATLAS_PATH);
    public static Skin DIALOGUI_SKIN = new Skin(Gdx.files.internal(DIALOGUI_SKIN_PATH), DIALOGUI_ATLAS);


    public static void unloadAsset(String assetFilenamePath){
        // once the asset manager is done loading
        if( _assetManager.isLoaded(assetFilenamePath) ){
            _assetManager.unload(assetFilenamePath);
        } else {
            Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath );
        }
    }

    public static float loadCompleted(){
        return _assetManager.getProgress();
    }

    public static int numberAssetsQueued(){
        return _assetManager.getQueuedAssets();
    }

    public static boolean updateAssetLoading(){
        return _assetManager.update();
    }

    public static boolean isAssetLoaded(String fileName){
        return _assetManager.isLoaded(fileName);

    }

    public static void loadMapAsset(String mapFilenamePath){
        if( mapFilenamePath == null || mapFilenamePath.equals("") ){
            return;
        }

        if( _assetManager.isLoaded(mapFilenamePath) ){
            return;
        }

        //load asset
        if( _filePathResolver.resolve(mapFilenamePath).exists() ){
            _assetManager.setLoader(TiledMap.class, new TmxMapLoader(_filePathResolver));
            _assetManager.load(mapFilenamePath, TiledMap.class);


            _assetManager.finishLoadingAsset(mapFilenamePath);
            Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Map doesn't exist!: " + mapFilenamePath );
        }
    }


    public static TiledMap getMapAsset(String mapFilenamePath){
        TiledMap map = null;

        // once the asset manager is done loading
        if( _assetManager.isLoaded(mapFilenamePath) ){
            map = _assetManager.get(mapFilenamePath,TiledMap.class);
        } else {
            Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath );
        }

        return map;
    }

    public static void loadSoundAsset(String soundFilenamePath){
        if( soundFilenamePath == null || soundFilenamePath.equals("") ){
            return;
        }

        if( _assetManager.isLoaded(soundFilenamePath) ){
            return;
        }

        //load asset
        if( _filePathResolver.resolve(soundFilenamePath).exists() ){
            _assetManager.setLoader(Sound.class, new SoundLoader(_filePathResolver));
            _assetManager.load(soundFilenamePath, Sound.class);


            _assetManager.finishLoadingAsset(soundFilenamePath);
            Gdx.app.log(TAG, "Sound loaded!: " + soundFilenamePath);
        }
        else{
            Gdx.app.log(TAG, "Sound doesn't exist!: " + soundFilenamePath );
        }
    }


    public static Sound getSoundAsset(String soundFilenamePath){
        Sound sound = null;

        // once the asset manager is done loading
        if( _assetManager.isLoaded(soundFilenamePath) ){
            sound = _assetManager.get(soundFilenamePath,Sound.class);
        } else {
            Gdx.app.log(TAG, "Sound is not loaded: " + soundFilenamePath );
        }

        return sound;
    }

    public static void loadMusicAsset(String musicFilenamePath){
        if( musicFilenamePath == null || musicFilenamePath.equals("") ){
            return;
        }

        if( _assetManager.isLoaded(musicFilenamePath) ){
            return;
        }

        //load asset
        if( _filePathResolver.resolve(musicFilenamePath).exists() ){
            _assetManager.setLoader(Music.class, new MusicLoader(_filePathResolver));
            _assetManager.load(musicFilenamePath, Music.class);


            _assetManager.finishLoadingAsset(musicFilenamePath);
            Gdx.app.log(TAG, "Music loaded!: " + musicFilenamePath);
        }
        else{
            Gdx.app.log(TAG, "Music doesn't exist!: " + musicFilenamePath );
        }
    }


    public static Music getMusicAsset(String musicFilenamePath){
        Music music = null;

        // once the asset manager is done loading
        if( _assetManager.isLoaded(musicFilenamePath) ){
            music = _assetManager.get(musicFilenamePath,Music.class);
        } else {
            Gdx.app.debug(TAG, "Music is not loaded: " + musicFilenamePath );
        }

        return music;
    }


    public static void loadTextureAsset(String textureFilenamePath){
        if( textureFilenamePath == null || textureFilenamePath.equals("") ){
            return;
        }

        if( _assetManager.isLoaded(textureFilenamePath) ){
            return;
        }

        //load asset
        if( _filePathResolver.resolve(textureFilenamePath).exists() ){
            _assetManager.setLoader(Texture.class, new TextureLoader(_filePathResolver));
            _assetManager.load(textureFilenamePath, Texture.class);


            _assetManager.finishLoadingAsset(textureFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath );
        }
    }

    public static Texture getTextureAsset(String textureFilenamePath){
        Texture texture = null;

        // once the asset manager is done loading
        if( _assetManager.isLoaded(textureFilenamePath) ){
            texture = _assetManager.get(textureFilenamePath,Texture.class);
        } else {
            Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath );
        }

        return texture;
    }


}
