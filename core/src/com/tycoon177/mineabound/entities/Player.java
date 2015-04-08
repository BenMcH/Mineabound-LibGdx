package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.utils.TexturePack;
import com.tycoon177.mineabound.world.blocks.Block;
import com.tycoon177.mineabound.world.blocks.BlockType;

/**
 * This class handles the player interactions
 * 
 * @author Ben
 *
 */
public class Player extends Entity {
	/** The default player size **/
	private Vector2 playerSize = new Vector2(.6f, 1.8f);
	private OrthographicCamera headsUpDisplayCamera;
	private boolean openInventory = false;
	private Sprite hotbarTexture = TexturePack.getTexture("hotbar");
	private Sprite selected = TexturePack.getTexture("selectedCell");
	private Sprite heart = TexturePack.getTexture("heart");
	private Sprite halfHeart = TexturePack.getTexture("half_heart");
	private Sprite emptyHeart = TexturePack.getTexture("empty_heart");
	private Entity[] inventory;
	private Block[] hotbar;
	private int hotbarIndex = 0;
	public static final float JUMP_VELOCITY = 10f;
	public static final float forceX = 6f;
	private static final int INVENTORY_WIDTH = 10, INVENTORY_HEIGHT = 5;

	public Player() {
		super();
		headsUpDisplayCamera = new OrthographicCamera(20, 10);
		setSize(playerSize.x, playerSize.y);
		this.setSprite(TexturePack.getTexture("playerStill"));
		hotbar = new Block[9];
		hotbar[0] = new Block(BlockType.BEDROCK);
		hotbar[1] = new Block(BlockType.DIRT);
		hotbar[2] = new Block(BlockType.GRASS);
		hotbar[3] = new Block(BlockType.STONE);
		hotbar[4] = new Block(BlockType.AIR);
		hotbar[5] = new Block(BlockType.AIR);
		hotbar[6] = new Block(BlockType.AIR);
		hotbar[7] = new Block(BlockType.AIR);
		hotbar[8] = new Block(BlockType.AIR);
		inventory = new Entity[INVENTORY_HEIGHT * INVENTORY_WIDTH];
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
		drawHealth(batch, offset);
		batch.setProjectionMatrix(oldMat);
	}

	private void drawHealth(SpriteBatch batch, float offset) {
		Sprite s = heart;
		float size = .25f;
		float y = -headsUpDisplayCamera.viewportHeight / 2.1f + 1 + offset * 2f;
		for (int i = 0; i < getMaxHealth() / 2; i++) {
			float x = -headsUpDisplayCamera.viewportWidth * size + i * size * 1.25f;
			if (i * 2 + 2 <= getHealth()) {
				s = heart;
			}
			else {
				if (i * 2 + 1 <= getHealth()) {
					s = halfHeart;
				}
				else {
					s = emptyHeart;
				}
			}
			batch.draw(s, x, y, size, size);
		}
	}

	private void drawHotbar(SpriteBatch batch, float offset, float size) {
		batch.draw(hotbarTexture, -headsUpDisplayCamera.viewportWidth / 4f, -headsUpDisplayCamera.viewportHeight / 2.1f, 9, 1);
		float y = -headsUpDisplayCamera.viewportHeight / 2.1f + offset;
		for (int i = 0; i < 9; i++) {
			Sprite s = (Sprite) getHotbar()[i].getBlockType().getSprite();
			float x = i + 1 - headsUpDisplayCamera.viewportWidth / 4f - .5f - size / 2f;
			if (s != null)
				batch.draw(s, x, y, size, size);
		}
		selected.setSize(1, 1);
		selected.setPosition(getHotbarIndex() - headsUpDisplayCamera.viewportWidth / 4, -headsUpDisplayCamera.viewportHeight / 2.1f);
		selected.setColor(1, 1, 1, .5f);
		selected.draw(batch);
	}

	public Entity getItemFromInventory(int xLoc, int yLoc) {
		if (xLoc < 0 || yLoc < 0 || xLoc >= INVENTORY_WIDTH || yLoc >= INVENTORY_HEIGHT) {
			return null;
		}
		return inventory[xLoc + yLoc * INVENTORY_WIDTH];
	}

	public Block[] getHotbar() {
		return hotbar;
	}

	public void jump() {
		if (canJump())
			setYVelocity(JUMP_VELOCITY);
	}
	
	public void moveHotbarIndex(int amount){
		this.hotbarIndex += amount;
		if(hotbarIndex <= 0){
			hotbarIndex = hotbar.length-1;
		}
		hotbarIndex %= hotbar.length;
	}
	
	public int getHotbarIndex(){
		return hotbarIndex;
	}
}
