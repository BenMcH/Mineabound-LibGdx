package com.tycoon177.mineabound.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.tycoon177.mineabound.MineaboundLauncher;
import com.tycoon177.mineabound.utils.TexturePack;
import com.tycoon177.mineabound.utils.input.KeyBindings;

public class MainMenu implements Screen {
	private Stage stage;
	private Table table;
	private VisTextButton play, options, exit;

	@Override
	public void show() {
		MineaboundLauncher.preferences = Gdx.app.getPreferences("mineabound_settings");
		KeyBindings.loadBindings();
		KeyBindings.saveBindings();
		TexturePack.init();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		VisUI.load();
		table = new Table();
		table.setFillParent(true);

		play = new VisTextButton("Play");
		play.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameWorld());
			}
		});
		options = new VisTextButton("Options");
		options.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new ControlsMenu());
			}
		});
		exit = new VisTextButton("Exit");
		exit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		table.add(play).pad(10).expand(true, false).fill(true, false).row();
		table.add(options).pad(10).expand(true, false).fill(true, false).row();
		table.add(exit).pad(10).expand(true, false).fill(true, false).row();
		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.15f, .15f, .15f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		stage.dispose();
		VisUI.dispose();
	}

}
