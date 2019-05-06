package it.aiv;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dungeon {
	
	int _width, _height;
	int[][] _innerMap;
	
	int[][] m;
	
	public Dungeon(int width, int height) {
		_width = width;
		_height = height;
		
		_innerMap = new int[_height][_width];
		
		m = new int[_height][_width];
	}
	
	public int[][] GenerateRandomMap(int seed)
	{
		System.out.println("\nRandom Generation:\n");
		
		Random random = new Random(seed);
		
		int[][] matrix = new int[_height][_width];
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = random.nextInt(2);
			}
		}
		
		_innerMap = matrix;
		
		PrintMatrix(matrix);
		
		return matrix;
	}
	
	public int[][] TransformMap(int[][] matrix, int steps)
	{		
		System.out.println("\nTransforming map:\n");
		
		/* applicando la legge di Moore:
		   - per ogni cella prendo tutte le celle vicine (fino a 9 item totali).
		   Applicando la legge di Von Neumann:
		   - per ogni cella prendo le celle vicine cardinalmente (a croce) (fino a 5 item totali).
		   Infine stabilisco a scelta che valore assumerà la cella, almeno come.
		   Tutto questo ripetuto per steps.
		*/
				
		int[] fieldMoore;
		for (int s = 0; s < steps; s++) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					
					if (i == 0 || j == 0 || i == matrix.length - 1 || j == matrix[0].length - 1) {
						matrix[i][j] = 1;						
					}
					else {
						fieldMoore = new int[]
						{
							matrix[i-1][j-1],
							matrix[i-1][j],
							matrix[i-1][j+1],
							matrix[i][j-1],
							//matrix[i][j],
							matrix[i][j+1],
							matrix[i+1][j-1],
							matrix[i+1][j],
							matrix[i+1][j+1],
						};
					
						matrix[i][j] = SetNewValue(matrix[i][j], fieldMoore);
					}
				}
			}
		}
		
		_innerMap = matrix;
		
		PrintMatrix(matrix);
		
		return matrix;
	}
	
	public int SetNewValue(int current, int[] array)
	{
		int wallCount = 0;
		
		for (int i = 0; i < array.length; i++) {			
			wallCount += array[i] == 1 ? 1 : 0;		
		}	
		
		return current == 1 ? (wallCount > 3 ? 1 : 0) : (wallCount > 4 ? 1 : 0);		
	}
	
	public int[][] Linking(int[][] matrix, int maxdist)
	{	
		System.out.println("\nLinking Islands:\n");
		
		int x = 0, y = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 0)
				{
					int currVdist = maxdist;
					if (i + maxdist > matrix.length) {
						currVdist -= i + maxdist - matrix.length;
					}
					int currHdist = maxdist;
					if (j + maxdist > matrix[0].length) {
						currHdist -= j + maxdist - matrix[0].length;
					}
					for (int k = i; k < i + currVdist; k++) {
						if (matrix[i + (currVdist - k)][j] == 0) {
							for (int kIn = i; kIn < i + (currVdist - k); kIn++) {
								if (matrix[kIn][i] == 1)
									matrix[kIn][i] = 0;
							}
						}
					}
					
					for (int k = j; k < j + currHdist; k++) {
						if (matrix[i][j + (currVdist - k)] == 0) {
							for (int kIn = j; kIn < j + (currVdist - k); kIn++) {
								if (matrix[i][kIn] == 1)
									matrix[i][kIn] = 0;
							}
						}
					}
				}
			}
		}
		
		PrintMatrix(matrix);
		
		return matrix;
	}

	public int[][] Flooding(int [][] matrix, boolean eraseMinorShapes)
	{	
		System.out.println("\nFlooding:\n");
		
		int address = 1;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 0) {					
					address++;
					Fill(matrix, i, j, address);
				}
			}
		}
		
		if (address == 1) return matrix;
		
		int[] addresses = new int[address - 1];		
		for (int ad = 0; ad < addresses.length; ad++) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					if (matrix[i][j] == ad + 2) {					
						addresses[ad]++;
					}
				}
			}	
		}
		
		int max = 0;
		int maxIndex = 0;
		for (int i = 0; i < addresses.length; i++) {
			if (addresses[i] > max) {
				max = addresses[i];
				maxIndex = i;
			}			
		}
		
		if (!eraseMinorShapes) return matrix;
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] != maxIndex + 2) {					
					matrix[i][j] = 1;
				}
				else matrix[i][j] = 0;
			}
		}
		
		PrintMatrix(matrix);
		
		return matrix;
	}
	
	public void Fill(int[][] matrix, int x, int y, int replace)
	{
		if (x < 0 || x > matrix.length || y < 0 || y > matrix[0].length) return;
		if (matrix[x][y] != 0) 
			return;
		matrix[x][y] = replace;
		
		Fill(matrix, x-1, y, replace);
		Fill(matrix, x+1, y, replace);
		Fill(matrix, x, y-1, replace);
		Fill(matrix, x, y+1, replace);
	}
	
	public int[][] Crop(int[][] matrix, int border)
	{
		System.out.println("\nCropping:\n");
		
		int minX = matrix[0].length, minY = matrix.length, maxX = 0, maxY = 0;
		
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				if (matrix[y][x] == 0) {
					minY = Math.min(minY, y);
					minX = Math.min(minX, x);
					maxY = Math.max(maxY, y);
					maxX = Math.max(maxX, x);					
				}					
			}
		}
		
		//Cropping
		int[][] cropped = new int[maxY - minY + 1][maxX - minX + 1];
		for (int y = 0; y < cropped.length; y++) {
			for (int x = 0; x < cropped[0].length; x++) {
				cropped[y][x] = matrix[y + minY][x + minX];
			}
		}		
		
		// Bordering
		if (border == 0) return cropped;
		int[][] bordered = new int[cropped.length + (border * 2)][cropped[0].length + (border * 2)];
		for (int y = 0; y < bordered.length; y++) {
			for (int x = 0; x < bordered[0].length; x++) {
				if (y < border || y > bordered.length - 1 - border || x < border || x > bordered[0].length - 1 - border) {
					bordered[y][x] = 1;
					continue;
				}
				bordered[y][x] = cropped[y - border][x - border];
			}
		}
		
		PrintMatrix(bordered);
		
		return bordered;
	}
	
	public int[][] HorizontalBlanking(int[][] matrix, int lines, int fillWith)
	{
		System.out.println("\nHorizontal Blanking:\n");
		
		if (_height < 3) return matrix;
		int midMap = _height % 2 == 0 ? _height / 2 : _height / 2 + 1;
		
		for (int i = midMap - lines; i < midMap + lines; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = fillWith;
			}
		}
		
		PrintMatrix(matrix);
		
		return matrix;
	}	
	
	public int[][] GetLastMapState()
	{
		return _innerMap;
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
