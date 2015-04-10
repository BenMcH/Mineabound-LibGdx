package com.tycoon177.mineabound.utils.input;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.tycoon177.mineabound.MineaboundLauncher;

public class KeyBindings {
	public static HashMap<String, Integer> keys = new HashMap<String, Integer>();
	public static Preferences bindingsPreferences = Gdx.app.getPreferences(MineaboundLauncher.preferencesDirectory + "keyBindings");
	static {
		loadBindings();
	}

	public static void loadBindings() {
		@SuppressWarnings("unchecked")
		Map<String, Integer> map = (Map<String, Integer>) bindingsPreferences.get();
		if (map.containsKey("right")) {
			keys.put("right", bindingsPreferences.getInteger("right"));
		}
		else
			keys.put("right", Keys.D);
		if (map.containsKey("left")) {
			keys.put("left", bindingsPreferences.getInteger("left"));
		}
		else
			keys.put("left", Keys.A);

		if (map.containsKey("jump")) {
			keys.put("jump", bindingsPreferences.getInteger("jump"));
		}
		else
			keys.put("jump", Keys.W);
		if (map.containsKey("drop")) {
			keys.put("drop", bindingsPreferences.getInteger("drop"));
		}
		else
			keys.put("drop", Keys.Q);
		if (map.containsKey("inventory")) {
			keys.put("inventory", bindingsPreferences.getInteger("inventory"));
		}
		else
			keys.put("inventory", Keys.E);
		if (map.containsKey("debug")) {
			keys.put("debug", bindingsPreferences.getInteger("debug"));
		}
		else
			keys.put("debug", Keys.F3);
	}

	public static int getBinding(String key) {
		loadBindings();
		if (keys == null)
			return -1;
		return keys.get(key);
	}

	public static void saveBindings() {
		bindingsPreferences.put(keys);
		bindingsPreferences.flush();
	}

	public static boolean setBinding(String key, int keycode) {
		for (int i : keys.values()) {
			if (i == keycode)
				return false;
		}
		keys.put(key, keycode);
		saveBindings();
		return true;
	}

}
