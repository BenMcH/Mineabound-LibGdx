package com.tycoon177.mineabound.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tycoon177.mineabound.MineaboundLauncher;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		config.title = "Mineabound";
		new LwjglApplication(new MineaboundLauncher(), config);
	}
}
