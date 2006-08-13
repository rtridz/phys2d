package org.newdawn.physiball;

import java.io.IOException;
import java.util.ArrayList;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.World;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public class Level {
	private ArrayList entities = new ArrayList();
	private World world;
	private int worldUpdateInterval = 1000 / 60;
	private int counter;
	
	public Level() {
		world = new World(new Vector2f(0,-10), 10);
		world.setDamping(0.95f);
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		entity.addToWorld(world);
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
		entity.removeFromWorld(world);
	}
	
	public void init() throws IOException {
		for (int i=0;i<entities.size();i++) {
			Entity ent = (Entity) entities.get(i);
			ent.init();
		}
	}
	
	public Level copy() {
		Level copy = new Level();
		
		for (int i=0;i<entities.size();i++) {
			copy.addEntity(((Entity) entities.get(i)).copy());
		}
		
		return copy;
	}
	
	public void render() {
		ArrayList onscreen = entities;
		
		for (int i=0;i<onscreen.size();i++) {
			Entity ent = (Entity) onscreen.get(i);
			ent.render();
		}
	}
	
	public World getPhysicsWorld() {
		return world;
	}
	
	public void update(int delta) {
		for (int i=0;i<entities.size();i++) {
			Entity ent = (Entity) entities.get(i);
			ent.update(delta);
		}
		
		counter += delta;
		while (counter > worldUpdateInterval) {
			world.step();
			counter -= worldUpdateInterval;
		}
	}
}
