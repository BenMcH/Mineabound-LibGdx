package com.tycoon177.mineabound.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tycoon177.mineabound.utils.LoadedTextureAtlas;

public enum BlockType {
	AIR(0, "air"),
	STONE(1,"stone", .2f),
	GRASS(2,"grass"),
	DIRT(3, "dirt"), 
	BEDROCK(7, "bedrock");

	private int id;
	private TextureRegion sprite;
	private float bounciness;
	
	private BlockType(int id, String texture){
		this.id = id;
		sprite = LoadedTextureAtlas.blockAtlas.createSprite(texture);
		this.bounciness = 0;
	}

	private BlockType(int id, String texture, float bounciness){
		this.id = id;
		sprite = LoadedTextureAtlas.blockAtlas.createSprite(texture);
		this.bounciness = 0;
	}
	
	public TextureRegion getSprite(){
		return sprite;
	}
	
	public int getBlockID(){
		return id;
	}
	
	public float getBounciness(){
		return bounciness;
	}
}
