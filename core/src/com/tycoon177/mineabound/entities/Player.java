package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.utils.LoadedTextureAtlas;

/**
 * This class handles the player interactions
 * 
 * @author Ben
 *
 */
public class Player extends LivingEntity {
	/** The default player size **/
	private Vector2 playerSize = new Vector2(.6f, 1.8f);
	private OrthographicCamera headsUpDisplayCamera;
	private boolean openInventory = false;
	private Sprite inventoryGridPiece = LoadedTextureAtlas.blockAtlas.createSprite("cell");
	private Sprite selectedInventoryGridPiece = LoadedTextureAtlas.blockAtlas.createSprite("selectedCell");

	public Player() {
		super();
		headsUpDisplayCamera = new OrthographicCamera(20, 10);
		setSize(playerSize.x, playerSize.y);
		this.setSprite(LoadedTextureAtlas.blockAtlas.createSprite("playerStill"));
	}

	@Override
	public void draw(SpriteBatch batch, int direction) {
		super.draw(batch, direction);
	}

	public boolean isInventoryOpen() {
		return openInventory;
	}

	public void setInventoryOpen(boolean state) {
		openInventory = state;
	}

	/**
	 * Draw the HUD (Health, hotbar, etc)
	 * 
	 * @param batch
	 *            The Spritebatch used for drawing
	 */
	public void drawHUD(SpriteBatch batch) {
		Matrix4 oldMat = batch.getProjectionMatrix().cpy();
		batch.setProjectionMatrix(headsUpDisplayCamera.combined);
		if (isInventoryOpen()) {
			// Draw Inventory here!
		}
		for (int i = 0; i < 9; i++) {
			Sprite s = (Sprite) getHotbar()[i].getBlockType().getSprite();
			batch.draw(i == getHotbarIndex() ? selectedInventoryGridPiece : inventoryGridPiece, i - headsUpDisplayCamera.viewportWidth / 4f, -headsUpDisplayCamera.viewportHeight / 2.1f, 1, 1);
			if (s != null)
				batch.draw(s, i - headsUpDisplayCamera.viewportWidth / 4f + .01f, -headsUpDisplayCamera.viewportHeight / 2.1f + .01f, .98f, .98f);
		}
		batch.setProjectionMatrix(oldMat);
	}

}
