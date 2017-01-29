package si.banani.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import si.banani.entities.BasicPlayer;
import si.banani.entities.FemalePlayer;
import si.banani.entities.Player;
import si.banani.tiles.Ladder;
import si.banani.tiles.Switch;

/**
 * Created by Urban on 9.1.2017.
 */

public class WorldCollideListener {
    private World world;
    private boolean sensorSwitched = false;

    public WorldCollideListener(World world){
        this.world = world;
    }

    public void update(){
        for(Contact contact: world.getContactList()){
            sensorSwitched = false;
            overlaper(contact.getFixtureA(), contact.getFixtureB(), true);
        }
    }

    private void overlaper(Fixture a, Fixture b, boolean reverse){

        //Player and switch
        if(a.getUserData() instanceof Player && b.getUserData() instanceof Switch){
            Player player = (Player) a.getUserData();
            Switch _switch = (Switch) b.getUserData();

            if(player.isSwitching() && _switch.getSwitchType() == Switch.SwitchType.SWITCH_PLAYER){
                _switch.activate();
            }

        }
        if(a.getUserData() instanceof FemalePlayer && b.getUserData() instanceof Switch && ((Switch) b.getUserData()).getSwitchType() == Switch.SwitchType.SWITCH_GHOST){

            if(((FemalePlayer)a.getUserData()).isSwitching()){
                ((Switch)b.getUserData()).activate();
            }
        }





        if(reverse)
            overlaper(b, a, false);
    }
}
