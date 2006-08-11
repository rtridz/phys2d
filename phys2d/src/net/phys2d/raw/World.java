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

import net.phys2d.math.Vector2f;
import net.phys2d.raw.strategies.BruteCollisionStrategy;

/**
 * The physics model in which the bodies exist. The world is "stepped"
 * which causes the bodies to be moved and resulting forces be resolved.
 * 
 * @author Kevin Glass
 */
public strictfp class World implements CollisionContext {
	/** The bodies contained in the world */
	private BodyList bodies = new BodyList(); 
	/** The joints contained in the world */
	private JointList joints = new JointList(); 
	/** The arbiters that have been required in the world */
	private ArbiterList arbiters = new ArbiterList(); 
	/** The direction and force of gravity */
	private Vector2f gravity = new Vector2f(0,0);
	/** The number of iteration to run each step */
	private int iterations;
	/** The broad phase collision strategy we're using */
	private BroadCollisionStrategy collisionStrategy;

	/**
	 * Create a new physics model World
	 * 
	 * @param gravity The direction and force of gravity
	 * @param iterations The number of iterations to run each step. More iteration
	 * is more accurate but slower
	 */
	public World(Vector2f gravity, int iterations) {
		this(gravity, iterations, new BruteCollisionStrategy());
	}
	
	/**
	 * Create a new physics model World
	 * 
	 * @param gravity The direction and force of gravity
	 * @param iterations The number of iterations to run each step. More iteration
	 * is more accurate but slower
	 * @param strategy The strategy used to determine which bodies to check detailed
	 * collision on
	 */
	public World(Vector2f gravity, int iterations, BroadCollisionStrategy strategy) {
		this.gravity = gravity;
		this.iterations = iterations;
		this.collisionStrategy = strategy;
	}

	/**
	 * Set the strategy used to determine the bodies for collision in the
	 * broad phase.
	 * 
	 * @param strategy The strategy used to determine which bodies to check detailed
	 * collision on
	 */
	public void setCollisionStrategy(BroadCollisionStrategy strategy) {
		this.collisionStrategy = strategy;
	}
	
	/**
	 * Set the gravity applied in the world
	 * 
	 * @param x The x component of the gravity factor
	 * @param y The y component of the gravity factor
	 */
	public void setGravity(float x, float y) {
		gravity.x = x;
		gravity.y = y;
	}
	
	/**
	 * Retrieve a immutable list of bodies in the simulation
	 * 
	 * @return The list of bodies
	 */
	public BodyList getBodies() {
		return bodies;
	}

	/**
	 * Retrieve a immutable list of joints in the simulation
	 * 
	 * @return The list of joints
	 */
	public JointList getJoints() {
		return joints;
	}

	/**
	 * Retrieve a immutable list of arbiters in the simulation
	 * 
	 * @return The list of arbiters
	 */
	public ArbiterList getArbiters() {
		return arbiters;
	}
	
	/**
	 * Add a body to the simulation
	 * 
	 * @param body The body to be added
	 */
	public void add(Body body) {
		bodies.add(body);
	}
	
	/**
	 * Remove a body from the simulation
	 * 
	 * @param body The body to be removed
	 */
	public void remove(Body body) {
		bodies.remove(body);
	}
	
	/**
	 * Add a joint to the simulation
	 * 
	 * @param joint The joint to be added 
	 */
	public void add(Joint joint) {
		joints.add(joint);
	}
	
	/**
	 * Remove a joint from the simulation
	 * 
	 * @param joint The joint to be removed
	 */
	public void remove(Joint joint) {
		joints.remove(joint);
	}
	
	/**
	 * Remove all the elements from this world
	 */
	public void clear() {
		bodies.clear();
		joints.clear();
		arbiters.clear();
	}

	/**
	 * Step the simulation. Currently anything other than 1/60f as a 
	 * step leads to unpredictable results - hence the default step 
	 * fixes us to this step
	 */
	public void step() {
		step(1/60.0f);
	}
	
	/**
	 * Step the simulation. Currently anything other than 1/60f as a 
	 * step leads to unpredictable results - hence the default step 
	 * fixes us to this step
	 * 
	 * @param dt The amount of time to step
	 */
	public void step(float dt) {
		float invDT = dt > 0.0f ? 1.0f / dt : 0.0f;

		broadPhase();

		for (int i = 0; i < bodies.size(); ++i)
		{
			Body b = bodies.get(i);

			if (b.getInvMass() == 0.0f) {
				continue;
			}
			
			Vector2f temp = new Vector2f(b.getForce());
			temp.scale(b.getInvMass());
			temp.add(gravity);
			temp.scale(dt);
			
			b.adjustVelocity(temp);
			b.adjustAngularVelocity(dt * b.getInvI() * b.getTorque());
		}

		for (int i=0;i<arbiters.size();i++) {
			Arbiter arb = arbiters.get(i);
			arb.preStep(invDT, dt);
		}

		for (int i = 0; i < joints.size(); ++i) {
			Joint j = joints.get(i);
			j.preStep(invDT);	
		}

		for (int i = 0; i < iterations; ++i)
		{
			for (int k=0;k<arbiters.size();k++) {
				Arbiter arb = arbiters.get(k);
				arb.applyImpulse();
			}
			
			for (int k=0;k<joints.size();++k) {
				Joint j = joints.get(k);
				j.applyImpulse();
			}
		}

		for (int i=0;i < bodies.size(); ++i)
		{
			Body b = bodies.get(i);

			b.adjustPosition(b.getVelocity(), dt);
			b.adjustPosition(b.getBiasedVelocity(), dt);
			
			b.adjustRotation(dt * b.getAngularVelocity());
			b.adjustRotation(dt * b.getBiasedAngularVelocity());

			b.resetBias();
			b.setForce(0,0);
			b.setTorque(0);
		}
	}
	
	/**
	 * The broad collision phase
	 */
	void broadPhase() {
		collisionStrategy.collideBodies(this, bodies);
	} 
	
	/**
	 * @see net.phys2d.raw.CollisionContext#resolve(net.phys2d.raw.BodyList)
	 */
	public void resolve(BodyList bodyList) {
		// O(n^2) broad-phase
		for (int i = 0; i < bodyList.size(); ++i)
		{
			Body bi = bodyList.get(i);

			for (int j = i + 1; j < bodyList.size(); ++j)
			{
				Body bj = bodyList.get(j);

				if (bi.getInvMass() == 0.0f && bj.getInvMass() == 0.0f) {
					continue;
				}
				if (!bi.getShape().getBounds().touches(bi.getPosition().getX(), 
													   bi.getPosition().getY(), 
													   bj.getShape().getBounds(), 
													   bj.getPosition().getX(), 
													   bj.getPosition().getY())) {

					arbiters.remove(new Arbiter(bi,bj));
					continue;
				}

				Arbiter newArb = new Arbiter(bi, bj);
				newArb.collide();
				
				if (newArb.getNumContacts() > 0)
				{
					if (arbiters.contains(newArb)) {
						int index = arbiters.indexOf(newArb);
						Arbiter arb = arbiters.get(index);
						arb.update(newArb.getContacts(), newArb.getNumContacts());
					} else {
						arbiters.add(newArb);
						newArb.init();
					}
				}
				else
				{
					arbiters.remove(newArb);
				}
			}
		}
	}
	
	/**
	 * Get the total energy in the system
	 * 
	 * @return The total energy in all the bodies in the system
	 */
	public float getTotalEnergy() {
		float total = 0;
		
		for (int i=0;i<bodies.size();i++) {
			total += bodies.get(i).getEnergy();
		}
		
		return total;
	}
}
