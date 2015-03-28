package com.tycoon177.mineabound;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;

public class MineaboundLauncher extends Game {

	public static final boolean IS_DEBUG_RENDERING = true;
	public static final Color PLAYER_BOUNDING_BOX_COLOR = new Color(1,0,1, 1);
	public static final Color BLOCK_BOUNDING_BOX_COLOR = Color.BLACK;
	public static final Color ENTITY_BOUNDING_BOX_COLOR = Color.RED;
	
	
	@Override
	public void create() {
		setScreen(new MainMenu());
	}

}
