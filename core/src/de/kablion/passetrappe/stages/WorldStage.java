package de.kablion.passetrappe.stages;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.actors.OnFieldHUD;
import de.kablion.passetrappe.actors.bodies.Disk;
import de.kablion.passetrappe.actors.bodies.ElasticBand;
import de.kablion.passetrappe.actors.bodies.Ground;
import de.kablion.passetrappe.actors.bodies.HoleSensor;
import de.kablion.passetrappe.actors.bodies.Wall;
import de.kablion.passetrappe.utils.GameState;
import de.kablion.passetrappe.utils.Player;

import static de.kablion.passetrappe.utils.Constants.BAND_GAP;
import static de.kablion.passetrappe.utils.Constants.BAND_WIDTH;
import static de.kablion.passetrappe.utils.Constants.BIT_BAND_LIMIT;
import static de.kablion.passetrappe.utils.Constants.BIT_DISK;
import static de.kablion.passetrappe.utils.Constants.BIT_HOLE;
import static de.kablion.passetrappe.utils.Constants.BOARD_HALF_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.BOARD_HALF_WIDTH;
import static de.kablion.passetrappe.utils.Constants.DISKS_PER_COL;
import static de.kablion.passetrappe.utils.Constants.DISK_RADIUS;
import static de.kablion.passetrappe.utils.Constants.FIRST_DISK_X;
import static de.kablion.passetrappe.utils.Constants.FIRST_DISK_Y;
import static de.kablion.passetrappe.utils.Constants.FORCE_FACTOR;
import static de.kablion.passetrappe.utils.Constants.FORCE_FACTOR_DEFLECT;
import static de.kablion.passetrappe.utils.Constants.SHOOT_ANGLE_MAX;
import static de.kablion.passetrappe.utils.Constants.WALL_CENTER_HALF_WIDTH;
import static de.kablion.passetrappe.utils.Constants.WORLD_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.WORLD_WIDTH;

public class WorldStage extends Stage {

    private final PasseTrappe app;

    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    private World world;

    private Array<Disk> disks;
    private Array<Wall> walls;
    private Array<ElasticBand> elasticBands;
    private Ground ground;
    private HoleSensor holeSensor;
    private OnFieldHUD onFieldHUD;

    private Player cheatingPlayer;
    private boolean cheaterDetected = false;

    public WorldStage(final PasseTrappe app) {
        super(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), app.polyBatch);

        this.app = app;
    }

    public void reset() {
        world = new World(new Vector2(0, 0), true);
        disks = new Array<Disk>();
        walls = new Array<Wall>();
        elasticBands = new Array<ElasticBand>();

        clear();
        Box2D.init();
        world.clearForces();
        getCamera().position.set(0, 0, 0);

        initActors();
        initListeners();
    }

    private void initActors() {
        initGround();
        initWalls();
        initOnFieldHUD();
        initDisks();
        initJoints();
        initHoleSensor();
        initElasticBands();
    }

    private void initGround() {
        ground = new Ground(app, this);
        addActor(ground);
    }

    private void initHoleSensor() {
        holeSensor = new HoleSensor(app, this);
        addActor(holeSensor);
    }

    private void initOnFieldHUD() {
        onFieldHUD = new OnFieldHUD(app,this);
        addActor(onFieldHUD);

    }

    private void initDisks() {

        int amountPerPlayer = 1;
        BodyDef jointBodyDef = new BodyDef();
        jointBodyDef.type = BodyDef.BodyType.DynamicBody;
        Body jointBody = world.createBody(jointBodyDef);

        for (int p = 1; p <= 2; p++) {
            Player player;
            if (p == 1) player = Player.ONE;
            else player = Player.TWO;

            for (int i = 0; i < amountPerPlayer; i++) {

                int col = (int) Math.floor(i / DISKS_PER_COL);
                int row = i % DISKS_PER_COL;
                // Position
                float x = (FIRST_DISK_X + col * DISK_RADIUS * 2);
                float y = (FIRST_DISK_Y + row * DISK_RADIUS * 2);
                if (player == Player.TWO) {
                    y *= -1;
                    x *= -1;
                }

                Disk disk = new Disk(app, this, player, false, x, y, ground.getBody());
                disks.add(disk);
                addActor(disk);
            }
        }

    }

    private void initJoints() {

    }

    private void initWalls() {
        Wall tempWall = new Wall(app, this, Wall.WallType.LEFT);
        walls.add(tempWall);
        addActor(tempWall);

        tempWall = new Wall(app, this, Wall.WallType.RIGHT);
        walls.add(tempWall);
        addActor(tempWall);

        tempWall = new Wall(app, this, Wall.WallType.TOP);
        walls.add(tempWall);
        addActor(tempWall);

        tempWall = new Wall(app, this, Wall.WallType.BOTTOM);
        walls.add(tempWall);
        addActor(tempWall);

        tempWall = new Wall(app, this, Wall.WallType.CENTER_LEFT);
        walls.add(tempWall);
        addActor(tempWall);

        tempWall = new Wall(app, this, Wall.WallType.CENTER_RIGHT);
        walls.add(tempWall);
        addActor(tempWall);
    }

    private void initElasticBands() {
        ElasticBand tempBand = new ElasticBand(app, this, Player.ONE);
        elasticBands.add(tempBand);
        addActor(tempBand);

        tempBand = new ElasticBand(app, this, Player.TWO);
        elasticBands.add(tempBand);
        addActor(tempBand);
    }

    private void initListeners() {
        ContactListener contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

                // Logic for shooting / elastic band
                ////////////////////////////////////////////////////////////////////////////////
                if (contact.getFixtureA().getFilterData().categoryBits == BIT_DISK && contact.getFixtureB().getFilterData().categoryBits == BIT_BAND_LIMIT ||
                        contact.getFixtureA().getFilterData().categoryBits == BIT_BAND_LIMIT && contact.getFixtureB().getFilterData().categoryBits == BIT_DISK) {
                    // Contact is between a Disk and a Band_Limit

                    Fixture disk;
                    Fixture bandLimit;

                    if (contact.getFixtureA().getFilterData().categoryBits == BIT_DISK && contact.getFixtureB().getFilterData().categoryBits == BIT_BAND_LIMIT) {
                        // A = Disk B = Band_Limit
                        disk = contact.getFixtureA();
                        bandLimit = contact.getFixtureB();


                    } else {
                        // A = Band_Limit B = Disk

                        bandLimit = contact.getFixtureA();
                        disk = contact.getFixtureB();

                    }

                    Player player = ((ElasticBand) bandLimit.getBody().getUserData()).getPlayer();

                    if (player == Player.ONE) {
                        if (disk.getBody().getPosition().y >= bandLimit.getBody().getPosition().y)
                            contact.setEnabled(false);
                    } else if (player == Player.TWO) {
                        if (disk.getBody().getPosition().y <= bandLimit.getBody().getPosition().y)
                            contact.setEnabled(false);
                    }

                    if (contact.isEnabled()) {

                        boolean isToShoot = false;
                        if (player == Player.ONE) {
                            if (disk.getBody().getLinearVelocity().y > 0) {
                                isToShoot = true;
                            }
                        } else if (player == Player.TWO) {
                            if (disk.getBody().getLinearVelocity().y < 0) {
                                isToShoot = true;
                            }
                        }

                        if (isToShoot) {
                            Vector2 deflect = new Vector2(disk.getBody().getLinearVelocity());
                            deflect.y *= -1;
                            deflect.scl(FORCE_FACTOR_DEFLECT);
                            disk.getBody().setLinearVelocity(deflect);
                            ((Disk) disk.getBody().getUserData()).throwedWithoutBand = false;
                        }

                    }

                }

                // Logic for Hole
                //////////////////////////////////////////////////
                if (contact.getFixtureA().getFilterData().categoryBits == BIT_DISK && contact.getFixtureB().getFilterData().categoryBits == BIT_HOLE ||
                        contact.getFixtureA().getFilterData().categoryBits == BIT_HOLE && contact.getFixtureB().getFilterData().categoryBits == BIT_DISK) {
                    Disk disk;
                    HoleSensor hole;

                    if (contact.getFixtureA().getFilterData().categoryBits == BIT_DISK && contact.getFixtureB().getFilterData().categoryBits == BIT_HOLE) {
                        // A = Disk B = Hole
                        disk = (Disk) contact.getFixtureA().getBody().getUserData();
                        hole = (HoleSensor) contact.getFixtureB().getBody().getUserData();
                    } else {
                        // A = Hole B = Disk
                        hole = (HoleSensor) contact.getFixtureA().getBody().getUserData();
                        disk = (Disk) contact.getFixtureB().getBody().getUserData();
                    }

                    if (!disk.throwedWithoutBand) {
                        contact.setEnabled(false);
                        /*if(disk.getBody().getPosition().y > 0) {
                            disk.setPlayer(Player.ONE);
                        } else {
                            disk.setPlayer(Player.TWO);
                        }*/
                    } else {
                        // Cheater
                        cheaterDetected = true;
                        cheatingPlayer = disk.getPlayer();
                        disk.throwedWithoutBand = false;
                    }

                }

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        world.setContactListener(contactListener);
    }

    public void punishCheater(Player player) {

        float x = MathUtils.random(-BOARD_HALF_WIDTH, BOARD_HALF_WIDTH);
        float y = MathUtils.random(WALL_CENTER_HALF_WIDTH + DISK_RADIUS,
                BOARD_HALF_HEIGHT - BAND_GAP - BAND_WIDTH - DISK_RADIUS);
        if(player == Player.TWO) y *= -1;
        Disk tempDisk = new Disk(app, this, player, true, x, y, ground.getBody());
        disks.add(tempDisk);
        addActor(tempDisk);

    }

    @Override
    public void draw() {
        super.draw();
        //debugRenderer.render(world, getCamera().combined);
    }

    @Override
    public void act(float delta) {
        world.step(1 / 60f, 8, 3);
        if(cheaterDetected) {
            cheaterDetected = false;
            punishCheater(cheatingPlayer);
        }
        if(onFieldHUD.getPlayerCount(Player.ONE) == 0) {
            //Player ONE wins
            app.gameScreen.endingStage.winner = Player.ONE;
            app.gameScreen.setGameState(GameState.ENDING);
        } else if(onFieldHUD.getPlayerCount(Player.TWO) == 0) {
            //Player TWO wins
            app.gameScreen.endingStage.winner = Player.TWO;
            app.gameScreen.setGameState(GameState.ENDING);
        }
        super.act(delta);
    }

    @Override
    public void dispose() {
        super.clear();

        world.dispose();
        world = null;

        if(onFieldHUD != null) {
            onFieldHUD.dispose();
        }
        onFieldHUD = null;

        for (int i = 0; i < disks.size; i++) {
            disks.get(i).dispose();
        }
        disks.clear();
        disks = null;

        for (int i = 0; i < walls.size; i++) {
            walls.get(i).dispose();
        }
        walls.clear();
        walls = null;

        for (int i = 0; i < elasticBands.size; i++) {
            elasticBands.get(i).dispose();
        }
        elasticBands.clear();
        elasticBands = null;

        if (ground != null) {
            ground.dispose();
            ground = null;
        }
    }


    private Vector2 worldTouch = new Vector2();
    private int currentPointer = 0;

    private void validateTouchPosition(Vector2 worldTouch, Player player) {

        if (worldTouch.x <= -(BOARD_HALF_WIDTH - DISK_RADIUS)) {
            // Links außerhalb des Boards
            worldTouch.x = -(BOARD_HALF_WIDTH - DISK_RADIUS);
            if(Math.abs(worldTouch.y) > BOARD_HALF_HEIGHT-BAND_GAP-BAND_WIDTH) worldTouch.x += BAND_WIDTH;
        } else if (worldTouch.x >= (BOARD_HALF_WIDTH - DISK_RADIUS)) {
            // Rechts außerhalb des Boards
            worldTouch.x = (BOARD_HALF_WIDTH - DISK_RADIUS);
            if(Math.abs(worldTouch.y) > BOARD_HALF_HEIGHT-BAND_GAP-BAND_WIDTH) worldTouch.x -= BAND_WIDTH;
        }

        if (player == Player.ONE) {
            if (worldTouch.y <= WALL_CENTER_HALF_WIDTH + DISK_RADIUS) {
                worldTouch.y = WALL_CENTER_HALF_WIDTH + DISK_RADIUS;
            } else if (worldTouch.y >= BOARD_HALF_HEIGHT - DISK_RADIUS - BAND_WIDTH) {
                worldTouch.y = BOARD_HALF_HEIGHT - DISK_RADIUS - BAND_WIDTH;
            }
        } else if (player == Player.TWO) {
            if (worldTouch.y >= -(WALL_CENTER_HALF_WIDTH + DISK_RADIUS)) {
                worldTouch.y = -(WALL_CENTER_HALF_WIDTH + DISK_RADIUS);
            } else if (worldTouch.y <= -(BOARD_HALF_HEIGHT - DISK_RADIUS - BAND_WIDTH)) {
                worldTouch.y = -(BOARD_HALF_HEIGHT - DISK_RADIUS - BAND_WIDTH);
            }
        }
    }

    private QueryCallback queryCallback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            //if (fixture.testPoint(worldTouch.x, worldTouch.y)) {
                if (fixture.getBody().getUserData() instanceof Disk) {
                    // init Touch Down on Disk
                    Disk disk = (Disk) fixture.getBody().getUserData();
                    if (disk.mouseJoint == null) {
                        disk.mouseJointDef.bodyB = disk.getBody();
                        disk.mouseJointDef.target.set(disk.getBody().getWorldCenter());
                        disk.mouseJoint = (MouseJoint) world.createJoint(disk.mouseJointDef);
                        disk.pointerID = currentPointer;
                        Filter filter = new Filter();
                        filter.categoryBits = BIT_DISK;
                        filter.maskBits = (short) (fixture.getFilterData().maskBits & (~BIT_BAND_LIMIT)); // Delete BIT_BAND_LIMIT
                        fixture.setFilterData(filter);
                        return false;
                    }
                }
            //}
            return true;
        }
    };

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);

        currentPointer = pointer;
        worldTouch.set(screenX, screenY);
        getViewport().unproject(worldTouch);
        world.QueryAABB(queryCallback, worldTouch.x, worldTouch.y, worldTouch.x, worldTouch.y);

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        super.touchDragged(screenX, screenY, pointer);

        // Check Disk Touches
        for (int i = 0; i < disks.size; i++) {
            Disk disk = disks.get(i);
            if (disk != null && disk.mouseJoint != null && disk.pointerID == pointer) {
                getViewport().unproject(worldTouch.set(screenX, screenY));

                // Is Touch in the Players Region?
                validateTouchPosition(worldTouch, disk.getPlayer());
                disk.mouseJoint.setTarget(worldTouch);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);

        // Check Disk Touches
        for (int i = 0; i < disks.size; i++) {
            Disk disk = disks.get(i);
            if (disk != null && disk.mouseJoint != null && disk.pointerID == pointer) {
                world.destroyJoint(disk.mouseJoint);
                disk.mouseJoint = null;

                // maybe shoot:
                float distance = BOARD_HALF_HEIGHT - Math.abs(disk.getBody().getPosition().y);
                if (distance < BAND_GAP + BAND_WIDTH + DISK_RADIUS) {
                    float percent = (distance - DISK_RADIUS) / (BAND_GAP + BAND_WIDTH);
                    percent = 1 - percent;
                    if (distance - DISK_RADIUS < 0.001) percent = 1;
                    float angle = MathUtils.random(-SHOOT_ANGLE_MAX, SHOOT_ANGLE_MAX);
                    Vector2 force = new Vector2(0, 1);
                    force.setLength(percent * FORCE_FACTOR);
                    force.rotate(angle);
                    if (disk.getPlayer() == Player.ONE) force.scl(-1);
                    disk.getBody().setLinearVelocity(force);
                    disk.throwedWithoutBand = false;
                } else {
                    disk.throwedWithoutBand = true;
                }

                Filter filter = new Filter();
                filter.categoryBits = BIT_DISK;
                filter.maskBits = (short) (disk.getBody().getFixtureList().get(0).getFilterData().maskBits | BIT_BAND_LIMIT); // Add BIT_BAND_LIMIT
                disk.getBody().getFixtureList().get(0).setFilterData(filter);

                return true;
            }
        }
        return false;
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, false);
    }

    public World getWorld() {
        return world;
    }

    public Array<Disk> getDisks() {
        return disks;
    }

    public Wall getWall(Wall.WallType wallType) {
        for (int i = 0; i < walls.size; i++) {
            if (walls.get(i).getWallType() == wallType) {
                return walls.get(i);
            }
        }
        return null;
    }

    public Ground getGround() {
        return ground;
    }
}
