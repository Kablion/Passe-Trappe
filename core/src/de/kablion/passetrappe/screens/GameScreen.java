package de.kablion.passetrappe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2D;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.stages.HUDStage;
import de.kablion.passetrappe.stages.WorldStage;

public class GameScreen implements Screen {


    private final PasseTrappe app;
    private InputMultiplexer multiplexer = new InputMultiplexer();

    private WorldStage worldStage;
    private HUDStage hudStage;

    public GameScreen(final PasseTrappe app) {
        this.app = app;
        worldStage = new WorldStage(app);
        hudStage = new HUDStage(app);
    }

    @Override
    public void show() {
        Box2D.init();

        worldStage.reset();
        hudStage.reset();
        multiplexer.clear();
        multiplexer.addProcessor(hudStage);
        multiplexer.addProcessor(worldStage);

        Gdx.input.setInputProcessor(multiplexer);
    }

    private void update(float delta) {
        worldStage.act(delta);
        hudStage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        worldStage.draw();
        hudStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        worldStage.resize(width, height);
        hudStage.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        worldStage.dispose();
        hudStage.dispose();
    }
}
