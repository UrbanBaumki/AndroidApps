package si.banani.maps;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.camera.Parallaxer;
import si.banani.sound.AudioObserver;
import si.banani.world.WorldCollideListener;
import si.banani.world.WorldCreator;

/**
 * Created by Urban on 28.1.2017.
 */

public class LoneMap extends Map {

    private static String _mapPath = "map.tmx";

    public LoneMap() {
        super(MapFactory.MapType.CHAPTER1, _mapPath);

        world = new World(new Vector2(0, -10), true);
        overlaper = new WorldCollideListener(world);

        debugRenderer = new Box2DDebugRenderer();
        worldCreator = new WorldCreator(world, _currentMap);
        parallaxer = new Parallaxer(camera);


    }

    @Override
    public void unloadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_ONE);
    }

    @Override
    public void loadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_ONE);
        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, AudioObserver.AudioTypeEvent.MUSIC_CHAPTER_ONE);
    }
}
