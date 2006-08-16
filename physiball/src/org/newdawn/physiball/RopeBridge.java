package org.newdawn.physiball;

import java.io.IOException;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.AABox;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public class RopeBridge extends Entity {
	private Block left;
	private Block right;
	private Block[] blocks;
	private float x;
	private float y;
	private float width;
	private int segments;
	
	public RopeBridge(float x, float y, float width, int segments) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.segments = segments;
	
		left = new Block(x-(width/2)-0.5f,y-0.25f,0.5f,1f,0,true,0.4f,false);
		right = new Block(x+(width/2)+0.5f,y-0.25f,0.5f,1f,0,true,0.4f,false);
	
		float segmentSize = width / segments;
		float blockSize = segmentSize - 0.2f;
	
		blocks = new Block[segments];
		for (int i=0;i<segments;i++) {
			blocks[i] = new Block(x-(width/2)+(i*segmentSize)+(segmentSize/2),y,blockSize,0.3f,0.5f,false,0.1f,false);
		}
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
		left.render();
		right.render();
		for (int i=0;i<blocks.length;i++) {
			blocks[i].render();
		}
	}

	/**
	 * @see org.newdawn.physiball.Entity#update(int)
	 */
	public void update(int delta) {
		left.update(delta);
		right.update(delta);
		for (int i=0;i<blocks.length;i++) {
			blocks[i].update(delta);
		}
	}

	/**
	 * @see org.newdawn.physiball.Entity#init()
	 */
	public void init() throws IOException {
		left.init();
		right.init();
		for (int i=0;i<blocks.length;i++) {
			blocks[i].init();
		}
	}

	/**
	 * @see org.newdawn.physiball.Entity#copy()
	 */
	public Entity copy() {
		return new RopeBridge(x,y,width,segments);
	}

	/**
	 * @see org.newdawn.physiball.Entity#addToWorld(net.phys2d.raw.World)
	 */
	public void addToWorld(World world) {
		left.addToWorld(world);
		right.addToWorld(world);
		for (int i=0;i<blocks.length;i++) {
			blocks[i].addToWorld(world);
		}
		
		Vector2f anchor = new Vector2f(blocks[0].getBody().getPosition());
		anchor.add(left.getBody().getPosition());
		anchor.scale(0.5f);
		
		BasicJoint j = new BasicJoint(left.getBody(),blocks[0].getBody(),anchor);
		j.setRelaxation(0f);
		world.add(j);
		
		anchor = new Vector2f(blocks[segments-1].getBody().getPosition());
		anchor.add(right.getBody().getPosition());
		anchor.scale(0.5f);
		
		BasicJoint j2 = new BasicJoint(right.getBody(),blocks[segments-1].getBody(),anchor);
		j2.setRelaxation(0f);
		world.add(j2);
		for (int i=0;i<segments-1;i++) {
			anchor = new Vector2f(blocks[i].getBody().getPosition());
			anchor.add(blocks[i+1].getBody().getPosition());
			anchor.scale(0.5f);
			BasicJoint j3 = new BasicJoint(blocks[i].getBody(),blocks[i+1].getBody(),anchor);
			j3.setRelaxation(0);
			world.add(j3);
		}
	}

	/**
	 * @see org.newdawn.physiball.Entity#removeFromWorld(net.phys2d.raw.World)
	 */
	public void removeFromWorld(World world) {
		// TODO Auto-generated method stub

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
