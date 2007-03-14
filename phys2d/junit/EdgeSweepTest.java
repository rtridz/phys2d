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
package net.phys2d.raw.test.collide;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.collide.EdgeSweep;
import junit.framework.TestCase;

public class EdgeSweepTest extends TestCase {
	
	public void assertEquals(int[][] a, int[][] b) {
		if ( a.length != b.length )
			fail("Expected an array of size " + a.length + " but it has length " + b.length);
		
		for ( int i = 0; i < a.length; i++ ) {
			if ( a[i].length != b[i].length )
				fail("Expected an array of size " + a[i].length + " but it has length " + b[i].length);
			
			for ( int j = 0; j < a[i].length; j++ ) {
				assertEquals(a[i][j], b[i][j]);
			}
		}
	}

	public void testSweep() {
		{ // first sweep on an empty list
			EdgeSweep sweep = new EdgeSweep(new Vector2f());
			assertEquals(0, sweep.getOverlappingEdges().length);
		}
		
		{ // sweep with two infinite lines
			EdgeSweep sweep = new EdgeSweep(new Vector2f());
			sweep.insert(0, true, 1);
			sweep.insert(1, false, 1);
			int[][] result = sweep.getOverlappingEdges();
			int[][] reference = {{0,1}};
			assertEquals(reference, result);
		}
		
		{ // sweep with two finite equal lines
			EdgeSweep sweep = new EdgeSweep(new Vector2f());
			sweep.insert(0, true, 1);
			sweep.insert(0, true, 2);
			sweep.insert(1, false, 1);
			sweep.insert(1, false, 2);
			int[][] result = sweep.getOverlappingEdges();
			int[][] reference = {{0,1}};
			assertEquals(reference, result);
		}
		
		{ // sweep with a dot-line
			EdgeSweep sweep = new EdgeSweep(new Vector2f());
			sweep.insert(0, true, 0.5f);
			sweep.insert(0, true, 0.5f);
			sweep.insert(1, false, 1);
			sweep.insert(1, false, 2);
			assertEquals(0, sweep.getOverlappingEdges().length);
		}
		
		{ // sweep with two non overlapping lines
			EdgeSweep sweep = new EdgeSweep(new Vector2f());
			sweep.insert(0, true, 3);
			sweep.insert(0, true, 4);
			sweep.insert(1, false, 1);
			sweep.insert(1, false, 2);
			assertEquals(0, sweep.getOverlappingEdges().length);;
		}
		
		{ // sweep with two lines sharing a point
			EdgeSweep sweep = new EdgeSweep(new Vector2f());
			sweep.insert(0, true, 2);
			sweep.insert(0, true, 4);
			sweep.insert(1, false, 1);
			sweep.insert(1, false, 2);
			assertEquals(1, sweep.getOverlappingEdges().length);
		}		
		{ // sweep with two lines sharing a point (different insertion order)
			EdgeSweep sweep = new EdgeSweep(new Vector2f());
			sweep.insert(0, true, 2);
			sweep.insert(1, false, 1);
			sweep.insert(0, true, 4);
			sweep.insert(1, false, 2);
			assertEquals(1, sweep.getOverlappingEdges().length);
		}
	}
}
