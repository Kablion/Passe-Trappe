package de.kablion.passetrappe.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.actors.bodies.Disk;
import de.kablion.passetrappe.stages.WorldStage;
import de.kablion.passetrappe.utils.Player;

import static de.kablion.passetrappe.utils.Constants.DEFAULT_FONT_PATH;
import static de.kablion.passetrappe.utils.Constants.OPPONENT_DISK_COUNTER_Y;
import static de.kablion.passetrappe.utils.Constants.OWN_DISK_COUNTER_Y;

public class OnFieldHUD extends Group {

    private final PasseTrappe app;
    private final WorldStage worldStage;

    private Array<HUD> huds = new Array<HUD>();

    private HashMap<Player,Integer> counts = new HashMap<Player, Integer>();

    public OnFieldHUD(PasseTrappe app, WorldStage worldStage) {
        this.app = app;
        this.worldStage = worldStage;
        setDebug(true);
        initHUDS();
    }

    private void initHUDS() {
        HUD tempHUD = new HUD(Player.ONE);
        huds.add(tempHUD);
        addActor(tempHUD);

        tempHUD = new HUD(Player.TWO);
        huds.add(tempHUD);
        addActor(tempHUD);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateCounters();
    }

    public void updateCounters() {
        Array<Disk> disks = worldStage.getDisks();
        int p1 = 0;
        int p2 = 0;

        for (int i=0;i<disks.size;i++) {
            if(disks.get(i).getPlayer() == Player.ONE) p1++;
            else p2++;
        }
        counts.clear();
        counts.put(Player.ONE,p1);
        counts.put(Player.TWO,p2);

        for(int i = 0; i < huds.size; i++) {
            huds.get(i).updateCounters(counts);
        }

    }

    public void dispose() {

    }

    private class HUD extends Group {

        private Player player;

        private Counter ownCounter;
        private Counter opponentCounter;

        private HUD(Player player) {
            this.player = player;
            if(player == Player.ONE) {
                setTransform(true);
                rotateBy(180);
            }

            initCounters();
        }

        private void initCounters() {
            Label.LabelStyle labelStyle = new Label.LabelStyle();

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(DEFAULT_FONT_PATH));
            FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
            params.size = 50;
            params.color = Color.BLACK;
            labelStyle.font = generator.generateFont(params);
            labelStyle.font.setUseIntegerPositions(false);
            generator.dispose();

            ownCounter = new Counter(labelStyle,player);
            if(player == Player.ONE) opponentCounter = new Counter(labelStyle,Player.TWO);
            else opponentCounter = new Counter(labelStyle,Player.ONE);

            ownCounter.setY(OWN_DISK_COUNTER_Y-ownCounter.getHeight()/2);
            ownCounter.setX(-ownCounter.getWidth()/2);
            opponentCounter.setY(OPPONENT_DISK_COUNTER_Y-opponentCounter.getHeight()/2);
            opponentCounter.setX(-opponentCounter.getWidth()/2);

            addActor(ownCounter);
            addActor(opponentCounter);
        }

        private void updateCounters(HashMap<Player,Integer> counts) {
            ownCounter.update(counts);
            opponentCounter.update(counts);
        }

    }
}
