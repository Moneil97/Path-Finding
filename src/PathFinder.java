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
			m.setStart(0, 0);
			m.setEnd(size-1, size-1);
			hasExit = m.hasPath();
			System.out.println((hasExit ? "Exit Found":"There is no escape") + "\n");
			if (hasExit){
				//m.updateAllPaths();
				//System.out.println("Found " + m.pathManager.paths.size() + " paths\n");
				//System.out.println(m.pathManager);
				m.updateShortestPath();
				//System.out.println(m.shortestPath);
				System.out.println("Shortest Path:\n" + m.shortestPath + "\n" + m.shortestPath.fancyToString() + "\n\n");
			}
		}
		while (hasExit == false/* || (size > 2 && m.pathManager.paths.size() < 2)*/);
	}
}

class Maze
{
	/**Holds matrix*/
	private int[][] matrix;
	/**Used by hasPath() to keep track of previously checked points*/
	private ArrayList<Slot> marked = new ArrayList<Slot>();
	protected PathManager pathManager = new PathManager();
	protected Path shortestPath;
	protected int startRow=-1, endRow=-1, startCol=-1, endCol=-1;

	public Maze(int size)
	{
		matrix = new int[size][size];
		
		for (int r =0; r < size; r++)
			for (int c =0; c < size; c++)
				matrix[r][c] = Math.random() > .3 ? 1:0;
	}
	
	/**
	 * Sets starting point
	 * <br>Set to a negative number to ignore
	 * <br>Ex: (2,-1) will have a starting point of the entire row 2
	 * @param row row to check
	 * @param col column to check
	 */
	
	public void setStart(int row, int col){
		startRow = row;
		startCol = col;
	}
	
	public void setStartRow(int row){
		startRow = row;
	}
	
	public void setStartCol(int col){
		startCol = col;
	}
	
	/**
	 * Sets ending point
	 * <br>Set to a negative number to ignore
	 * <br>Ex: (2,-1) will have a starting point of the entire row 2
	 * @param row row to check
	 * @param col column to check
	 */
	
	public void setEnd(int row, int col){
		endRow = row;
		endCol = col;
	}
	
	public void setEndRow(int row){
		endRow = row;
	}
	
	public void setEndCol(int col){
		endCol = col;
	}
	
	/**
	 * Quick check if there is an available path
	 * @return true if has path
	 */
	
	public boolean hasPath(){
		marked.clear();
		return hasPath(startRow, startCol);
	}

	/**
	 * recursive function used by hasPath()
	 * @param r row
	 * @param c	col
	 * @return true if has available path
	 */
	
	private boolean hasPath(int r, int c)
	{
		if (r>=0 && c>=0 && r<matrix.length && c < matrix.length && matrix[r][c] == 1 && !isMarked(r,c)){
			if ((endCol >= 0 && c == endCol && endRow < 0) || (endRow >= 0 && r == endRow && endCol < 0) || (endRow >= 0 && r == endRow && endCol >= 0 && c == endCol))
				return true;
			mark(r,c);
			return hasPath(r+1,c) || hasPath(r-1,c) || hasPath(r,c+1) || hasPath(r,c-1);
		}
		return false;
	}
	
	private void mark(int r, int c){
		marked.add(new Slot(r,c));
	}
	
	private boolean isMarked(int r, int c){
		return marked.contains(new Slot(r,c));
	}
	
	public void updateAllPaths(){
		pathManager.clear();
		updateAllPaths(startRow,startCol, new ArrayList<Slot>());
	}
	
	private void updateAllPaths(int r, int c, ArrayList<Slot> used){
		
		if (r>=0 && c>=0 && r<matrix.length && c < matrix.length){
			if (!used.contains(new Slot(r,c))){
				if (matrix[r][c] == 1){
					
					used.add(new Slot(r,c));
					if ((endCol >= 0 && c == endCol && endRow < 0) || (endRow >= 0 && r == endRow && endCol < 0) || (endRow >= 0 && r == endRow && endCol >= 0 && c == endCol)){
						pathManager.add(new Path(used));
						return;
					}
					
					updateAllPaths(r,c+1, new ArrayList<Slot>(used));
					updateAllPaths(r,c-1, new ArrayList<Slot>(used));
					updateAllPaths(r+1,c, new ArrayList<Slot>(used));
					updateAllPaths(r-1,c, new ArrayList<Slot>(used));
				}
			}
		}
	}
	
	public void sortAllPaths(){
		pathManager.sort();
	}
	
	public void updateShortestPath(){
		updateShortestPath(startRow,startCol, new ArrayList<Slot>());
	}
	
	private void updateShortestPath(int r, int c, ArrayList<Slot> used){
		
		if (r>=0 && c>=0 && r<matrix.length && c < matrix.length){
			if (!used.contains(new Slot(r,c))){
				if (matrix[r][c] == 1){
					used.add(new Slot(r,c));
					if ((endCol >= 0 && c == endCol && endRow < 0) || (endRow >= 0 && r == endRow && endCol < 0) || (endRow >= 0 && r == endRow && endCol >= 0 && c == endCol)){
						shortestPath = new Path(used);
						say("Found new shortest path: " + shortestPath);
						used.clear();
						used = null;
						//Runtime.getRuntime().gc();
						return;
					}
					
					if (shortestPath != null && used.size() >= shortestPath.getSize()-1){
						used.clear();
						used = null;
						//Runtime.getRuntime().gc();
						return;
					}
					else{
						updateShortestPath(r,c+1, new ArrayList<Slot>(used));
						updateShortestPath(r+1,c, new ArrayList<Slot>(used));
						updateShortestPath(r,c-1, new ArrayList<Slot>(used));
						updateShortestPath(r-1,c, new ArrayList<Slot>(used));
						used.clear();
						used = null;
						//Runtime.getRuntime().gc();
						return;
					}
				}
			}
		}
		used.clear();
		used = null;
		//Runtime.getRuntime().gc();
	}
	
	public static void say(Object s){
		System.out.println(s);
	}
	
	public String toString()
	{
		String out = "";
		
		for (int [] m : matrix)
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
				else out+=(char)(col+48) + " ";//Integer.toUnsignedString(col, 32) + " ";
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