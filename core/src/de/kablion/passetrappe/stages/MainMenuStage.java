package de.kablion.passetrappe.stages;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.utils.GameState;

import static de.kablion.passetrappe.utils.Constants.DEFAULT_FONT_PATH;
import static de.kablion.passetrappe.utils.Constants.HANDWRITTEN_FONT_PATH;
import static de.kablion.passetrappe.utils.Constants.MAINMENU_BUTTON_SIZE;
import static de.kablion.passetrappe.utils.Constants.MAINMENU_HEAD_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.MAINMENU_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.MAINMENU_MARGIN_X;
import static de.kablion.passetrappe.utils.Constants.MAINMENU_MARGIN_Y;
import static de.kablion.passetrappe.utils.Constants.MAINMENU_WIDTH;
import static de.kablion.passetrappe.utils.Constants.MENU_ATLAS_PATH;
import static de.kablion.passetrappe.utils.Constants.UI_HEIGHT;
import static de.kablion.passetrappe.utils.Constants.UI_WIDTH;

public class MainMenuStage extends Stage {

    private final PasseTrappe app;

    private Table mainTable = new Table();
    private Table headTable = new Table();
    private Table menuTable = new Table();

    public MainMenuStage(final PasseTrappe app) {
        super(new ExtendViewport(UI_WIDTH, UI_HEIGHT), app.batch);
        this.app = app;

    }

    public void reset() {
        clear();
        initMainTable();
        initHeadTable();
        initMenuTable();
    }

    private void initMainTable() {
        mainTable.clear();
        addActor(mainTable);

        //mainTable.setDebug(true);
        mainTable.setSize(MAINMENU_WIDTH, MAINMENU_HEIGHT);
        mainTable.setPosition(-UI_WIDTH,MAINMENU_MARGIN_Y);
        TextureAtlas textureAtlas = app.assets.get(MENU_ATLAS_PATH, TextureAtlas.class);
        mainTable.background(new TextureRegionDrawable(textureAtlas.findRegion("board_bg")));
        mainTable.pad(50, 40, 50, 40);

        mainTable.addAction(Actions.moveToAligned(MAINMENU_MARGIN_X, MAINMENU_MARGIN_Y,Align.bottomLeft,3f, Interpolation.bounceOut));

        mainTable.add(headTable).expandX().height(MAINMENU_HEAD_HEIGHT).fill();
        mainTable.row();
        mainTable.add(menuTable).expand().fill();

    }

    private void initHeadTable() {
        headTable.clear();
        TextureAtlas textureAtlas = app.assets.get(MENU_ATLAS_PATH, TextureAtlas.class);
        headTable.background(new TextureRegionDrawable(textureAtlas.findRegion("head_bg")));
        //headTable.setDebug(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(DEFAULT_FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 50;
        params.color = Color.BLUE;

        Label label = new Label("Main Menu", new Label.LabelStyle(generator.generateFont(params), Color.BLUE));
        label.setAlignment(Align.center);
        headTable.add(label).expandX().height(MAINMENU_HEAD_HEIGHT).fill();
    }

    private void initMenuTable() {
        menuTable.clear();
        TextureAtlas textureAtlas = app.assets.get(MENU_ATLAS_PATH, TextureAtlas.class);
        //menuTable.setDebug(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(HANDWRITTEN_FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 35;
        params.color = Color.BLUE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(new TextureRegionDrawable(textureAtlas.findRegion("note_bg")),
                new TextureRegionDrawable(textureAtlas.findRegion("note_pulled_bg")),
                null,
                generator.generateFont(params));

        final TextButton playButton = new TextButton("Play", buttonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //app.assets.get(SOUNDS_PATH+"button_click.wav", Sound.class).play();
                mainTable.addAction(Actions.sequence(Actions.moveBy(UI_WIDTH,0,3,Interpolation.bounceIn),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        app.gameScreen.setGameState(GameState.PLAY);
                    }
                })));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (isPressed()) playButton.getLabel().setPosition(-10, 10);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                playButton.getLabel().setPosition(0, 0);
            }
        });
        menuTable.add(playButton).width(MAINMENU_BUTTON_SIZE).height(MAINMENU_BUTTON_SIZE);
        menuTable.add().expand().fill();
        menuTable.row();

        final TextButton optionsButton = new TextButton("Options", buttonStyle);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (isPressed()) optionsButton.getLabel().setPosition(-10, 10);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                optionsButton.getLabel().setPosition(0, 0);
            }
        });
        menuTable.add().expand().fill();
        menuTable.add(optionsButton).width(MAINMENU_BUTTON_SIZE).height(MAINMENU_BUTTON_SIZE);
        menuTable.row();

        final TextButton exitButton = new TextButton("Exit", buttonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //app.assets.get(SOUNDS_PATH+"button_click.wav", Sound.class).play();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                }, 0.5f);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (isPressed()) exitButton.getLabel().setPosition(-10, 10);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                exitButton.getLabel().setPosition(0, 0);
            }
        });
        menuTable.add(exitButton).width(MAINMENU_BUTTON_SIZE).height(MAINMENU_BUTTON_SIZE);
        menuTable.add().expand().fill();
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, false);
    }
}
