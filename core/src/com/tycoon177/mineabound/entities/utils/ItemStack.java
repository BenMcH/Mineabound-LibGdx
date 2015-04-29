package com.tycoon177.mineabound.entities.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.entities.Entity;
import com.tycoon177.mineabound.utils.interfaces.Droppable;

public class ItemStack extends Droppable {
	public static final int STACK_SIZE = 64;
	private Entity entity;
	private int sizeOfStack;

	public ItemStack(Entity e) {
		this();
		sizeOfStack = 1;
		entity = e;
	}

	/**
	 * Create it with a default of 0
	 */
	public ItemStack() {
		sizeOfStack = 0;
	}

	public int getSize() {
		return sizeOfStack;
	}

	public boolean addEntity(Entity e) {
		if(entity == null){
			this.entity = e;
		}
		if (sizeOfStack < STACK_SIZE) {
			if (e.getClassName().equals(this.getType())) {
				sizeOfStack++;
				return true;
			}
		}
		return false;
	}

	public Entity pop() {
		boolean isZero = sizeOfStack == 0;
		if (sizeOfStack > 0) {
			sizeOfStack--;
		}
		if (sizeOfStack == 0) {
			entity = null;
		}
		if (!isZero) {
			return new Entity(new Vector2(), entity);
		}
		return null;
	}

	public Entity peek() {
		if (entity != null)
			return new Entity(new Vector2(), entity);
		return null;
	}

	public Class<? extends Droppable> getType() {
		if(entity == null) return null;
		return entity.getClassName();
	}

	@Override
	public Sprite getSprite() {
		return entity.getSprite();
	}

}
