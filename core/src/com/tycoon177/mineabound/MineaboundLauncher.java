package com.tycoon177.mineabound;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

public class MineaboundLauncher extends Game {

	public static final boolean IS_DEBUG_RENDERING = true;
	public static final Color PLAYER_BOUNDING_BOX_COLOR = new Color(1,0,1, 1);
	public static final Color BLOCK_BOUNDING_BOX_COLOR = Color.BLACK;
	public static final Color ENTITY_BOUNDING_BOX_COLOR = Color.RED;
	public static FileHandle texturePacksDirectory;
	
	public static Preferences preferences;
	
	@Override
	public void create() {
		createFilePaths();
		setScreen(new MainMenu());
	}
	
	private void createFilePaths(){
		FileHandle handle = Gdx.files.external(".mineabound");
		texturePacksDirectory = handle.child("texture packs");
		texturePacksDirectory.file().mkdirs();
	}

}
