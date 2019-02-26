package de.kablion.passetrappe.stages;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.utils.GameState;
import de.kablion.passetrappe.utils.Player;

import static de.kablion.passetrappe.utils.Constants.*;

public class EndingStage extends Stage {

    private final PasseTrappe app;

    private Table mainTable = new Table();
    private Table topTable = new Table();
    private Table menuTable = new Table();
    private Table bottomTable = new Table();

    public Player winner = null;

    public EndingStage(final PasseTrappe app) {
        super(new ExtendViewport(UI_WIDTH, UI_HEIGHT), app.batch);
        this.app = app;

    }

    public void reset() {
        clear();
        initMainTable();
        initTopTable();
        initMenuTable();
        initBottomTable();
    }

    private void initMainTable() {
        mainTable.clear();
        addActor(mainTable);

        //mainTable.setDebug(true);
        mainTable.setSize(ENDING_WIDTH, ENDING_HEIGHT);
        mainTable.setPosition(-UI_WIDTH,MAINMENU_MARGIN_Y);
        TextureAtlas textureAtlas = app.assets.get(MENU_ATLAS_PATH, TextureAtlas.class);
        mainTable.background(new TextureRegionDrawable(textureAtlas.findRegion("board_bg")));
        mainTable.pad(50, 40, 50, 40);

        mainTable.addAction(Actions.moveToAligned(ENDING_MARGIN_X, ENDING_MARGIN_Y,Align.bottomLeft,3f, Interpolation.bounceOut));

        mainTable.add(topTable).expandX().height(ENDING_TOP_HEIGHT).fill();
        mainTable.row();
        mainTable.add(menuTable).expand().fill();
        mainTable.row();
        mainTable.add(bottomTable).expandX().height(ENDING_BOTTOM_HEIGHT).fill();

    }

    private void initTopTable() {
        topTable.clear();
        TextureAtlas textureAtlas = app.assets.get(MENU_ATLAS_PATH, TextureAtlas.class);
        topTable.background(new TextureRegionDrawable(textureAtlas.findRegion("head_bg")));
        //topTable.setDebug(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(DEFAULT_FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 50;

        String string = "";
        if(winner == Player.ONE) {
            string = "You win!";
            params.color = Color.GREEN;
        }
        if(winner == Player.TWO) {
            string = "You lose!";
            params.color = Color.RED;
        }
        Label label = new Label(string, new Label.LabelStyle(generator.generateFont(params),params.color));
        label.setAlignment(Align.center);
        topTable.add(label).expand().height(ENDING_TOP_HEIGHT).align(Align.center).fill();
        topTable.setTransform(true);
        topTable.setOrigin((ENDING_WIDTH-80)/2,ENDING_TOP_HEIGHT/2);
        topTable.setRotation(180);
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

        final TextButton playButton = new TextButton("Again", buttonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //app.assets.get(SOUNDS_PATH+"button_click.wav", Sound.class).play();
                app.gameScreen.worldStage.reset();
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

        final TextButton exitButton = new TextButton("Menu", buttonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.gameScreen.worldStage.reset();
                mainTable.addAction(Actions.sequence(Actions.moveBy(UI_WIDTH,0,3,Interpolation.bounceIn),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        app.gameScreen.setGameState(GameState.MAINMENU);
                    }
                })));
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

        if(winner == Player.ONE) {
            menuTable.add(playButton).width(ENDING_BUTTON_SIZE).height(ENDING_BUTTON_SIZE).expand();
            menuTable.add(exitButton).width(ENDING_BUTTON_SIZE).height(ENDING_BUTTON_SIZE).expand();
        }
        if(winner == Player.TWO) {
            menuTable.add(exitButton).width(ENDING_BUTTON_SIZE).height(ENDING_BUTTON_SIZE).expand();
            menuTable.add(playButton).width(ENDING_BUTTON_SIZE).height(ENDING_BUTTON_SIZE).expand();
        }
        menuTable.row();
    }

    private void initBottomTable() {
        bottomTable.clear();
        TextureAtlas textureAtlas = app.assets.get(MENU_ATLAS_PATH, TextureAtlas.class);
        bottomTable.background(new TextureRegionDrawable(textureAtlas.findRegion("head_bg")));
        //topTable.setDebug(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(DEFAULT_FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 50;

        String string = "";
        if(winner == Player.TWO) {
            string = "You win!";
            params.color = Color.GREEN;
        }
        if(winner == Player.ONE) {
            string = "You lose!";
            params.color = Color.RED;
        }

        Label label = new Label(string, new Label.LabelStyle(generator.generateFont(params), params.color));
        label.setAlignment(Align.center);
        bottomTable.add(label).expandX().height(ENDING_BOTTOM_HEIGHT).fill();
    }


    public void resize(int width, int height) {
        getViewport().update(width, height, false);
    }
}
