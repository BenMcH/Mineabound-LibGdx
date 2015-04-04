package com.tycoon177.mineabound.world.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tycoon177.mineabound.utils.TexturePack;

public enum BlockType {
	AIR(0, "air"),
	STONE(1,"stone", .2f),
	GRASS(2,"grass_side"),
	DIRT(3, "dirt"), 
	BEDROCK(7, "bedrock");

	private int id;
	private String name;
	private float bounciness;
	
	private BlockType(int id, String texture){
		this.id = id;
		this.bounciness = 0;
		this.name = texture;
	}

	private BlockType(int id, String texture, float bounciness){
		this(id, texture);
		this.bounciness = bounciness;
	}
	
	public TextureRegion getSprite(){
		return TexturePack.getTexture(name);
	}
	
	public int getBlockID(){
		return id;
	}
	
	public float getBounciness(){
		return bounciness;
	}
	
}
