package si.banani.world;

/**
 * Created by Urban on 8.12.2016.
 */

public class CollisionBits {
    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short SPIKES_BIT = 4;
    public static final short ENEMY_BIT = 8;
    public static final short OBJECT_BIT = 16;
    public static final short ROCK_ENEMY_BIT = 32;
    public static final short SPIDER_ENEMY_BIT = 64;
    public static final short SWITCH_BIT = 128;
    public static final short GHOST_BIT = 256;
    public static final short DOORS_BIT = 512;
    public static final short NONCOLLISION_BIT = 1024;
    public static final short GHOST_PATH_BIT = 2048;
}
