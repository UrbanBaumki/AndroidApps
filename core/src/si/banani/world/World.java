package si.banani.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Urban on 27.10.2016.
 */

public class World {
    private TextureRegion[][] region;

    public World (TextureRegion [][] region ) {
        this.region = region;
    }

    public TextureRegion getTile(Tiles tileType){
        int i,j;
        switch(tileType){
            case TALL_GRASS:
                i = 0;
                j = 1;
                break;
            case SHORT_GRASS:
                i = 0;
                j = 0;
                break;
            case TOP_FLOOR:
                i = 1;
                j = 1;
                break;
            case FILL_FLOOR:
                i = 2;
                j = 1;
                break;
            case FILL_LEFT:
                i = 2;
                j = 0;
                break;
            case FILL_RIGHT:
                i = 2;
                j = 2;
                break;
            case TOP_EDGE_LEFT:
                i = 1;
                j = 0;
                break;
            case TOP_EDGE_RIGHT:
                i = 1;
                j = 2;
                break;
            default:
                i = 0;
                j = 0;
        }
        return region[i][j];
    }

}
