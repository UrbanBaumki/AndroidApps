package si.banani.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Urban on 3.2.2017.
 */

public class EnemyManager {
    private static Array<BasicPlayer> enemies = new Array<BasicPlayer>();
    private static EnemyManager enemyManager;
    //singleton
    public static EnemyManager getInstance(){
        if(enemyManager == null)
            enemyManager = new EnemyManager();

        return enemyManager;
    }

    public void addEnemy(BasicPlayer enemy){
        enemies.add(enemy);
    }
    public void updateEnemies(float dt){
        for(BasicPlayer enemy : enemies){
            enemy.update(dt);
        }
    }
    public void renderEnemies(SpriteBatch batch, float dt){
        for(BasicPlayer enemy: enemies){
            enemy.render(batch, dt);
        }
    }
    public void clearCachedEnemies(){
        enemies.clear();
    }
}
