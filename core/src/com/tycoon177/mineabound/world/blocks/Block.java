package com.tycoon177.mineabound.world.blocks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.entities.Entity;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.utils.TexturePack;
import com.tycoon177.mineabound.utils.interfaces.Droppable;

public class Block extends Droppable {
	// Properties
	private Vector2 position;
	private Vector2 size;
	private boolean isActive = false;
	private boolean isSolid;
	private float restitution;
	// Identifiers
	private int id;
	private String name;
	private BlockType type;
	private Class<? extends Droppable> className;

	/**
	 * Explicitly define a type and a position
	 * 
	 * @param type
	 * @param position
	 */
	public Block(BlockType type, Vector2 position) {
		this(type);
		this.position.set(position);
		this.size = new Vector2(1, 1);
	}

	/**
	 * When only provided a position, assume air.
	 * 
	 * @param position
	 */
	public Block(Vector2 position) {
		this(BlockType.AIR);
		this.position.set(position);
	}

	/**
	 * Set a block of a given type at (0,0)
	 * 
	 * @param type
	 */
	public Block(BlockType type) {
		this();
		setBlockType(type);
	}

	/**
	 * Creates the default block of air at (0,0)
	 */
	public Block() {
		this.position = new Vector2();
		setBlockType(BlockType.AIR);
		this.size = new Vector2(1, 1);
		className = getClass();
	}

	public void setBlockType(BlockType type) {
		this.type = type;
		this.isSolid = type.isSolid();
		this.name = type.getName();
		this.id = type.getBlockID();
		this.restitution = type.getRestitution();
	}

	public BlockType getBlockType() {
		return type;
	}

	public void draw(SpriteBatch renderer) {
		if (name.equalsIgnoreCase("air")) // Save Rendering time by not rendering air
			return;
		TextureRegion sprite = getSprite();
		if (sprite == null)
			return;
		renderer.draw(getSprite(), getPosition().x, getPosition().y, size.x, size.y);
	}

	public void destroyBlock() {
		setBlockType(BlockType.AIR);
	}

	public Vector2 getPosition() {
		return position;
	}

	public boolean isShown(Vector2 topLeft, Vector2 size) {
		Rectangle rect = new Rectangle(getPosition().x, getPosition().y, this.size.x, this.size.y);
		// Gets the Block as a rectangle
		Rectangle screen = new Rectangle(topLeft.x - getSize().x * 3, topLeft.y - getSize().y * 3, size.x + getSize().x * 6, size.y + getSize().y * 6);
		// Gets the screen coordinates plus about 3 blocks outside to assure that there are no rendering mistakes
		return rect.overlaps(screen);
	}

	public boolean collides(Rectangle player) {
		if (type == BlockType.AIR) {
			return false;
		}
		Rectangle block = new Rectangle(getPosition().x, getPosition().y, this.size.x, this.size.y);
		return block.overlaps(player);
	}

	public void debugDraw(ShapeRenderer debugRenderer) {
		if (type != BlockType.AIR)
			debugRenderer.rect(position.x, position.y, size.x, size.y);
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	/**
	 * Used to apply updates to the blocks (Such as opening and closing gates and the like)
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(float width, float height) {
		size.set(width, height);
	}

	public void setSize(Vector2 size) {
		setSize(size.x, size.y);
	}

	public boolean isSolid() {
		return isSolid;
	}

	public void setSolid(boolean solid) {
		isSolid = solid;
	}

	public void setLocation(int x, int y) {
		this.position.set(x, y);
	}

	@Override
	public Sprite getSprite() {
		return TexturePack.getTexture(name);
	}

	public int getBlockID() {
		return id;
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float bounciness) {
		this.restitution = bounciness;
	}

	public void setClass(Class<? extends Block> classN) {
		className = classN;
	}

	public Class<? extends Droppable> getClassName() {
		return className;
	}

	public void setAdditionalAtributes(Block nBlock) {
		setBlockType(nBlock.getBlockType());
		// Must be overridden in subclasses
	}

	public void onDestroy() {
		GameWorld.world.getEntityHandler().addEntity(new Entity(getCenterPosition(), this));
	}

	private Vector2 getCenterPosition() {
		Vector2 pos = getPosition();
		pos.x += getSize().x / 4;
		pos.y += getSize().y / 4;
		return pos;
	}

}
