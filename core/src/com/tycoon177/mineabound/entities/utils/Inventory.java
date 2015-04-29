package com.tycoon177.mineabound.entities.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.entities.Entity;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.utils.TexturePack;

public class Inventory {
	private static final int STANDARD_INVENTORY_WIDTH = 9, STANDARD_INVENTORY_HEIGHT = 4;
	private int width, height;
	private ItemStack[] inventory;
	private TextureRegion cell = TexturePack.getTexture("cell");

	public Inventory() {
		this(STANDARD_INVENTORY_WIDTH, STANDARD_INVENTORY_HEIGHT);
		width = STANDARD_INVENTORY_WIDTH;
		height = STANDARD_INVENTORY_HEIGHT;
	}

	public Inventory(int width, int height) {
		inventory = new ItemStack[width * height];
		for (int i = 0; i < inventory.length; i++) {
			inventory[i] = new ItemStack();
		}
		this.width = width;
		this.height = height;

	}

	public Entity get(int x, int y) {
		return getStack(x, y).peek();
	}

	public ItemStack getStack(int x, int y) {
		if (x < 0 || y < 0 || x >= STANDARD_INVENTORY_WIDTH || y >= STANDARD_INVENTORY_HEIGHT) {
			return null;
		}
		return inventory[x + y * STANDARD_INVENTORY_WIDTH];
	}

	public boolean set(int x, int y, Entity type) {
		if (x < 0 || y < 0 || x >= STANDARD_INVENTORY_WIDTH || y >= STANDARD_INVENTORY_HEIGHT) {
			return false;
		}
		Entity entity = get(x, y);
		if (entity != null) {
			if (entity.getClassName() != type.getClassName()) {
				return false;
			}
		}

		inventory[x + y * STANDARD_INVENTORY_WIDTH].addEntity(type);

		return true;
	}

	public void draw(SpriteBatch batch, float size) {
		Vector2 loc = getCoordinates();
		float x = loc.x;
		float y = loc.y;
		// Render the actual inventory boxes
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				batch.draw(cell, x + j, y + i, 1, 1);
			}
		}

		// Render inventory items
		for (int iX = 0; iX < STANDARD_INVENTORY_WIDTH; iX++) {
			for (int iY = 0; iY < STANDARD_INVENTORY_HEIGHT; iY++) {
				Entity ent = get(iX, iY);
				if (ent != null && ent.getSprite() != null) {
					batch.draw(ent.getSprite(), x + iX + .1f, y + iY + .1f, .8f, .8f);
				}
			}
		}
	}
	
	public Vector2 getCoordinates(){
		OrthographicCamera cam = GameWorld.world.getPlayer().headsUpDisplayCamera;
		float x = -cam.viewportWidth / 2f;
		float y = cam.viewportHeight / 2f - height;
		return new Vector2(x, y);
	}

	public Vector2 getSize() {
		
		return new Vector2(width, height);
	}
}
