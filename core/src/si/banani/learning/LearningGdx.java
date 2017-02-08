package si.banani.learning;


import com.badlogic.gdx.Game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import si.banani.maps.MapFactory;
import si.banani.screens.ScreenEnums;
import si.banani.screens.ScreenManager;
import si.banani.serialization.Chapter;
import si.banani.serialization.ChapterDescriptor;
import si.banani.serialization.ProgressDescriptor;
import si.banani.serialization.SaveGameDescriptor;
import si.banani.serialization.Serializer;
import si.banani.sound.Utility;
import si.banani.textures.TextureManager;


public class LearningGdx extends Game {

	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 240;
	public static final float PPM = 100;
	public static final String TITLE = "Beneath the Surface";

	private static SpriteBatch batch;

	@Override
	public void create () {


		if(batch == null)
			batch = new SpriteBatch();
		ScreenManager.getInstance().bindWithMainGameClass(this);
		ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.MAIN_MENU, batch);
	}

	public static SpriteBatch getSpriteBatch(){
		if(batch == null){
			batch = new SpriteBatch();
		}
		return batch;
	}
	@Override
	public void render () {
		super.render();
	}
	@Override
	public void dispose () {

		batch.dispose();
		ScreenManager.getInstance().disposeAll();
		TextureManager.getInstance().disposeAll();
		Utility._assetManager.dispose();


	}
	@Override
	public void resize(int width, int height){
		super.resize(width, height);
	}
}
