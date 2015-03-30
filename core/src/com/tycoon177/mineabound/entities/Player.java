package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.utils.LoadedTextureAtlas;

public class Player extends LivingEntity {
	/** The default player size **/
	private Vector2 playerSize = new Vector2(.6f, 1.8f);
	private OrthographicCamera headsUpDisplayCamera;
	private boolean openInventory = false;
	//private Sprite inventoryGridPiece = LoadedTextureAtlas.blockAtlas.createSprite("gridPiece");
	public Player() {
		super();
		headsUpDisplayCamera = new OrthographicCamera(20, 10);
		setSize(playerSize.x, playerSize.y);
		this.setSprite(LoadedTextureAtlas.blockAtlas.createSprite("playerStill"));
	}

	@Override
	public void draw(SpriteBatch batch, int direction) {
		super.draw(batch, direction);
		drawHUD(batch);
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
	private void drawHUD(SpriteBatch batch) {
		Matrix4 oldMat = batch.getProjectionMatrix();
		batch.setProjectionMatrix(headsUpDisplayCamera.combined);
		if(isInventoryOpen()){
			// Draw Inventory here!
		}
		
		batch.setProjectionMatrix(oldMat);
	}

}
