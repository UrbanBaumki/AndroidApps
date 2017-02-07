package si.banani.water;

import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import si.banani.entities.BasicPlayer;
import si.banani.entities.Player;

/**
 * Created by Urban on 5.2.2017.
 */

public class WaterHandler {
    private Hashtable<Fixture, Fixture> contactingFixtures = new Hashtable<Fixture, Fixture>();
    private static WaterHandler handler;
    public WaterHandler(){

    }
    public static WaterHandler getInstance(){
        if(handler == null)
            handler = new WaterHandler();
        return handler;
    }
    public void clearPairs(){
        contactingFixtures.clear();
    }
    public void addFixturePair(Fixture objectFixture, Fixture waterFixture){
        contactingFixtures.put(objectFixture, waterFixture);
    }
    public void removeFixtureContact(Fixture fixture){
        contactingFixtures.remove(fixture);
    }
    public void update(){
        Set<Fixture> keys = contactingFixtures.keySet();

        Iterator<Fixture> itr = keys.iterator();
        while (itr.hasNext()) {

            Fixture objectFixture = itr.next(); //object
            Fixture waterFixture = contactingFixtures.get(objectFixture);    //"water" fixture

            float density = waterFixture.getDensity();


            ArrayList<Vector2> intersectionPoints = new ArrayList<Vector2>();
            if(PolygonClipping.findIntersectionForFixtures(waterFixture, objectFixture, intersectionPoints)){

                float [] poli = new float[intersectionPoints.size()*2];
                for(int i = 0; i < intersectionPoints.size(); i++){
                    poli[i*2] = intersectionPoints.get(i).x;
                    poli[i*2+1] = intersectionPoints.get(i).y;
                }
                float area = GeometryUtils.polygonArea(poli, 0, poli.length);

                Vector2 centroid = new Vector2();

                centroid = GeometryUtils.polygonCentroid(poli, 0, poli.length, centroid);
                Vector2 grav = new Vector2(0, 10);

                float displacedMass = density * area;
                Vector2 bouyancyF = grav.cpy().scl(displacedMass);
                if(!(objectFixture.getUserData() instanceof Player))objectFixture.getBody().applyForce(bouyancyF, centroid, true);



                    //applying the drag force
                    Vector2 velocityDirection = objectFixture.getBody().getLinearVelocityFromWorldPoint(centroid);
                    Vector2 normalizedVelocity = velocityDirection.cpy().nor();

                    float dragMagnitudeX = density * normalizedVelocity.x * normalizedVelocity.x;
                    float dragMagnitudeY = density * normalizedVelocity.y * normalizedVelocity.y;

                    Vector2 dragForce = new Vector2(velocityDirection.cpy().scl(-1, -1).scl(dragMagnitudeX, dragMagnitudeY));
                    if((objectFixture.getUserData() instanceof BasicPlayer))
                        objectFixture.getBody().applyForce(dragForce.scl(0.15f), centroid, true);
                    else
                        objectFixture.getBody().applyForce(dragForce, centroid, true);

                    //angular drag
                    float angularDrag = area * -objectFixture.getBody().getAngularVelocity() / 2;
                    objectFixture.getBody().applyTorque(angularDrag, true);




            }
        }
    }
}
