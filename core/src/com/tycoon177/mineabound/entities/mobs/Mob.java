package com.tycoon177.mineabound.entities.mobs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.entities.Entity;

/**
 * 
 * The basis for the Mob framework. Will need to update to add basic AI and the like.
 *
 */
public abstract class Mob extends Entity {

	public Mob() {
		super();
	}

	public Mob(Vector2 vec2, Sprite sprite) {
		super(vec2, sprite);

	}

}
