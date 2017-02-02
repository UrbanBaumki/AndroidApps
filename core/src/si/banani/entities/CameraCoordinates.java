package si.banani.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Urban on 27.1.2017.
 */

public class CameraCoordinates {
    private Player player;
    private FemalePlayer femalePlayer;
    private OrthographicCamera camera;

    public CameraCoordinates(Player player, FemalePlayer femalePlayer, OrthographicCamera camera){
        this.player = player;
        this.femalePlayer = femalePlayer;
        this.camera = camera;
    }

    public Vector3 getProjectedPosition(int i, Viewport viewport){

        if( i == 0)
            return viewport.project(new Vector3(player.getPosition().x, player.getPosition().y, 0));
        else
            return viewport.project(new Vector3(femalePlayer.getPosition().x, femalePlayer.getPosition().y, 0));
    }
    public Vector3 getProjectedPositionFromCamera(int i){

        if( i == 0)
            return camera.project(new Vector3(player.getPosition().x, player.getPosition().y, 0));
        else
            return camera.project(new Vector3(femalePlayer.getPosition().x, femalePlayer.getPosition().y, 0));
    }
    public Vector3 projectWithCamera(int i , Vector3 vec){
        if( i == 0)
            return camera.project(vec);
        else
            return camera.project(vec);
    }
}
