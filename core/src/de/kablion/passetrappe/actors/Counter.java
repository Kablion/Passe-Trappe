package de.kablion.passetrappe.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;

import de.kablion.passetrappe.utils.Player;

public class Counter extends Label {

    private Player countedOfPlayer;

    public Counter(Label.LabelStyle labelStyle, Player countedOfPlayer) {
        super("0",labelStyle);
        this.countedOfPlayer = countedOfPlayer;

        init();
    }

    private void init() {
        setFontScale(0.05f);
        setColor(0,0,0,0.5f);
        setWidth(4);
        setHeight(4f);
        setAlignment(Align.center);
        //setOrigin(getWidth()/2,getHeight()/2);
        //setX(-getWidth()/2);
    }

    public void update(HashMap<Player,Integer> counts) {
        setText(""+counts.get(countedOfPlayer));
    }

    public Player getCountedOfPlayer() {
        return countedOfPlayer;
    }
}
