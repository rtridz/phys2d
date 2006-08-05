/*
 * Phys2D - a 2D physics engine based on the work of Erin Catto.
 * 
 * This source is provided under the terms of the BSD License.
 * 
 * Copyright (c) 2006, Phys2D
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the following 
 * conditions are met:
 * 
 *  * Redistributions of source code must retain the above 
 *    copyright notice, this list of conditions and the 
 *    following disclaimer.
 *  * Redistributions in binary form must reproduce the above 
 *    copyright notice, this list of conditions and the following 
 *    disclaimer in the documentation and/or other materials provided 
 *    with the distribution.
 *  * Neither the name of the Phys2D/New Dawn Software nor the names of 
 *    its contributors may be used to endorse or promote products 
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE.
 */
package net.phys2d.raw.collide;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.Contact;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.Line;

/**
 * Collision routines betwene a circle and a line. The create method is
 * provided in case this collider becomes stateful at some point.
 * 
 * @author Kevin Glass
 */
public strictfp class LineCircleCollider implements Collider {
	/** The single instance of this class */
	private static LineCircleCollider single = new LineCircleCollider();
	
	/**
	 * Create a new line circle collider. Place holder method just
	 * in case this class becomes stateful
	 *  
	 * @return The newly created line circle collider
	 */
	public static LineCircleCollider create() {
		return single;
	}

	/**
	 * Prevent construction outside of this class
	 */
	private LineCircleCollider() {
		
	}
	/**
	 * @see net.phys2d.raw.collide.Collider#collide(net.phys2d.raw.Contact[], net.phys2d.raw.Body, net.phys2d.raw.Body)
	 */
	public int collide(Contact[] contacts, Body bodyA, Body bodyB) {
		Line line = (Line) bodyA.getShape();
		Circle circle = (Circle) bodyB.getShape();
		
		line = line.getPositionedLine(bodyA.getPosition().getX(),
				 					  bodyA.getPosition().getY());
		
		float dis2 = line.distanceSquared(bodyB.getPosition());
		float r2 = circle.getRadius() * circle.getRadius();

		if (dis2 < r2) {
			Vector2f pt = new Vector2f();
			
			line.getClosestPoint(bodyB.getPosition(), pt);
			Vector2f normal = new Vector2f(bodyB.getPosition());
			normal.sub(pt);
			float sep = circle.getRadius() - normal.length();
			normal.normalise();
			
			contacts[0].setSeparation(-sep);
			contacts[0].setPosition(pt);
			contacts[0].setNormal(normal);
			contacts[0].setFeature(new FeaturePair());
			
			return 1;
		}
		
		return 0;
	}

}
