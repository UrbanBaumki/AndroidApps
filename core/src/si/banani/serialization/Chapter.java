package si.banani.serialization;

/**
 * Created by Urban on 5.2.2017.
 */

public class Chapter {
    private Integer isFinished;
    private Integer lastLevelPlayed;
    private float lastX, lastY;
    private int lastPlayerHealth;
    private float lastGhostEnergy;
    public Chapter(){

    }

    public int isFinished() {
        return isFinished;
    }

    public void setFinished(int finished) {
        this.isFinished = finished;
    }

    public int getLastLevelPlayed() {
        return lastLevelPlayed;
    }

    public void setLastLevelPlayed(int lastLevelPlayed) {
        this.lastLevelPlayed = lastLevelPlayed;
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

    public int getLastPlayerHealth() {
        return lastPlayerHealth;
    }

    public void setLastPlayerHealth(int lastPlayerHealth) {
        this.lastPlayerHealth = lastPlayerHealth;
    }

    public float getLastGhostEnergy() {
        return lastGhostEnergy;
    }

    public void setLastGhostEnergy(float lastGhostEnergy) {
        this.lastGhostEnergy = lastGhostEnergy;
    }


}
