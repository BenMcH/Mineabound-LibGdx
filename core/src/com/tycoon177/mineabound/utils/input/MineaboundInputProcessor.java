package com.tycoon177.mineabound.utils.input;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tycoon177.mineabound.MineaboundLauncher;
import com.tycoon177.mineabound.entities.Player;
import com.tycoon177.mineabound.entities.utils.ItemStack;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.screens.MainMenu;
import com.tycoon177.mineabound.world.blocks.Block;
import com.tycoon177.mineabound.world.blocks.BlockType;

public class MineaboundInputProcessor implements InputProcessor {

	private GameWorld world;

	private Vector2 lastTouchedPoint;

	private boolean jump = false;
	private float shiftModifier = 1f;
	private float velocityX = 0f;
	// private ItemStack heldItemstack;

	/**
	 * Creates an input processor that will act on the current world.
	 * 
	 * @param world
	 */
	public MineaboundInputProcessor(GameWorld world) {
		this.world = world;
		lastTouchedPoint = new Vector2();
	}

	/**
	 * Acts when a button is pressed down
	 */
	@Override
	public boolean keyDown(int keycode) {
		if (!isPaused()) {
			if (keycode == KeyBindings.getBinding("sprint")) {
				shiftModifier = 1.3f;
			}
			if (keycode == KeyBindings.getBinding("left")) {
				velocityX = (-Player.forceX);
				world.getPlayer().setDirection(Player.LEFT);
			}
			if (keycode == KeyBindings.getBinding("right")) {
				velocityX = (Player.forceX);
				world.getPlayer().setDirection(Player.RIGHT);
			}
			if (keycode == KeyBindings.getBinding("jump")) {
				jump = true;
			}
			if (keycode == KeyBindings.getBinding("drop")) {
				world.getPlayer().drop();
			}
		}
		if (keycode == KeyBindings.getBinding("inventory")) {
			world.getPlayer().setInventoryOpen(!world.getPlayer().isInventoryOpen());
		}
		if (keycode == KeyBindings.getBinding("debug")) {
			MineaboundLauncher.isDebugRendering = !MineaboundLauncher.isDebugRendering;
		}
		if (keycode == Keys.ESCAPE) {
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				}
			});
		}
		return true;
	}

	/**
	 * Acts when a button is released
	 */
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == KeyBindings.getBinding("left") || keycode == KeyBindings.getBinding("right")) {
			velocityX = (0);
		}
		if (keycode == KeyBindings.getBinding("jump")) {
			jump = false;
		}
		if (keycode == KeyBindings.getBinding("sprint")) {
			shiftModifier = 1f;
		}
		return true;
	}

	/**
	 * Called when the mouse is pressed
	 */
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

		if (world.getPlayer().isInventoryOpen()) {
			ItemStack stack;
			OrthographicCamera cam = world.getPlayer().headsUpDisplayCamera;
			Vector3 touchLoc = cam.unproject(new Vector3(screenX, screenY, 0));
			Vector2 hudLoc = world.getPlayer().getInventory().getCoordinates();
			Vector2 inventorySize = world.getPlayer().getInventory().getSize();
			if (touchLoc.x >= hudLoc.x && touchLoc.x <= hudLoc.x + inventorySize.x) {
				if (touchLoc.y >= hudLoc.y && touchLoc.y <= hudLoc.y + inventorySize.y) {
					touchLoc.x -= hudLoc.x;
					touchLoc.y -= hudLoc.y;
					touchLoc.x = MathUtils.floor(touchLoc.x);
					touchLoc.y = MathUtils.floor(touchLoc.y);
					if (button == Buttons.LEFT) {
						stack = world.getPlayer().getInventory().getStack((int) touchLoc.x, (int) touchLoc.y);
					}
				}
			}
		}
		else {
			// If the touch position is within the minimum distance, respond to the touch
			if (this.world.getPlayer().getDistanceFromPoint(inWorldLocation.x, inWorldLocation.y) < 5) {
				if (button == Buttons.LEFT)
					this.world.getChunkHandler().removeBlock(inWorldLocation);
				else
					if (button == Buttons.RIGHT) {
						Block randBlock = new Block(BlockType.getRandomType());
						Block block = world.getPlayer().getHotbar()[world.getPlayer().getHotbarIndex()];
						if (block == null)
							return false;
						if (block.getBlockType() != BlockType.AIR) {
							this.world.getChunkHandler().addBlock(inWorldLocation, block);
						}
					}
			}
		}
		return true;
	}

	/**
	 * called after the mouse has been released
	 */
	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	/**
	 * Called when the mouse is released
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	/**
	 * Called when the mouse is dragged
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	/**
	 * Called on every mouse movement
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	/**
	 * Called on scroll
	 */
	@Override
	public boolean scrolled(int amount) {
		world.getPlayer().moveHotbarIndex(amount);
		return true;
	}

	/**
	 * Returns the last point that was clicked
	 * 
	 * @return
	 */
	public Vector2 getLastTouchedPoint() {
		return lastTouchedPoint;
	}

	/**
	 * Updates aspects of the world on a "tick"
	 */
	public void update() {
		world.getPlayer().setXVelocity(velocityX * shiftModifier);
		if (jump)
			if (world.getPlayer().canJump())
				world.getPlayer().jump();
	}

	private boolean isPaused() {
		return world.getPlayer().isInventoryOpen();
	}

}
