package it.aiv;

import static org.junit.Assert.*;



import org.junit.Test;


public class TestMapGenerator {
	
	@Test
	public void TestMap2x2AllOnes() {
		Dungeon dungeon = new Dungeon(2, 2);		
		int[][] map = dungeon.GenerateRandomMap(0);
		map = dungeon.TransformMap(map, 1);
		
		assertArrayEquals(map, new int[][] {{1,1},{1,1}});
	}
	
	@Test
	public void TestMap2x2AllOnesAfterSteps() {
		Dungeon dungeon = new Dungeon(2, 2);		
		int[][] map = dungeon.GenerateRandomMap(0);
		map = dungeon.TransformMap(map, 10);
		
		assertArrayEquals(map, new int[][]{{1,1}, {1,1}});
	}
	
	/**
	 * Tests for seed in random map generation
	 */
	@Test
	public void TestMapSameMapsBySeed()
	{
		Dungeon dungeon = new Dungeon(10, 20);		
		int[][] map1 = dungeon.GenerateRandomMap(2);
		int[][] map2 = dungeon.GenerateRandomMap(2);
		
		assertArrayEquals(map1, map2);
	}
	
	@Test
	public void TestMapDifferentMapsBySeed()
	{
		Dungeon dungeon = new Dungeon(10, 20);		
		int[][] map1 = dungeon.GenerateRandomMap(2);
		int[][] map2 = dungeon.GenerateRandomMap(4);
		
		assertTrue(map1 != map2);
	}
	
	/**
	 * Tests for Dungeon's method SetNewValue
	 */
	@Test
	public void TestMap3x3MiddleIsZeroToZero() {
		Dungeon dungeon = new Dungeon(3, 3);		
		int[] array = new int[] { 0, 0, 1, 1, 0, 1, 0, 0 };
		
		assertEquals(dungeon.SetNewValue(0, array), 0);
	}
	
	@Test
	public void TestMap3x3MiddleIsZeroToOne() {
		Dungeon dungeon = new Dungeon(3, 3);		
		int[] array = new int[] { 1, 1, 1, 1, 1, 1, 0, 0 };

		assertEquals(dungeon.SetNewValue(0, array), 1);
	}
	
	@Test
	public void TestMap3x3MiddleIsOneToZero() {
		Dungeon dungeon = new Dungeon(3, 3);	
		int[] array = new int[] { 0, 0, 0, 1, 0, 1, 0, 0 };
		
		assertEquals(dungeon.SetNewValue(1, array), 0);
	}
	
	@Test
	public void TestMap3x3MiddleIsOneToOne() {
		Dungeon dungeon = new Dungeon(3, 3);		
		int[] array = new int[] { 1, 0, 1, 1, 0, 1, 0, 0 };

		assertEquals(dungeon.SetNewValue(1, array), 1);
	}

	/**
	 * Tests for Linking
	 */
	@Test
	public void TestLinkingAllOnesAround()
	{
		Dungeon dungeon = new Dungeon(5, 5);
		
		int[][] map = new int[][] {
			{1,1,1,1,1},
			{1,1,1,1,1},
			{1,1,0,1,1},
			{1,1,1,1,1},
			{1,1,1,1,1}
		};
		int[][] initial = map;
		
		map = dungeon.Linking(map,  2);
		
		assertArrayEquals(map, initial);
	}
	
	@Test
	public void TestLinking()
	{
		Dungeon dungeon = new Dungeon(5, 5);
		
		int[][] map = new int[][] {
			{0,1,1,0,1},
			{1,1,1,1,1},
			{1,1,0,1,0},
			{1,0,0,1,1},
			{1,1,0,1,0}
		};
		int[][] initial = new int[][] {
			{0,1,1,0,1},
			{1,1,1,1,1},
			{1,1,0,1,0},
			{1,0,0,1,1},
			{1,1,0,1,0}
		};
		
		map = dungeon.Linking(map,  2);
		
		assertFalse(map == initial);
	}
	
	
	/**
	 * Tests for Flooding
	 */
	@Test
	public void TestFloodingAllOnesAround()
	{
		Dungeon dungeon = new Dungeon(5, 5);
		
		int[][] map = new int[][] {
			{1,1,1,1,1},
			{1,1,1,1,1},
			{1,1,0,1,1},
			{1,1,1,1,1},
			{1,1,1,1,1}
		};
		
		map = dungeon.Flooding(map, false);
		
		assertEquals(map[2][2], 2);
	}
	
	@Test
	public void TestFlooding()
	{
		Dungeon dungeon = new Dungeon(5, 5);
		
		int[][] map = new int[][] {
			{1,1,1,1,1},
			{1,0,0,0,1},
			{1,1,0,0,1},
			{1,1,1,0,1},
			{1,1,1,1,1}
		};
		int[][] finalM = new int[][] {
			{1,1,1,1,1},
			{1,2,2,2,1},
			{1,1,2,2,1},
			{1,1,1,2,1},
			{1,1,1,1,1}
		};
		
		map = dungeon.Flooding(map, false);
		
		assertArrayEquals(map, finalM);
	}
	
	@Test
	public void TestFloodingTwoIsles()
	{
		Dungeon dungeon = new Dungeon(7, 5);
		
		int[][] map = new int[][] {
			{1,1,1,1,1,1,1},
			{1,0,0,1,0,0,1},
			{1,1,0,1,0,0,1},
			{1,1,1,1,0,0,1},
			{1,1,1,1,1,1,1}
		};
		int[][] finalM = new int[][] {
			{1,1,1,1,1,1,1},
			{1,1,1,1,0,0,1},
			{1,1,1,1,0,0,1},
			{1,1,1,1,0,0,1},
			{1,1,1,1,1,1,1}
		};
		
		map = dungeon.Flooding(map, true);
		
		assertArrayEquals(map, finalM);
	}
	
	@Test
	public void TestFloodingNotRepeat()
	{
		Dungeon dungeon = new Dungeon(7, 5);
		
		int[][] map = new int[][] {
			{1,1,1,1,1,1,1},
			{1,0,0,1,0,0,1},
			{1,1,0,1,0,0,1},
			{1,1,1,1,0,0,1},
			{1,1,1,1,1,1,1}
		};
		int[][] finalM = new int[][] {
			{1,1,1,1,1,1,1},
			{1,2,2,1,3,3,1},
			{1,1,2,1,3,3,1},
			{1,1,1,1,3,3,1},
			{1,1,1,1,1,1,1}
		};
		
		map = dungeon.Flooding(map, false);
		map = dungeon.Flooding(map, false);
		
		assertArrayEquals(map, finalM);
	}
	
	/**
	 * Tests for Cropping
	 */
	@Test
	public void TestCrop()
	{
		Dungeon dungeon = new Dungeon(0, 0);
		
		int[][] map = new int[][] {
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,0,0,0,1,1,1},
			{1,1,1,1,0,0,1,1,1},
			{1,1,1,1,1,0,0,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1}
		};
		
		int[][] finalM = new int[][] {
			{1,1,1,1,1,1},
			{1,0,0,0,1,1},
			{1,1,0,0,1,1},
			{1,1,1,0,0,1},
			{1,1,1,1,1,1},
		};
		
		map = dungeon.Crop(map, 1);
		
		assertArrayEquals(map, finalM);
	}
	
	@Test
	public void TestCropBorderOfTwo()
	{
		Dungeon dungeon = new Dungeon(0, 0);
		
		int[][] map = new int[][] {
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,0,0,0,1,1,1},
			{1,1,1,1,0,0,1,1,1},
			{1,1,1,1,1,0,0,1,1},
			{1,1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1,1}
		};
		
		int[][] finalM = new int[][] {
			{1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1},
			{1,1,0,0,0,1,1,1},
			{1,1,1,0,0,1,1,1},
			{1,1,1,1,0,0,1,1},
			{1,1,1,1,1,1,1,1},
			{1,1,1,1,1,1,1,1}
		};
		
		int[][] crop = dungeon.Crop(map, 2);
		
		assertArrayEquals(crop, finalM);
	}
}
