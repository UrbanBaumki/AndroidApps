package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.camera.CameraEffects;
import si.banani.entities.EntityFactory;
import si.banani.learning.LearningGdx;
import si.banani.maps.MapFactory;

/**
 * Created by Urban on 3.2.2017.
 */

public class CutsceneScreen extends Play {

    private Image transitionImage;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    //actions
    private Action screenFadeOutAction, screenFadeInAction;
    private Action switchScreenAction;
    private Action setupScene1;

    private Action intro;

    public CutsceneScreen(SpriteBatch spriteBatch) {
        super(spriteBatch);
        super.gameState = GameState.PAUSED;

        viewport = mapManager.getCurrentViewport();
        stage = new Stage(viewport);





        //creating a black transition image from pixmap
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        transitionImage = new Image();
        transitionImage.setDrawable(drawable);
        transitionImage.setFillParent(true);

        //defining reusable actions
        screenFadeOutAction = new Action() {
            @Override
            public boolean act(float delta) {
                transitionImage.addAction( Actions.sequence( Actions.alpha(0), Actions.fadeIn(3f) ) );
                return true;
            }
        };

        screenFadeInAction = new Action() {
            @Override
            public boolean act(float delta) {
                transitionImage.addAction( Actions.sequence( Actions.alpha(1), Actions.fadeOut(3f) ) );
                return true;
            }
        };

        switchScreenAction = new RunnableAction(){

            @Override
            public void run() {

                ScreenManager.getInstance().changeScreensAndPause(ScreenEnums.MAIN_MENU, batch);
            }
        };


        //SCENE SETUPING

        setupScene1 = new RunnableAction(){

            @Override
            public void run() {

                mapManager.loadMap(MapFactory.MapType.CHAPTER5);
                //mapManager.disableCurrentMapMusic();



                camera = mapManager.getCurrentCamera();
                viewport = mapManager.getCurrentViewport();
                stage.setViewport(viewport);

                CameraEffects.setCamera(camera);

                camera.position.set(2,20,0);
                camera.update();
                //here we set initial things, positioning of images, camera etc...

            }
        };


        stage.addActor(transitionImage);
        transitionImage.toFront();



    }
    @Override
    public void show(){

        intro = getCutsceneAction();
        stage.addAction(intro);


        if(mapRenderer == null){
            mapRenderer = new OrthogonalTiledMapRenderer(mapManager.getCurrentTiledMap(), 1/ LearningGdx.PPM );
            camera = mapManager.getCurrentCamera();
            EntityFactory.giveWorld(mapManager.getCurrentWorld());
        }
    }
    @Override
    public void render(float delta){
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(mapManager.hasMapChanged()){
            mapRenderer.setMap(mapManager.getCurrentTiledMap());
            camera = mapManager.getCurrentCamera();
            mapRenderer.setView(camera);

            mapManager.set_mapChanged(false);

            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

       mapManager.renderCurrentMapForCutscene(batch, delta, mapRenderer);
        camera.position.y -= 2f * delta;
        camera.update();

        stage.act(delta);
        stage.draw();

    }
    @Override
    public void update(float delta){
        //CameraEffects.updateCamera(delta);
    }

    private Action getCutsceneAction(){
        //reset
        screenFadeInAction.reset();
        screenFadeOutAction.reset();
        switchScreenAction.reset();
        setupScene1.reset();

        return Actions.sequence( Actions.addAction(setupScene1),
                                Actions.addAction(screenFadeInAction),
                                Actions.delay(3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        Gdx.app.log("Teče tole ljiepo", "");
                                    }
                                }),
                                Actions.addAction(switchScreenAction)
                );
    }
}
