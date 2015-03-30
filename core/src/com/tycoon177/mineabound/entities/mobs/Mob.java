package com.tycoon177.mineabound.entities.mobs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.entities.LivingEntity;

/**
 * 
 * The basis for the Mob framework. Will need to update to add basic AI and the like.
 *
 */
public class Mob extends LivingEntity {

	public Mob() {
		super();
	}

	public Mob(Vector2 vec2, Sprite sprite) {
		super(vec2, sprite);

	}

}
