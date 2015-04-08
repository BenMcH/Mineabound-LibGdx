package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.utils.TexturePack;

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
	private Sprite hotbar = TexturePack.getTexture("hotbar");

	private Sprite selected = TexturePack.getTexture("selectedCell");

	public Player() {
		super();
		headsUpDisplayCamera = new OrthographicCamera(20, 10);
		setSize(playerSize.x, playerSize.y);
		this.setSprite(TexturePack.getTexture("playerStill"));
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
		float offset = 2.1f / 9f;
		float size = 1 - (1 / 9f) * 4;
		Matrix4 oldMat = batch.getProjectionMatrix().cpy();
		batch.setProjectionMatrix(headsUpDisplayCamera.combined);
		if (isInventoryOpen()) {
			// Draw Inventory here!
		}
		drawHotbar(batch, offset, size);
		
		batch.setProjectionMatrix(oldMat);
	}
	
	private void drawHotbar(SpriteBatch batch, float offset, float size){
		batch.draw(hotbar, -headsUpDisplayCamera.viewportWidth / 4f, -headsUpDisplayCamera.viewportHeight / 2.1f, 9, 1);
		float y = -headsUpDisplayCamera.viewportHeight / 2.1f + offset;
		for (int i = 0; i < 9; i++) {
			Sprite s = (Sprite) getHotbar()[i].getBlockType().getSprite();
			float x = i + 1 - headsUpDisplayCamera.viewportWidth / 4f - .5f - size / 2f;
			if (s != null)
				batch.draw(s, x, y, size, size);
		}
		selected.setSize(1, 1);
		selected.setPosition(getHotbarIndex() - headsUpDisplayCamera.viewportWidth / 4, -headsUpDisplayCamera.viewportHeight / 2.1f);
		selected.setColor(.5f,.5f,.5f,.5f);
		selected.draw(batch);
	}
}
