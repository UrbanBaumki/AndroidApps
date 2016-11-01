package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import si.banani.animation.Animation;
import si.banani.si.banani.screens.fades.Tweener;
import si.banani.textures.TextureManager;
import si.banani.world.World;

/**
 * Created by Urban on 18.10.2016.
 */

public class MainMenu implements Screen {
    private SpriteBatch batch;
    private Sprite boxS, playerS, terrainS1, terrainS2, terrainS3,grassS, grassM;
    TextureRegion box, player, terrain;
    private Animation animation,animation2;
    private float x = 200;
    @Override
    public void show() {
        //First method called when screen opened
        this.batch = new SpriteBatch();




        TextureManager.addAtlas("content.pack", "contentAtlas");
        TextureManager.splitAtlasIntoRegions();
        box = TextureManager.getRegionByName("box");
        terrain =  TextureManager.getRegionByName("terrain");
        player = TextureManager.getRegionByName("playerFemale");
        TextureRegion[][] terrainSplit = terrain.split(64,64);


        animation = new Animation(TextureManager.getRegionByName("playerMale").split(32,64)[0], 1/6f);
        animation2 = new Animation(TextureManager.getRegionByName("switch").split(7,16)[0], 1/7f);
        //animation.setStartingFrame(1);
        World world = new World(terrainSplit);

        grassS = new Sprite(terrainSplit[0][1]);
        terrainS1 = new Sprite(terrainSplit[1][0]);
        terrainS2 = new Sprite(terrainSplit[1][1]);
        terrainS3 = new Sprite(terrainSplit[1][2]);
        terrainS1.setPosition(300,100);
        terrainS2.setPosition(650,100);
        terrainS3.setPosition(900,100);

        grassM = new Sprite(terrainSplit[0][0]);
        grassM.scale(5f);
        grassM.setPosition(300,482);
        playerS = new Sprite(player);
        playerS.scale(5f);
        playerS.setPosition(400,482);
        terrainS1.scale(5f);
        terrainS2.scale(5f);
        terrainS3.scale(5f);
        grassS.scale(5f);
        grassS.setPosition(650,482);

        boxS = new Sprite(box);
        boxS.scale(5f);
        boxS.setPosition(1000 , 400);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(TextureManager.getRegionByName("playerMale"), 200, 640, 180 * 7, 320);
        batch.draw(animation.getCurrentFrame(), x, 320, 180, 320);
        x += 80 * delta;
        boxS.draw(batch);
        terrainS1.draw(batch);
        terrainS2.draw(batch);
        terrainS3.draw(batch);
        animation.update(delta);
        animation2.update(delta);


        batch.draw(animation2.getCurrentFrame(), 1200, 320, 50, 100);

        //batch.draw(TextureManager.getRegionByName("playerFemale"), 0 ,0);


        grassS.draw(batch);
        grassM.draw(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.batch.dispose();
        TextureManager.disposeAll();

    }
}
