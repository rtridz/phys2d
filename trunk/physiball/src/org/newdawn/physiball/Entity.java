package org.newdawn.physiball;

import java.io.IOException;

import net.phys2d.raw.World;
import net.phys2d.raw.shapes.AABox;

import org.lwjgl.opengl.GL11;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public abstract class Entity {
	protected float scalex = 1.05f;
	protected float scaley = 1.05f;
	protected float scalez = 1.05f;
	
	public abstract AABox getBounds();
	
	public abstract void render();
	
	public abstract void update(int delta);
	
	public abstract void init() throws IOException;
	
	public abstract Entity copy();

	public abstract void addToWorld(World world);
	
	public abstract void removeFromWorld(World world);
	
	public abstract float getX();
	
	public abstract float getY();
	
	protected void enterOutline() {
		GL11.glColor3f(0,0,0f);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glCullFace(GL11.GL_FRONT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPolygonMode(GL11.GL_BACK,GL11.GL_LINE);
		GL11.glLineWidth(2.0f);
	}
	
	protected void leaveOutline() {
		GL11.glColor3f(1,1,1);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
