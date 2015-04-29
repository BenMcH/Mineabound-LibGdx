package com.tycoon177.mineabound.world.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.tycoon177.mineabound.utils.TexturePack;

public enum BlockType {
	AIR(0, "air", 0, false), 
	STONE(1, "stone"), 
	GRASS(2, "grass_side"), 
	DIRT(3, "dirt"), 
	BEDROCK(7, "bedrock"), 
//	LOG(8, "log"),
//	PLANK(9, "plank"),
//	WOOL(10, "wool"),
	IRON_ORE(11, "ore_iron"),
	GOLD_ORE(12, "ore_gold"),
	DIAMOND_ORE(13, "ore_diamond"),
	COAL_ORE(14, "ore_coal");
	//DOOR(64, "door");

	private int id;
	private String name;
	private float restitution;
	private boolean solid;

	private BlockType(int id, String texture) {
		this.id = id;
		this.restitution = 0;
		this.name = texture;	
		this.solid = true;
	}

	private BlockType(int id, String texture, float restitution) {
		this(id, texture);
		this.restitution = restitution;
	}

	private BlockType(int id, String texture, float restitution, boolean solid) {
		this(id, texture, restitution);
		this.solid = solid;
	}

	public TextureRegion getSprite() {
		return TexturePack.getTexture(name);
	}

	public int getBlockID() {
		return id;
	}

	public float getRestitution() {
		return restitution;
	}

	public boolean isSolid() {
		return solid;
	}

	public String getName() {
		return name;
	}

	public static BlockType getRandomType() {
		
		return BlockType.values()[MathUtils.random(BlockType.values().length-1)];
	}

}
