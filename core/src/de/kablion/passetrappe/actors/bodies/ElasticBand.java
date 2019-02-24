package de.kablion.passetrappe.actors.bodies;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.stages.WorldStage;
import de.kablion.passetrappe.utils.Player;
import de.kablion.passetrappe.utils.RepeatablePolygonSprite;

import static de.kablion.passetrappe.utils.Constants.BAND_DIVISIONS;
import static de.kablion.passetrappe.utils.Constants.BAND_GAP;
import static de.kablion.passetrappe.utils.Constants.BAND_HALF_WIDTH;
import static de.kablion.passetrappe.utils.Constants.BAND_SEGMENT_HALF_LENGTH;
import static de.kablion.passetrappe.utils.Constants.BAND_SEGMENT_LENGTH;
import static de.kablion.passetrappe.utils.Constants.BAND_WIDTH;
import static de.kablion.passetrappe.utils.Constants.BIT_BAND;
import static de.kablion.passetrappe.utils.Constants.BIT_BAND_LIMIT;
import static de.kablion.passetrappe.utils.Constants.BIT_DISK;
import static de.kablion.passetrappe.utils.Constants.BIT_WALL;
import static de.kablion.passetrappe.utils.Constants.BOARD_HALF_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.BOARD_HALF_WIDTH;
import static de.kablion.passetrappe.utils.Constants.WALL_HALF_WIDTH;

public class ElasticBand extends BodyActor {

    private Player player;

    private Body[] bodies;
    private RepeatablePolygonSprite[] sprites;

    public ElasticBand(final PasseTrappe app, final WorldStage worldStage, Player player) {
        super(app, worldStage);
        this.player = player;

        initBodies();
        initJoints();
        initSprites();

    }

    private void initBodies() {

        //init segments
        bodies = new Body[BAND_DIVISIONS];

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        PolygonShape segmentBox = new PolygonShape();
        segmentBox.setAsBox(BAND_SEGMENT_HALF_LENGTH, BAND_HALF_WIDTH);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = segmentBox;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 0.5f;
        fixtureDef.filter.categoryBits = BIT_BAND;
        fixtureDef.filter.maskBits = (short) (BIT_DISK | BIT_WALL | BIT_BAND_LIMIT);

        float y = (BOARD_HALF_HEIGHT - BAND_GAP - BAND_HALF_WIDTH), x;
        if (player == Player.TWO) y *= -1;

        for (int i = 0; i < bodies.length; i++) {
            x = -BOARD_HALF_WIDTH + BAND_SEGMENT_HALF_LENGTH + (i * BAND_SEGMENT_LENGTH);
            bodyDef.position.set(x, y);
            bodies[i] = worldStage.getWorld().createBody(bodyDef);
            bodies[i].createFixture(fixtureDef);
            bodies[i].setUserData(this);
        }

        segmentBox.dispose();


        // init Band Limit
        bodyDef.type = BodyDef.BodyType.StaticBody;
        y = (BOARD_HALF_HEIGHT - BAND_GAP - BAND_WIDTH - 0.1f);
        if (player == Player.TWO) y *= -1;
        bodyDef.position.set(0, y);

        EdgeShape edge = new EdgeShape();
        edge.set(-BOARD_HALF_WIDTH, 0, BOARD_HALF_WIDTH, 0);

        fixtureDef.shape = edge;
        fixtureDef.density = 0;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 1;
        fixtureDef.filter.categoryBits = BIT_BAND_LIMIT;
        fixtureDef.filter.maskBits = (short) (BIT_DISK | BIT_BAND);

        body = worldStage.getWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        edge.dispose();
    }

    private void initJoints() {
        RevoluteJoint[] joints = new RevoluteJoint[BAND_DIVISIONS + 1];

        Wall leftWall = worldStage.getWall(Wall.WallType.LEFT);
        Wall rightWall = worldStage.getWall(Wall.WallType.RIGHT);
        if (leftWall == null || rightWall == null)
            throw new IllegalArgumentException("There are not enough Walls for the Rope to connect.");

        float y = (BOARD_HALF_HEIGHT - BAND_GAP - BAND_HALF_WIDTH);
        if (player == Player.TWO) y *= -1;

        // Revolute Joints

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.collideConnected = false;

        //jointDef.lowerAngle = 10 * MathUtils.degreesToRadians;
        //jointDef.upperAngle = 10 * MathUtils.degreesToRadians;
        jointDef.enableLimit = true;

        jointDef.enableMotor = false;
        jointDef.motorSpeed = 0;
        jointDef.maxMotorTorque = 200;

        jointDef.localAnchorA.set(y, -WALL_HALF_WIDTH);
        jointDef.localAnchorB.set(-BAND_SEGMENT_HALF_LENGTH, 0);
        jointDef.bodyA = leftWall.getBody();
        jointDef.bodyB = bodies[0];
        jointDef.referenceAngle = jointDef.bodyB.getAngle() - jointDef.bodyA.getAngle();
        joints[0] = (RevoluteJoint) worldStage.getWorld().createJoint(jointDef);

        jointDef.referenceAngle = 0;
        jointDef.localAnchorA.set(BAND_SEGMENT_HALF_LENGTH, 0);
        for (int i = 1; i < joints.length - 1; i++) {
            jointDef.bodyA = bodies[i - 1];
            jointDef.bodyB = bodies[i];
            joints[i] = (RevoluteJoint) worldStage.getWorld().createJoint(jointDef);
        }

        jointDef.localAnchorB.set(y, WALL_HALF_WIDTH);
        jointDef.bodyA = bodies[bodies.length - 1];
        jointDef.bodyB = rightWall.getBody();
        jointDef.referenceAngle = jointDef.bodyB.getAngle() - jointDef.bodyA.getAngle();
        joints[joints.length - 1] = (RevoluteJoint) worldStage.getWorld().createJoint(jointDef);

        // Distance Joints
        DistanceJoint[] distanceJoints = new DistanceJoint[bodies.length];
        DistanceJointDef dJointDef = new DistanceJointDef();
        dJointDef.collideConnected = false;
        dJointDef.localAnchorA.set(0,0);
        dJointDef.localAnchorB.set(0,0);

        dJointDef.dampingRatio = 20;
        dJointDef.frequencyHz = 30;

        dJointDef.bodyA = worldStage.getGround().getBody();
        Vector2 groundPosition = worldStage.getGround().getBody().getPosition();

        for(int i = 0; i<bodies.length;i++) {
            dJointDef.bodyB = bodies[i];
            dJointDef.length = groundPosition.dst(bodies[i].getPosition());
            //dJointDef.maxLength = groundPosition.dst(bodies[i].getPosition())+BAND_GAP*2;
            //distanceJoints[i] = (DistanceJoint) worldStage.getWorld().createJoint(dJointDef);
        }
    }

    private void initSprites() {
        float[] verts = new float[]
                {-BAND_SEGMENT_HALF_LENGTH,-BAND_HALF_WIDTH,
                        -BAND_SEGMENT_HALF_LENGTH,BAND_HALF_WIDTH,
                        BAND_SEGMENT_HALF_LENGTH,BAND_HALF_WIDTH,
                        BAND_SEGMENT_HALF_LENGTH,-BAND_HALF_WIDTH,};
        sprites = new RepeatablePolygonSprite[bodies.length];
        for(int i = 0; i<sprites.length;i++) {
            sprites[i] = new RepeatablePolygonSprite();
            sprites[i].setVertices(verts);
            sprites[i].setColor(Color.BLACK);
            sprites[i].setPosition(bodies[i].getPosition());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for(int i = 0; i<sprites.length;i++) {
            if(sprites[i] != null) {
                sprites[i].setPosition(bodies[i].getPosition());
                sprites[i].setRotation(bodies[i].getAngle() * MathUtils.radiansToDegrees);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for(int i = 0; i<sprites.length;i++) {
            if(sprites[i] != null) {
                sprites[i].draw((PolygonSpriteBatch) batch);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (int i = 0; i < bodies.length; i++) {
            bodies[i] = null;
        }
        bodies = null;
    }

    public Player getPlayer() {
        return player;
    }
}
