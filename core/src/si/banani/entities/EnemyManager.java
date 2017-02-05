package si.banani.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import si.banani.learning.LearningGdx;

/**
 * Created by Urban on 3.2.2017.
 */

public class EnemyManager {
    private static Array<BasicPlayer> enemies = new Array<BasicPlayer>();
    private static Array<BasicPlayer> inactiveEnemies = new Array<BasicPlayer>();

    public static ArrayList<BasicPlayer> removables = new ArrayList<BasicPlayer>();

    private float activateOffset = 200 / LearningGdx.PPM;

    private static Array<BasicPlayer> forTransfer = new Array<BasicPlayer>();
    private static EnemyManager enemyManager;
    //singleton
    public static EnemyManager getInstance(){
        if(enemyManager == null)
            enemyManager = new EnemyManager();

        return enemyManager;
    }

    public void activateEnemy(float x){
        for(BasicPlayer inactive : inactiveEnemies)
        {
            if(inactive.getPosition().x  < x + activateOffset){

                inactive.getBody().setActive(true);
                forTransfer.add(inactive);
            }
        }
        for(BasicPlayer activated : forTransfer){
            inactiveEnemies.removeValue(activated, true);
            enemies.add(activated);
        }
        forTransfer.clear();

    }
    public void addEnemy(BasicPlayer enemy){
        inactiveEnemies.add(enemy);
    }
    public void updateEnemies(float dt, float playerX){
        activateEnemy(playerX);

        for(BasicPlayer enemy : enemies){
            BasicPlayer pleaseRemove = null;
            if(enemy instanceof ZombieEnemy)
            {
                ZombieEnemy z = (ZombieEnemy) enemy;
                enemy.update(dt);
                if(z.isDestroy()){
                    z.setDestroyed(true);
                    pleaseRemove = enemy;
                }

            }
            removables.add(pleaseRemove);
        }
        for(BasicPlayer o : removables){
            enemies.removeValue(o, true);
        }
        removables.clear();
    }
    public void renderEnemies(SpriteBatch batch, float dt){
        for(BasicPlayer enemy: enemies){
            enemy.render(batch, dt);
        }
    }
    public void clearCachedEnemies(){
        enemies.clear();
        inactiveEnemies.clear();
        forTransfer.clear();
    }
}
