package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

/**
 * Created by Timotej on 21.2.2018.
 */

public class PolygonGenerator {
    private static PolygonSprite polygonSprite;
    private static PolygonSpriteBatch polyBatch;
    ShapeRenderer shapeRenderer;
    FloatArray vertices;
    Vector2 center;
    Texture texture;


    /**
     *
     * @param vector2s  array of vertices of polygon
     * @param numberOfVertices number of vertices of polygon
     * @param color Color of the polygon
     */
    public PolygonGenerator(Vector2[] vector2s, int numberOfVertices, Color color){
        shapeRenderer = new ShapeRenderer();
        polyBatch = new PolygonSpriteBatch();

        //todo put values insted fo method getWidth/getHeight
        center = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);


        Pixmap pix = new Pixmap(1,1,Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();
        texture = new Texture(pix);
        TextureRegion textureRegion = new TextureRegion(texture);


        // ordered array of x,y coordinates of all vertices
        vertices = new FloatArray(numberOfVertices *2);
        for (int i = 0; i < numberOfVertices; i++){
            vertices.add(vector2s[i].x * 32 + center.x);
            vertices.add(vector2s[i].y * 32 + center.y);
            System.out.println(vertices.get(i));
        }

        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleINdeces = triangulator.computeTriangles(vertices);
        PolygonRegion polygonRegion = new PolygonRegion(textureRegion,vertices.toArray(),triangleINdeces.toArray());
        polygonSprite = new PolygonSprite(polygonRegion);

    }

    //todo add update to update vertices

    public static void render(){
        polyBatch.begin();
        polygonSprite.draw(polyBatch);
        polyBatch.end();
    }

}