package com.tycoon177.mineabound.utils.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.world.Chunk;

public class CellularAutomata {
	private Array<Integer> stayAlive;
	private Array<Integer> comeAlive;

	private boolean[] board;
	private int width = Chunk.WIDTH;
	private int height = Chunk.HEIGHT;
	private float chance = .8f;

	/**
	 * Creates a "board" for cellular automation with conways game of life type systems
	 * 
	 * @param width
	 * @param height
	 */
	public CellularAutomata(int width, int height) {
		this.width = width;
		this.height = height;
		board = new boolean[width * height];
		setDefaultRules();
		randomBoard();
	}

	/**
	 * Creates the board with the size being that of a chunk.
	 */
	public CellularAutomata() {
		this(Chunk.WIDTH, Chunk.HEIGHT);
	}

	/**
	 * Resets every value to false
	 */
	public void resetBoard() {
		if (board == null) {
			board = new boolean[width * height];
		}
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				board[y * width + x] = false;

			}
	}

	/**
	 * Generates a random board for better chunk generation
	 */
	private void randomBoard() {
		resetBoard();
		for (int i = 0; i < board.length; i++) {
			board[i] = MathUtils.randomBoolean(chance);
		}
		step();
	}

	/**
	 * Returns the number of neighbors that are alive next to the current cell
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getNeighbors(int x, int y) {
		int neighbors = 0;
		int locInArr = y * width + x;
		if (locInArr < board.length && locInArr >= 0) {
			// left
			neighbors += getValue(x - 1, y - 1);
			neighbors += getValue(x - 1, y);
			neighbors += getValue(x - 1, y + 1);
			// top and bottom
			neighbors += getValue(x, y - 1);
			neighbors += getValue(x, y + 1);
			// right
			neighbors += getValue(x + 1, y - 1);
			neighbors += getValue(x + 1, y);
			neighbors += getValue(x + 1, y + 1);
		}
		return neighbors;
	}

	/**
	 * Returns 0 if it is "dead" or 1 if true. This is used to count neighbors easier
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int getValue(int x, int y) {
		int locInArr = y * width + x;
		if (locInArr < board.length && locInArr >= 0) {
			return board[locInArr] ? 1 : 0;
		}
		return 0;
	}

	/**
	 * returns whether or not the current cell is "alive"
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isAlive(int x, int y) {
		return getValue(x, y) == 1;
	}

	/**
	 * Does one iteration of the cellular automata
	 */
	private void step() {

		boolean[] newBoard = new boolean[board.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int neighbors = getNeighbors(x, y);
				if (isAlive(x, y)) {
					if (stayAlive.contains(neighbors, false))
						newBoard[y * width + x] = (true);
				}
				else
					if (comeAlive.contains(neighbors, false)) {
						newBoard[y * width + x] = (true);
					}
			}
		}
		board = newBoard;
	}

	/**
	 * Sets the amount of neighbors that will keep the cells alive
	 * 
	 * @param a
	 */
	public void setStayAliveRules(int... a) {
		stayAlive = new Array<Integer>();
		for (int i : a)
			stayAlive.add(i);
	}

	/**
	 * Sets the amount of neighbors that will bring the cells to life
	 * 
	 * @param a
	 */
	public void setComeAliveRules(int... a) {
		comeAlive = new Array<Integer>();
		for (int i : a)
			comeAlive.add(i);
	}

	/**
	 * Sets the rules back to default.
	 */
	public void setDefaultRules() {
		setStayAliveRules(7, 6, 5, 4);
		setComeAliveRules(1, 2, 3, 4, 5, 6, 7, 8);
	}
}
