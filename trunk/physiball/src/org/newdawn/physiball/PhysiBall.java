package org.newdawn.physiball;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;
import org.newdawn.render.window.LWJGLWindow;
import org.newdawn.render.window.WindowListener;
import org.newdawn.state.StateBasedGame;
import org.newdawn.util.Log;

/**
 * Oops. Forgot to document this one.
 * 
 * @author Kevin Glass
 */
public class PhysiBall extends StateBasedGame implements WindowListener {
	private LWJGLWindow window;
	private InGameState ingame;
	
	private int width;
	private int height;
	
	public PhysiBall() {
		width = 800;
		height = 600;
		
		window = new LWJGLWindow("PhysiBall!",this,width,height,false);
		setWindow(window);
	}
	
	public void startRendering() {
		window.startRendering();
	}
	
	/**
	 * @see org.newdawn.render.window.WindowListener#init()
	 */
	public void init() {
		try {
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			Display.setVSyncEnabled(true);
			
	        GL11.glViewport(0,0,width,height);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();		
			GLU.gluPerspective(45.0f, ((float) width) / 
									   ((float) height), 
							   1f, 20.0f);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			float b = 0.2f;
			window.setClearBuffer(false);
			
			ingame = new InGameState(window);
			
			addState(InGameState.ID, ingame);
			super.init(InGameState.ID);
		} catch (Throwable e) {
			Log.log(e);
			Sys.alert("PhysiBall!","Failed to initialise");
			System.exit(0);
		}
	}

	/**
	 * @see org.newdawn.render.window.WindowListener#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see org.newdawn.state.StateBasedGame#reinit()
	 */
	public void reinit() {
	}
	
	/**
	 * @see org.newdawn.render.window.WindowListener#closeRequested()
	 */
	public boolean closeRequested() {
		return false;
	}

	public static void main(String[] args) {
		PhysiBall game = new PhysiBall();
		game.startRendering();
	}
	/**
	 * @see org.newdawn.state.StateBasedGame#update(int)
	 */
	public void update(int delta) {
		super.update(delta);
		
		Display.sync(100);
	}
}
