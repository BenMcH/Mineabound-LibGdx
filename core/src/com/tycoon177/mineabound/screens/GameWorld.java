package com.tycoon177.mineabound.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.tycoon177.mineabound.MineaboundLauncher;
import com.tycoon177.mineabound.entities.EntityHandler;
import com.tycoon177.mineabound.entities.Player;
import com.tycoon177.mineabound.utils.MineaboundInputProcessor;
import com.tycoon177.mineabound.world.ChunkHandler;

public class GameWorld implements Screen {
	private static OrthographicCamera camera;
	private SpriteBatch renderer;
	private ShapeRenderer debugRenderer;
	public static Player player;
	private ChunkHandler chunkHandler;
	private MineaboundInputProcessor inputProcessor;
	public static GameWorld world;
	public static final float VIEWPORT_SIZE = 20;
	private Music song;
	private EntityHandler entityHandler = new EntityHandler();
	
	@Override
	public void show() {

		// song = Gdx.audio.newMusic(Gdx.files.internal("atazir.mp3"));
		if (song != null) {
			song.setVolume(.1f);
			song.setLooping(true);
			song.play();
		}

		GameWorld.world = this;
		renderer = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10, 10);
		player = new Player();
		player.setPosition(0, 100);
		createInputProcessor();
		chunkHandler = new ChunkHandler();
		debugRenderer = new ShapeRenderer();
	}

	private void createInputProcessor() {
		Gdx.input.setInputProcessor((inputProcessor = new MineaboundInputProcessor(this)));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.2f, .2f, .75f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin();
		float color = .1f;
		chunkHandler.render(renderer);
		entityHandler.render(renderer);
		player.draw(renderer, player.getDirection());
		renderer.end();
		if (MineaboundLauncher.IS_DEBUG_RENDERING) {
			debugRenderer.setProjectionMatrix(camera.combined);
			// Gdx.gl.glLineWidth(2f);
			debugRenderer.begin(ShapeType.Line);
			debugRenderer.setColor(MineaboundLauncher.PLAYER_BOUNDING_BOX_COLOR);
			player.debugRender(debugRenderer);
			debugRenderer.setColor(MineaboundLauncher.BLOCK_BOUNDING_BOX_COLOR);
			chunkHandler.debugRender(debugRenderer);
			debugRenderer.end();
		}
		renderer.begin();
		player.drawHUD(renderer);
		renderer.end();
		update(delta);
		Gdx.graphics.setTitle(Gdx.graphics.getFramesPerSecond() + "");
	}

	private void update(float delta) {
		player.update(delta);
		entityHandler.update(delta);
		camera.position.set(player.getPosition(), 0);
		chunkHandler.update(delta);
		cameraZoom();
		getInputProcessor().update();
	}

	private void cameraZoom() {
		if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)) {
			camera.zoom -= 0.02;
		}
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / camera.viewportWidth);

	}

	@Override
	public void resize(int width, int height) {
		// 80x80 = 1x1
		camera.viewportWidth = VIEWPORT_SIZE;
		camera.viewportHeight = VIEWPORT_SIZE * height / width;

	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		renderer.dispose();
		if (song != null) {
			song.stop();
			song.dispose();
		}
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Player getPlayer() {
		return player;
	}

	public MineaboundInputProcessor getInputProcessor() {
		return inputProcessor;
	}

	public ChunkHandler getChunkHandler() {
		return chunkHandler;
	}

	public EntityHandler getEntityHandler() {
		return entityHandler;
	}
}
