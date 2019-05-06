package it.aiv;


public class Main {

	public static void main(String[] args) {
		/*
		if (args.length == 1 && args[0] == "--help")
		{
			System.out.println("    This program generates a random 2D map that can be used for procedural maps.");	
			System.out.println("    First argument: Map width;");
			System.out.println("    Second argument: Map height;");
			System.out.println("    Third argument: Map Transofmation steps;");
			System.out.println("    First argument: Seed used for random generation;");
			System.out.println("    If you don't specify a parameter, the program will provide to use a default value.");
			return;
		}	*/	
		
		int width = args.length > 0 ? Integer.parseInt(args[0]) : 64;
		int height = args.length > 0 ? Integer.parseInt(args[1]) : 64;
		int steps = args.length > 0 ? Integer.parseInt(args[2]) : 16;
		int seed = args.length > 0 ? Integer.parseInt(args[3]) : 48;

		Dungeon dungeon = new Dungeon(width, height);		
		
		// 1: New map
		int[][] map = dungeon.GenerateRandomMap(seed);
		
		// 1.5: Horizontal Blanking
		//map = dungeon.HorizontalBlanking(map, height / 10, 1);
		
		// 2: Transform map <steps> times
		map = dungeon.TransformMap(map, steps);
		
		// 3a: Linking Islands
		//map = dungeon.Linking(map, 3);
		
		// 3b: Flooding
		map = dungeon.Flooding(map, true);
		
		// 3b.5: Cropping
		map = dungeon.Crop(map, 2);
	}
	
	
	public static void PrintMatrix(int[][] matrix)
	{
		String s = "";
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {	
				s += (matrix[i][j] == 0 ? " " : matrix[i][j]) + " ";
			}			
			s += "\n";
		}
		System.out.println(s);
	}
	
	
}
