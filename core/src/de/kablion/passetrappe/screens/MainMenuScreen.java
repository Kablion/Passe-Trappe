package de.kablion.passetrappe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.kablion.passetrappe.PasseTrappe;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static de.kablion.passetrappe.utils.Constants.DEFAULT_FONT;
import static de.kablion.passetrappe.utils.Constants.FONTS_PATH;
import static de.kablion.passetrappe.utils.Constants.SKINS_PATH;
import static de.kablion.passetrappe.utils.Constants.UI_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.UI_WIDTH;

public class MainMenuScreen implements Screen {


    private final PasseTrappe app;
    private final Stage stage;
    private Table rootTable = new Table();
    private Table menuTable = new Table();
    private Label titleLabel;

    private Skin mainMenuSkin;

    // Menu Buttons

    TextButton buttonPlay;
    TextButton buttonExit;

    public MainMenuScreen(final PasseTrappe app) {
        this.app = app;
        this.stage = new Stage(new ExtendViewport(UI_WIDTH, UI_HEIGHT), app.batch);
    }

    @Override
    public void show() {
        Gdx.app.log("Screen:", "MAIN_MENU");

        stage.clear();
        rootTable.clear();
        menuTable.clear();

        initSkin();

        initStage();
        initButtons();

        Gdx.input.setInputProcessor(stage);
    }

    public void update(float delta) {
        stage.act();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.clear();
    }

    private void initSkin() {
        mainMenuSkin = new Skin();
        mainMenuSkin.addRegions(app.assets.get(SKINS_PATH + "default.atlas", TextureAtlas.class));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONTS_PATH + DEFAULT_FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 50;
        params.color = Color.BLACK;
        mainMenuSkin.add("default-font", generator.generateFont(params));
        params.size = 100;
        mainMenuSkin.add("big-font", generator.generateFont(params));
        generator.dispose();
        mainMenuSkin.load(Gdx.files.internal(SKINS_PATH + "default.json"));
    }

    private void initStage() {
        rootTable.setFillParent(true);

        // Title Label
        titleLabel = new Label("Main Menu", mainMenuSkin, "big-font", Color.BLACK);
        menuTable.add(titleLabel).padBottom(40);
        menuTable.row();
        menuTable.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));

        rootTable.add(menuTable).center().top().width(300).padTop(100).expandY();
        stage.addActor(rootTable);
    }

    private void initButtons() {
        // Play Button
        buttonPlay = new TextButton("2 Player", mainMenuSkin, "default");
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //app.assets.get(SOUNDS_PATH+"button_click.wav", Sound.class).play();
                app.setScreen(app.gameScreen);
            }
        });
        menuTable.add(buttonPlay).expandX().fill().padBottom(20);
        menuTable.row();

        // Exit Button
        buttonExit = new TextButton("Exit", mainMenuSkin, "default");
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //app.assets.get(SOUNDS_PATH+"exit.wav", Sound.class).play();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                }, 0.5f);
            }
        });
        menuTable.add(buttonExit).expandX().fill();


    }


}
