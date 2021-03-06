package si.banani.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.banani.camera.CameraEffects;
import si.banani.conversations.ConversationHolder;
import si.banani.entities.EntityFactory;
import si.banani.learning.LearningGdx;
import si.banani.maps.MapFactory;
import si.banani.scenes.DialogUI;
import si.banani.sound.AudioObserver;
import si.banani.sound.AudioSubject;
import si.banani.textures.TextureManager;

/**
 * Created by Urban on 3.2.2017.
 */

public class CutsceneScreen extends Play implements AudioSubject {

    private Image transitionImage;
    private Stage stage;
    private Stage dialogStage;
    private Viewport viewport;
    private OrthographicCamera camera;

    //actions
    private Action screenFadeOutAction, screenFadeInAction;
    private Action switchScreenAction;
    private Action setupScene1;

    private Action cutscene;

    private Array<AudioObserver> observers;

    private CutSceneAnimated playerWalking;

    private DialogUI dialogUI;
    private boolean dialogRunning = false;

    private int cutsceneNum;

    public CutsceneScreen(SpriteBatch spriteBatch, int cutsceneNum) {
        super(spriteBatch, MapFactory.MapType.CHAPTER5);

        this.cutsceneNum = cutsceneNum;

        TextureManager.addAtlas("content.pack", "contentAtlas");
        //TextureManager.splitAtlasIntoRegions();


        viewport = mapManager.getCurrentViewport();
        stage = new Stage(viewport);

        dialogStage = new Stage(new FitViewport(LearningGdx.V_WIDTH,LearningGdx.V_HEIGHT));


        playerWalking = new CutSceneAnimated("playerMale", 15, 48, 1/7f);

        dialogUI = new DialogUI();


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
                mapManager.disableCurrentMapMusic();
                ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.MAIN_MENU, batch);
            }
        };


        //SCENE SETUPING

        setupScene1 = new RunnableAction(){

            @Override
            public void run() {

                mapManager.loadMap(MapFactory.MapType.CHAPTER3, 1);

                mapManager.disableCurrentMapMusic();



                camera = mapManager.getCurrentCamera();
                viewport = mapManager.getCurrentViewport();
                stage.setViewport(viewport);

                CameraEffects.setCamera(camera);



                playerWalking.setVisible(true);
                playerWalking.setPosition(1, 5);
                playerWalking.setAnimate(false);

                dialogUI.setVisible(false);

                ConversationHolder.getInstance().setCurrent_chapter("11");

                camera.position.set(playerWalking.getX(),playerWalking.getY(),0);
                camera.update();

                //here we set initial things, positioning of images, camera etc...

            }
        };

        stage.addActor(playerWalking);


        dialogStage.addActor(dialogUI);
        dialogUI.setVisible(false);
        dialogUI.setKeepWithinStage(false);
        dialogStage.addActor(transitionImage);
       // stage.addActor(dialogUI);
        transitionImage.toFront();




    }
    @Override
    public void show(){

        cutscene = getCutsceneAction(cutsceneNum);
        stage.addAction(cutscene);


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


            mapManager.set_mapChanged(false);

            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        mapRenderer.setView(camera);
        mapManager.renderCurrentMapForCutscene(batch, delta, mapRenderer);



        //playerWalking.setPosition(camera.position.x- LearningGdx.V_WIDTH/2/LearningGdx.PPM, camera.position.y- LearningGdx.V_HEIGHT/2/LearningGdx.PPM);
        camera.position.y = playerWalking.getY();
        camera.position.x = playerWalking.getX();
        //transitionImage.setPosition(camera.position.x- LearningGdx.V_WIDTH/2/LearningGdx.PPM, camera.position.y- LearningGdx.V_HEIGHT/2/LearningGdx.PPM);
        transitionImage.setPosition(0,0);
        camera.update();

        stage.act(delta);
        stage.draw();


        if(dialogRunning){



            //dialogUI.setPosition(playerWalking.getX(), playerWalking.getY()+0.5f);
            dialogUI.setPosition(dialogUI.getStage().getWidth()/2 - dialogUI.getWidth()/2, dialogUI.getStage().getHeight() - dialogUI.getHeight());
            if(dialogUI.render(delta)){

                String next = ConversationHolder.getInstance().getCurrentText();

                if (next == null)
                    enableDialog(false);
                else
                    dialogUI.setNextText(next, next.length()/10f);
            }

        }

        dialogStage.act(delta);
        dialogStage.draw();

    }

    public void enableDialog(boolean b){
        dialogUI.setVisible(b);
        dialogRunning = b;

    }
    @Override
    public void update(float delta){
        //CameraEffects.updateCamera(delta);
    }

    private Action getCutsceneAction(int cutsceneNum){
        Array<Action> allActions = new Array<Action>();
        //reset
        screenFadeInAction.reset();
        screenFadeOutAction.reset();
        switchScreenAction.reset();
        setupScene1.reset();

        Action intro = Actions.sequence( Actions.addAction(setupScene1),
                                Actions.addAction(screenFadeInAction),
                                Actions.delay(1f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {

                                        enableDialog(true);

                                    }
                                }),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        playerWalking.setAnimate(true);
                                    }
                                }),
                                Actions.addAction(Actions.moveBy(2f, 0f, 5, Interpolation.linear), playerWalking),
                                Actions.delay(5),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        playerWalking.setAnimate(false);
                                    }
                                }),
                                Actions.delay(5f),
                                Actions.addAction(screenFadeOutAction),
                                Actions.delay(3f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        ScreenManager.getInstance().changeScreensAndDispose(ScreenEnums.PLAY, batch, MapFactory.MapType.CHAPTER3);
                                    }
                                })


                );

        allActions.add(intro);


        return allActions.get(cutsceneNum);

    }
    @Override
    public void dispose(){
        super.dispose();
    }

    @Override
    public void hide(){}
    @Override
    public void pause(){

    }

    @Override
    public void addObserver(AudioObserver audioObserver) {
        observers.add(audioObserver);
    }

    @Override
    public void removeObserver(AudioObserver audioObserver) {
        observers.removeValue(audioObserver, true);
    }

    @Override
    public void removeAllObservers() {
        observers.removeAll(observers, true);
    }

    @Override
    public void notify(AudioObserver.AudioCommand command, AudioObserver.AudioTypeEvent event) {
        for(AudioObserver observer: observers){
            observer.onNotify(command, event);
        }
    }
}
