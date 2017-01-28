package si.banani.world;

import com.badlogic.gdx.Gdx;
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

import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.textures.TextureManager;
import si.banani.tiles.Box;
import si.banani.tiles.DialogPoint;
import si.banani.tiles.Door;
import si.banani.tiles.Ladder;
import si.banani.tiles.Potion;
import si.banani.tiles.Prop;
import si.banani.tiles.Spikes;
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
    private MapManager mapManager;
    public WorldCreator(World world, String mapName){
        this.world = world;

        this.currSwitch = 0;
        this.switches = new Array<Switch>();
        mapManager = new MapManager();
        mapManager.loadMap(mapName);

        createTileFixtures("Floor", Tiles.FLOOR);
        createTileFixtures("Spikes", Tiles.SPIKES);
        createTileFixtures("Boxes", Tiles.BOX);
        createTileFixtures("Ceil", Tiles.FLOOR);
        createTileFixtures("Switches", Tiles.SWITCHES);
        createTileFixtures("Doors", Tiles.DOORS);
        createTileFixtures("GhostPath", Tiles.GHOST_PATH);
        createTileFixtures("Props", Tiles.PROPS);
        createTileFixtures("Swings", Tiles.SWINGS);
        createTileFixtures("Ladders", Tiles.LADDERS);
        createTileFixtures("Dialogs", Tiles.DIALOG);
        createTileFixtures("Potions", Tiles.POTION);
    }

    private void createTileFixtures(String layerName, Tiles type){
        BodyDef bdef = new BodyDef();

        FixtureDef fdef = new FixtureDef();
        Body body;

        MapLayer mapLayer = mapManager.getMapLayer(layerName);
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
                    Switch s = new Switch(world, rect, TextureManager.getRegionByName("switch").split(7, 16)[0], 1/10f);
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

                    Scene.addObjectToScene( new Prop(world, rect) );
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



                    bdef.type = BodyDef.BodyType.DynamicBody;
                    bdef.position.set( (rect.getX()  ) / LearningGdx.PPM , (rect.getY()  ) / LearningGdx.PPM);

                    body = world.createBody(bdef);


                    //swing fixture
                    PolygonShape pS = new PolygonShape();

                    float [] v2 = rect.getVertices();
                    for( int i = 0;i < v2.length; i++){

                        v2[i]= v2[i] / LearningGdx.PPM;

                    }
                    pS.set(v2);

                    fdef.shape = pS;
                    fdef.friction = 1f;
                    fdef.density = 1f;

                    fdef.restitution = 0f;


                    body.createFixture(fdef);


                    //the circle
                    bdef = new BodyDef();
                    bdef.type = BodyDef.BodyType.DynamicBody;
                    bdef.position.set((rect.getX()  ) / LearningGdx.PPM, (rect.getY()  ) / LearningGdx.PPM - 20/LearningGdx.PPM);

                    Body circleBody = world.createBody(bdef);


                    //fixture definition for the circle fixture
                    FixtureDef fdef2 = new FixtureDef();
                    CircleShape circleShape = new CircleShape();
                    circleShape.setRadius(8f/LearningGdx.PPM);

                    fdef2.shape = circleShape;
                    fdef2.density = 200f;
                    fdef2.restitution = 0f;
                    fdef2.friction = 2f;

                    circleBody.createFixture(fdef2);
                    circleBody.setFixedRotation(true);


                    //the revolute joint def
                    RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                    revoluteJointDef.bodyA = body;
                    revoluteJointDef.bodyB = circleBody;
                    revoluteJointDef.localAnchorA.set(rect.getBoundingRectangle().getWidth()/2, -10/LearningGdx.PPM);

                    revoluteJointDef.collideConnected = false;
                    revoluteJointDef.enableMotor = true;
                    revoluteJointDef.maxMotorTorque = 1;
                    revoluteJointDef.motorSpeed = 0;

                    world.createJoint(revoluteJointDef);

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
    public TiledMap getCurrentMap(){
        return mapManager.getCurrentMap();
    }
}
