package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.world.Block;

public class LivingEntity extends Entity {

	private float health = 10;
	private Entity[] inventory;
	private static final int INVENTORY_WIDTH = 10, INVENTORY_HEIGHT = 5;
	private int direction = RIGHT;
	public static final float JUMP_VELOCITY = .22f;

	public LivingEntity() {
		this(new Vector2(), null);
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
	

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean canJump() {
		// float thicknessOfBB = .01f;

		/*
		 * Rectangle boundingBox = new Rectangle(getPosition().x + thicknessOfBB, getPosition().y - thicknessOfBB, getSize().x - 2 * thicknessOfBB, thicknessOfBB); for (Block block : GameWorld.world.getChunkHandler().getVisibleBlocks()) if (block.collides(boundingBox)) return true; return false;
		 */

		return MathUtils.isEqual(getVelocity().y, 0);
	}

	public void jump() {
		if (canJump())
			setYVelocity(JUMP_VELOCITY);
	}

	@Override
	public boolean isColliding() {
		Array<Block> blocks = GameWorld.world.getChunkHandler().getVisibleBlocks();
		Rectangle player = new Rectangle(getPosition().x + .00000001f, getPosition().y, this.getSize().x - .00000001f, this.getSize().y);
		for (Block block : blocks) {
			if (block.collides(player))
				return true;
		}
		return false;
	}


}
