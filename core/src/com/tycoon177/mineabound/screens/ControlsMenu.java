package com.tycoon177.mineabound.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.tycoon177.mineabound.utils.input.KeyBindings;

public class ControlsMenu implements Screen {
	private Stage stage;
	private Table table;
	private String changing;
	private InputListener inputListener;
	@Override
	public void show() {
		ClickListener listener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changing = (String) ((Label) event.getTarget()).getText().toString().toLowerCase().split(" ")[0];
			}
		};
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		VisUI.load();
		table = new Table();
		table.setFillParent(true);
		VisLabel leftLabel = new VisLabel("Left");
		VisLabel rightLabel = new VisLabel("Right");
		VisLabel jumpLabel = new VisLabel("Jump");
		VisLabel dropLabel = new VisLabel("Drop");
		VisLabel inventoryLabel = new VisLabel("Open Inventory");
		VisLabel debugLabel = new VisLabel("Debug Rendering");
		final VisTextButton left = new VisTextButton(Keys.toString(KeyBindings.getBinding("left")));
		left.addListener(listener);
		final VisTextButton right = new VisTextButton(Keys.toString(KeyBindings.getBinding("right")));
		right.addListener(listener);
		final VisTextButton jump = new VisTextButton(Keys.toString(KeyBindings.getBinding("jump")));
		jump.addListener(listener);
		final VisTextButton drop = new VisTextButton(Keys.toString(KeyBindings.getBinding("drop")));
		drop.addListener(listener);
		final VisTextButton inventory = new VisTextButton(Keys.toString(KeyBindings.getBinding("inventory")));
		inventory.addListener(listener);
		final VisTextButton debug = new VisTextButton(Keys.toString(KeyBindings.getBinding("debug")));
		debug.addListener(listener);
		table.add(leftLabel).expandX().pad(10);
		table.add(left).fillX().expandX().pad(10).colspan(2).row();
		table.add(rightLabel).expandX().pad(10);
		table.add(right).fillX().expandX().pad(10).colspan(2).row();
		table.add(jumpLabel).expandX().pad(10);
		table.add(jump).fillX().expandX().pad(10).colspan(2).row();
		table.add(dropLabel).expandX().pad(10);
		table.add(drop).fillX().expandX().pad(10).colspan(2).row();
		table.add(inventoryLabel).expandX().pad(10);
		table.add(inventory).fillX().expandX().pad(10).colspan(2).row();
		table.add(debugLabel).expandX().pad(10);
		table.add(debug).fillX().expandX().pad(10).colspan(2).row();
		stage.addActor(table);
		inputListener = new InputListener() {

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.ESCAPE) {
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
						}
					});
				}

				if (changing == null)
					return false;
				if (changing.equalsIgnoreCase(left.getText().toString())) {
					if (KeyBindings.setBinding("left", keycode))
						left.setText(Keys.toString(keycode));
				}
				if (changing.equalsIgnoreCase(right.getText().toString())) {
					if (KeyBindings.setBinding("right", keycode))
						right.setText(Keys.toString(keycode));
				}
				if (changing.equalsIgnoreCase(jump.getText().toString())) {
					if (KeyBindings.setBinding("jump", keycode))
						jump.setText(Keys.toString(keycode));
				}
				if (changing.equalsIgnoreCase(drop.getText().toString())) {
					if (KeyBindings.setBinding("drop", keycode))
						drop.setText(Keys.toString(keycode));
				}
				if (changing.equalsIgnoreCase(inventory.getText().toString())) {
					if (KeyBindings.setBinding("inventory", keycode))
						inventory.setText(Keys.toString(keycode));
				}
				if (changing.equalsIgnoreCase(debug.getText().toString())) {
					if (KeyBindings.setBinding("debug", keycode))
						debug.setText(Keys.toString(keycode));
				}
				changing = null;
				KeyBindings.saveBindings();
				return true;
			}

		};
		stage.addListener(inputListener);
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
		stage.removeListener(inputListener);
		VisUI.dispose();
		stage.dispose();
	}

}
