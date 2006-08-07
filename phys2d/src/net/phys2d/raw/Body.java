/*
 * Phys2D - a 2D physics engine based on the work of Erin Catto. The
 * original source remains:
 * 
 * Copyright (c) 2006 Erin Catto http://www.gphysics.com
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
package net.phys2d.raw;

import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.shapes.DynamicShape;
import net.phys2d.raw.shapes.Shape;

/**
 * A single body within the physics model
 * 
 * @author Kevin Glass
 */
public strictfp class Body {
	/** The next ID to be assigned */
	private static int NEXT_ID = 0;
	/** The maximum value indicating that body won't move */
	public static final float INFINITE_MASS = Float.MAX_VALUE;
	
	/** The current position of this body */
	private Vector2f position = new Vector2f();
	/** The current rotation of this body */
	private float rotation;

	/** The velocity of this body */
	private Vector2f velocity = new Vector2f();
	/** The angular velocity of this body */
	private float angularVelocity;

	/** The force being applied to this body - i.e. driving velocity */
	private Vector2f force = new Vector2f();
	/** The angular force being applied this body - i.e. driving angular velocity */
	private float torque;

	/** The shape representing this body */
	private Shape shape;
	
	/** The friction on the surface of this body */
	private float friction;
	/** The mass of this body */
	private float mass;
	/** The inverse mass of this body */
	private float invMass;
	/** The density of this body */
	private float I;
	/** The inverse of this density */
	private float invI;
	/** The name assigned to this body */
	private String name;
	/** The id assigned ot this body */
	private int id;
	/** The hardness of this body */
	private float hardness = 0f;

	/**
	 * Create a new un-named body
	 * 
	 * @param shape The shape describing this body
	 * @param m The mass of the body
	 */
	public Body(DynamicShape shape, float m) {
		this("UnnamedBody",(Shape) shape,m);
	}
	
	/**
	 * Create a new un-named body
	 * 
	 * @param shape The shape describing this body
	 * @param m The mass of the body
	 */
	protected Body(Shape shape, float m) {
		this("UnnamedBody",shape,m);
	}

	/**
	 * Create a named body
	 * 
	 * @param name The name to assign to the body
	 * @param shape The shape describing this body
	 * @param m The mass of the body
	 */
	public Body(String name,DynamicShape shape, float m) {
		this(name,(Shape) shape,m);
	}
	
	/**
	 * Create a named body
	 * 
	 * @param name The name to assign to the body
	 * @param shape The shape describing this body
	 * @param m The mass of the body
	 */
	protected Body(String name,Shape shape, float m) {
		this.name = name;

		id = NEXT_ID++;
		position.set(0.0f, 0.0f);
		rotation = 0.0f;
		velocity.set(0.0f, 0.0f);
		angularVelocity = 0.0f;
		force.set(0.0f, 0.0f);
		torque = 0.0f;
		friction = 0.2f;

		//size.set(1.0f, 1.0f);
		mass = INFINITE_MASS;
		invMass = 0.0f;
		I = INFINITE_MASS;
		invI = 0.0f;
		
		set(shape,m);
	}
	
	/**
	 * Set the "hardness" of the body. Hard bodies transfer
	 * momentum well. A value of 1.0 would be a pool ball. The 
	 * default is 1f
	 * 
	 * Note this is very very experimental - hence the terrible
	 * name.
	 * 
	 * @param hard The hardness of this body
	 */
	public void setHardness(float hard) {
		this.hardness = hard;
	}
	
	/**
	 * Get the "hardness" of the body. Hard bodies transfer
	 * momentum well. A value of 1.0 would be a pool ball. The 
	 * default is 1f
	 * 
	 * Note this is very very experimental - hence the terrible
	 * name.
	 * 
	 * @return The "hardness" of the body
	 */
	public float getHardness() {
		return hardness;
	}
	
	/**
	 * Reconfigure the body
	 * 
	 * @param shape The shape describing this body
	 * @param m The mass of the body
	 */
	public void set(Shape shape, float m) {
		position.set(0.0f, 0.0f);
		rotation = 0.0f;
		velocity.set(0.0f, 0.0f);
		angularVelocity = 0.0f;
		force.set(0.0f, 0.0f);
		torque = 0.0f;
		friction = 0.2f;

		this.shape = shape;
		mass = m;

		if (mass < INFINITE_MASS)
		{
			invMass = 1.0f / mass;
			//I = mass * (size.x * size.x + size.y * size.y) / 12.0f;
			
			I = (mass * shape.getSurfaceFactor()) / 12.0f;
			invI = 1.0f / I;
		}
		else
		{
			invMass = 0.0f;
			I = INFINITE_MASS;
			invI = 0.0f;
		}
	}

	/**
	 * Set the friction on the surface of this body
	 * 
	 * @param friction The friction on the surface of this body
	 */
	public void setFriction(float friction) {
		this.friction = friction;
	}
	
	/**
	 * Set the rotation of this body
	 * 
	 * @param rotation The new rotation of the body
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Get the shape representing this body
	 * 
	 * @return The shape representing this body
	 */
	public Shape getShape() {
		return shape;
	}
	
	/**
	 * Set the position of this body 
	 * 
	 * @param x The x position of this body
	 * @param y The y position of this body
	 */
	public void setPosition(float x, float y) {
		position.set(x,y);
	}
	
	/**
	 * Get the position of this body
	 * 
	 * @return The position of this body
	 */
	public ROVector2f getPosition() {
		return position;
	}
	
	/**
	 * Get the rotation of this body
	 * 
	 * @return The rotation of this body
	 */
	public float getRotation() {
		return rotation;
	}
	
	/**
	 * Get the inverse density of this body
	 * 
	 * @return The inverse denisity of this body
	 */
	float getInvI() {
		return invI;
	}

	/**
	 * Adjust the position of this body
	 * 
	 * @param delta The amount to change the position by
	 * @param scale The amount to scale the delta by
	 */
	public void adjustPosition(ROVector2f delta, float scale) {
		position.x += delta.getX() * scale;
		position.y += delta.getY() * scale;
	}
	
	/**
	 * Adjust the position of this body
	 * 
	 * @param delta The amount to change the position by
	 */
	public void adjustPosition(Vector2f delta) {
		position.add(delta);
	}

	/**
	 * Adjust the rotation of this body
	 * 
	 * @param delta The amount to change the rotation by
	 */
	public void adjustRotation(float delta) {
		rotation += delta;
	}
	
	/**
	 * Set the force being applied to this body
	 * 
	 * @param x The x component of the force
	 * @param y The y component of the force
	 */
	public void setForce(float x, float y) {
		force.set(x,y);
	}
	
	/**
	 * Set the torque being applied to this body
	 * 
	 * @param t The torque being applied to this body
	 */
	public void setTorque(float t) {
		torque = t;
	}
	
	/**
	 * Get the velocity of this body
	 * 
	 * @return The velocity of this body
	 */
	public ROVector2f getVelocity() {
		return velocity;
	}
	
	/**
	 * Get the angular velocity of this body
	 * 
	 * @return The angular velocity of this body
	 */
	public float getAngularVelocity() {
		return angularVelocity;
	}
	
	/** 
	 * Adjust the velocity of this body
	 * 
	 * @param delta The amount to change the velocity by
	 */
	public void adjustVelocity(Vector2f delta) {
		velocity.add(delta);
	}
	
	/** 
	 * Adjust the angular velocity of this body
	 * 
	 * @param delta The amount to change the velocity by
	 */
	public void adjustAngularVelocity(float delta) {
		angularVelocity += delta;
	}
	
	/**
	 * Get the friction on the surface of this body
	 * 
	 * @return The friction of this surface of this body
	 */
	public float getFriction() {
		return friction;
	}
	
	/**
	 * Get the force applied to this body
	 * 
	 * @return The force applied to this body
	 */
	public ROVector2f getForce() {
		return force;
	}

	/**
	 * Get the torque applied to this body
	 * 
	 * @return The torque applied to this body
	 */
	public float getTorque() {
		return torque;
	}
	
	/**
	 * Add a force to this body
	 * 
	 * @param f The force to be applied
	 */
	public void addForce(Vector2f f) {
		force.add(f);
	}
	
	/**
	 * Get the inverse mass of this body
	 * 
	 * @return The inverse mass of this body
	 */
	float getInvMass() {
		return invMass;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[Body '"+name+"' "+position+" vel: "+velocity+" ("+angularVelocity+")]";
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return id;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other.getClass() == getClass()) {
			return ((Body) other).id == id;
		}
		
		return false;
	}
}
