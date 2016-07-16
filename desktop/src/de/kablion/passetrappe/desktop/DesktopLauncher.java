package de.kablion.passetrappe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.kablion.passetrappe.PasseTrappe;
import de.kablion.passetrappe.utils.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Constants.APP_NAME;
		config.width = Constants.UI_WIDTH;
		config.height = Constants.UI_HEIGHT;
		config.samples = 8;
		new LwjglApplication(new PasseTrappe(), config);
	}
}
