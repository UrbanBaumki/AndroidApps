package si.banani.learning;


import com.badlogic.gdx.Game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import si.banani.screens.ScreenEnums;
import si.banani.screens.ScreenManager;



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
		ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.PLAY, batch);
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
		super.dispose();
	}
	@Override
	public void resize(int width, int height){
		super.resize(width, height);
	}
}
