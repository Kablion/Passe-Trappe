package de.kablion.passetrappe.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.stages.WorldStage;
import de.kablion.passetrappe.utils.RepeatablePolygonSprite;

import static de.kablion.passetrappe.utils.Constants.BIT_GROUND;
import static de.kablion.passetrappe.utils.Constants.BOARD_HALF_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.BOARD_HALF_WIDTH;

public class Ground extends BodyActor {

    public Ground(final PasseTrappe app, final WorldStage worldStage) {
        super(app, worldStage);

        initBody();
        initSprite();
    }

    private void initBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = worldStage.getWorld().createBody(bodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(BOARD_HALF_WIDTH + 1, BOARD_HALF_HEIGHT);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = BIT_GROUND;

        body.createFixture(fixtureDef);
        body.setUserData(this);
        groundBox.dispose();
    }

    private void initSprite() {
        sprite = new RepeatablePolygonSprite();
        sprite.setVertices(new float[]
                {-BOARD_HALF_WIDTH, -BOARD_HALF_HEIGHT,
                        -BOARD_HALF_WIDTH, BOARD_HALF_HEIGHT,
                        BOARD_HALF_WIDTH, BOARD_HALF_HEIGHT,
                        BOARD_HALF_WIDTH, -BOARD_HALF_HEIGHT});
        sprite.setColor(Color.OLIVE);
    }

}
