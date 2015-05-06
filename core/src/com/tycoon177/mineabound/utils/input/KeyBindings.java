package com.tycoon177.mineabound.utils.input;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.tycoon177.mineabound.MineaboundLauncher;

public class KeyBindings {
	public static HashMap<String, Integer> keyBindings = new HashMap<String, Integer>();
	public static Preferences bindingsPreferences = Gdx.app.getPreferences(MineaboundLauncher.preferencesDirectory + "keyBindings");

	/**
	 * Load the bindings in on creation
	 */
	static {
		loadBindings();
	}

	/**
	 * Load the controls in from the file or the default if it is not in the file.
	 */
	public static void loadBindings() {
		@SuppressWarnings("unchecked")
		Map<String, Integer> map = (Map<String, Integer>) bindingsPreferences.get();
		if (map.containsKey("right")) {
			keyBindings.put("right", bindingsPreferences.getInteger("right"));
		}
		else
			keyBindings.put("right", Keys.D);
		if (map.containsKey("left")) {
			keyBindings.put("left", bindingsPreferences.getInteger("left"));
		}
		else
			keyBindings.put("left", Keys.A);

		if (map.containsKey("jump")) {
			keyBindings.put("jump", bindingsPreferences.getInteger("jump"));
		}
		else
			keyBindings.put("jump", Keys.W);
		if (map.containsKey("drop")) {
			keyBindings.put("drop", bindingsPreferences.getInteger("drop"));
		}
		else
			keyBindings.put("drop", Keys.Q);
		if (map.containsKey("inventory")) {
			keyBindings.put("inventory", bindingsPreferences.getInteger("inventory"));
		}
		else
			keyBindings.put("inventory", Keys.E);
		if (map.containsKey("debug")) {
			keyBindings.put("debug", bindingsPreferences.getInteger("debug"));
		}
		else
			keyBindings.put("debug", Keys.F3);
		if (map.containsKey("sprint")) {
			keyBindings.put("sprint", bindingsPreferences.getInteger("sprint"));
		}
		else
			keyBindings.put("sprint", Keys.SHIFT_LEFT);
	}

	/**
	 * Returns the binding for the current button
	 * 
	 * @param key
	 * @return
	 */
	public static int getBinding(String key) {
		loadBindings();
		if (keyBindings == null)
			return -1;
		return keyBindings.get(key);
	}

	/**
	 * Saves the bindings to its file.
	 */
	public static void saveBindings() {
		bindingsPreferences.put(keyBindings);
		bindingsPreferences.flush();
	}

	/**
	 * Changes the current binding then saves them.
	 * 
	 * @param key
	 * @param keycode
	 * @return
	 */
	public static boolean setBinding(String key, int keycode) {
		for (int i : keyBindings.values()) {
			if (i == keycode)
				return false;
		}
		keyBindings.put(key, keycode);
		saveBindings();
		return true;
	}

}
