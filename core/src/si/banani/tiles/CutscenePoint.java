package si.banani.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.maps.MapFactory;

/**
 * Created by Urban on 7.2.2017.
 */

public class CutscenePoint extends InteractiveTile {
    int cutsceneNum;
    private MapFactory.MapType nextChap;
    private int nextLevel;
    private boolean finished;
    private boolean dialogScreen;
    public CutscenePoint(World world, Rectangle rectangle, int cutsceneNum, String nextChap, int nextLevel, boolean finished, boolean dialogScreen) {
        super(world, rectangle);
        this.cutsceneNum = cutsceneNum;
        this.nextChap = MapFactory.MapType.valueOf(nextChap);
        this.nextLevel = nextLevel;
        this.finished = finished;
        this.dialogScreen = dialogScreen;
        fixture.setSensor(true);
        fixture.setUserData(this);
    }

    @Override
    public void render(SpriteBatch batch, float dt) {

    }

    @Override
    public void update(float dt) {

    }

    public int getCutsceneNum() {
        return cutsceneNum;
    }

    public void setCutsceneNum(int cutsceneNum) {
        this.cutsceneNum = cutsceneNum;
    }

    public MapFactory.MapType getNextChap() {
        return nextChap;
    }

    public void setNextChap(MapFactory.MapType nextChap) {
        this.nextChap = nextChap;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(int nextLevel) {
        this.nextLevel = nextLevel;
    }

    public boolean isDialogScreen() {
        return dialogScreen;
    }

    public void setDialogScreen(boolean dialogScreen) {
        this.dialogScreen = dialogScreen;
    }
}
