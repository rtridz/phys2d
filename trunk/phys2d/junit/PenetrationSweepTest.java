package net.phys2d.raw.test.collide;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.collide.Intersection;
import net.phys2d.raw.collide.PenetrationSweep;
import net.phys2d.raw.collide.PenetrationSweep.ContourWalker;
import junit.framework.TestCase;

public class PenetrationSweepTest extends TestCase {

	public void testGetPenetrationDepth() {
		{
			// +--------+                                                   
			// |        |                                                   
			// |   +    |                                                  
			// |  / \   |                                                   
			// +-x---x--+                                                   
			//  /     \                                                     
			// /_______\                                                    
			Vector2f[] vertsA = {new Vector2f(0,0), new Vector2f(2,0), new Vector2f(2,2), new Vector2f(0,2)};
			Vector2f[] vertsB = {new Vector2f(0,-1), new Vector2f(2,-1), new Vector2f(1,1)};
			Intersection in = new Intersection(0, 2, new Vector2f(0.5f, 0), true);
			Intersection out = new Intersection(0, 1, new Vector2f(1.5f, 0), false);
			Vector2f normal = new Vector2f(0,-1);
			assertEquals(1f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsA, vertsB), 0f);
			out = new Intersection(2, 0, new Vector2f(0.5f, 0), false);
			in = new Intersection(1, 0, new Vector2f(1.5f, 0), true);
			normal = new Vector2f(0,1);
			assertEquals(1f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsB, vertsA), 0f);
		}
		{
			//   +-------------+
			//    \+---------+/                                                  
			//     \         /                                                   
			//     |\       /|                                                  
			//     | \     / |                                                   
			//     +--x---x--+                                                   
			//         \ /                                                       
			//          +                                                        
			Vector2f[] vertsA = {new Vector2f(0,0), new Vector2f(2,0), new Vector2f(2,2), new Vector2f(0,2)};
			Vector2f[] vertsB = {new Vector2f(-1,2), new Vector2f(1,-1), new Vector2f(3,2)};
			Intersection in = new Intersection(0, 0, new Vector2f(0.5f, 0), true);
			Intersection out = new Intersection(0, 1, new Vector2f(1.5f, 0), false);
			Vector2f normal = new Vector2f(0,-1);
			assertEquals(2f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsA, vertsB), 0f);
			out = new Intersection(0, 0, new Vector2f(0.5f, 0), false);
			in = new Intersection(1, 0, new Vector2f(1.5f, 0), true);
			normal = new Vector2f(0,1);
			assertEquals(2f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsB, vertsA), 0f);
		}
		{                                               
			//      +---------+                                            
			//      |         |                                             
			// +----x----+    |
			// |    |    |    |                                              
			// |    +----x----+                                              
			// |         | 
			// +---------+                                                   

			Vector2f[] vertsA = {new Vector2f(0,0), new Vector2f(2,0), new Vector2f(2,2), new Vector2f(0,2)};
			Vector2f[] vertsB = {new Vector2f(1,1), new Vector2f(3,1), new Vector2f(3,3), new Vector2f(1,3)};
			Intersection in = new Intersection(1, 0, new Vector2f(2, 1), true);
			Intersection out = new Intersection(2, 3, new Vector2f(1, 2), false);
			Vector2f normal = new Vector2f(0,1);
			assertEquals(1f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsA, vertsB), 0f);
			normal = new Vector2f(1,1);
			normal.normalise();
			assertEquals((float) Math.sqrt(2), PenetrationSweep.getPenetrationDepth(in, out, normal, vertsA, vertsB), 0);
			out = new Intersection(0, 1, new Vector2f(2, 1), false);
			in = new Intersection(3, 2, new Vector2f(1, 2), true);
			normal = new Vector2f(-1,-1);
			normal.normalise();
			assertEquals((float) Math.sqrt(2), PenetrationSweep.getPenetrationDepth(in, out, normal, vertsB, vertsA), 0.00001f);
		}
		{                                               
			//  +------------+                                                                
			//  |            |                                               
			//  |   ------   |                                              
			//  |  /      \  |                                               
			//  +-x--------x-+                                               
			//   /          \                                                
			//  /            \
			// /______________\

			Vector2f[] vertsA = {new Vector2f(0,0), new Vector2f(3,0), new Vector2f(3,2), new Vector2f(0,2)};
			Vector2f[] vertsB = {new Vector2f(-0.5f,-1.5f), new Vector2f(3.5f,-1.5f), new Vector2f(2,1), new Vector2f(1,1)};
			Intersection in = new Intersection(0, 3, new Vector2f(0.5f, 0), true);
			Intersection out = new Intersection(0, 1, new Vector2f(2.5f, 0), false);
			Vector2f normal = new Vector2f(0,-1);
			assertEquals(1f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsA, vertsB), 0f);
			out = new Intersection(3, 0, new Vector2f(0.5f, 0), false);
			in = new Intersection(1, 0, new Vector2f(2.5f, 0), true);
			normal = new Vector2f(0,1);
			assertEquals(1f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsB, vertsA), 0f);
		}
		{                                               
			//   ----------------                                                               
			//   \              /                                              
			//    \   /`-._    /                                             
			//     \ /      \ /                                                
			//      x        x                                                 
			//     / \     _/ \                                                
			//    /   \_.-'    \
			//   /______________\

			Vector2f[] vertsA = {new Vector2f(0,3), new Vector2f(1.1f,0.8f), new Vector2f(2,1), new Vector2f(3,3)};
			Vector2f[] vertsB = {new Vector2f(0,0), new Vector2f(3,0), new Vector2f(2,2), new Vector2f(1.1f,2.2f)};
			Intersection in = new Intersection(0, 3, new Vector2f(0.5f, 1.5f), true);
			Intersection out = new Intersection(2, 1, new Vector2f(2.5f, 1.5f), false);
			Vector2f normal = new Vector2f(0,-1);
			assertEquals(1.4f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsA, vertsB), 0.000001f);
			out = new Intersection(3, 0, new Vector2f(0.5f, 1.5f), false);
			in = new Intersection(1, 2, new Vector2f(2.5f, 1.5f), true);
			normal = new Vector2f(0,1);
			assertEquals(1.4f, PenetrationSweep.getPenetrationDepth(in, out, normal, vertsB, vertsA), 0.000001f);
		}
		{                                               
			//   +--------------------+                                                             
			// +-|---+                |                                            
			//  \|   |                |                                          
			//   x   |                |                                            
			//   |\  |                |                                            
			//   +-x------------------+                                         
			//      \|       
			//                  

			Vector2f[] vertsA = {new Vector2f(2,0), new Vector2f(2,2), new Vector2f(0,2)};
			Vector2f[] vertsB = {new Vector2f(1,3), new Vector2f(1,1), new Vector2f(10,1), new Vector2f(10,3)};
			// note that these intersection positions do not occur in practice, but they server their purpose
			Intersection in = new Intersection(2, 0, new Vector2f(0.9f, 1.1f), true); 
			Intersection out = new Intersection(2, 1, new Vector2f(1.0f, 1.0f), false);
			Vector2f normal = new Vector2f(-1,-1);
			normal.normalise();
			assertEquals((float) Math.sqrt(8), PenetrationSweep.getPenetrationDepth(in, out, normal, vertsA, vertsB), 0.000001f);
			out = new Intersection(0, 2, new Vector2f(0.9f, 1.1f), false); 
			in = new Intersection(1, 2, new Vector2f(1.0f, 1.0f), true);
			normal = new Vector2f(1,1);
			normal.normalise();
			assertEquals((float) Math.sqrt(8), PenetrationSweep.getPenetrationDepth(in, out, normal, vertsB, vertsA), 0.000001f);
		}
	}
	
	public void testContourWalkerConstructor() {
		{
			Vector2f[] vertsA = {new Vector2f(1,1), new Vector2f(3,1), new Vector2f(2,2)};
			PenetrationSweep ps = new PenetrationSweep(new Vector2f(0,1), new Vector2f(1,0), new Vector2f(-100,-100), new Vector2f(100,100));
			ContourWalker cw = ps.new ContourWalker(vertsA, 0, 1, false);
			
			assertEquals(1f, cw.getDistance(), Float.MIN_VALUE);
			assertEquals(3f, cw.getNextDistance(), Float.MIN_VALUE);
			assertEquals(1f, cw.getPenetration(), Float.MIN_VALUE);
			assertEquals(1f, cw.getPenetration(1.5f), Float.MIN_VALUE);
		}
		
		{
			Vector2f[] vertsA = {new Vector2f(1,1), new Vector2f(3,1), new Vector2f(2,2)};
			PenetrationSweep ps = new PenetrationSweep(new Vector2f(0,1), new Vector2f(1,0), new Vector2f(-100,-100), new Vector2f(100,100));
			ContourWalker cw = ps.new ContourWalker(vertsA, 0, 1, true);
			
			assertEquals(3f, cw.getDistance(), Float.MIN_VALUE);
			assertEquals(1f, cw.getNextDistance(), Float.MIN_VALUE);
			assertEquals(1f, cw.getPenetration(), Float.MIN_VALUE);
			assertEquals(1f, cw.getPenetration(1.5f), Float.MIN_VALUE);
		}
		
		{
			Vector2f[] vertsA = {new Vector2f(1,1), new Vector2f(3,1), new Vector2f(2,2)};
			PenetrationSweep ps = new PenetrationSweep(new Vector2f(0,1), new Vector2f(1,0), new Vector2f(-100,-100), new Vector2f(100,100));
			ContourWalker cw = ps.new ContourWalker(vertsA, 2, 2, false);
			
			assertEquals(2f, cw.getDistance(), Float.MIN_VALUE);
			assertEquals(1f, cw.getNextDistance(), Float.MIN_VALUE);
			assertEquals(2f, cw.getPenetration(), Float.MIN_VALUE);
			assertEquals(1.5f, cw.getPenetration(1.5f), Float.MIN_VALUE);
		}
		
		{
			Vector2f[] vertsA = {new Vector2f(1,1), new Vector2f(3,1), new Vector2f(2,2)};
			PenetrationSweep ps = new PenetrationSweep(new Vector2f(0,1), new Vector2f(1,0), new Vector2f(-100,-100), new Vector2f(100,100));
			ContourWalker cw = ps.new ContourWalker(vertsA, 2, 2, true);
			
			assertEquals(2f, cw.getDistance(), Float.MIN_VALUE);
			assertEquals(3f, cw.getNextDistance(), Float.MIN_VALUE);
			assertEquals(2f, cw.getPenetration(), Float.MIN_VALUE);
			assertEquals(1.5f, cw.getPenetration(2.5f), Float.MIN_VALUE);
		}
	}
	
	public void testContourWalkerHasNext() {
		Vector2f[] vertsA = {new Vector2f(1,1), new Vector2f(3,1), new Vector2f(2,2)};
		PenetrationSweep ps = new PenetrationSweep(new Vector2f(0,1), new Vector2f(1,0), new Vector2f(-100,-100), new Vector2f(100,100));
		ContourWalker cw;
		
		cw = ps.new ContourWalker(vertsA, 0, 0, false);
		assertFalse(cw.hasNext());
		
		cw = ps.new ContourWalker(vertsA, 0, 0, true);
		assertFalse(cw.hasNext());
		
		cw = ps.new ContourWalker(vertsA, 0, 1, false);
		assertTrue(cw.hasNext());
		cw.next();
		assertFalse(cw.hasNext());
		
		cw = ps.new ContourWalker(vertsA, 0, 1, true);
		assertTrue(cw.hasNext());
		cw.next();
		assertFalse(cw.hasNext());
		
		cw = ps.new ContourWalker(vertsA, 1, 0, false);
		assertTrue(cw.hasNext());
		cw.next();
		assertTrue(cw.hasNext());
		cw.next();
		assertFalse(cw.hasNext());
		
		cw = ps.new ContourWalker(vertsA, 1, 0, true);
		assertTrue(cw.hasNext());
		cw.next();
		assertTrue(cw.hasNext());
		cw.next();
		assertFalse(cw.hasNext());
	}
	
	public void testContourWalkerReverse() {
		{
			Vector2f[] vertsA = {new Vector2f(1,1), new Vector2f(3,1), new Vector2f(2,2)};
			PenetrationSweep ps = new PenetrationSweep(new Vector2f(0,1), new Vector2f(1,0), new Vector2f(-100,-100), new Vector2f(100,100));
			ContourWalker cw = ps.new ContourWalker(vertsA, 0, 2, false);
			
			assertTrue(cw.hasNext());
			cw.next();
			assertTrue(cw.hasNext());
			cw.next();
			assertFalse(cw.hasNext());
			cw.reverse();
			assertTrue(cw.hasNext());
			cw.next();
			assertTrue(cw.hasNext());
			cw.next();
			assertFalse(cw.hasNext());
			
		}
	}
//	
//	public void testGetMaxSeparation() {
//		{
//			Vector2f[] vertsA = {new Vector2f(3,0), new Vector2f(2,2), new Vector2f(1,1), new Vector2f(0,0)};
//			Vector2f[] vertsB = {new Vector2f(0,0), new Vector2f(1,-1), new Vector2f(2,-1), new Vector2f(3,0)};
//			EdgeSweep sweep = new EdgeSweep();
//			sweep.insert(0, true, 3);
//			sweep.insert(1, true, 2);
//			sweep.insert(2, true, 1);
//			sweep.insert(3, true, 0);
//			sweep.insert(3, false, 0);
//			sweep.insert(2, false, 1);
//			sweep.insert(1, false, 2);
//			sweep.insert(0, false, 3);
//			assertEquals(3f, sweep.getMaxSeparation(vertsA, vertsB, new Vector2f(0,1), new Vector2f(0,0)), 0f);
//		}
//		
//		{
//			Vector2f[] vertsA = {new Vector2f(3,1), new Vector2f(2,3), new Vector2f(1,2), new Vector2f(0,1)};
//			Vector2f[] vertsB = {new Vector2f(0,1), new Vector2f(1,0), new Vector2f(2,0), new Vector2f(3,1)};
//			EdgeSweep sweep = new EdgeSweep();
//			sweep.insert(0, true, 3);
//			sweep.insert(1, true, 2);
//			sweep.insert(2, true, 1);
//			sweep.insert(3, true, 0);
//			sweep.insert(3, false, 0);
//			sweep.insert(2, false, 1);
//			sweep.insert(1, false, 2);
//			sweep.insert(0, false, 3);
//			assertEquals(3f, sweep.getMaxSeparation(vertsA, vertsB, new Vector2f(0,1), new Vector2f(0,1)), 0f);
//		}
//	}

}
