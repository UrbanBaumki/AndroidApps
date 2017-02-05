package si.banani.water;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.ArrayList;

/**
 * Created by Urban on 4.2.2017.
 */

public final class PolygonClipping {


    public static boolean inside(Vector2 cp1, Vector2 cp2, Vector2 p){
        return (cp2.x-cp1.x)*(p.y-cp1.y) > (cp2.y-cp1.y)*(p.x-cp1.x);
    }

    public static Vector2 intersection(Vector2 cp1, Vector2 cp2, Vector2 s, Vector2 e){
        Vector2 dc = new Vector2(cp1.x - cp2.x, cp1.y - cp2.y);
        Vector2 dp = new Vector2(s.x - e.x, s.y - e.y);

        float n1 = cp1.x * cp2.y - cp1.y * cp2.x;
        float n2 = s.x * e.y - s.y * e.x;
        float n3 = 1.0f/ (dc.x * dp.y - dc.y * dp.x);

        return new Vector2((n1*dp.x - n2*dc.x) * n3, (n1*dp.y - n2*dc.y)*n3 );
    }


    public static boolean findIntersectionForFixtures(Fixture a, Fixture b, ArrayList<Vector2> outputVertices){

        if(a.getShape().getType() != Shape.Type.Polygon || b.getShape().getType() != Shape.Type.Polygon)
            return false;

        PolygonShape polyA = (PolygonShape) a.getShape();
        PolygonShape polyB = (PolygonShape) b.getShape();
        Vector2 tmpVert;
        for(int i = 0; i < polyA.getVertexCount(); i++)
        {
            tmpVert = new Vector2();
            polyA.getVertex(i, tmpVert);
            Vector2 t = new Vector2(a.getBody().getWorldPoint(tmpVert));
            outputVertices.add(t);
        }

        ArrayList<Vector2> clipPolygon = new ArrayList<Vector2>();
        for(int i = 0; i < polyB.getVertexCount(); i++){
            tmpVert = new Vector2();
            polyB.getVertex(i, tmpVert);
            Vector2 t2 = new Vector2(b.getBody().getWorldPoint(tmpVert));
            clipPolygon.add(t2);
        }


        Vector2 cp1 = clipPolygon.get(clipPolygon.size() - 1 );
        for(int j = 0; j < clipPolygon.size(); j++){
            Vector2 cp2 = clipPolygon.get(j);

            if( outputVertices.isEmpty())
                return false;

            ArrayList<Vector2> inputList = new ArrayList<Vector2>(outputVertices);

            outputVertices.clear();

            Vector2 s = inputList.get(inputList.size() - 1);

            for(int i = 0; i < inputList.size(); i++){
                Vector2 e = inputList.get(i);

                if( inside(cp1, cp2, e) ){

                    if(!inside(cp1, cp2, s) ){
                        outputVertices.add( intersection(cp1, cp2, s, e) );
                    }
                    outputVertices.add(e);

                }else if( inside(cp1, cp2, s) ){
                    outputVertices.add( intersection(cp1, cp2, s, e) );
                }

                s = e;

            }
            cp1 = cp2;
        }

        return !outputVertices.isEmpty();
    }

}
