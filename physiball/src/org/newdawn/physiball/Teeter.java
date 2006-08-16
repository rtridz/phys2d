package org.newdawn.physiball;

import java.io.IOException;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.Joint;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.AABox;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public class Teeter extends Entity {
	private Block top;
	private Block pivot;
	private float x;
	private float y;
	private float width;
	
	public Teeter(float x, float y, float width) {
		this.x = x;
		this.y = y;
		this.width = width;
		
		top = new Block(x,y,width,0.2f,1,false,0,false);
		pivot = new Block(x,y-1,0.5f,0.5f,0,true,0,false);
	}
	
	/**
	 * @see org.newdawn.physiball.Entity#getBounds()
	 */
	public AABox getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.newdawn.physiball.Entity#render()
	 */
	public void render() {
		top.render();
		pivot.render();
	}

	/**
	 * @see org.newdawn.physiball.Entity#update(int)
	 */
	public void update(int delta) {
		top.update(delta);
		pivot.update(delta);
	}

	/**
	 * @see org.newdawn.physiball.Entity#init()
	 */
	public void init() throws IOException {
		top.init();
		pivot.init();
	}

	/**
	 * @see org.newdawn.physiball.Entity#copy()
	 */
	public Entity copy() {
		return new Teeter(x,y,width);
	}

	/**
	 * @see org.newdawn.physiball.Entity#addToWorld(net.phys2d.raw.World)
	 */
	public void addToWorld(World world) {
		top.addToWorld(world);
		pivot.addToWorld(world);
		
		Vector2f pos = new Vector2f(top.getBody().getPosition());
		Joint j = new BasicJoint(top.getBody(),pivot.getBody(),pos);
	
		world.add(j);
	}

	/**
	 * @see org.newdawn.physiball.Entity#removeFromWorld(net.phys2d.raw.World)
	 */
	public void removeFromWorld(World world) {
	}

	/**
	 * @see org.newdawn.physiball.Entity#getX()
	 */
	public float getX() {
		return x;
	}

	/**
	 * @see org.newdawn.physiball.Entity#getY()
	 */
	public float getY() {
		return y;
	}

}
