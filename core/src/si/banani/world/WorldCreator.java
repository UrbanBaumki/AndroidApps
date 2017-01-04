package si.banani.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import si.banani.learning.LearningGdx;
import si.banani.scene.Scene;
import si.banani.textures.TextureManager;
import si.banani.tiles.Box;
import si.banani.tiles.Door;
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
    public WorldCreator(World world, TiledMap map){
        this.world = world;
        this.map = map;
        this.currSwitch = 0;
        this.switches = new Array<Switch>();

        createTileFixtures(2, Tiles.FLOOR);
        createTileFixtures(4, Tiles.SPIKES);
        createTileFixtures(5, Tiles.BOX);
        createTileFixtures(6, Tiles.FLOOR);
        createTileFixtures(7, Tiles.SWITCHES);
        createTileFixtures(8, Tiles.DOORS);
    }

    private void createTileFixtures(int index, Tiles type){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object: map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
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

                    shape.setAsBox(rect.getWidth()/2 / LearningGdx.PPM, rect.getHeight()/2 / LearningGdx.PPM);
                    fdef.shape = shape;
                    fdef.friction = 0.3f;
                    fdef.density = 1f;
                    fdef.restitution = 0f;

                    body.createFixture(fdef);
                    break;
                case SWITCHES:
                    Switch s = new Switch(world, rect, TextureManager.getRegionByName("switch").split(7, 16)[0], 1/8f);
                    Scene.addObjectToScene( s );
                    this.switches.add(s);
                    break;
                case DOORS:
                    Door door = new Door(world, rect, TextureManager.getRegionByName("doors").split(32, 64)[0], 0f);
                    Scene.addObjectToScene( door );
                    addDoorToSwitchWithIndex(currSwitch, door);
                    break;
            }

        }
        //for polygons
        for(MapObject object: map.getLayers().get(index).getObjects().getByType(PolygonMapObject.class)){
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
                    fdef.friction = 0f;
                    fdef.density = 1f;

                    fdef.restitution = 0f;

                    Fixture f = body.createFixture(fdef);
                    f.setUserData("stairs");
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
