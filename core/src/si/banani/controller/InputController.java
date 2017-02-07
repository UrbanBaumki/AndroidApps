package si.banani.controller;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.learning.LearningGdx;
import si.banani.screens.Play;
import si.banani.screens.ScreenEnums;
import si.banani.screens.ScreenManager;
import si.banani.textures.TextureManager;

/**
 * Created by Urban on 25.12.2016.
 */

public class InputController {

    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private Table movementTable, usageTable;
    private final int arrow_size = 35;
    private final int change_size = 40;


    public InputController(SpriteBatch batch){
        this.batch = batch;
        this.camera = new OrthographicCamera();

        this.viewport = new FitViewport(LearningGdx.V_WIDTH, LearningGdx.V_HEIGHT, camera);
        this.stage = new Stage(viewport, batch );

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        createTableLayout();
    }
    private void createTableLayout(){
        this.movementTable = new Table();
        usageTable = new Table();

        //right bottom
        Image up = new Image( TextureManager.getRegionByName("up_arrow").split(51 , 51)[0][0] );
        up.setSize(arrow_size ,arrow_size);
        Image use = new Image( TextureManager.getRegionByName("use").split(51 , 51)[0][0] );
        use.setSize(change_size ,change_size);
        Image change = new Image( TextureManager.getRegionByName("switch_player").split(51,51)[0][0]);
        change.setSize(change_size, change_size);

        //left bottom side
        movementTable.left().bottom();

        Image left = new Image( TextureManager.getRegionByName("left_arrow").split(51 , 51)[0][0] );
        left.setSize(arrow_size , arrow_size);

        Image right = new Image( TextureManager.getRegionByName("right_arrow").split(51 , 51)[0][0] );
        right.setSize(arrow_size ,arrow_size);
        Image down = new Image( TextureManager.getRegionByName("down_arrow").split(51 , 51)[0][0] );
        down.setSize(arrow_size, arrow_size);

        //setting the listeners

        left.addListener(new ImageInputListener(left){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().movePlayerLeft(true);
                image.setScale(scale, scale);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().movePlayerLeft(false);
                image.setScale(1f,1f);
            }
        });
        right.addListener(new ImageInputListener(right){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().movePlayerRigth(true);
                image.setScale(scale,scale);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().movePlayerRigth(false);
                image.setScale(1f,1f);
            }
        });
        up.addListener(new ImageInputListener(up){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().movePlayerUp( true);
                image.setScale(scale,scale);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().movePlayerUp(false);
                image.setScale(1f,1f);
            }
        });

        down.addListener(new ImageInputListener(down){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().movePlayerDown(true);
                image.setScale(scale,scale);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().movePlayerDown(false);
                image.setScale(1f,1f);
            }
        });
        change.addListener(new ImageInputListener(change){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                image.setScale(scaleSmall,scaleSmall);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().switchPlayer();
                image.setScale(1f,1f);
            }
        });
        use.addListener(new ImageInputListener(use){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                image.setScale(scaleSmall,scaleSmall);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayerMovementController.getInstance().doSwitch();
                //Play.gameState = Play.GameState.PAUSED;
                image.setScale(1f,1f);
            }
        });

        stage.addListener(new InputListener(){


            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.BACK) {
                    Play.gameState = Play.GameState.PAUSED;
                }
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return false;
            }
        });

        movementTable.setFillParent(true);
        movementTable.row().pad(6);
        movementTable.add(left).size(left.getWidth(), left.getHeight()).padLeft(8);
        movementTable.add();
        movementTable.add(right).size(right.getWidth(), right.getHeight());


        usageTable.bottom().right();
        usageTable.setFillParent(true);
        usageTable.row().pad(4);
        usageTable.add().expandX();
        usageTable.add(change).size(change.getWidth(), change.getHeight());
        usageTable.add(use).size(use.getWidth(), use.getHeight());
        usageTable.add(up).size(up.getWidth(), up.getHeight());
        usageTable.add(down).size(down.getWidth(), down.getHeight()).padRight(8);


        this.stage.addActor(movementTable);
        this.stage.addActor(usageTable);

    }

    public void resetInputProcessor(){
        Gdx.input.setInputProcessor(stage);
    }

    public void draw(){

        this.stage.draw();
    }
    public void resize(int width, int height){
        this.viewport.update(width,height);
    }


}
