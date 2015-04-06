package com.tycoon177.mineabound.world.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {
	private BlockType type;
	private Vector2 position;
	private boolean isActive = false;
	private Vector2 size;
	
	public Block(BlockType type, Vector2 position) {
		this(type);
		this.position.set(position);
		this.size= new Vector2(1, 1);
	}

	public Block(BlockType type) {
		this();
		this.type = type;
	}

	public Block() {
		this.type = BlockType.AIR;
		this.position = new Vector2();
	}

	public void setBlockType(BlockType type) {
		this.type = type;
	}

	public BlockType getBlockType() {
		return type;
	}

	public void draw(SpriteBatch renderer) {
		if (type == BlockType.AIR) // Save Rendering time by not rendering air
			return;
		TextureRegion sprite = getSprite();
		if (sprite == null)
			return;
		renderer.draw(getSprite(), getPosition().x, getPosition().y, size.x, size.y);
	}

	private TextureRegion getSprite() {
		TextureRegion sprite = type.getSprite();
		return sprite;
	}

	public void destroyBlock() {
		this.type = BlockType.AIR;
	}

	public Vector2 getPosition() {
		return position;
	}

	public boolean isShown(Vector2 topLeft, Vector2 size) {
		Rectangle rect = new Rectangle(getPosition().x, getPosition().y, this.size.x, this.size.y);
		// Gets the Block as a rectangle
		Rectangle screen = new Rectangle(topLeft.x - size.x * 3, topLeft.y - size.y * 3, size.x + size.x * 6, size.y + size.y * 6);
		// Gets the screen coordinates plus about 3 blocks outside to assure that there are no rendering mistakes
		return rect.overlaps(screen);
	}

	public boolean collides(Rectangle player) {
		if (this.type == BlockType.AIR)
			return false;
		Rectangle block = new Rectangle(getPosition().x, getPosition().y, this.size.x, this.size.y);
		return block.overlaps(player);
	}

	public void debugDraw(ShapeRenderer debugRenderer) {
		if (type != BlockType.AIR)
			debugRenderer.rect(position.x, position.y, size.x, size.y);
	}
	
	public void setActive(boolean isActive){
		this.isActive = isActive;
	}
	
	public boolean getIsActive(){
		return isActive;
	}
	
	
	/**
	 * Used to apply updates to the blocks (Such as opening and closing gates and the like)
	 * @param deltaTime
	 */
	public void update(float deltaTime){}

	public Vector2 getSize() {
		return size;
	}

}
