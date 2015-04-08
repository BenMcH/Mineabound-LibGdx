package com.tycoon177.mineabound.utils;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tycoon177.mineabound.MineaboundLauncher;
import com.tycoon177.mineabound.entities.Entity;
import com.tycoon177.mineabound.entities.Player;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.world.blocks.Block;
import com.tycoon177.mineabound.world.blocks.BlockType;

public class MineaboundInputProcessor implements InputProcessor {

	private GameWorld world;
	
	private Vector2 lastTouchedPoint;

	private boolean jump = false;
	private boolean shiftModifier = false;
	public MineaboundInputProcessor(GameWorld world) {
		this.world = world;
		lastTouchedPoint = new Vector2();
	}

	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Keys.A:
				world.getPlayer().setXVelocity(-Player.forceX * (shiftModifier ? 1.5f : 1f));
				world.getPlayer().setDirection(Player.LEFT);
				break;
			case Keys.D:
				world.getPlayer().setXVelocity(Player.forceX * (shiftModifier ? 1.5f : 1f));
				world.getPlayer().setDirection(Player.RIGHT);
				break;
			case Keys.W:
			case Keys.SPACE:
				jump = true;
				break;
			case Keys.SHIFT_LEFT:
				shiftModifier = true;
				break;
			case Keys.L:
				Entity r = new Entity(new Vector2(world.getPlayer().getPosition()), TexturePack.getTexture("bedrock"));
				world.getEntityHandler().addEntity(r);
				break;
			case Keys.F3:
				MineaboundLauncher.isDebugRendering = !MineaboundLauncher.isDebugRendering;
				break;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Keys.A:
			case Keys.D:
				world.getPlayer().setXVelocity(0);
				break;
			case Keys.W:
				jump = false;
				break;
			case Keys.SHIFT_LEFT:
			case Keys.SHIFT_RIGHT:
				shiftModifier = false;
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		// Gets the touch within the game and saves it
		final Vector3 touchLocation = new Vector3(screenX, screenY, 0);
		Vector3 inWorldLocation = world.getCamera().unproject(touchLocation); // In world point
		this.lastTouchedPoint.set(new Vector2(inWorldLocation.x, inWorldLocation.y));

		// Turn the player towards the mouse
		if (world.getPlayer().getPosition().x + world.getPlayer().getSize().x / 2f - inWorldLocation.x < 0) {
			world.getPlayer().setDirection(Player.RIGHT);
		}
		else {
			world.getPlayer().setDirection(Player.LEFT);
		}

		// If the touch position is within the minimum distance, respond to the touch
		if (this.world.getPlayer().getDistanceFromPoint(inWorldLocation.x, inWorldLocation.y) < 5) {
			if (button == Buttons.LEFT)
				this.world.getChunkHandler().removeBlock(inWorldLocation);
			else
				if (button == Buttons.RIGHT) {
					Block block = world.getPlayer().getHotbar()[world.getPlayer().getHotbarIndex()];
					if (block != null && block.getBlockType() != BlockType.AIR)
						this.world.getChunkHandler().addBlock(inWorldLocation, block.getBlockType());
				}
		}

		return true;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		world.getPlayer().moveHotbarIndex(amount);
		return true;
	}

	public Vector2 getLastTouchedPoint() {
		return lastTouchedPoint;
	}

	public void update() {
		if (jump)
			if (world.getPlayer().canJump())
				world.getPlayer().jump();
	}

}
