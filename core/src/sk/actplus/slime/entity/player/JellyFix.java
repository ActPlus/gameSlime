package sk.actplus.slime.entity.player;

/**
 * Created by Ja on 4.4.2017.
 */


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import sk.actplus.slime.other.BodyArray;
import sk.actplus.slime.constants.Category;

/**
 * Created by Admin on 20.12.2016.
 */

public class JellyFix extends Player {

    public static final BodyDef.BodyType BODY_TYPE = BodyDef.BodyType.DynamicBody;
    public static short HITBOX_category = Category.JELLY_HITBOX;
    public static short PARTICLES_category = Category.JELLY;

    public static final int NUM_SEGMENTS = 7;
    public static final boolean HITBOX_ROTATION = false;
    public static final boolean PARTICLES_ROTATION = false;

    public static final float HITBOX_SIDE = 3f;
    public static float PARTICLES_RADIUS = HITBOX_SIDE/NUM_SEGMENTS/2f;
    public static final float ORBITAL_SIDE = 3f;

    public static final float HITBOX_DENSITY = 0.0001f;
    public static final float PARTICLES_DENSITY = 0.01f;

    public static final float HITBOX_RESTITUTION = 0f;
    public static final float PARTICLES_RESTITUTION = 0f;



    public static final float HITBOX_FRICTION = 1f;
    public static final float PARTICLES_FRICTION = 1f;

    public static final float HITBOX_FREQUENCY_HZ = 3f;
    public static final float PARTICLES_FREQUENCY_HZ = 1.5f;

    public static final float HITBOX_DAMPING = 0.2f;
    public static final float PARTICLES_DAMPING = 0.2f;


    private Neighbors bodyParticles[][] = new Neighbors[NUM_SEGMENTS][NUM_SEGMENTS];
    public BodyArray bodies = new BodyArray();
    public Body body;


    public JellyFix(World world, float xi, float yi) {
        super(world);
        this.world = world;

        /**
         * Main Body's Definitions, Body Definition and Fixture Definition
         * Creates Main ExpectsInput Body, controlled by Input
         * */

        BodyDef bodyDefMain = defineBody(BODY_TYPE, xi, yi, HITBOX_ROTATION);
        PolygonShape shapeMain = new PolygonShape();
        shapeMain.setAsBox(HITBOX_SIDE /2f, HITBOX_SIDE /2f);

        FixtureDef fixtureDefMain = defineFixture(shapeMain, HITBOX_DENSITY, HITBOX_RESTITUTION, HITBOX_FRICTION, HITBOX_category,HITBOX_category);

        /**
         * Orbital's Body Definitions Definitions, Body Definition and Fixture Definition
         */

        BodyDef bodyDefOrbital = defineBody(BODY_TYPE, xi, yi, PARTICLES_ROTATION);

        CircleShape shapeOrbital = new CircleShape();
        shapeOrbital.setRadius(PARTICLES_RADIUS / 2);
        FixtureDef fixtureDefOrbital = defineFixture(shapeOrbital, PARTICLES_DENSITY, PARTICLES_RESTITUTION, PARTICLES_FRICTION, PARTICLES_category,Category.NOTHING);


        body = createBody(bodyDefMain, fixtureDefMain);
        body.getFixtureList().get(0).setUserData("player");



        float space = (ORBITAL_SIDE / ((float) NUM_SEGMENTS));

        /**
         * Create NUM_SEG x NUM_SEG body grid
         *
         */
        for (int i = 0; i < NUM_SEGMENTS; i++) {
            for (int j = 0; j < NUM_SEGMENTS; j++) {
                //if ((j==0)||(i==0)||(i==NUM_SEGMENTS-1)||(j==NUM_SEGMENTS-1)) {
                    //crete body
                    float x, y;
                    x = ORBITAL_SIDE / 2f - j * space - space / 2f;
                    y = ORBITAL_SIDE / 2f - i * space - space / 2f;

                    Vector2 circlePosition = new Vector2(x, y);

                    Vector2 v2 = body.getPosition().cpy().add(circlePosition);
                    bodyDefOrbital.position.set(v2);

                    // Create the body and fixture
                    Body particle = createBody(bodyDefOrbital, fixtureDefOrbital);
                    bodies.add(particle);
                    bodyParticles[j][i] = new Neighbors(particle);
                //}

                particle.getFixtureList().get(0).setUserData("player");


            }
        }

        for (int i = 0; i < NUM_SEGMENTS; i++) {
            for (int j = 0; j < NUM_SEGMENTS; j++) {
                //load neighbors
                try {
                    bodyParticles[j][i].neighbors.add(bodyParticles[j-1][i-1].body);
                } catch (Exception e){

                }

                try {
                    bodyParticles[j][i].neighbors.add(bodyParticles[j][i-1].body);
                } catch (Exception e){

                }

                try {
                    bodyParticles[j][i].neighbors.add(bodyParticles[j+1][i-1].body);
                } catch (Exception e){

                }

                try {
                    bodyParticles[j][i].neighbors.add(bodyParticles[j+1][i].body);
                } catch (Exception e){

                }
            }
        }

        DistanceJointDef HITBOX_jointDef = defineDistanceJointDef(HITBOX_FREQUENCY_HZ, HITBOX_DAMPING, FIXED_ROTATION);
        DistanceJointDef PARTICLES_jointDef = defineDistanceJointDef(PARTICLES_FREQUENCY_HZ, PARTICLES_DAMPING, FIXED_ROTATION);


        DistanceJointDef tempDef = defineDistanceJointDef(0f,1f,false);
        for (int i = 0; i < NUM_SEGMENTS; i++) {
            for (int j = 0; j < NUM_SEGMENTS; j++) {
                //create joints
                try {
                for (int k = 0; k < bodyParticles[j][i].neighbors.size; k++) {

                        createDistanceJointAtCenter(PARTICLES_jointDef, bodyParticles[j][i].body, bodyParticles[j][i].neighbors.get(k));

                }
                }catch(Exception e){}

                try {
                    if((i==NUM_SEGMENTS/2)&&(j==NUM_SEGMENTS/2)){


                        createDistanceJointSpec(tempDef, bodyParticles[j][i].body, body);

                    } else {
                        createDistanceJointSpec(HITBOX_jointDef, bodyParticles[j][i].body, body);
                    }
                }catch(Exception e){}

            }
        }
    }


    public void initializeBodyList(BodyArray bodyArray, Body bodyMain, float[][] points, BodyDef bodyDef, FixtureDef fixDef) {
        for (int i = 1; i < NUM_SEGMENTS - 1; i++) {
            // Remember to divide by PTM_RATIO to convert to Box2d coordinates
            Vector2 circlePosition = new Vector2(points[i][0], points[i][1]);
            Vector2 v2 = bodyMain.getPosition().cpy().add(circlePosition);
            bodyDef.position.set(v2);

            // Create the body and fixture
            Body body = createBody(bodyDef, fixDef);

            // Add the body to the array to connect joints to it
            // later. b2Body is a C++ object, so must wrap it
            // in NSValue when inserting into it NSMutableArray
            bodyArray.add(body);
        }

        for (Body body: bodyArray) {
            body.getFixtureList().first().setUserData("player");
        }
    }

    public void connectJointsOfLine(BodyArray bodies, Body bodyMain, DistanceJointDef jointDefOUTER, DistanceJointDef jointDefCENTER) {
        Body first;
        Body second = null;

        for (int i = 0; i < bodies.size - 1; i++) {
            first = bodies.get(i);
            second = bodies.get(i + 1);
            createDistanceJointAtCenter(jointDefOUTER, first, second);
            createDistanceJointAtCenter(jointDefCENTER, first, bodyMain);
        }
        createDistanceJointAtCenter(jointDefCENTER, second, bodyMain);
    }

}
