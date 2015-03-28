package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.utils.LoadedTextureAtlas;
import com.tycoon177.mineabound.world.Block;

public class Player extends Entity {
	/** The default player size **/
	private Vector2 playerSize = new Vector2(.6f, 1.8f);
	private int direction = RIGHT;
	public static final float JUMP_VELOCITY = .22f;

	public Player() {
		setSize(playerSize.x, playerSize.y);
		this.setSprite(LoadedTextureAtlas.blockAtlas.createSprite("playerStill"));
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean canJump() {
		//float thicknessOfBB = .01f;

	/*	Rectangle boundingBox = new Rectangle(getPosition().x + thicknessOfBB, getPosition().y - thicknessOfBB, getSize().x - 2 * thicknessOfBB, thicknessOfBB);
		for (Block block : GameWorld.world.getChunkHandler().getVisibleBlocks())
			if (block.collides(boundingBox))
				return true;
		return false;*/
		
		return MathUtils.isEqual(getVelocity().y, 0);
	}

	public void jump() {
		if (canJump())
			setYVelocity(JUMP_VELOCITY);
	}

	@Override
	public boolean isColliding() {
		Array<Block> blocks = GameWorld.world.getChunkHandler().getVisibleBlocks();
		Rectangle player = new Rectangle(getPosition().x, getPosition().y, this.getSize().x, this.getSize().y);
		for (Block block : blocks) {
			if (block.collides(player))
				return true;
		}
		return false;
	}
}
