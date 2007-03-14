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
import net.phys2d.raw.collide.FeaturePair;
import net.phys2d.raw.collide.IntersectionGatherer;
import junit.framework.TestCase;

public class FeaturePairListTest extends TestCase {
	
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
	
	public void assertEquals(FeaturePair[] a, FeaturePair[] b) {
		if ( a.length != b.length )
			fail("Expected an array of size " + a.length + " but it has length " + b.length);
		
		for ( int i = 0; i < a.length; i++ ) {
			if ( !a[i].equals(b[i]) )
				fail(b[i].toString() + " does not equal " + a[i].toString());
		}
	}

	public void testGetFeaturePairs() {
//		{
//			IntersectionGatherer fpl = new IntersectionGatherer(5);
//			fpl.addInFeature(0, 2);
//			fpl.addOutFeature(0, 1);
//			FeaturePair[] reference = {new FeaturePair(0,2,0,1)};
//			assertEquals(reference, fpl.getFeaturePairs());
//		}
//		{
//			IntersectionGatherer fpl = new IntersectionGatherer(5);
//			fpl.addOutFeature(2, 0);
//			fpl.addInFeature(1, 0);
//			FeaturePair[] reference = {new FeaturePair(1,0,2,0)};
//			assertEquals(reference, fpl.getFeaturePairs());
//		}
//		{
//			IntersectionGatherer fpl = new IntersectionGatherer(5);
//			fpl.addInFeature(3, 4);
//			fpl.addOutFeature(0, 3);
//			fpl.addOutFeature(1, 2);
//			fpl.addInFeature(0, 2);
//			FeaturePair[] reference = {new FeaturePair(0,2,1,2), new FeaturePair(3,4,0,3)};
//			assertEquals(reference, fpl.getFeaturePairs());
//		}
//		{
//			IntersectionGatherer fpl = new IntersectionGatherer(10);
//			fpl.addOutFeature(0, 0);
//			fpl.addInFeature(0, 4);
//			fpl.addOutFeature(0, 5);
//			fpl.addInFeature(0, 6);
//			fpl.addOutFeature(0, 3);
//			fpl.addInFeature(2, 3);
//			fpl.addOutFeature(2, 4);
//			fpl.addInFeature(2, 5);
//			fpl.addOutFeature(2, 6);
//			fpl.addInFeature(2, 0);
//			FeaturePair[] reference = {new FeaturePair(2,3,0,3)};
//			assertEquals(reference, fpl.getFeaturePairs());
//		}
	}

//	public void testSortFeatures() {
//		{ // empty list
//			int[][] emptyList = {};
//			int[][] reference = {};
//			IntersectionGatherer.sortFeatures(emptyList,0);
//			
//			assertEquals(reference, emptyList);
//		}
//		
//		{ // list of one element
//			int[][] list = {{1,0}};
//			int[][] reference = {{1,0}};
//			IntersectionGatherer.sortFeatures(list,1);
//			
//			assertEquals(reference, list);
//		}
//		
//		{ // already sorted list
//			int[][] list = {{0,1},{1,0},{3,5},{10,-1}};
//			int[][] reference = {{0,1},{1,0},{3,5},{10,-1}};
//			IntersectionGatherer.sortFeatures(list,4);
//			
//			assertEquals(reference, list);
//		}
//		
//		{ // already sorted list with duplicate
//			int[][] list = {{0,1},{1,0},{3,5},{3,6},{10,-1}};
//			int[][] reference = {{0,1},{1,0},{3,6},{3,5},{10,-1}};
//			IntersectionGatherer.sortFeatures(list,5);
//			
//			assertEquals(reference, list);
//		}
//		
//		{ // unsorted list with duplicate
//			int[][] list = {{3,5},{10,-1},{1,0},{3,6},{0,1}};
//			int[][] reference = {{0,1},{1,0},{3,6},{3,5},{10,-1}};
//			IntersectionGatherer.sortFeatures(list,5);
//			
//			assertEquals(reference, list);
//		}
//	}

}

