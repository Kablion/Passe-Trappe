package de.kablion.passetrappe.actors.bodies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.stages.WorldStage;
import de.kablion.passetrappe.utils.RepeatablePolygonSprite;

public class BodyActor extends Actor {
    protected final PasseTrappe app;
    protected final WorldStage worldStage;

    protected Body body;
    protected RepeatablePolygonSprite sprite;

    public BodyActor(final PasseTrappe app, final WorldStage worldStage) {
        this.app = app;
        this.worldStage = worldStage;
    }

    @Override
    public void act(float delta) {
        if (body != null && sprite != null) {
            Vector2 pos = body.getWorldCenter();
            sprite.setPosition(pos.x, pos.y);
            sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.draw((PolygonSpriteBatch) batch);
            //sprite.drawDebug(app.shapeRenderer, (PolygonSpriteBatch) batch, Color.RED);
        }
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        body = null;
        if (sprite != null) {
            sprite.dispose();
            sprite = null;
        }
    }

}
