package si.banani.sound;

/**
 * Created by Urban on 20.1.2017.
 */

public interface AudioObserver {

    public static enum AudioTypeEvent{

        MUSIC_CHAPTER_ONE("audio/ambience1.ogg"),
        MUSIC_CHAPTER_TWO("audio/ambience2.ogg"),
        MUSIC_CHAPTER_THREE("audio/ambience3_2.ogg"),
        MUSIC_CHAPTER_FIVE("audio/ambience4_2.ogg"),
        MUSIC_ENDING("audio/ending.ogg"),
        SOUND_SWITCH_ON("audio/switchOn.ogg"),
        SOUND_SWITCH_OFF("audio/switchOff.ogg"),
        SOUND_STEP_GRASS_1("audio/step_grass1.ogg"),
        SOUND_STEP_GRASS_2("audio/step_grass2.ogg"),
        SOUND_STEP_GRASS_3("audio/step_grass3.ogg"),
        SOUND_STEP_GRASS_4("audio/step_grass4.ogg"),
        SOUND_HURT_1("audio/hurt1.ogg"),
        SOUND_HURT_2("audio/hurt2.ogg"),
        SOUND_POTION("audio/potion.ogg"),
        SOUND_ZOMBIE_DIE("audio/zombie_die.ogg"),
        NONE("");

        private String _fullFilePath;

        AudioTypeEvent(String fullPath){
            this._fullFilePath = fullPath;
        }

        public String getValue(){
            return _fullFilePath;
        }
    }

    public static enum AudioCommand{
        MUSIC_LOAD,
        MUSIC_PLAY_ONCE,
        MUSIC_PLAY_LOOP,
        MUSIC_STOP,
        MUSIC_STOP_ALL,
        SOUND_LOAD,
        SOUND_PLAY_ONCE,
        SOUND_PLAY_LOOP,
        SOUND_STOP
    }

    void onNotify(AudioCommand command, AudioTypeEvent event);
}
