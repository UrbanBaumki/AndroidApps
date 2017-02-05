package si.banani.serialization;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by Urban on 4.2.2017.
 */

public class SaveGameDescriptor {

    private float lastX, lastY;
    private float lastGhostEnergy;
    private int playerHealth;

    public SaveGameDescriptor(){

    }


    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public float getLastGhostEnergy() {
        return lastGhostEnergy;
    }

    public void setLastGhostEnergy(float lastGhostEnergy) {
        this.lastGhostEnergy = lastGhostEnergy;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }
    public String toJson(){
        Json json = new Json();
        String s = "";
        json.setOutputType(JsonWriter.OutputType.json);
        s = json.toJson(this);
        return s;
    }
}
