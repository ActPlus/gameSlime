package sk.actplus.slime.version2;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.entity.EntityArray;
import sk.actplus.slime.version2.entity.PolygonGenerator;
import sk.actplus.slime.version2.entity.friendly.Player;
import sk.actplus.slime.version2.entity.mapentity.Triangle;

/**
 * Created by Ja on 17.2.2018.
 */

public class Game {
    private World world;
    private GameScreen screen;
    protected EntityArray entities;
    private MapGenerator mapGen;
    private boolean paused;
    private String savePath;
    private InputMultiplexer mux;
    GameArray array;
    //PolygonGenerator polygonGenerator;

    public
    Game(GameScreen screen, InputMultiplexer mux) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.mux = mux;
        entities = new EntityArray();
        array = new GameArray();
        mapGen= new MapGenerator(screen,array.triangles,new Vector2[]{new Vector2(-2,2),new Vector2(3,3)},new Vector2(2,-3));
        //array.polygonGenerators.add(new PolygonGenerator(array.triangles.get(0).getArrayOfVertices(),3, Color.BLUE));

        Player player= new Player(screen,mux);

        entities.add(player);
        paused = false;
    }



    public void render(float delta) {
        entities.render(delta);
        for (int i = 0; i < array.triangles.size;i++){
            array.triangles.get(i).render(delta);
        }
    }

    //private int index = 0;
    private float dt = 0;
    public void update(float delta) {
        //array.polygonGenerators.get(0).update();
        dt+=delta;
        entities.update(delta);
        if(dt>=0.5) {
            //index++;
            dt =0;
            array.triangles.add(mapGen.generate(mapGen.last,array.triangles));
            //array.polygonGenerators.add(new PolygonGenerator(array.triangles.get(index).getArrayOfVertices(),3, Color.BLUE));
        }

    }

    public void dispose() {
        world.dispose();
        screen.dispose();
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        save();
        paused = true;
    }

    public void resume() {
        load();
        paused = false;
    }

    public void save() {
         //TODO: Save to file method
    }

    public void load() {
        //TODO: load from file method
    }
}
