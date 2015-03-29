package com.tycoon177.mineabound.utils;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tycoon177.mineabound.entities.Player;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.world.BlockType;

public class MineaboundInputProcessor implements InputProcessor {

	private GameWorld world;
	private final float forceX = .1f;
	private Vector2 lastTouchedPoint;

	public MineaboundInputProcessor(GameWorld world) {
		this.world = world;
		lastTouchedPoint = new Vector2();
	}

	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Keys.A:
				world.getPlayer().setXVelocity(-forceX);
				world.getPlayer().setDirection(Player.LEFT);
				break;
			case Keys.D:
				world.getPlayer().setXVelocity(forceX);
				world.getPlayer().setDirection(Player.RIGHT);
				break;
			case Keys.W:
			case Keys.SPACE:
				if (world.getPlayer().canJump())
					world.getPlayer().jump();// characterMovement.y = forceY;
				break;
			case Keys.S:
				// characterMovement.y = -forceY;
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
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		final Vector3 touchLocation = new Vector3(screenX + .5f, screenY + .5f, 0);
		Vector3 onScreenLoc = world.getCamera().unproject(touchLocation); // In world point
		this.lastTouchedPoint.set(new Vector2(onScreenLoc.x, onScreenLoc.y));
		if (this.world.getPlayer().getDistanceFromPoint(onScreenLoc.x, onScreenLoc.y) < 5) {
			if (button == Buttons.LEFT)
				this.world.getChunkHandler().removeBlock(onScreenLoc);
			else
				if (button == Buttons.RIGHT) {
					this.world.getChunkHandler().addBlock(onScreenLoc, BlockType.STONE);
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

		return false;
	}

	public Vector2 getLastTouchedPoint() {
		return lastTouchedPoint;
	}

}
