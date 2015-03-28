package com.tycoon177.mineabound.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {
	private BlockType type;
	private float width = 1, height = 1;
	private Vector2 position;

	public Block(BlockType type, Vector2 position) {
		this(type);
		this.position.set(position);
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
		renderer.draw(getSprite(), getPosition().x, getPosition().y, width, height);
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
		Rectangle rect = new Rectangle(getPosition().x, getPosition().y, this.width, this.height);
		// Gets the Block as a rectangle
		Rectangle screen = new Rectangle(topLeft.x - width * 3, topLeft.y - height * 3, size.x + width * 6, size.y + height * 6);
		// Gets the screen coordinates plus about 3 blocks outside to assure that there are no rendering mistakes
		return rect.overlaps(screen);
	}

	public boolean collides(Rectangle player) {
		if (this.type == BlockType.AIR)
			return false;
		Rectangle block = new Rectangle(getPosition().x, getPosition().y, this.width, this.height);
		return block.overlaps(player);
	}

	public void debugDraw(ShapeRenderer debugRenderer) {
		if (type != BlockType.AIR)
			debugRenderer.rect(position.x, position.y, width, height);
	}

}
