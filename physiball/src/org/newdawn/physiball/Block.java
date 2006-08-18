package org.newdawn.physiball;

import java.io.IOException;

import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.AABox;
import net.phys2d.raw.shapes.Box;

import org.lwjgl.opengl.GL11;
import org.newdawn.render.texture.Texture;
import org.newdawn.render.texture.TextureLoader;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public class Block extends Entity {
	private float startx;
	private float starty;
	private float width;
	private float height;
	private boolean fixed;
	private float mass;
	
	private int list;
	private Texture texture;
	private Body body;
	private float rest;
	private boolean tracked;
	
	public Block(float x,float y,float width,float height,float mass, boolean fixed,float rest,boolean tracked) {
		this.tracked = tracked;
		this.startx = x;
		this.starty = y;
		this.width = width;
		this.height = height;
		this.fixed = fixed;
		this.mass = mass;
		this.rest = rest;
		
		if (fixed) {
			body = new StaticBody(new Box(width * Level.SCALE_UP,height * Level.SCALE_UP));
		} else {
			body = new Body(new Box(width * Level.SCALE_UP,height * Level.SCALE_UP),mass);
		}
		body.setPosition(x * Level.SCALE_UP,y * Level.SCALE_UP);
		body.setRestitution(rest);
		body.setFriction(0.1f);
		if (tracked) {
			InGameState.addTrackedBody(body);
		}
	}

	public float getX() {
		return body.getPosition().getX();
	}
	
	public float getY() {
		return body.getPosition().getY();
	}
	
	/**
	 * @see org.newdawn.physiball.Entity#getBounds()
	 */
	public AABox getBounds() {
		return null;
	}

	public void render() {
		float rotation = (float) Math.toDegrees(body.getRotation());
		
		float xp = body.getPosition().getX();
		float yp = body.getPosition().getY();
		
		xp /= Level.SCALE_UP;
		yp /= Level.SCALE_UP;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(xp,yp,0);
		GL11.glRotatef(rotation,0,0,1);
		GL11.glScalef(1,1,2);
		texture.bind();

		GL11.glCallList(list);
		
		GL11.glPopMatrix();
	}

	/**
	 * @see org.newdawn.physiball.Entity#update(int)
	 */
	public void update(int delta) {
	}

	/**
	 * @see org.newdawn.physiball.Entity#init()
	 */
	public void init() throws IOException {
		texture = TextureLoader.get().getTextureMipMap("res/crate.tga");
		float ep = 0.01f;
		
		list = GL11.glGenLists(1);
		float w2 = width / 2;
		float h2 = height / 2;
		GL11.glNewList(list, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glNormal3f(0,0,1);
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(-w2,-h2,0.5f+ep);
				GL11.glTexCoord2f(width,0);
				GL11.glVertex3f(w2,-h2,0.5f+ep);
				GL11.glTexCoord2f(width,height);
				GL11.glVertex3f(w2,h2,0.5f+ep);
				GL11.glTexCoord2f(0,height);
				GL11.glVertex3f(-w2,h2,0.5f+ep);
				
				GL11.glNormal3f(0,-1,0);
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(-w2,-h2,0.5f);
				GL11.glTexCoord2f(0,1);
				GL11.glVertex3f(-w2,-h2,-0.5f);
				GL11.glTexCoord2f(width,1);
				GL11.glVertex3f(w2,-h2,-0.5f);
				GL11.glTexCoord2f(width,0);
				GL11.glVertex3f(w2,-h2,0.5f);
				
				GL11.glNormal3f(0,1,0);
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(-w2,h2,0.5f);
				GL11.glTexCoord2f(width,0);
				GL11.glVertex3f(w2,h2,0.5f);
				GL11.glTexCoord2f(width,1);
				GL11.glVertex3f(w2,h2,-0.5f);
				GL11.glTexCoord2f(0,1);
				GL11.glVertex3f(-w2,h2,-0.5f);

				GL11.glNormal3f(-1,0,0);
				GL11.glTexCoord2f(1,1);
				GL11.glVertex3f(-w2,h2,0.5f);
				GL11.glTexCoord2f(0,1);
				GL11.glVertex3f(-w2,h2,-0.5f);
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(-w2,-h2,-0.5f);
				GL11.glTexCoord2f(1,0);
				GL11.glVertex3f(-w2,-h2,0.5f);
				
				GL11.glNormal3f(1,0,0);
				GL11.glTexCoord2f(1,1);
				GL11.glVertex3f(w2,h2,0.5f);
				GL11.glTexCoord2f(1,0);
				GL11.glVertex3f(w2,-h2,0.5f);
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(w2,-h2,-0.5f);
				GL11.glTexCoord2f(0,1);
				GL11.glVertex3f(w2,h2,-0.5f);
			GL11.glEnd();
		GL11.glEndList();
	}

	public Body getBody() {
		return body;
	}
	
	/**
	 * @see org.newdawn.physiball.Entity#copy()
	 */
	public Entity copy() {
		Block copy = new Block(startx,starty,width,height,mass,fixed,rest,tracked);
		copy.list = list;
		copy.texture = texture;
		copy.body.setRotation(body.getRotation());
		return copy;
	}

	/**
	 * @see org.newdawn.physiball.Entity#addToWorld(net.phys2d.raw.World)
	 */
	public void addToWorld(World world) {
		world.add(body);
	}

	/**
	 * @see org.newdawn.physiball.Entity#removeFromWorld(net.phys2d.raw.World)
	 */
	public void removeFromWorld(World world) {
		world.remove(body);
	}
}
