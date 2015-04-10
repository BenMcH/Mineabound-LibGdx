package com.tycoon177.mineabound.world.blocks;

import com.badlogic.gdx.math.Vector2;

public class BlockBottomDoor extends Block {
	public BlockBottomDoor(){
		this(new Vector2());
	}
	
	public BlockBottomDoor(Vector2 position){
		super(BlockType.DOOR, position);
		setSize(1,2);
	}

	@Override
	public void update(float deltaTime) {
		setSolid(isActive());
		if(isActive()){
			//Edit sprite
		}
	}
	
	
}
