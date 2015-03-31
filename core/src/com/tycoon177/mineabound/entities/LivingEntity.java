package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.world.blocks.Block;
import com.tycoon177.mineabound.world.blocks.BlockType;

/**
 * Handles the entities which will have things such as health and an inventory.
 * @author Ben
 *
 */
public class LivingEntity extends Entity {

	private float health = 10;
	private Entity[] inventory;
	private Block[] hotbar;
	private int hotbarIndex = 0;
	private static final int INVENTORY_WIDTH = 10, INVENTORY_HEIGHT = 5;
	private int direction = RIGHT;
	public static final float JUMP_VELOCITY = .22f;
	public static final float forceX = 6f;
	public LivingEntity() {
		this(new Vector2(), null);
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
	}

	public LivingEntity(Vector2 vec2, Sprite sprite) {
		super(vec2, sprite);
		inventory = new Entity[INVENTORY_HEIGHT * INVENTORY_WIDTH];
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public void damage(float amount) {
		this.health -= Math.abs(amount);
	}

	public boolean isDead() {
		return health <= 0;
	}

	public void kill() {
		setHealth(0);
		dropInventory();
	}

	private void dropInventory() {
	}

	public Entity getItemFromInventory(int xLoc, int yLoc) {
		if (xLoc < 0 || yLoc < 0 || xLoc >= INVENTORY_WIDTH || yLoc >= INVENTORY_HEIGHT) {
			return null;
		}
		return inventory[xLoc + yLoc * INVENTORY_WIDTH];
	}
	
	public Block[] getHotbar(){
		return hotbar;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean canJump() {

		float thicknessOfBB = .01f;

		Array<Block> blocks = GameWorld.world.getChunkHandler().getVisibleBlocks();
		Rectangle player = new Rectangle(getPosition().x + thicknessOfBB, getPosition().y - thicknessOfBB, this.getSize().x - thicknessOfBB * 2, this.getSize().y);
		for (Block block : blocks) {
			if (block.collides(player))
				return true;
		}
		return false;
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
