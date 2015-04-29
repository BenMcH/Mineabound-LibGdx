package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tycoon177.mineabound.screens.GameWorld;

public class EntityHandler {
	public static Array<Entity> entities;

	public EntityHandler() {
		entities = new Array<Entity>();
	}

	public Entity addEntity(Entity entity) {
		entities.add(entity);
		return entity;
	}

	public Entity getEntity(int index) {
		Entity c = entities.get(index);
		return c;
	}

	public void update(float deltaTime) {
		for (Entity entity : getVisibleEntities()) {
			entity.update(deltaTime);
		}
	}

	public void render(SpriteBatch batch) {
		for (Entity e : entities) {
			if (e.getSprite() == null)
				continue;
			e.drawEntity(batch);
		}
	}

	public Array<Entity> getVisibleEntities() {
		Array<Entity> visibleEntities = new Array<Entity>();
		OrthographicCamera cam = GameWorld.world.getCamera();
		Vector2 topLeft = new Vector2(cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
		Vector2 size = new Vector2(cam.viewportWidth, cam.viewportHeight);
		Rectangle r = new Rectangle(topLeft.x, topLeft.y, size.x, size.y);
		for (Entity ent : entities) {
			if (r.overlaps(ent.getHitBox())) {
				visibleEntities.add(ent);
			}
		}
		return visibleEntities;
	}

	public void removeBlock(Vector2 touchLocation) {
		// removeBlock(new Vector3(touchLocation.x, touchLocation.y, 0));
	}

	public void removeAllEntitysFromWorld() {

	}

	public void debugRender(ShapeRenderer debugRenderer) {
		// for (Entity b : getVisibleEntities()) {
		// b.debugDraw(debugRenderer);
		// }
	}

	public void removeEntity(Entity ent) {
		entities.removeValue(ent, true);
	}

}
