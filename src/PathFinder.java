import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PathFinder
{
	public static void main( String args[] ) throws IOException
	{
//		for (int i = 0; i < 20; i++){
		int i =4;
		boolean hasExit = false;
		do {
			Maze m = new Maze(i);
			System.out.println(m);
			hasExit = m.hasExitPath(0,0);
			System.out.println((hasExit ? "Exit Found":"There is no escape"));
			System.out.println(m.getAllPaths() + "\n\n");
		}
		while (hasExit == false);
//		}
		
	}
}

class Maze
{
   private int[][] maze;
   private ArrayList<Slot> marked = new ArrayList<Slot>();
   private PathManager pathManager = new PathManager();

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
		return false;
	}
	
//	public ArrayList<Slot> getShortestPath(){
//		
//	}
	
	public PathManager getAllPaths(){
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
	
	public void mark(int r, int c){
		marked.add(new Slot(r,c));
	}
	
	public boolean isMarked(int r, int c){
		return marked.contains(new Slot(r,c));
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
	
	@Override
	public String toString() {
		
		String out = "Paths: \n";
		
		for (Path path : paths)
			out += "[" + path.toString()+"]\n";
		
		return out;
	}
	
}

class Path{
	
	public ArrayList<Slot> path;
	
	public Path(ArrayList<Slot> used) {
		path = new ArrayList<Slot>(used);
	}

	public void add(Slot slot){
		path.add(slot);
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