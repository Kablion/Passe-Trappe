package de.kablion.passetrappe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2D;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.stages.EndingStage;
import de.kablion.passetrappe.stages.HUDStage;
import de.kablion.passetrappe.stages.MainMenuStage;
import de.kablion.passetrappe.stages.WorldStage;
import de.kablion.passetrappe.utils.GameState;

public class GameScreen implements Screen {


    private final PasseTrappe app;
    private InputMultiplexer multiplexer = new InputMultiplexer();

    public WorldStage worldStage;
    private MainMenuStage mainMenuStage;
    private HUDStage hudStage;
    public EndingStage endingStage;

    private GameState gameState = GameState.LOADING;

    public GameScreen(final PasseTrappe app) {
        this.app = app;
        worldStage = new WorldStage(app);
        mainMenuStage = new MainMenuStage(app);
        hudStage = new HUDStage(app);
        endingStage = new EndingStage(app);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);

        setGameState(GameState.MAINMENU);
    }

    private void update(float delta) {
        if(gameState == GameState.PLAY || gameState == GameState.MAINMENU || gameState == GameState.ENDING) worldStage.act(delta);
        if(gameState == GameState.PLAY) hudStage.act(delta);
        if(gameState == GameState.MAINMENU) mainMenuStage.act(delta);
        if(gameState == GameState.ENDING) endingStage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        worldStage.draw();
        if(gameState == GameState.PLAY) hudStage.draw();
        if(gameState == GameState.MAINMENU) mainMenuStage.draw();
        if(gameState == GameState.ENDING) endingStage.draw();
    }

    public void setGameState(GameState gameState) {
        if(gameState == this.gameState) return;
        switch (gameState) {
            case LOADING: {
                break;
            }
            case MAINMENU: {
                worldStage.reset();
                hudStage.reset();
                mainMenuStage.reset();
                multiplexer.clear();
                multiplexer.addProcessor(mainMenuStage);
                break;
            }
            case PLAY: {
                multiplexer.clear();
                multiplexer.addProcessor(hudStage);
                multiplexer.addProcessor(worldStage);
                break;
            }
            case ENDING: {
                endingStage.reset();
                multiplexer.clear();
                multiplexer.addProcessor(endingStage);
                break;
            }
        }
        this.gameState = gameState;
    }

    @Override
    public void resize(int width, int height) {
        worldStage.resize(width, height);
        hudStage.resize(width, height);
        mainMenuStage.resize(width,height);
        endingStage.resize(width,height);
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
        mainMenuStage.dispose();
        endingStage.dispose();
    }
}
