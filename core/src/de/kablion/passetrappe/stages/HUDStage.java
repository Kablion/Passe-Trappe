package de.kablion.passetrappe.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.kablion.passetrappe.PasseTrappe;

import static de.kablion.passetrappe.utils.Constants.UI_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.UI_WIDTH;

public class HUDStage extends Stage {

    private final PasseTrappe app;

    public HUDStage(final PasseTrappe app) {
        super(new ExtendViewport(UI_WIDTH, UI_HEIGHT), app.batch);
        this.app = app;
    }

    public void reset() {

    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void dispose() {
        super.clear();
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
}
