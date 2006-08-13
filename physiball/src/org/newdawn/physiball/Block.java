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
	
	public Block(float x,float y,float width,float height,float mass, boolean fixed) {
		this.startx = x;
		this.starty = y;
		this.width = width;
		this.height = height;
		this.fixed = fixed;
		this.mass = mass;
		
		scalex = 1f + (0.05f / width);
		scaley = 1f + (0.05f / height);
		scalez = 1.05f;
		
		if (fixed) {
			body = new StaticBody(new Box(width,height));
		} else {
			body = new Body(new Box(width,height),mass);
		}
		body.setPosition(x,y);
		body.setRestitution(0.2f);
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

	/**
	 * @see org.newdawn.physiball.Entity#render()
	 */
	public void render() {
		float rotation = (float) Math.toDegrees(body.getRotation());
		
		float xp = body.getPosition().getX();
		float yp = body.getPosition().getY();
		
		GL11.glTranslatef(xp,yp,0);
		GL11.glRotatef(rotation,0,0,1);
		texture.bind();
		
		GL11.glCallList(list);
		
		enterOutline();
		GL11.glCallList(list);
		leaveOutline();
		
		GL11.glRotatef(-rotation,0,0,1);
		GL11.glTranslatef(-xp,-yp,0);
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
		texture = TextureLoader.get().getTextureLinear("res/crate.tga");
	
		list = GL11.glGenLists(1);
		float w2 = width / 2;
		float h2 = height / 2;
		GL11.glNewList(list, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glNormal3f(0,0,1);
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(-w2,-h2,0.5f);
				GL11.glTexCoord2f(width,0);
				GL11.glVertex3f(w2,-h2,0.5f);
				GL11.glTexCoord2f(width,height);
				GL11.glVertex3f(w2,h2,0.5f);
				GL11.glTexCoord2f(0,height);
				GL11.glVertex3f(-w2,h2,0.5f);
				
				GL11.glNormal3f(0,0,-1);
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(-w2,-h2,-0.5f);
				GL11.glTexCoord2f(0,height);
				GL11.glVertex3f(-w2,h2,-0.5f);
				GL11.glTexCoord2f(width,height);
				GL11.glVertex3f(w2,h2,-0.5f);
				GL11.glTexCoord2f(width,0);
				GL11.glVertex3f(w2,-h2,-0.5f);
				
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

				GL11.glNormal3f(0,-1,0);
				GL11.glTexCoord2f(1,1);
				GL11.glVertex3f(-w2,h2,0.5f);
				GL11.glTexCoord2f(0,1);
				GL11.glVertex3f(-w2,h2,-0.5f);
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(-w2,-h2,-0.5f);
				GL11.glTexCoord2f(1,0);
				GL11.glVertex3f(-w2,-h2,0.5f);
				
				GL11.glNormal3f(0,1,0);
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
		Block copy = new Block(startx,starty,width,height,mass,fixed);
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
