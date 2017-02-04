package si.banani.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

import si.banani.entities.EnemyManager;
import si.banani.entities.EntityFactory;
import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.textures.TextureManager;
import si.banani.tiles.Box;
import si.banani.tiles.CheckPoint;
import si.banani.tiles.DialogPoint;
import si.banani.tiles.Door;
import si.banani.tiles.EndPoint;
import si.banani.tiles.Ladder;
import si.banani.tiles.Potion;
import si.banani.tiles.Prop;
import si.banani.tiles.Spikes;
import si.banani.tiles.Swings;
import si.banani.tiles.Switch;
import si.banani.tiles.Tiles;

/**
 * Created by Urban on 8.12.2016.
 */

public class WorldCreator {

    private World world;
    private TiledMap map;
    private int currSwitch;
    private Array<Switch> switches;
    //private MapManager mapManager;
    public WorldCreator(World world, TiledMap map){
        this.world = world;
        this.map = map;
        this.currSwitch = 0;
        this.switches = new Array<Switch>();
        //mapManager = new MapManager();
        //mapManager.loadMap(mapName);


    }

    public void createTileFixtures(String layerName, Tiles type){
        BodyDef bdef = new BodyDef();

        FixtureDef fdef = new FixtureDef();
        Body body;

        MapLayer mapLayer = map.getLayers().get(layerName);
        if(mapLayer == null) return;

        for(MapObject object: mapLayer.getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            switch (type){
                case SPIKES:
                    new Spikes(world, rect);
                    break;
                case BOX:
                    Scene.addObjectToScene( new Box(world, rect, TextureManager.getRegionByName("box").split(32, 32)[0], 0) );
                    break;
                case FLOOR:
                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set( (rect.getX() + rect.getWidth()/2 ) / LearningGdx.PPM , (rect.getY() + rect.getHeight()/2 ) / LearningGdx.PPM);

                    body = world.createBody(bdef);
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox(rect.getWidth()/2 / LearningGdx.PPM, rect.getHeight()/2 / LearningGdx.PPM);
                    fdef.shape = shape;
                    fdef.friction = 0.3f;
                    fdef.density = 1f;
                    fdef.restitution = 0f;

                    body.createFixture(fdef);
                    break;
                case SWITCHES:
                    int d = Integer.valueOf((String)object.getProperties().get("Dir"));
                    Switch.SwitchType switchType = ((String) object.getProperties().get("Type")).equals("player") ? Switch.SwitchType.SWITCH_PLAYER : Switch.SwitchType.SWITCH_GHOST;
                    TextureRegion [] tr = switchType == Switch.SwitchType.SWITCH_PLAYER ? TextureManager.getRegionByName("switches_red").split(7, 16)[0] : TextureManager.getRegionByName("switches_blue").split(7, 16)[0];
                    Switch s = new Switch(world, rect, tr, 1/10f, d, switchType);
                    Scene.addObjectToScene( s );
                    this.switches.add(s);
                    break;
                case DOORS:
                    Door door = new Door(world, rect, TextureManager.getRegionByName("doors").split(32, 64)[0], 0f);
                    Scene.addObjectToScene( door );
                    addDoorToSwitchWithIndex(currSwitch, door);
                    break;
                case GHOST_PATH:
                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set( (rect.getX() + rect.getWidth()/2 ) / LearningGdx.PPM , (rect.getY() + rect.getHeight()/2 ) / LearningGdx.PPM);

                    body = world.createBody(bdef);
                    PolygonShape path = new PolygonShape();
                    path.setAsBox(rect.getWidth()/2 / LearningGdx.PPM, rect.getHeight()/2 / LearningGdx.PPM);
                    fdef.shape = path;
                    fdef.friction = 0.3f;
                    fdef.density = 1f;
                    fdef.restitution = 0f;

                    Fixture fix = body.createFixture(fdef);
                    Filter f = new Filter();
                    f.categoryBits = CollisionBits.GHOST_PATH_BIT;
                    fix.setFilterData(f);
                    break;

                case PROPS:
                    Texture t = TextureManager.getTexture("box.png");
                    Scene.addObjectToScene( new Prop(world, rect, t) );
                    break;
                case LADDERS:

                    //Scene.addObjectToScene(new Ladder(world, rect, TextureManager.getRegionByName("ladder").split(33, 64)[0]));
                    Scene.addObjectToScene(new Ladder(world, rect, new TextureRegion(TextureManager.getTexture("ladder.png"))));
                    break;
                case DIALOG:
                    String ss =  object.getProperties().get("Chapter", String.class);
                    new DialogPoint(world, rect, ss);
                    break;
                case POTION:
                    TextureRegion [] reg = object.getProperties().get("Type").equals("energy") ? TextureManager.getRegionByName("potion_blue").split(17, 21)[0] : TextureManager.getRegionByName("potion_red").split(17, 21)[0];
                    Potion.PotionType pt = object.getProperties().get("Type").equals("energy") ? Potion.PotionType.ENERGY : Potion.PotionType.HEALTH;
                    Scene.addObjectToScene(new Potion(world, rect, reg ,pt ));
                    break;
                case START:
                    float x = rect.getX();
                    float y = rect.getY();
                    //EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).setTransform(x/LearningGdx.PPM, y/LearningGdx.PPM, 0);
                    EntityFactory.getEntity(EntityFactory.EntityType.PLAYER).setStart(x/LearningGdx.PPM, y/LearningGdx.PPM);
                    //EntityFactory.getEntity(EntityFactory.EntityType.FEMALE).setTransform(x/LearningGdx.PPM - 0.2f, y/LearningGdx.PPM, 0);
                    EntityFactory.getEntity(EntityFactory.EntityType.FEMALE).setStart(x/LearningGdx.PPM - 0.2f, y/LearningGdx.PPM);

                    break;
                case END:

                    new EndPoint(world, rect);

                    break;
                case CHECKPOINT:
                    new CheckPoint(world, rect);
                    break;
                case ENEMIES:
                    String eType =  object.getProperties().get("Type", String.class);
                    EntityFactory.EntityType enemyType = EntityFactory.EntityType.valueOf(eType);
                    EnemyManager.getInstance().addEnemy(EntityFactory.createEnemy(enemyType, rect, world));
                    break;
            }

        }
        //for polygons
        for(MapObject object: mapLayer.getObjects().getByType(PolygonMapObject.class)){
            Polygon rect = ((PolygonMapObject) object).getPolygon();

            switch (type){
                case FLOOR:
                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set( (rect.getX()  ) / LearningGdx.PPM , (rect.getY()  ) / LearningGdx.PPM);

                    body = world.createBody(bdef);
                    PolygonShape polyShape = new PolygonShape();

                    float [] verticies = rect.getVertices();
                    for( int i = 0;i < verticies.length; i++){
                        verticies[i]= verticies[i] / LearningGdx.PPM;
                    }
                    polyShape.set(verticies);

                    fdef.shape = polyShape;
                    fdef.friction = 1f;
                    fdef.density = 1f;

                    fdef.restitution = 0f;

                    Fixture f = body.createFixture(fdef);
                    f.setUserData("stairs");
                    break;
                case SWINGS:

                    Scene.addObjectToScene(new Swings(world, rect));



                    break;
            }

        }

    }

    private void addDoorToSwitchWithIndex(int index, Door door){
        if(currSwitch >= switches.size){
            switches.clear();
            return;
        }

        switches.get(index).addDoor(door);
        currSwitch++;
    }

}
