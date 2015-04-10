package com.tycoon177.mineabound.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.world.blocks.Block;
import com.tycoon177.mineabound.world.blocks.BlockType;

public class ChunkHandler {
	public static HashMap<Integer, Chunk> chunks;
	public static Array<Block> visibleBlocks;

	public ChunkHandler() {
		chunks = new HashMap<Integer, Chunk>();
		addChunk(new Chunk(0));
		visibleBlocks = new Array<Block>();
	}

	public Chunk addChunk(Chunk chunk) {
		chunks.put(chunk.getID(), chunk);
		return chunk;
	}

	public Chunk getChunk(int key) {
		Chunk c = chunks.get(key);
		return c == null ? addChunk(new Chunk(key)) : c;
	}

	public void update(float deltaTime) {
		for (Chunk chunk : getVisibleChunks()) {
			chunk.update(deltaTime);
		}
	}

	public void render(SpriteBatch batch) {
		for (Block block : getVisibleBlocks()) {
			block.draw(batch);
		}
	}

	public Array<Block> getVisibleBlocks() {
		Array<Block> visibleBlocks = new Array<Block>();
		Array<Chunk> visibleChunks = getVisibleChunks();
		visibleChunks.shrink();
		OrthographicCamera cam = GameWorld.world.getCamera();
		Vector2 topLeft = new Vector2(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
		Vector2 size = new Vector2(cam.viewportWidth, cam.viewportHeight);
		for (Chunk c : visibleChunks) {
			for (Block[] b : c.getBlocks())
				for (Block block : b) {
					if (block != null)
						if (block.isShown(topLeft, size))
							visibleBlocks.add(block);
				}
		}
		return visibleBlocks;
	}

	private Array<Chunk> getVisibleChunks() {
		int chunkNum = (MathUtils.floor(GameWorld.world.getPlayer().getPosition().x)) / Chunk.WIDTH;
		if (GameWorld.player.getPosition().x < 0)
			chunkNum -= 1;
		Array<Chunk> visible = new Array<Chunk>();
		visible.add(getChunk(chunkNum));
		visible.add(getChunk(chunkNum - 1));
		visible.add(getChunk(chunkNum + 1));
		return visible;
	}

	public void removeBlock(Vector3 touchLocation) {
		float xClickLocation = touchLocation.x;
		int chunkIndex = (int) (xClickLocation / Chunk.WIDTH);
		if (xClickLocation < 0)
			chunkIndex -= 1; // Negative x values start in chunk -1 but the integer division would return 0
		int x = (int) xClickLocation % Chunk.WIDTH;
		if (xClickLocation < 0) {
			x = Chunk.WIDTH - 1 + x; // If the location is negative, bring it to the corresponding positive location in the chunk.
		}
		getChunk(chunkIndex).removeBlock(x, MathUtils.floor(touchLocation.y));
	}

	public void removeBlock(Vector2 touchLocation) {
		removeBlock(new Vector3(touchLocation.x, touchLocation.y, 0));
	}

	public void removeAllChunksFromWorld() {

	}

	public void debugRender(ShapeRenderer debugRenderer) {
		for (Block b : getVisibleBlocks()) {
			b.debugDraw(debugRenderer);
		}
	}

	public void addBlock(Vector3 touchLocation, BlockType type) {
		float xClickLocation = touchLocation.x;
		int chunkIndex = (int) (xClickLocation / Chunk.WIDTH);
		if (xClickLocation < 0)
			chunkIndex -= 1; // Negative x values start in chunk -1 but the integer division would return 0
		int x = (int) xClickLocation % Chunk.WIDTH;
		if (xClickLocation < 0) {
			x = Chunk.WIDTH - 1 + x; // If the location is negative, bring it to the corresponding positive location in the chunk.
		}
		getChunk(chunkIndex).addBlock(x, MathUtils.floor(touchLocation.y), type);
	}
	
	public void addBlock(Vector3 touchLocation, Block block){
		float xClickLocation = touchLocation.x;
		int chunkIndex = (int) (xClickLocation / Chunk.WIDTH);
		if (xClickLocation < 0)
			chunkIndex -= 1; // Negative x values start in chunk -1 but the integer division would return 0
		int x = (int) xClickLocation % Chunk.WIDTH;
		if (xClickLocation < 0) {
			x = Chunk.WIDTH - 1 + x; // If the location is negative, bring it to the corresponding positive location in the chunk.
		}
		getChunk(chunkIndex).addBlock(x, MathUtils.floor(touchLocation.y), block);
	}

	public BlockType getBlockTypeAtPos(float x, float y) {
		int chunkNum = (int) (x) / Chunk.WIDTH;
		// System.out.println(chunkNum);
		if (x < 0)
			chunkNum -= 1;
		int nX = (MathUtils.floor(x) % Chunk.WIDTH);
		if (nX < 0) {
			nX = (Chunk.WIDTH + nX);
		}
		int nY = MathUtils.floor(y) % Chunk.HEIGHT;
		return getChunk(chunkNum).getBlocks()[nX][nY].getBlockType();
	}
}
