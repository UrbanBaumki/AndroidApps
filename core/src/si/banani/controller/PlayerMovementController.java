package si.banani.controller;


import com.badlogic.gdx.utils.Array;
import si.banani.camera.CameraEffects;
import si.banani.entities.BasicPlayer;
import si.banani.entities.Player;


/**
 * Created by Urban on 25.12.2016.
 */

public class PlayerMovementController {

    private static PlayerMovementController pmc;
    private static Array<BasicPlayer> players;
    private static int current_player = 0;

    public static PlayerMovementController getInstance(){
        if(pmc == null)
            return new PlayerMovementController();
        return pmc;
    }
    public void addPlayer(BasicPlayer player){
        if(players == null)
            players = new Array<BasicPlayer>();
        players.add(player);
    }
    public void doSwitch(){
        ((players.get(current_player))).doSwitch();
    }
    public void switchPlayer(){
        movePlayerDown(false);
        movePlayerRigth(false);
        movePlayerUp(false);
        movePlayerLeft(false);

        players.get(current_player).setActive(false);
        current_player = 1 - current_player;
        players.get(current_player).setActive(true);
        //also switch the camera focus
        CameraEffects.setTarget(players.get(current_player));

    }
    public void setPlayer(int i){
        current_player = i;
    }

    public void movePlayerLeft(boolean b){
        if(b)
            players.get(current_player).goLeft();
        else
            players.get(current_player).stopLeft();
    }

    public void movePlayerRigth(boolean b){
        if(b)
            players.get(current_player).goRight();
        else
            players.get(current_player).stopRight();
    }
    public void movePlayerUp(boolean b){
        if(b)
            players.get(current_player).goUp();
        else
            players.get(current_player).stopUp();
    }
    public void movePlayerDown(boolean b){
        if(b)
            players.get(current_player).goDown();
        else
            players.get(current_player).stopDown();
    }

    public int getCurrent_player(){ return current_player;}

    public void clearPlayers(){

        players.clear();
    }

}
