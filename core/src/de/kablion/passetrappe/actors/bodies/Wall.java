package de.kablion.passetrappe.actors.bodies;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.stages.WorldStage;
import de.kablion.passetrappe.utils.RepeatablePolygonSprite;

import static de.kablion.passetrappe.utils.Constants.BIT_BAND;
import static de.kablion.passetrappe.utils.Constants.BIT_DISK;
import static de.kablion.passetrappe.utils.Constants.BIT_WALL;
import static de.kablion.passetrappe.utils.Constants.BOARD_HALF_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.BOARD_HALF_WIDTH;
import static de.kablion.passetrappe.utils.Constants.HOLE_HALF_WIDTH;
import static de.kablion.passetrappe.utils.Constants.TEXTURES_ATLAS_PATH;
import static de.kablion.passetrappe.utils.Constants.WALL_CENTER_HALF_WIDTH;
import static de.kablion.passetrappe.utils.Constants.WALL_HALF_WIDTH;
import static de.kablion.passetrappe.utils.Constants.WALL_WIDTH;


public class Wall extends BodyActor {

    public enum WallType {
        LEFT, TOP, RIGHT, BOTTOM, CENTER_LEFT, CENTER_RIGHT
    }

    private WallType wallType;

    private float half_length = 0;
    private float half_width = 0;

    public Wall(final PasseTrappe app, final WorldStage worldStage, WallType wallType) {
        super(app, worldStage);
        this.wallType = wallType;

        initBody();
        initSprite();
    }

    private void initBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = BIT_WALL;
        fixtureDef.filter.maskBits = (short) (BIT_DISK | BIT_BAND);

        switch (wallType) {
            case LEFT: {
                bodyDef.position.set(-(BOARD_HALF_WIDTH + WALL_HALF_WIDTH), 0);
                half_length = BOARD_HALF_HEIGHT;
                half_width = WALL_HALF_WIDTH;
                bodyDef.angle = 90 * MathUtils.degreesToRadians;
                break;
            }
            case RIGHT: {
                bodyDef.position.set((BOARD_HALF_WIDTH + WALL_HALF_WIDTH), 0);
                half_length = BOARD_HALF_HEIGHT;
                half_width = WALL_HALF_WIDTH;
                bodyDef.angle = 90 * MathUtils.degreesToRadians;
                break;
            }
            case TOP: {
                bodyDef.position.set(0, (BOARD_HALF_HEIGHT + WALL_HALF_WIDTH));
                half_length = BOARD_HALF_WIDTH + WALL_WIDTH;
                half_width = WALL_HALF_WIDTH;
                break;
            }
            case BOTTOM: {
                bodyDef.position.set(0, -(BOARD_HALF_HEIGHT + WALL_HALF_WIDTH));
                half_length = BOARD_HALF_WIDTH + WALL_WIDTH;
                half_width = WALL_HALF_WIDTH;
                break;
            }
            case CENTER_LEFT: {
                bodyDef.position.set(-((BOARD_HALF_WIDTH / 2) + HOLE_HALF_WIDTH / 2), 0);
                half_length = BOARD_HALF_WIDTH / 2 - (HOLE_HALF_WIDTH / 2);
                half_width = WALL_CENTER_HALF_WIDTH;
                break;
            }
            case CENTER_RIGHT: {
                bodyDef.position.set(((BOARD_HALF_WIDTH / 2) + HOLE_HALF_WIDTH / 2), 0);
                half_length = BOARD_HALF_WIDTH / 2 - (HOLE_HALF_WIDTH / 2);
                half_width = WALL_CENTER_HALF_WIDTH;
                break;
            }
        }

        PolygonShape wallBox = new PolygonShape();
        wallBox.setAsBox(half_length, half_width);
        fixtureDef.shape = wallBox;

        body = worldStage.getWorld().createBody(bodyDef);

        body.createFixture(fixtureDef);
        body.setUserData(this);

        wallBox.dispose();
    }

    private void initSprite() {
        sprite = new RepeatablePolygonSprite();
        sprite.setVertices(new float[]
                {-half_length, -half_width,
                        -half_length, half_width,
                        half_length, half_width,
                        half_length, -half_width});
        TextureRegion textureRegion = app.assets.get(TEXTURES_ATLAS_PATH, TextureAtlas.class).findRegion("wall_texture");
        sprite.setTextureRegion(textureRegion,
                RepeatablePolygonSprite.WrapType.REPEAT_MIRRORED,
                RepeatablePolygonSprite.WrapType.STRETCH);
        float textureRatio = (float)textureRegion.getRegionWidth() / (float)(textureRegion.getRegionHeight());
        sprite.setTextureSize(half_width * 2 * textureRatio, 0);
    }

    public WallType getWallType() {
        return wallType;
    }
}
