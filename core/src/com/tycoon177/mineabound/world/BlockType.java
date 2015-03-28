package com.tycoon177.mineabound.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tycoon177.mineabound.utils.LoadedTextureAtlas;

public enum BlockType {
	AIR(0, "air"),
	STONE(1,"stone"),
	GRASS(2,"grass"),
	DIRT(3, "dirt"), 
	BEDROCK(7, "bedrock");

	private TextureRegion sprite;
	private int id;
	private BlockType(int id, String texture){
		this.id = id;
		sprite = LoadedTextureAtlas.blockAtlas.createSprite(texture);
	}
	
	public TextureRegion getSprite(){
		return sprite;
	}
	
	public int getBlockID(){
		return id;
	}
}
