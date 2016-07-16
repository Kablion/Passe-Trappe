package de.kablion.passetrappe.actors;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.stages.WorldStage;
import de.kablion.passetrappe.utils.Player;
import de.kablion.passetrappe.utils.RepeatablePolygonSprite;
import de.kablion.passetrappe.utils.ShapeUtils;

import static de.kablion.passetrappe.utils.Constants.BIT_BAND;
import static de.kablion.passetrappe.utils.Constants.BIT_BAND_LIMIT;
import static de.kablion.passetrappe.utils.Constants.BIT_DISK;
import static de.kablion.passetrappe.utils.Constants.BIT_HOLE;
import static de.kablion.passetrappe.utils.Constants.BIT_WALL;
import static de.kablion.passetrappe.utils.Constants.DISK_RADIUS;
import static de.kablion.passetrappe.utils.Constants.TEXTURES_ATLAS;

public class Disk extends BodyActor {

    private Player player;

    private RepeatablePolygonSprite riffleSprite = new RepeatablePolygonSprite();
    private RepeatablePolygonSprite cheaterSprite;

    private FrictionJoint frictionJoint;

    public MouseJointDef mouseJointDef = new MouseJointDef();
    public MouseJoint mouseJoint;
    public int pointerID;
    public boolean throwedWithoutBand = false;

    private boolean forCheater = false;

    public Vector2 mouseJointTarget = new Vector2();

    public Disk(final PasseTrappe app, final WorldStage worldStage, Player player, boolean forCheater, float x, float y, Body bodyForJoint) {
        super(app, worldStage);
        this.player = player;
        this.forCheater = forCheater;

        initBody(x, y);
        initJoint(bodyForJoint);

        initSprite(forCheater);
    }

    private void initBody(float x, float y) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = worldStage.getWorld().createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(DISK_RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = BIT_DISK;
        fixtureDef.filter.maskBits = (short) (BIT_DISK | BIT_WALL | BIT_BAND | BIT_BAND_LIMIT | BIT_HOLE);

        body.createFixture(fixtureDef);
        body.setUserData(this);

        circle.dispose();
    }

    private void initSprite(boolean forCheater) {
        sprite = new RepeatablePolygonSprite();
        sprite.setVertices(ShapeUtils.buildCircle(DISK_RADIUS, 20));
        sprite.setTextureRegion(app.assets.get(TEXTURES_ATLAS, TextureAtlas.class).findRegion("disk_texture"));
        sprite.setTextureSize(15, 15);
        float rndX = MathUtils.random(sprite.getTextureWidth() - DISK_RADIUS * 2);
        float rndY = MathUtils.random(sprite.getTextureHeight() - DISK_RADIUS * 2);
        sprite.setTextureOffset(-rndX, -rndY);

        riffleSprite.setVertices(new float[]
                {-DISK_RADIUS,-DISK_RADIUS,
                        -DISK_RADIUS,DISK_RADIUS,
                        DISK_RADIUS,DISK_RADIUS,
                        DISK_RADIUS,-DISK_RADIUS});
        riffleSprite.setTextureRegion(app.assets.get(TEXTURES_ATLAS, TextureAtlas.class).findRegion("disk_riffle"),
                RepeatablePolygonSprite.WrapType.STRETCH,
                RepeatablePolygonSprite.WrapType.STRETCH);

        if(forCheater) {
            cheaterSprite = new RepeatablePolygonSprite();
            cheaterSprite.setVertices(riffleSprite.getOriginalVertices());
            cheaterSprite.setTextureRegion(app.assets.get(TEXTURES_ATLAS, TextureAtlas.class).findRegion("disk_cheater"),
                    RepeatablePolygonSprite.WrapType.STRETCH,
                    RepeatablePolygonSprite.WrapType.STRETCH);
        }
    }

    private void initJoint(Body uselessBody) {
        FrictionJointDef frictionJointDef = new FrictionJointDef();

        frictionJointDef.bodyA = worldStage.getGround().getBody();
        frictionJointDef.bodyB = body;

        frictionJointDef.maxForce = 230f;
        frictionJointDef.maxTorque = 200f;

        frictionJoint = (FrictionJoint) worldStage.getWorld().createJoint(frictionJointDef);

        mouseJointDef.bodyA = uselessBody;
        mouseJointDef.collideConnected = true;
        mouseJointDef.maxForce = 10000.0f * body.getMass();
        mouseJointDef.dampingRatio = 0;
        mouseJointDef.frequencyHz = 100;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        riffleSprite.setPosition(sprite.getX(), sprite.getY());
        if(cheaterSprite != null)
        cheaterSprite.setPosition(sprite.getX(), sprite.getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        riffleSprite.draw((PolygonSpriteBatch) batch);
        if(cheaterSprite != null)
            cheaterSprite.draw((PolygonSpriteBatch) batch);
    }

    @Override
    public void dispose() {
        super.dispose();

        riffleSprite.dispose();
        riffleSprite = null;

        if (cheaterSprite != null)
            cheaterSprite.dispose();
        cheaterSprite = null;

        frictionJoint = null;

        mouseJoint = null;
        mouseJointDef = null;
    }

    public int getPointerID() {
        return pointerID;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
