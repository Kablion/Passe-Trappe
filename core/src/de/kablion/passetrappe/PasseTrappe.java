package de.kablion.passetrappe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.kablion.passetrappe.screens.GameScreen;
import de.kablion.passetrappe.screens.LoadingScreen;
import de.kablion.passetrappe.screens.MainMenuScreen;

public class PasseTrappe extends Game {

    // Res Heavy Objects that have to be initialized only once
    public SpriteBatch batch;
    public PolygonSpriteBatch polyBatch;
    public ShapeRenderer shapeRenderer;
    public AssetManager assets;

    //Screens
    public LoadingScreen loadingScreen;
    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        polyBatch = new PolygonSpriteBatch();
        shapeRenderer = new ShapeRenderer();
        assets = new AssetManager();

        loadingScreen = new LoadingScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);

        // the Game Starts with the LoadingScreen
        this.setScreen(loadingScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        loadingScreen.dispose();
        mainMenuScreen.dispose();
        gameScreen.dispose();

        batch.dispose();
        polyBatch.dispose();
        shapeRenderer.dispose();
        assets.dispose();
    }
}
