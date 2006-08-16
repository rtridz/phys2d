package org.newdawn.physiball;

import java.io.IOException;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.AABox;
import net.phys2d.raw.shapes.Circle;

import org.lwjgl.opengl.GL11;
import org.newdawn.render.models.obj.ObjLoader;
import org.newdawn.render.models.obj.ObjModel;
import org.newdawn.render.texture.Texture;
import org.newdawn.render.texture.TextureLoader;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public class StaticBall extends Entity {
	private ObjModel model;
	private Texture texture;
	
	private float startx;
	private float starty;
	private float radius;
	
	private Body body;
	
	public StaticBall(float x, float y, float radius) {
		this.startx = x;
		this.starty = y;
		this.radius = radius;
		
		body = new StaticBody(new Circle(radius * Level.SCALE_UP));
		body.setPosition(x * Level.SCALE_UP, y * Level.SCALE_UP);
		body.setRestitution(0.4f);
		body.setFriction(1f);
		body.setRotation(0.5f);
	}

	public float getX() {
		float xp = body.getPosition().getX();
		
		return xp;
	}
	
	public float getY() {
		float yp = body.getPosition().getY();
	
		return yp;
	}
	
	/**
	 * @see org.newdawn.physiball.Entity#getBounds()
	 */
	public AABox getBounds() {
		return null;
	}

	public void apply(float x,float y) {
		body.addForce(new Vector2f(x,y));
	}
	
	public Body getBody() {
		return body;
	}
	
	/**
	 * @see org.newdawn.physiball.Entity#render()
	 */
	public void render() {
		float rotation = (float) Math.toDegrees(body.getRotation());

		float xp = body.getPosition().getX();
		float yp = body.getPosition().getY();

		xp = Math.round(xp);
		yp = Math.round(yp);
		rotation = Math.round(rotation);
		
		xp /= Level.SCALE_UP;
		yp /= Level.SCALE_UP;
		
		GL11.glTranslatef(xp,yp,0);
		GL11.glRotatef(rotation,0,0,1);
		GL11.glScalef(radius*2,radius*2,0.95f);
		texture.bind();
		
		model.render();
		GL11.glScalef(1/(radius*2),1/(radius*2),1/(radius*2));	
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
		model = ObjLoader.loadObj("res/ball.obj");
		texture = TextureLoader.get().getTextureLinear("res/marble.tga");
	}

	/**
	 * @see org.newdawn.physiball.Entity#copy()
	 */
	public Entity copy() {
		StaticBall copy = new StaticBall(startx,starty,radius);
		copy.model = model;
		copy.texture = texture;
		
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
