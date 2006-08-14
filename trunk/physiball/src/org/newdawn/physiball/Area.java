package org.newdawn.physiball;

import java.io.IOException;
import java.util.ArrayList;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.AABox;
import net.phys2d.raw.shapes.Line;
import net.phys2d.util.Triangulator;

import org.lwjgl.opengl.GL11;
import org.newdawn.render.texture.Texture;
import org.newdawn.render.texture.TextureLoader;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public class Area extends Entity {
	private class Point {
		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		public float x;
		public float y;
	}
	
	private ArrayList points = new ArrayList();
	private float x;
	private float y;
	
	private int list;
	private Texture texture;
	
	public Area(float x, float y) {
		this.x = x;
		this.y = y;
		
		addPoint(x,y);
	}
	
	public void addPoint(float nx, float ny) {
		points.add(new Point(nx,ny));
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
		texture.bind();

		GL11.glCallList(list);
		
		enterOutline();
		GL11.glCallList(list);
		leaveOutline();
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
		texture = TextureLoader.get().getTextureMipMap("res/crazy.tga");
		
		list = GL11.glGenLists(1);
		float scale = 0.5f;
		
		Triangulator tris = new Triangulator();
		for (int i=0;i<points.size();i++) {
			Point pt = (Point) points.get(i);
			
			tris.addPolyPoint(pt.x,pt.y);
		}
		tris.triangulate();
		ArrayList t = new ArrayList();
		for (int i=0;i<tris.getTriangleCount();i++) {
			for (int j=0;j<3;j++) {
				float[] pt = tris.getTrianglePoint(i,j);
				t.add(new Vector2f(pt[0],pt[1]));
			}
		}
		
		GL11.glNewList(list, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_TRIANGLES);
				GL11.glNormal3f(0,0,1);
				for (int i=0;i<t.size();i++) {
					Vector2f pt = (Vector2f) t.get(i);
					
					GL11.glTexCoord2f(pt.x * scale,pt.y * scale);
					GL11.glVertex3f(pt.x,pt.y,0.5f);
				}
				GL11.glNormal3f(0,0,-1);
				for (int i=t.size()-1;i>=0;i--) {
					Vector2f pt = (Vector2f) t.get(i);
					
					GL11.glTexCoord2f(pt.x * scale,pt.y * scale);
					GL11.glVertex3f(pt.x,pt.y,-0.5f);
				}
			GL11.glEnd();
//			GL11.glBegin(GL11.GL_POLYGON);
//				GL11.glNormal3f(0,0,1);
//				for (int i=0;i<points.size();i++) {
//					Point pt = (Point) points.get(i);
//					
//					GL11.glTexCoord2f(pt.x * scale,pt.y * scale);
//					GL11.glVertex3f(pt.x,pt.y,0.5f);
//				}
//			GL11.glEnd();
//			GL11.glBegin(GL11.GL_POLYGON);
//				GL11.glNormal3f(0,0,-1);
//				for (int i=points.size()-1;i>=0;i--) {
//					Point pt = (Point) points.get(i);
//
//					GL11.glTexCoord2f(pt.x * scale,pt.y * scale);
//					GL11.glVertex3f(pt.x,pt.y,-0.5f);
//				}
//			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_QUADS);
				for (int i=0;i<points.size()-1;i++) {
					Point pt = (Point) points.get(i);
					Point pt2 = (Point) points.get(i+1);
					
					Vector2f vec = new Vector2f(pt2.x,pt2.y);
					vec.sub(new Vector2f(pt.x,pt.y));
					vec.normalise();
					
					GL11.glNormal3f(vec.y,-vec.x,0);

					GL11.glTexCoord2f(pt.x* scale,0);
					GL11.glVertex3f(pt.x,pt.y,0.5f);
					GL11.glTexCoord2f(pt.x* scale,1* scale);
					GL11.glVertex3f(pt.x,pt.y,-0.5f);
					GL11.glTexCoord2f(pt2.x* scale,1* scale);
					GL11.glVertex3f(pt2.x,pt2.y,-0.5f);
					GL11.glTexCoord2f(pt2.x* scale,0);
					GL11.glVertex3f(pt2.x,pt2.y,0.5f);
				}
				
				Point pt = (Point) points.get(points.size()-1);
				Point pt2 = (Point) points.get(0);

				Vector2f vec = new Vector2f(pt2.x,pt2.y);
				vec.sub(new Vector2f(pt.x,pt.y));
				vec.normalise();
				
				GL11.glNormal3f(vec.y,-vec.x,0);
				GL11.glTexCoord2f(pt.x* scale,0);
				GL11.glVertex3f(pt.x,pt.y,0.5f);
				GL11.glTexCoord2f(pt.x* scale,1* scale);
				GL11.glVertex3f(pt.x,pt.y,-0.5f);
				GL11.glTexCoord2f(pt2.x* scale,1* scale);
				GL11.glVertex3f(pt2.x,pt2.y,-0.5f);
				GL11.glTexCoord2f(pt2.x* scale,0);
				GL11.glVertex3f(pt2.x,pt2.y,0.5f);
			GL11.glEnd();
		GL11.glEndList();
	}

	/**
	 * @see org.newdawn.physiball.Entity#copy()
	 */
	public Entity copy() {
		Area area = new Area(x,y);
		area.points = points;
		area.list = list;
		area.texture = texture;
		
		return area;
	}

	/**
	 * @see org.newdawn.physiball.Entity#addToWorld(net.phys2d.raw.World)
	 */
	public void addToWorld(World world) {
		for (int i=0;i<points.size()-1;i++) {
			Point pt = (Point) points.get(i);
			Point pt2 = (Point) points.get(i+1);
			
			StaticBody body = new StaticBody(new Line(pt2.x-pt.x,pt2.y-pt.y,true,false));
			body.setPosition(pt.x,pt.y);
			body.setRestitution(0.4f);
			world.add(body);
		}
		
		Point pt = (Point) points.get(points.size()-1);
		Point pt2 = (Point) points.get(0);
		StaticBody body = new StaticBody(new Line(pt2.x-pt.x,pt2.y-pt.y,true,false));
		body.setPosition(pt.x,pt.y);
		body.setRestitution(0.4f);
		world.add(body);
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
		return y;
	}

	/**
	 * @see org.newdawn.physiball.Entity#getY()
	 */
	public float getY() {
		return x;
	}
}
