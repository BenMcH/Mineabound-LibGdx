package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.utils.interfaces.Droppable;
import com.tycoon177.mineabound.world.Chunk;
import com.tycoon177.mineabound.world.blocks.Block;
import com.tycoon177.mineabound.world.blocks.BlockType;

public class Entity extends Droppable {
	public static final float DEFAULT_WIDTH = .5f;
	public static final float DEFAULT_HEIGHT = .5f;
	public static final float GRAVITY_FORCE = 40f;
	public static final float TERMINAL_VELOCITY = 100f;
	private static final float STANDARD_CHANGE = 0.125f;
	public static final int RIGHT = 1, LEFT = 0;
	private int direction = RIGHT;
	private float maxHealth = 20f;
	private float health = maxHealth;
	private float pixelOffset = 0;
	private float width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
	private Sprite sprite;
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 oldPosition;
	private Rectangle boundingBox;
	private float startOfFall;
	private float healthRegenTimer = 0;
	private boolean living;
	private Class<? extends Droppable> className = Entity.class;

	public Entity() {
		velocity = new Vector2();
		position = new Vector2();
		oldPosition = new Vector2();
		boundingBox = new Rectangle();
	}

	public Entity(Vector2 position, Sprite sprite) {
		this();
		this.position.set(position);
		setSprite(sprite);
	}

	public Entity(Vector2 pos, Droppable droppable) {
		this(pos, droppable.getSprite());
		this.className = droppable.getClass();
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

	@Override
	public Sprite getSprite() {
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
		healthRegen(deltaTime);
		applyGravity(deltaTime);
		if (getVelocity().x == 0)
			return;
		float change = Math.abs(getVelocity().x) * deltaTime;
		float vel = getVelocity().x * deltaTime;
		float direction = vel > 0 ? 1 : -1;
		if (vel != 0 && this instanceof Player)
			((Player) this).setDirection(vel > 0 ? RIGHT : LEFT);

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

	private void healthRegen(float deltaTime) {
		healthRegenTimer += deltaTime;
		if (healthRegenTimer >= 4f) {
			health += 1;
			health = MathUtils.clamp(health, 0, getMaxHealth());
			healthRegenTimer = 0;
		}
	}

	public boolean canMove(int direction) {
		boolean top = false, bottom = false;
		if (direction == LEFT) {
			top = !GameWorld.world.getChunkHandler().getBlockTypeAtPos(getPosition().x + pixelOffset, getPosition().y).isSolid();
			bottom = !GameWorld.world.getChunkHandler().getBlockTypeAtPos(getPosition().x + pixelOffset, getPosition().y + 1).isSolid();
		}
		else
			if (direction == RIGHT) {
				top = !GameWorld.world.getChunkHandler().getBlockTypeAtPos(getPosition().x + getSize().x - pixelOffset, getPosition().y).isSolid();
				bottom = !GameWorld.world.getChunkHandler().getBlockTypeAtPos(getPosition().x + getSize().x - pixelOffset, getPosition().y + 1).isSolid();
			}
		return top && bottom;
	}

	public void applyGravity(float deltaTime) {
		boolean canFall = canFall();
		boolean canRise = canRise();
		velocity.y -= GRAVITY_FORCE * deltaTime;
		velocity.y = MathUtils.clamp(velocity.y, -TERMINAL_VELOCITY, TERMINAL_VELOCITY);
		if (!canFall) {
			setPosition(getPosition().x, MathUtils.ceil(getPosition().y));
			setYVelocity(0);
			startOfFall = getPosition().y;
		}
		float vel = velocity.y * deltaTime;
		float direction = vel < 0 ? -1 : 1;
		while (vel != 0) {
			oldPosition.set(getPosition());
			float amountToChangeBy = Math.abs(vel) >= STANDARD_CHANGE ? (direction * STANDARD_CHANGE) : vel;
			if ((vel > 0 && canRise) || (vel < 0 && canFall)) {
				updateLocation(new Vector2(0, amountToChangeBy));
				if (getPosition().y > startOfFall)
					startOfFall = getPosition().y;
				canFall = canFall();
				canRise = canRise();
			}
			if (!canFall || !canRise) {
				vel = 0;
				if (!canFall) {
					setPosition(getPosition().x, MathUtils.ceil(getPosition().y));
					BlockType block = GameWorld.world.getChunkHandler().getBlockTypeAtPos(MathUtils.floor(getPosition().x), Math.round(getPosition().y) - 1);
					BlockType block2 = GameWorld.world.getChunkHandler().getBlockTypeAtPos(MathUtils.floor(getPosition().x + getSize().x), Math.round(getPosition().y) - 1);
					setYVelocity((-getVelocity().y) * Math.max(block.getRestitution(), block2.getRestitution()));
					if (Math.abs(getVelocity().y) < .01f) {
						float fallDamage = Math.abs(startOfFall - getPosition().y) - 4; // -4 for a 3 block fall because where the feet are and where
																						// the block under your feet are a 1 block difference
						if (fallDamage > 0) {
							this.startOfFall = getPosition().y;
							damage(fallDamage);
						}
					}
					if (!this.isLiving()) {
						this.velocity.x = 0;
					}
				}
				else {
					setPosition(getPosition().x, MathUtils.floor(getPosition().y + getSize().y) - getSize().y - 1);
					setYVelocity(0);
					// setPosition(oldPosition.x, oldPosition.y);
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

	public boolean isColliding(Vector2 coordinates, Vector2 size) {
		this.boundingBox.set(getPosition().x + pixelOffset, getPosition().y, getSize().x - pixelOffset * 2, getSize().y);
		System.out.println(coordinates);
		System.out.println(size);
		Rectangle block = new Rectangle(coordinates.x, coordinates.y, size.x, size.y);
		return block.overlaps(boundingBox);

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
		batch.rect(position.x + pixelOffset, position.y, width - pixelOffset * 2, height);
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public boolean canFall() {
		boolean left = !GameWorld.world.getChunkHandler().getBlockTypeAtPos(getPosition().x + pixelOffset, getPosition().y).isSolid();
		boolean right = !GameWorld.world.getChunkHandler().getBlockTypeAtPos(getPosition().x + getSize().x - pixelOffset, getPosition().y).isSolid();
		return left && right;
	}

	public boolean canRise() {
		boolean left = !GameWorld.world.getChunkHandler().getBlockTypeAtPos(getPosition().x + pixelOffset, getPosition().y + getSize().y).isSolid();
		boolean right = !GameWorld.world.getChunkHandler().getBlockTypeAtPos(getPosition().x + getSize().x - pixelOffset, getPosition().y + getSize().y).isSolid();
		return left && right;
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
		return boundingBox.set(getPosition().x, getPosition().y, getSize().x, getSize().y);
	}

	public void drawEntity(SpriteBatch renderer) {
		renderer.draw(getSprite(), getPosition().x, getPosition().y, getSize().x, getSize().y);
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
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean canJump() {
		return getVelocity().y == 0f && canRise();
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float max) {
		maxHealth = max;
	}

	public void setPixelOffset(float offset) {
		this.pixelOffset = offset;
	}

	public void resetFallHeight() {
		startOfFall = getPosition().y;
	}

	public Class<? extends Droppable> getClassName() {
		return className;
	}

	public boolean isLiving() {
		return living;
	}

	public void setLiving(boolean alive) {
		living = alive;
	}
}
