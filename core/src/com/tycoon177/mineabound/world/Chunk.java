package com.tycoon177.mineabound.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.utils.world.CellularAutomata;
import com.tycoon177.mineabound.utils.world.PerlinNoiseGenerator;
import com.tycoon177.mineabound.world.blocks.Block;
import com.tycoon177.mineabound.world.blocks.BlockType;

/**
 * This class is used by the ChunkHandler class to manage the blocks in smaller "Chunks"
 * 
 * @author Ben
 *
 */
public class Chunk {
	private static final PerlinNoiseGenerator caveNoiseGen = new PerlinNoiseGenerator();
	private static PerlinNoiseGenerator heightMap = new PerlinNoiseGenerator();
	private static PerlinNoiseGenerator bedrockNoise = new PerlinNoiseGenerator();
	private Block[][] block;
	public static int WIDTH = 16, HEIGHT = 256;
	private int id;

	// Frequency = features. Higher = more features
	// Weight = smoothness. Higher frequency = more smoothness
	private static final float HEIGHT_FREQ = 0.3f;
	private static final float HEIGHT_WEIGHT = 3f;
	// private static final float CAVE_FREQ = 0.45f;
	// private static final float CAVE_WEIGHT = 2f;
	private static final float BEDROCK_FREQ = 0.3f;
	private static final float BEDROCK_WEIGHT = 1f;

	public Chunk(int id) {
		this.id = id;
		generateChunk();
	}

	/*
	 * 
	 * 
	 * 
	 * Public Helper methods
	 */

	public int getID() {
		return id;
	}

	public void render(SpriteBatch renderer) {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (block[i][j] != null && block[i][j].getBlockType() != BlockType.AIR) {
					OrthographicCamera cam = GameWorld.world.getCamera();
					Vector2 topLeft = new Vector2(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
					Vector2 size = new Vector2(cam.viewportWidth, cam.viewportHeight);
					if (block[i][j].isShown(topLeft, size))
						block[i][j].draw(renderer);
				}
			}
		}
	}

	public Block[][] getBlocks() {
		return block;
	}

	public void removeBlock(int x, int y) {
		if (block[x][y] != null)
			block[x][y].setBlockType(BlockType.AIR);
		else
			block[x][y] = new Block(BlockType.AIR, new Vector2(this.id * WIDTH + x, y));
	}

	public boolean addBlock(int x, int y, BlockType type) {
		if (block[x][y] != null) {
			if (block[x][y].getBlockType() == BlockType.AIR) {
				block[x][y].setBlockType(type);
			}
			else
				return false;
		}
		else
			block[x][y] = new Block(type, new Vector2(this.id * WIDTH + x, y));
		if (GameWorld.world.getPlayer().isColliding(block[x][y].getPosition(), block[x][y].getSize())) {
			removeBlock(x, y);
			return false;
		}
		return true;
	}

	public void update(float deltaTime) {
		for (int i = 0; i < block.length; i++) {
			for (int j = 0; j < block[0].length; j++) {
				if (block[i][j] == null) {
					block[i][j] = new Block(BlockType.AIR, new Vector2(this.id * WIDTH + i, j));
				}
				block[i][j].update(deltaTime);
			}
		}
	}

	public int getCurrentHeight(int chunkID, PerlinNoiseGenerator generator) {
		return getCurrentHeight(chunkID, generator, 1, HEIGHT - 1, 50, HEIGHT_FREQ, HEIGHT_WEIGHT);
	}

	public int getCurrentHeight(int chunkID, PerlinNoiseGenerator generator, int minHeight, int maxHeight, int startingHeight, float frequency, float weight) {
		int currentHeight = startingHeight;
		if (id > 0)
			for (int i = 0; i < id; i++) {

				float[] noiseTemp = generate1DNoise(i, generator, frequency, weight);
				for (float dy : noiseTemp) {
					currentHeight += (dy);
					currentHeight = MathUtils.clamp(currentHeight, minHeight, maxHeight);
				}
			}
		else
			if (id < 0)
				for (int i = 0; i >= id; i--) {

					float[] noiseTemp = generate1DNoise(i, generator, frequency, weight);
					for (float dy : noiseTemp) {
						currentHeight += (dy);
						currentHeight = MathUtils.clamp(currentHeight, minHeight, maxHeight);
					}
				}
		return currentHeight;
	}

	/*
	 * 
	 * Private Methods
	 */

	private void generateChunk() {
		CellularAutomata cells = new CellularAutomata();
		int chunkHeight = getCurrentHeight(id, heightMap);
		int bedrockHeightMap = getCurrentHeight(id, bedrockNoise, 1, 3, 1, BEDROCK_FREQ, BEDROCK_WEIGHT);
		float[] heightNoise = generate1DNoise(id, heightMap, HEIGHT_FREQ, HEIGHT_WEIGHT);
		float[] bedrockNoiseMap = generate1DNoise(id, bedrockNoise, BEDROCK_FREQ, BEDROCK_WEIGHT);
		block = new Block[WIDTH][HEIGHT];
		// float[][] caveNoise = generate2DNoise(id, caveNoiseGen, CAVE_FREQ, CAVE_WEIGHT);
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				BlockType type = BlockType.AIR;
				if (y < chunkHeight)
					type = BlockType.DIRT;
				if (y <= bedrockHeightMap)
					type = BlockType.BEDROCK;
				block[x][y] = new Block(type, new Vector2(this.id * WIDTH + x, y));
				if (cells.getValue(x, y) == 0) {
					block[x][y].setBlockType(BlockType.AIR);
				}
				else
					if (y == chunkHeight - 1)
						block[x][y].setBlockType(BlockType.GRASS);
			}

			chunkHeight += (heightNoise[x]);
			bedrockHeightMap += bedrockNoiseMap[x];
			chunkHeight = MathUtils.clamp(chunkHeight, 0, HEIGHT - 1);
		}

	}

	private float[] generate1DNoise(int chunkId, PerlinNoiseGenerator noiseMap, float frequency, float weight) {

		int offset = chunkId * WIDTH;
		if (chunkId < 0)
			offset += WIDTH;
		float[] noise = new float[WIDTH];

		for (int passes = 0; passes < 6; passes++) {
			for (int x = 0; x < WIDTH; x++) {
				noise[x] += MathUtils.round((float) noiseMap.improvedNoise(offset + x * frequency, 0, 0) * weight);
				noise[x] = MathUtils.clamp(noise[x], -1.0f, 1.0f);
			}
			frequency *= 1.5f;
			weight *= 0.25f;
		}

		return noise;
	}

	private float[][] generate2DNoise(int chunkId, PerlinNoiseGenerator noiseMap, float frequency, float weight) {
		int offset = chunkId * WIDTH;
		if (chunkId < 0)
			offset += WIDTH;
		float[][] noise = new float[WIDTH][HEIGHT];
		// Frequency = features. Higher = more features
		// Weight = smoothness. Higher frequency = more smoothness

		for (int passes = 0; passes < 6; passes++) {
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					noise[x][y] += MathUtils.round((float) caveNoiseGen.improvedNoise(offset + x * frequency, y, 0) * weight);
					noise[x][y] = MathUtils.clamp(noise[x][y], -1.0f, 1.0f);
				}
			}
			frequency *= 1.5f;
			weight *= 0.25f;
		}

		return noise;

	}

	public boolean addBlock(int x, int y, Block nBlock) {
		if (nBlock == null)
			return false;
		nBlock.setLocation(x, y);

		if (block[x][y] == null || block[x][y].getBlockType() == BlockType.AIR) {
			block[x][y] = nBlock;
		}
		if (block[x][y] != null)
			if (GameWorld.world.getPlayer().isColliding(block[x][y].getPosition(), block[x][y].getSize())) {
				removeBlock(x, y);
				return false;
			}
		return true;
	}

}
