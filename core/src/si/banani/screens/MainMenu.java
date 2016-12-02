package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import si.banani.animation.Animation;
import si.banani.scenes.Hud;
import si.banani.si.banani.screens.fades.Tweener;
import si.banani.textures.TextureManager;
import si.banani.world.World;

/**
 * Created by Urban on 18.10.2016.
 */

public class MainMenu extends BaseScreen {

    private Sprite boxS, playerS, terrainS1, terrainS2, terrainS3,grassS, grassM;
    TextureRegion box, player, terrain;
    private Animation animation,animation2;
    private float x = 0, y = 0, speed = 0;
    int dir = 1;

    private Hud hud;

    public MainMenu() {
        super();
        this.hud = new Hud(this.batch);
    }

    @Override
    public void show() {



        //First method called when screen opened

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

        grassM.setPosition(300,482);
        playerS = new Sprite(player);

        playerS.setPosition(400,482);

        grassS.setPosition(650,482);

        boxS = new Sprite(box);

        boxS.setPosition(1000 , 400);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(this.hud.stage.getCamera().combined);
        hud.stage.draw();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(dir == 1)
            batch.draw( animation.getCurrentFrame() , x, y );
        else
            batch.draw( animation.getCurrentFrame() , x + player.getRegionWidth() , y, -player.getRegionWidth(), player.getRegionHeight() );

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
             dir = 1;
            if(Gdx.input.getX() <= Gdx.graphics.getWidth() /2){
                dir = -1;
            }
            speed = 40;
            x += 40 * delta * dir;

            animation.update(delta);
        }else
        {
            speed = speed - 5;
            if(speed < 0)
            {
                speed = 0;
                animation.setFrame(0);
            }
            x += speed * delta * dir;

        }

        boxS.draw(batch);
        boxS.setX(boxS.getX() + 90 * delta);
        terrainS1.draw(batch);
        terrainS2.draw(batch);
        terrainS3.draw(batch);

        animation2.update(delta);


        batch.draw(animation2.getCurrentFrame(), 1200, 320, 50, 100);

        //batch.draw(TextureManager.getRegionByName("playerFemale"), 0 ,0);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        TextureManager.disposeAll();
    }
}
