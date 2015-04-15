//© A+ Computer Science  -  www.apluscompsci.com
//Name - Cameron O'Neil

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathFinder
{
	public static void main( String args[] ) throws IOException
	{
//		for (int i = 0; i < 20; i++){
//		int i =4;
			Maze m = new Maze(i);
			System.out.println(m);
			System.out.println((m.hasExitPath(0,0) ? "Exit Found":"There is no escape") + "\n\n");
			
			System.out.println(m.getShortest());
//		}
		
		
	}
}

class Maze
{
   private int[][] maze;
   private ArrayList<Point> marked = new ArrayList<Point>();

	public Maze(int size)
	{
		maze = new int[size][size];
		
		for (int r =0; r < size; r++)
			for (int c =0; c < size; c++)
				maze[r][c] = Math.random() > .5 ? 1:0;
	}

	public boolean hasExitPath(int r, int c)
	{
		
		if (r>=0 && c>=0 && r<maze.length && c < maze.length && maze[r][c] == 1 && !isMarked(r,c)){
			if (c == maze.length-1)
				return true;
			mark(r,c);
			return hasExitPath(r+1,c) || hasExitPath(r-1,c) || hasExitPath(r,c+1) || hasExitPath(r,c-1);
		}
		
//		System.out.println("[" + r + "] [" + c + "] = " + false);
		return false;
	}
	
	
	private ArrayList<ArrayList<Point>> pathes = new ArrayList<ArrayList<Point>>();
	
	public ArrayList<ArrayList<Point>> getShortest(){
		pathes.clear();
		getShortest(0,0, new ArrayList<Point>());
		return pathes;
	}
	
	private void getShortest(int r, int c, ArrayList<Point> used){
		if (r>=0 && c>=0 && r<maze.length && c < maze.length){
			if (!used.contains(new Point(r,c))){
				if (maze[r][c] == 1){
					
					used.add(new Point(r,c));
					if (c == maze.length-1){
						//We done
						pathes.add(used);
						return;
					}
					
					getShortest(r,c+1, new ArrayList<Point>(used));
					getShortest(r,c-1, new ArrayList<Point>(used));
					getShortest(r+1,c, new ArrayList<Point>(used));
					getShortest(r-1,c, new ArrayList<Point>(used));
				}
			}
		}
	}
	
//	public int getShortest(int r, int c){
//		if (r>=0 && c>=0 && r<maze.length && c < maze.length){
//			if (maze[r][c] == 1){
//				int right = getShortest(r,c+1);
//				int left = getShortest(r,c-1);
//				int up = getShortest(r+1,c);
//				int down = getShortest(r-1,c);
//			}
//		}
//		return 0;
//	}
	
	public void mark(int r, int c){
		marked.add(new Point(r,c));
	}
	
	public boolean isMarked(int r, int c){
		return marked.contains(new Point(r,c));
	}

	public String toString()
	{
		String out = "";
		
		for (int [] m : maze)
			out += Arrays.toString(m) + "\n";
		
		return out;
	}
}

class Slot{
	
	public int row;
	public int col;

	public Slot(int r, int c){
		this.row = r;
		this.col = c;
	}
	
	@Override
	public boolean equals(Object paramObject) {
		Slot other = (Slot)paramObject;
		return row == other.row && col == other.col; 
	}
	
}