package org.newdawn.physiball;

import java.io.IOException;

import net.phys2d.raw.World;
import net.phys2d.raw.shapes.AABox;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public abstract class Entity {
	public abstract AABox getBounds();
	
	public abstract void render();
	
	public abstract void update(int delta);
	
	public abstract void init() throws IOException;
	
	public abstract Entity copy();

	public abstract void addToWorld(World world);
	
	public abstract void removeFromWorld(World world);
	
	public abstract float getX();
	
	public abstract float getY();
}
