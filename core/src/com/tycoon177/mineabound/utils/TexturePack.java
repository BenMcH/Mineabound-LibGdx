package com.tycoon177.mineabound.utils;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tycoon177.mineabound.MineaboundLauncher;

public class TexturePack {
	public static TexturePack currentPack;
	private FileHandle directory;
	public static final FileHandle texturePackFolder = MineaboundLauncher.texturePacksDirectory;
	public static final FileHandle defaultTexturepack = texturePackFolder.child("default");
	private HashMap<String, Sprite> cachedSprites;

	public TexturePack() {
		cachedSprites = new HashMap<String, Sprite>();
		directory = defaultTexturepack;
	}

	public TexturePack(FileHandle handle) {
		this();
		if (!handle.isDirectory())
			throw new IllegalArgumentException("The argument must be a directory!");
		this.directory = handle;
	}

	public TexturePack(String directory) {
		this(Gdx.files.external(directory));
	}

	public static Sprite getTexture(String name) {
		Sprite ret = null;
		if (currentPack.cachedSprites.containsKey(name))
			ret = currentPack.cachedSprites.get(name);
		else {
			FileHandle textureFile = currentPack.directory.child(name + ".png");
			Texture tex;
			if (textureFile.exists()) {
				tex = new Texture(textureFile);
			}
			else {
				tex = getDefaultTexture(name);
			}
			if (tex != null)
				ret = new Sprite(tex);
		}
		currentPack.cachedSprites.put(name, ret);
		return ret;
	}

	public static Texture getDefaultTexture(String name) {
		if (name.equalsIgnoreCase("air"))
			return null;
		return new Texture(defaultTexturepack.child(name + ".png"));
	}

	public void changePack(FileHandle handle) {
		directory = handle;
		cachedSprites.clear();
		MineaboundLauncher.preferences.putString("texturePack", handle.path());
		MineaboundLauncher.preferences.flush();
		;
		System.out.println("Changed to " + handle.path());
	}

	public void changePack(String directory) {
		changePack((texturePackFolder.child(directory)));
	}

	public static void init() {
		String name = MineaboundLauncher.preferences.getString("texturePack");
		if (name == null || name.equalsIgnoreCase(""))
			currentPack = new TexturePack();
		else
			currentPack = new TexturePack(name);
	}
}
