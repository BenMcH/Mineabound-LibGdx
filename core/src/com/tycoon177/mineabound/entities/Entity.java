package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.world.Chunk;
import com.tycoon177.mineabound.world.blocks.Block;
import com.tycoon177.mineabound.world.blocks.BlockType;

public class Entity {
	public static final float DEFAULT_WIDTH = .5f;
	public static final float DEFAULT_HEIGHT = .5f;
	public static final float GRAVITY_FORCE = 40f;
	public static final float TERMINAL_VELOCITY = 100f;
	private static final float STANDARD_CHANGE = 0.125f;
	public static final int RIGHT = 1, LEFT = 0;

	private float width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
	private Sprite sprite;
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 oldPosition;
	private Rectangle boundingBox;

	public Entity() {
		velocity = new Vector2();
		position = new Vector2();
		oldPosition = new Vector2();
		boundingBox = new Rectangle();
	}

	public Entity(Vector2 vec2, Sprite sprite) {
		this();
		position.set(vec2);
		setSprite(sprite);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	protected void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	protected Sprite getSprite() {
		return sprite;
	}

	public void draw(SpriteBatch batch) {
		draw(batch, RIGHT);
		// batch.draw(sprite, (getPosition().x - width / 2) + width, getPosition().y - height / 2, -width, height);
	}

	public void draw(SpriteBatch batch, int direction) {
		if (sprite == null)
			return;
		if (direction == RIGHT)
			batch.draw(sprite, (getPosition().x), getPosition().y, width, height);
		else
			if (direction == LEFT)
				batch.draw(sprite, (getPosition().x) + width, getPosition().y, -width, height);
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public Vector2 getSize() {
		return new Vector2(width, height);
	}

	public Array<Block> getCollisions() {

		return null;
	}

	public void update(float deltaTime) {
		applyGravity(deltaTime);
		if (getVelocity().x == 0)
			return;
		float change = Math.abs(getVelocity().x) * deltaTime;
		float vel = getVelocity().x * deltaTime;
		float direction = vel > 0 ? 1 : -1;
		while (vel != 0) {
			oldPosition.set(getPosition());
			float amountToChangeBy = Math.abs(vel) >= change ? (direction * change) : vel;
			if (canMove(vel > 0 ? RIGHT : LEFT)) {
				updateLocation(new Vector2(amountToChangeBy, 0));
				if (!canMove(LEFT) || !canMove(RIGHT)) {
					setPosition(oldPosition.x, oldPosition.y);
					vel = 0;
				}
			}
			if (Math.abs(vel) >= change)
				vel -= (direction * change);
			else
				vel = 0;
		}
	}

	public boolean canMove(int direction) {
		float thicknessOfBB = .01f;
		if (direction == LEFT) {
			boundingBox.set(getPosition().x, getPosition().y + thicknessOfBB, thicknessOfBB, getSize().y - 2 * thicknessOfBB);
		}
		else
			if (direction == RIGHT) {
				boundingBox.set(getPosition().x + getSize().x - thicknessOfBB, getPosition().y + thicknessOfBB, thicknessOfBB, getSize().y - 2 * thicknessOfBB);
			}
		for (Block block : GameWorld.world.getChunkHandler().getVisibleBlocks())
			if (block.collides(boundingBox))
				return false;
		return true;
	}

	public void applyGravity(float deltaTime) {
		boolean canFall = canFall();
		boolean canRise = canRise();
		velocity.y -= GRAVITY_FORCE * deltaTime;
		velocity.y = MathUtils.clamp(velocity.y, -TERMINAL_VELOCITY, TERMINAL_VELOCITY);
		float vel = velocity.y * deltaTime;
		float direction = vel < 0 ? -1 : 1;
		if (!canFall) {
			vel = 0;
		}
		while (vel != 0) {
			oldPosition.set(getPosition());
			float amountToChangeBy = Math.abs(vel) >= STANDARD_CHANGE ? (direction * STANDARD_CHANGE) : vel;
			if (!canFall) {
				// setYVelocity(0);
				vel = 0;
			}
			if (vel != 0) {
				if ((vel > 0 && canRise) || (vel < 0 && canFall)) {
					updateLocation(new Vector2(0, amountToChangeBy));
					canFall = canFall();
					canRise = canRise();
				}
				if (!canFall || !canRise) {
					vel = 0;
					if (!canFall) {
						setPosition(getPosition().x, MathUtils.ceil(getPosition().y));
						BlockType block = GameWorld.world.getChunkHandler().getBlockAtPos(MathUtils.floor(getPosition().x), Math.round(getPosition().y)-1);
						System.out.println(block.getBounciness());
						System.out.println(block.name());
						setYVelocity((-getVelocity().y) * block.getBounciness());
					}
					else {
						setPosition(getPosition().x, MathUtils.floor(getPosition().y + getSize().y - 1) - getSize().y);
						setYVelocity(0);
						// setPosition(oldPosition.x, oldPosition.y);
					}
				}
			}
			if (Math.abs(vel) >= STANDARD_CHANGE)
				vel -= (direction * STANDARD_CHANGE);
			else
				vel = 0;
		}
		if (MathUtils.isEqual(velocity.y, 0))
			setPosition(getPosition().x, MathUtils.ceil(getPosition().y));

	}

	public boolean isColliding() {
		Array<Block> blocks = GameWorld.world.getChunkHandler().getVisibleBlocks();
		Rectangle player = new Rectangle(getPosition().x + .000001f, getPosition().y, this.getSize().x - .000002f, this.getSize().y);
		for (Block block : blocks) {
			if (block.collides(player))
				return true;
		}
		return false;
	}

	public int getCurrentChunk() {
		int chunk = (int) (getPosition().x / Chunk.WIDTH);
		if (getPosition().x < 0)
			chunk--;
		return chunk;
	}

	public void updateLocation(Vector2 velocity2) {
		this.position.x += velocity2.x;
		this.position.y += velocity2.y;
	}

	public void setYVelocity(float y) {
		this.velocity.y = y;
	}

	public void setXVelocity(float x) {
		this.velocity.x = x;
	}

	public void setVelocity(Vector2 vel) {
		this.velocity.set(vel);
	}

	public void debugRender(ShapeRenderer batch) {
		batch.rect(position.x, position.y, width, height);
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public boolean canFall() {
		float reducedSize = 0.01f;
		boundingBox.set(getPosition().x + reducedSize, getPosition().y, getSize().x - 2 * reducedSize, getSize().y * .1f);
		for (Block block : GameWorld.world.getChunkHandler().getVisibleBlocks())
			if (block.collides(boundingBox))
				return false;

		return true;
	}

	public boolean canRise() {
		boundingBox.set(getPosition().x + .01f, getPosition().y + getSize().y - .01f, getSize().x * .98f, .01f);
		for (Block block : GameWorld.world.getChunkHandler().getVisibleBlocks())
			if (block.collides(boundingBox))
				return false;

		return true;
	}

	public float getDistanceFromPoint(Vector2 point) {
		Vector2 entityLocation = new Vector2(getPosition());
		entityLocation.x += getSize().x / 2f;
		entityLocation.y += getSize().y / 2f;
		return entityLocation.dst2(point);
	}

	public float getDistanceFromPoint(float x, float y) {
		return getDistanceFromPoint(new Vector2(x, y));
	}

	public Rectangle getHitBox() {
		return new Rectangle(getPosition().x, getPosition().y, getSize().x, getSize().y);
	}

	public void drawEntity(SpriteBatch renderer) {
		renderer.draw(getSprite(), getPosition().x, getPosition().y, getSize().x, getSize().y);
		// System.out.println(getPosition().x + " " + getPosition().y);
	}

}
