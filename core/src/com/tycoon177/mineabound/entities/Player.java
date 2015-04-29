package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.entities.utils.Inventory;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.utils.TexturePack;
import com.tycoon177.mineabound.utils.interfaces.Droppable;
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
	private Vector2 playerSize = new Vector2(.45f, 1.8f);
	public OrthographicCamera headsUpDisplayCamera;
	private boolean openInventory = false;
	private Sprite hotbarTexture = TexturePack.getTexture("hotbar");
	private Sprite selected = TexturePack.getTexture("selectedCell");
	private Sprite heart = TexturePack.getTexture("heart");
	private Sprite halfHeart = TexturePack.getTexture("half_heart");
	private Sprite emptyHeart = TexturePack.getTexture("empty_heart");
	private Inventory inventory;
	private Block[] hotbar;
	private int hotbarIndex = 0;
	public static final float JUMP_VELOCITY = 10f;
	public static final float forceX = 6f;

	public Player() {
		super();
		headsUpDisplayCamera = new OrthographicCamera(25, 12.5f);
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
		inventory = new Inventory();
		inventory.set(0, 0, new Entity(new Vector2(), (Sprite)BlockType.BEDROCK.getSprite()));
		System.out.println(inventory.getStack(0, 0).getType());
		setPixelOffset(.1f);
		setLiving(true);
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
			drawInventory(batch, size);
		}
		drawHotbar(batch, size);
		drawHealth(batch, offset);
		batch.setProjectionMatrix(oldMat);
	}

	private void drawHealth(SpriteBatch batch, float offset) {
		Sprite s = heart;
		float size = .25f;
		float y = -headsUpDisplayCamera.viewportHeight / 2f + offset * 7f;
		float x = -headsUpDisplayCamera.viewportWidth / 4f + 9f / 4f;
		for (int i = 0; i < getMaxHealth() / 2; i++) {
			float nx = x + size * 1.25f * i;
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
			batch.draw(s, nx, y, size, size);
		}
	}

	private void drawHotbar(SpriteBatch batch, float size) {
		float width = 9f;
		float height = 1f;
		float x = -headsUpDisplayCamera.viewportWidth / 4f + width / 4f;
		float y = -headsUpDisplayCamera.viewportHeight / 2f + (height - size);
		batch.draw(hotbarTexture, x, y, width, height);
		for (int i = 0; i < width; i++) {
			Sprite s = (Sprite) getHotbar()[i].getBlockType().getSprite();
			float nx = i + x + (1 - size) / 2f;
			if (s != null)
				batch.draw(s, nx, y + (height - size) / 2f, size, size);
		}
		selected.setSize(width / 9f, height);
		selected.setPosition(x + getHotbarIndex(), y);
		selected.setAlpha(.5f);
		selected.draw(batch);
	}

	private void drawInventory(SpriteBatch batch, float size) {
		inventory.draw(batch, size);
	}

	public Entity getItemFromInventory(int xLoc, int yLoc) {
		return inventory.get(xLoc, yLoc);
	}

	public Block[] getHotbar() {
		return hotbar;
	}

	public void jump() {
		if (canJump())
			setYVelocity(JUMP_VELOCITY);
	}

	public void moveHotbarIndex(int amount) {
		this.hotbarIndex += amount;
		if (hotbarIndex <= 0) {
			hotbarIndex = hotbar.length - 1;
		}
		hotbarIndex %= hotbar.length;
	}

	public int getHotbarIndex() {
		return hotbarIndex;
	}

	public BlockType getHeldItem() {
		return getHotbar()[getHotbarIndex()].getBlockType();
	}
	
	public Droppable getHeldDroppable(){
		return getHotbar()[getHotbarIndex()];
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		Rectangle hitbox = getHitBox();
		Array<Entity> en = GameWorld.world.getEntityHandler().getVisibleEntities();
		for (Entity ent : en) {
			if (hitbox.overlaps(ent.getHitBox())) {
				pickUp(ent);
			}
		}
	}

	private void pickUp(Entity ent) {
		// Put the entity in the inventory
		GameWorld.world.getEntityHandler().removeEntity(ent);

	}
	
	public void drop(){
		Vector2 position = new Vector2(getPosition().x + getSize().x * 2, getPosition().y + Player.DEFAULT_HEIGHT/2f);
		Vector2 velocity = new Vector2(1, 1);
		Entity droppedItem = new Entity(position, getHeldDroppable());
		droppedItem.setVelocity(velocity);
		GameWorld.world.getEntityHandler().addEntity(droppedItem);

	}
	
	public Inventory getInventory(){
		return inventory;
	}
}
