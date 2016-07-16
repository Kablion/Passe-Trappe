package de.kablion.passetrappe.actors;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.stages.WorldStage;

import static de.kablion.passetrappe.utils.Constants.BIT_DISK;
import static de.kablion.passetrappe.utils.Constants.BIT_HOLE;
import static de.kablion.passetrappe.utils.Constants.HOLE_HALF_WIDTH;

public class HoleSensor extends BodyActor {

    public HoleSensor(PasseTrappe app, WorldStage worldStage) {
        super(app, worldStage);

        initBody();
    }

    private void initBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = BIT_HOLE;
        fixtureDef.filter.maskBits = BIT_DISK;
        fixtureDef.isSensor = false;

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-HOLE_HALF_WIDTH, 0, HOLE_HALF_WIDTH, 0);
        fixtureDef.shape = edgeShape;

        body = worldStage.getWorld().createBody(bodyDef);

        body.createFixture(fixtureDef);
        body.setUserData(this);

        edgeShape.dispose();
    }

}
