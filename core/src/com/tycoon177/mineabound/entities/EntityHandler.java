package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.world.blocks.BlockType;

public class EntityHandler {
	public static Array<Entity> entitys;

	public EntityHandler() {
		entitys = new Array<Entity>();
	}

	public Entity addEntity(Entity Entity) {
		entitys.add(Entity);
		return Entity;
	}

	public Entity getEntity(int index) {
		Entity c = entitys.get(index);
		return c;
	}

	public void update(float deltaTime) {
		for (Entity Entity : getVisibleEntitys()) {
			Entity.update(deltaTime);
		}
	}

	public void render(SpriteBatch batch) {
		for (Entity entity : getVisibleEntities()) {
			batch.draw(entity.getSprite(), entity.getPosition().x, entity.getPosition().y, Entity.WIDTH, Entity.HEIGHT);
		}
	}

	public Array<Entity> getVisibleEntities() {
		Array<Entity> visibleBlocks = new Array<Entity>();
		OrthographicCamera cam = GameWorld.world.getCamera();
		Vector2 topLeft = new Vector2(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
		Vector2 size = new Vector2(cam.viewportWidth, cam.viewportHeight);
		Rectangle r = new Rectangle(topLeft.x, topLeft.y, size.x, size.y);
		for (Entity ent : entitys) {
			if(ent.getHitBox().overlaps(r)) visibleBlocks.add(ent);
		}
		return visibleBlocks;
	}

	private Array<Entity> getVisibleEntitys() {
		int entityNum = 0;//(MathUtils.floor(GameWorld.world.getPlayer().getPosition().x)) / Entity.WIDTH;
		// System.out.println(EntityNum);
		if (GameWorld.player.getPosition().x < 0)
			entityNum -= 1;
		Array<Entity> visible = new Array<Entity>();
		visible.add(getEntity(entityNum));
		visible.add(getEntity(entityNum - 1));
		visible.add(getEntity(entityNum + 1));
		return visible;
	}

	/*public void removeBlock(Vector3 touchLocation) {
		float xClickLocation = touchLocation.x;
		int EntityIndex = (int) (xClickLocation / Entity.WIDTH);
		if (xClickLocation < 0)
			EntityIndex -= 1; // Negative x values start in Entity -1 but the integer division would return 0
		int x = (int) xClickLocation % Entity.WIDTH;
		if (xClickLocation < 0) {
			x = Entity.WIDTH - 1 + x; // If the location is negative, bring it to the corresponding positive location in the Entity.
		}
		getEntity(EntityIndex).removeBlock(x, MathUtils.floor(touchLocation.y));
	}*/

	public void removeBlock(Vector2 touchLocation) {
		//removeBlock(new Vector3(touchLocation.x, touchLocation.y, 0));
	}

	public void removeAllEntitysFromWorld() {

	}

	public void debugRender(ShapeRenderer debugRenderer) {
		for (Entity b : getVisibleEntities()) {
			//b.debugDraw(debugRenderer);
		}
	}

	public void addEntity(Vector3 touchLocation, BlockType type) {
		float xClickLocation = touchLocation.x;
		int EntityIndex = (int) (xClickLocation / Entity.WIDTH);
		if (xClickLocation < 0)
			EntityIndex -= 1; // Negative x values start in Entity -1 but the integer division would return 0
		int x = 0;//(int) xClickLocation % Entity.WIDTH;
		if (xClickLocation < 0) {
			x = 0;//Entity.WIDTH - 1 + x; // If the location is negative, bring it to the corresponding positive location in the Entity.
		}
		//getEntity(EntityIndex).addBlock(x, MathUtils.floor(touchLocation.y), type);
	}
}
