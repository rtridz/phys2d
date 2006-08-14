package org.newdawn.physiball;

import java.io.IOException;
import java.nio.FloatBuffer;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.World;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.glui.font.BitmapFont;
import org.newdawn.render.texture.Texture;
import org.newdawn.render.texture.TextureLoader;
import org.newdawn.render.util.Color;
import org.newdawn.render.util.Tuple4;
import org.newdawn.render.window.LWJGLWindow;
import org.newdawn.state.GameState;
import org.newdawn.state.StateBasedGame;
import org.newdawn.util.Log;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public class InGameState implements GameState {
	public static final int ID = 1;

	private FloatBuffer buffer = BufferUtils.createFloatBuffer(4);	
	private FloatBuffer mcolor = BufferUtils.createFloatBuffer(4);
	private FloatBuffer mcolor2 = BufferUtils.createFloatBuffer(4);
	private FloatBuffer pos1 = new Tuple4(0.5f,0.1f,0.5f,0).toFloatBuffer();
	private FloatBuffer spec1 = new Tuple4(0.2f,0.2f,0.2f,1).toFloatBuffer();
	private FloatBuffer pos2 = new Tuple4(-0.5f,0.1f,0.5f,0).toFloatBuffer();
	private FloatBuffer spec2 = new Tuple4(0.2f,0.2f,0.2f,1).toFloatBuffer();
	
	private LWJGLWindow window;
	private Level current;
	private Level level;
	
	private Ball player;
	private boolean onground;
	private int count = 0;
	private Texture background;
	private float maxSpeed = 7;
	private float force = 4;
	
	private BitmapFont font;
	private BitmapFont bigfont;
	private String version = "0.1";
	
	public InGameState(LWJGLWindow window) {
		this.window = window;
	}
	
	private void configureLighting() {
		float ambient = 0.2f;	
		buffer.put(ambient).put(ambient).put(ambient).put(1);
		buffer.flip();
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, buffer);
		mcolor.put(1).put(1).put(1).put(1);
		mcolor.flip();
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, mcolor);

		mcolor2.put(0).put(0).put(0).put(1);
		mcolor2.flip();
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, mcolor2);
		
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos1);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, spec1);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, pos2);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, spec2);
		GL11.glEnable(GL11.GL_LIGHT1);
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}
	
	/**
	 * @see org.newdawn.state.GameState#init(org.newdawn.state.StateBasedGame)
	 */
	public void init(StateBasedGame game) throws IOException {
		font = new BitmapFont("res/new16.tga",16,16);
		bigfont = new BitmapFont("res/font.tga","res/font.dat",32,32);
		
		background = TextureLoader.get().getTextureLinear("res/bricks.tga");
		
		level = new Level();
		Area area = new Area(0,-5);
		area.addPoint(-20,-6);
		area.addPoint(-25,-5);
		area.addPoint(-50,-5);
		area.addPoint(-50,-6);
		area.addPoint(-20,-8);
		area.addPoint(30,-8);
		area.addPoint(30,-6);
		level.addEntity(area);
		
		level.addEntity(new Block(0,-2,10,1f,0,true));
		Block block = new Block(-11,0.5f,10,1f,0,true);
		block.getBody().setRotation(-0.5f);
		level.addEntity(block);
		block = new Block(11,0.5f,10,1f,0,true);
		block.getBody().setRotation(0.5f);
		level.addEntity(block);
		
		for (int y=0;y<3;y++) {
			for (int x=0;x<y+1;x++) {
				block = new Block(-40+(x*1)-(y*0.4f),-2-y,1,1f,1,false);
				level.addEntity(block);
			}
		}
		level.init();

		
		configureLighting();
	
		restart();
	}

	private void restart() {
		current = level.copy();
		try {
			player = new Ball(0,0,0.5f,1,false);
			player.init();
			current.addEntity(player);
		} catch (IOException e) {
			Log.log(e);
			System.exit(0);
		}
	}
	
	/**
	 * @see org.newdawn.state.GameState#reinit(org.newdawn.state.StateBasedGame)
	 */
	public void reinit(StateBasedGame game) throws IOException {
	}

	/**
	 * @see org.newdawn.state.GameState#render(org.newdawn.state.StateBasedGame, int)
	 */
	public void render(StateBasedGame game, int delta) {
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslatef(0,0,-15);
		
		float xo = player.getX();
		float yo = player.getY();
		
		float width = 10;
		float height = 10;
		float xscale = 0.5f;
		float yscale = 0.5f;
		float tx = xo * xscale;
		float ty = yo * yscale;
		
		background.bind();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3f(0,0,1);
			GL11.glTexCoord2f(tx,ty);
			GL11.glVertex3f(-width,-height,-0.55f);
			GL11.glTexCoord2f(tx+(width * 2 * xscale),ty);
			GL11.glVertex3f(width,-height,-0.55f);
			GL11.glTexCoord2f(tx+(width * 2 * xscale),ty+(height * 2 * yscale));
			GL11.glVertex3f(width,height,-0.55f);
			GL11.glTexCoord2f(tx,ty+(height * 2 * yscale));
			GL11.glVertex3f(-width,height,-0.55f);
		GL11.glEnd();

		GL11.glTranslatef(-xo,-yo-1,0);
		current.render();
		
		window.enterOrtho();
		GL11.glEnable(GL11.GL_BLEND);
		drawString(10,window.getHeight() - 50,"PhysiBall v"+version+" Java, OpenGL and 2D Physics");
		drawString(10,window.getHeight() - 30,"Kevin Glass (demo@cokeandcode.com)");
		drawString(10,window.getHeight() - 10,"http://www.cokeandcode.com");
		drawString(10,30,"FPS: "+window.getFPS());
		window.leaveOrtho();
	}

	private void drawString(int x, int y, String str) {
		font.drawString(x+1,y+1,str, Color.black);
		font.drawString(x,y,str, Color.white);
	}
	
	/**
	 * @see org.newdawn.state.GameState#update(org.newdawn.state.StateBasedGame, int)
	 */
	public void update(StateBasedGame game, int delta) {
		current.update(delta);
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_R) {
				if (Keyboard.getEventKeyState()) {
					restart();
				}
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			System.exit(0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			if (player.getBody().getVelocity().getX() > -maxSpeed) {
				player.apply(-force,0);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			if (player.getBody().getVelocity().getX() < maxSpeed) {
				player.apply(force,0);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			World world = current.getPhysicsWorld();
			
			CollisionEvent[] events = world.getContacts(player.getBody());
			onground = false;
			
			for (int i=0;i<events.length;i++) {
				if (events[i].getNormal().getY() > 0) {
					if (events[i].getBodyB() == player.getBody()) {
						onground = true;
					}
				}
				if (events[i].getNormal().getY() < 0) {
					if (events[i].getBodyA() == player.getBody()) {
						onground = true;
					}
				}
			}
			
			if (onground) {
				if (count <= 0) {
					count = 10;
					float sep = 0.1f;
					
					player.getBody().adjustPosition(new Vector2f(0,sep));
					player.getBody().adjustVelocity(new Vector2f(0,7));
				} else {
					count--;
				}
			}
		}
	}

	/**
	 * @see org.newdawn.state.GameState#enterState(org.newdawn.state.StateBasedGame)
	 */
	public void enterState(StateBasedGame game) {
	}

	/**
	 * @see org.newdawn.state.GameState#leaveState(org.newdawn.state.StateBasedGame)
	 */
	public void leaveState(StateBasedGame game) {
	}
}
