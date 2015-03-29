package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.utils.LoadedTextureAtlas;

public class Player extends LivingEntity {
	/** The default player size **/
	private Vector2 playerSize = new Vector2(.6f, 1.8f);
	
	public Player() {
		super();
		setSize(playerSize.x, playerSize.y);
		this.setSprite(LoadedTextureAtlas.blockAtlas.createSprite("playerStill"));
	}
}
