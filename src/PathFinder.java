import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JOptionPane;

public class PathFinder
{
	public static void main( String args[] ) throws IOException
	{
		int size = Integer.parseInt(JOptionPane.showInputDialog("Enter size of Matrix\nWarning: Larger numbers can cause stack overflows\nAnd small numbers are boring", "6"));
		boolean hasExit = false;
		Maze m;
		do {
			m = new Maze(size);
			System.out.println(m);
			m.setEndCol(size-1);
			m.setEndRow(3);
			hasExit = m.hasExitPath(0,0);
			System.out.println((hasExit ? "Exit Found":"There is no escape") + "\n");
			if (hasExit){
				m.runCalculations();
				System.out.println("Found " + m.pathManager.paths.size() + " paths\n");
				System.out.println(m.pathManager);
				System.out.println("Shortest Path:\n" + m.shortestPath + "\n" + m.shortestPath.fancyToString() + "\n\n");
			}
		}
		while (hasExit == false || (size > 2 && m.pathManager.paths.size() < 2));
	}
}

class Maze
{
	private int[][] maze;
	private ArrayList<Slot> marked = new ArrayList<Slot>();
	protected PathManager pathManager = new PathManager();
	protected Path shortestPath;
	protected int startRow=-1, endRow=-1, startCol=-1, endCol=-1;

	public Maze(int size)
	{
		maze = new int[size][size];
		
		for (int r =0; r < size; r++)
			for (int c =0; c < size; c++)
				maze[r][c] = Math.random() > .5 ? 1:0;
	}
	
	public void setStartRow(int row){
		startRow = row;
	}
	
	public void setEndRow(int row){
		endRow = row;
	}
	
	public void setStartCol(int col){
		startCol = col;
	}
	
	public void setEndCol(int col){
		endCol = col;
	}
	
//	public void setStart(Slot slot){
//		
//	}
//	
//	public void setEnd(Slot slot){
//		
//	}

	public boolean hasExitPath(int r, int c)
	{
		if (r>=0 && c>=0 && r<maze.length && c < maze.length && maze[r][c] == 1 && !isMarked(r,c)){
			if (endCol >= 0 && c == endCol && endRow < 0)
				return true;
			else if (endRow >= 0 && r == endRow && endCol < 0)
				return true;
			else if (endRow >= 0 && r == endRow && endCol >= 0 && c == endCol)
				return true;
			mark(r,c);
			return hasExitPath(r+1,c) || hasExitPath(r-1,c) || hasExitPath(r,c+1) || hasExitPath(r,c-1);
		}
		return false;
	}
	
	public void mark(int r, int c){
		marked.add(new Slot(r,c));
	}
	
	public boolean isMarked(int r, int c){
		return marked.contains(new Slot(r,c));
	}
	
	public void runCalculations(){
		getAllPaths();
		pathManager.sort();
		shortestPath = pathManager.paths.get(0);
	}
	
	private PathManager getAllPaths(){
		pathManager.clear();
		getAllPaths(0,0, new ArrayList<Slot>());
		return pathManager;
	}
	
	private void getAllPaths(int r, int c, ArrayList<Slot> used){
		if (r>=0 && c>=0 && r<maze.length && c < maze.length){
			if (!used.contains(new Slot(r,c))){
				if (maze[r][c] == 1){
					
					used.add(new Slot(r,c));
					if (c == maze.length-1){
						pathManager.add(new Path(used));
						return;
					}
					
					getAllPaths(r,c+1, new ArrayList<Slot>(used));
					getAllPaths(r,c-1, new ArrayList<Slot>(used));
					getAllPaths(r+1,c, new ArrayList<Slot>(used));
					getAllPaths(r-1,c, new ArrayList<Slot>(used));
				}
			}
		}
	}
	
	public String toString()
	{
		String out = "";
		
		for (int [] m : maze)
			out += Arrays.toString(m) + "\n";
		
		return out;
	}
}

class PathManager{
	
	ArrayList<Path> paths;
	
	public PathManager() {
		paths = new ArrayList<Path>();
	}
	
	public void add(Path path){
		paths.add(path);
	}
	
	public void clear() {
		paths.clear();
	}
	
	public void sort(){
		Collections.sort(paths);
	}
	
	@Override
	public String toString() {
		
		String out = "Paths: \n";
		
		for (Path path : paths)
			//out += "[" + path.toString()+"]\n";
			out += path.fancyToString()+"\n\n";
		
		return out;
	}
	
}

class Path implements Comparable<Path>{
	
	public ArrayList<Slot> path;
	
	public Path(ArrayList<Slot> used) {
		path = new ArrayList<Slot>(used);
	}

	public void add(Slot slot){
		path.add(slot);
	}
	
	public int getSize(){
		return path.size();
	}
	
	@Override
	public int compareTo(Path other) {
		return (this.getSize() == other.getSize()? 0: (this.getSize() < other.getSize() ? -1: 1));
	}
	
	public String fancyToString(){
		
		int largestRow=1, largestCol=1;
		
		for (Slot slot : path){
			largestRow = Math.max(largestRow, slot.row+1);
			largestCol = Math.max(largestCol, slot.col+1);
		}
		
		int matrix[][] = new int[largestRow][largestCol];
		for (int i=0; i <matrix.length; i++)
			Arrays.fill(matrix[i], 0);
		
		int x=0;
		for (Slot slot : path){
			matrix[slot.row][slot.col] = ++x;
		}
		
		String out = "";

		for (int[] row : matrix){
			//out+= Arrays.toString(row) + "\n";
			for (int col : row){
				if (col == 0) out += "* ";
				else out+=Integer.toUnsignedString(col, 32) + " ";
			}
			out+= "\n";
		}
		
		return out = out.substring(0, out.length());
	}
	
	@Override
	public String toString() {
		
		String out = "Path: {";
		
		for (Slot s : path)
			out+= s.toString() + ", ";
		
		out = out.substring(0, out.length()-2) + "} size: " + path.size();
		
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
	
	@Override
	public String toString() {
		return "[" + row + "][" + col + "]";
	}
}