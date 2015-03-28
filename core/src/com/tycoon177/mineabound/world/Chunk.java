package com.tycoon177.mineabound.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.utils.PerlinNoiseGenerator;

public class Chunk {
	private static final PerlinNoiseGenerator caveNoiseGen = new PerlinNoiseGenerator(128);
	private static PerlinNoiseGenerator noiseMap = new PerlinNoiseGenerator(1);
	private Block[][] block;
	public static int WIDTH = 16, HEIGHT = 256;
	private int id;

	public Chunk(int id) {
		this.id = id;
		generateChunk();
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

	public int getID() {
		return id;
	}

	public void generateChunk() {

		int currentHeight = 50;
		float[] noise = generateNoise(id, noiseMap);

		block = new Block[WIDTH][HEIGHT];
		float[][] caveNoise = getCaveNoise(id, caveNoiseGen);
		if (id > 0)
			for (int i = 0; i < id; i++) {

				float[] noiseTemp = generateNoise(i, noiseMap);
				for (float dy : noiseTemp) {
					currentHeight += (dy);
					currentHeight = MathUtils.clamp(currentHeight, 0, HEIGHT - 1);
				}
			}
		else
			if (id < 0)
				for (int i = 0; i >= id; i--) {

					float[] noiseTemp = generateNoise(i, noiseMap);
					for (float dy : noiseTemp) {
						currentHeight += (dy);
						currentHeight = MathUtils.clamp(currentHeight, 0, HEIGHT - 1);
					}
				}

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT && y < currentHeight; y++) {
				block[x][y] = new Block(y < currentHeight ? BlockType.DIRT : BlockType.AIR, new Vector2(this.id * WIDTH + x, y));
				if(caveNoise[x][y] > .1f){
					block[x][y].setBlockType(BlockType.AIR);
				}else
				if (y == currentHeight - 1)
					block[x][y].setBlockType(BlockType.GRASS);
				
				// if (caveNoise[x][y] > 0)
				// block[x][y].setBlockType(BlockType.AIR);
			}

			currentHeight += (noise[x]);
			currentHeight = MathUtils.clamp(currentHeight, 0, HEIGHT - 1);
		}

	}

	private float[] generateNoise(int chunkId, PerlinNoiseGenerator noiseMap) {

		int offset = chunkId * WIDTH;
		if (chunkId < 0)
			offset += WIDTH;
		float[] noise = new float[WIDTH];
		// Frequency = features. Higher = more features
		float frequency = 0.3f;
		// Weight = smoothness. Higher frequency = more smoothness
		float weight = 3f;

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

	private float[][] getCaveNoise(int chunkId, PerlinNoiseGenerator noiseMap) {
		int offset = chunkId * WIDTH;
		if (chunkId < 0)
			offset += WIDTH;
		float[][] noise = new float[WIDTH][HEIGHT];
		// Frequency = features. Higher = more features
		float frequency = 0.125f;
		// Weight = smoothness. Higher frequency = more smoothness
		float weight = 1.5f;

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
		if(GameWorld.world.getPlayer().isColliding()) return false;
		if(block[x][y] != null){
			if(block[x][y].getBlockType() == BlockType.AIR){
				block[x][y].setBlockType(type);
			}else return false;
		}else
			block[x][y] = new Block(type, new Vector2(this.id * WIDTH + x, y));
		if(GameWorld.world.getPlayer().isColliding()){
			removeBlock(x, y);
			return false;
		}
		return true;
	}

}
