package si.banani.learning;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import si.banani.screens.Splash;

public class LearningGdx extends Game {


	@Override
	public void create () {

		this.setScreen(new Splash());
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
		super.dispose();

	}
	@Override
	public void resize(int width, int height){
		super.resize(width, height);
	}
}
