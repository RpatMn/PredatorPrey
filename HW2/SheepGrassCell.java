package HW2;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import genDevs.plots.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.text.*;
import java.io.*;
import statistics.*;
import twoDCellSpace.TwoDimCell;
import quantization.*;

public class SheepGrassCell extends TwoDimCell{
	GlobalRef ref = GlobalRef.getInstance();
	public boolean Sheep;
	public boolean Grass;
	public double reproductionTime;
	public double grassReproductionTime;
	public double lifeTime;
	public double moveTime;
	public int[] N;
	public int[] NE;
	public int[] E;
	public int[] SE;
	public int[] S;
	public int[] SW;
	public int[] W;
	public int[] NW;
	public ArrayList<int []> neighbor = new ArrayList<>();
	public String currentTime = "";
	public boolean drawn = false;
	public int i=1;
	double tmp=0;

	 int births=0;
	 int death = 0;
	 int spreads = 0;
	 int eaten = 0;

	
	SheepGrassCell(){
	}

	public SheepGrassCell(int i, int j) {
		super(new Pair(new Integer(i), new Integer(j)));
	}

//	    int sumB;
//	    int movB;
//	    int spB;


	  
	 public void reproduce() {
	      births++; 
	       
	    }

	    public void die() {
	        death++;
	    }
	    public void spread() {
	        spreads++;
	        // Spreading logic
	    }
	    public void grassEaten() {
	    	eaten++;
	    }


	    public void writeOutput(double tmp) {
	        
	        try {
	        	int totalSheepAtTime= births-death;
	        	int totalGrassAtTime=spreads-eaten;
	        	
	        	
	        //	 SimpleDateFormat dateFormat = new SimpleDateFormat("ss");
	         //    String timestamp = dateFormat.format(new Date());
	            FileWriter fw = new FileWriter("sheep_outputOG.csv", true); 
	            fw.write(tmp + "," + births + "," + death + "," +spreads+ "," +eaten+ "\n");  
	            FileWriter fw2 = new FileWriter("sheep_outputTotal.csv", true); 
	            fw2.write(tmp + "," + totalSheepAtTime + "," + totalGrassAtTime + "\n");  
	           // i++;
	            fw.close();
	            fw2.close();
	        }
	        catch(IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	public void initialize() {
		super.initialize();
		
		N = ref.getNeighbor(xcoord, ycoord, 0);
		NE = ref.getNeighbor(xcoord, ycoord, 1);
		E = ref.getNeighbor(xcoord, ycoord, 2);
		SE = ref.getNeighbor(xcoord, ycoord, 3);
		S = ref.getNeighbor(xcoord, ycoord, 4);
		SW = ref.getNeighbor(xcoord, ycoord, 5);
		W = ref.getNeighbor(xcoord, ycoord, 6);
		NW = ref.getNeighbor(xcoord, ycoord, 7);
		
		neighbor.add(N);
		neighbor.add(NE);
		neighbor.add(E);
		neighbor.add(SE);
		neighbor.add(S);
		neighbor.add(SW);
		neighbor.add(W);
		neighbor.add(NW);
		
		String state = ref.state[xcoord][ycoord];
		if(state.compareTo("Sheep") == 0) {
			System.out.println("Sheep");
			drawn = false;
			reproductionTime = ref.sheepReproduceT;
			lifeTime = ref.sheepLifeT;
			moveTime = ref.sheepMoveT;
			if(lifeTime <= reproductionTime) {
				if(lifeTime <= moveTime) {
					currentTime = "lifeTime";
					holdIn("Sheep", .1);
				}else {
					currentTime = "moveTime";
					holdIn("Sheep", .1);
				}
			}else if(reproductionTime <= moveTime){
				currentTime = "reproductionTime";
				holdIn("Sheep", .1);
			}else {
				currentTime = "moveTime";
				holdIn("Sheep", .1);
			}
		}else if(state.compareTo("Grass") == 0) {
			grassReproductionTime = ref.grassReproduceT;
			holdIn("Grass", .1);
		}else {
			holdIn("Empty", .1);
		}
	}
	
	public void deltext(double e, message x) {
		Continue(e);
		tmp=e;
		for(int i = 0; i < x.getLength(); i++) {
			if(messageOnPort(x, "inN", i)) {
				data input = (data) x.getValOnPort("inN", i);
				int inpX = (int) input.key;
				int inpY = (int) input.value;
				if(ref.state[inpX][inpY].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepM") == 0){
						reproductionTime = (double) input.getReproductionTime();
						if(ref.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeTime = ref.sheepLifeT;
							grassEaten();

						}else {
							lifeTime = (double) input.getLifeTime();
						}
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						ref.state[inpX][inpY] = "Empty";
						nextEvent();
						drawn = false;
						holdIn("Sheep", .1);
					} else if(type.compareTo("SheepR") == 0){
						reproductionTime = ref.sheepReproduceT;
						lifeTime = ref.sheepLifeT;
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						nextEvent();
						drawn = false;
						reproduce();

						holdIn("Sheep", .1);
					}
				}else if(ref.state[inpX][inpY].compareTo("Grass") == 0){
					ref.state[xcoord][ycoord] = "Grass";
					grassReproductionTime = 1;
					drawn = false;
					spread();
					holdIn("Grass", .1);
				}
			}
			if(messageOnPort(x, "inNE", i)) {
				data input = (data) x.getValOnPort("inNE", i);
				int inpX = (int) input.key;
				int inpY = (int) input.value;
				if(ref.state[inpX][inpY].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepM") == 0){
						reproductionTime = (double) input.getReproductionTime();
						if(ref.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeTime = ref.sheepLifeT;
							grassEaten();
						}else {
							lifeTime = (double) input.getLifeTime();
						}
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						ref.state[inpX][inpY] = "Empty";
						nextEvent();
						drawn = false;
						holdIn("Sheep", .1);
					} else if(type.compareTo("SheepR") == 0){
						reproductionTime = ref.sheepReproduceT;
						lifeTime = ref.sheepLifeT;
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						nextEvent();
						drawn = false;
						reproduce();
						holdIn("Sheep", .1);
					}
				}else if(ref.state[inpX][inpY].compareTo("Grass") == 0){
					ref.state[xcoord][ycoord] = "Grass";
					grassReproductionTime = 1;
					drawn = false;
					spread();
					holdIn("Grass", .1);
				}
			}
			if(messageOnPort(x, "inE", i)) {
				data input = (data) x.getValOnPort("inE", i);
				int inpX = (int) input.key;
				int inpY = (int) input.value;
				if(ref.state[inpX][inpY].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepM") == 0){
						reproductionTime = (double) input.getReproductionTime();
						if(ref.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeTime = ref.sheepLifeT;
							grassEaten();

						}else {
							lifeTime = (double) input.getLifeTime();
						}
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						ref.state[inpX][inpY] = "Empty";
						nextEvent();
						drawn = false;
						holdIn("Sheep", .1);
					} else if(type.compareTo("SheepR") == 0){
						reproductionTime = ref.sheepReproduceT;
						lifeTime = ref.sheepLifeT;
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						nextEvent();
						drawn = false;
						reproduce();
						holdIn("Sheep", .1);
					}
				}else if(ref.state[inpX][inpY].compareTo("Grass") == 0){
					ref.state[xcoord][ycoord] = "Grass";
					grassReproductionTime = 1;
					drawn = false;
					spread();
					holdIn("Grass", .1);
				}
			}
			if(messageOnPort(x, "inSE", i)) {
				data input = (data) x.getValOnPort("inSE", i);
				int inpX = (int) input.key;
				int inpY = (int) input.value;
				if(ref.state[inpX][inpY].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepM") == 0){
						reproductionTime = (double) input.getReproductionTime();
						if(ref.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeTime = ref.sheepLifeT;
							grassEaten();

						}else {
							lifeTime = (double) input.getLifeTime();
						}
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						ref.state[inpX][inpY] = "Empty";
						nextEvent();
						drawn = false;
						holdIn("Sheep", .1);
					} else if(type.compareTo("SheepR") == 0){
						reproductionTime = ref.sheepReproduceT;
						lifeTime = ref.sheepLifeT;
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						nextEvent();
						drawn = false;
						reproduce();
						holdIn("Sheep", .1);
					}
				}else if(ref.state[inpX][inpY].compareTo("Grass") == 0){
					ref.state[xcoord][ycoord] = "Grass";
					grassReproductionTime = 1;
					drawn = false;
					spread();
					holdIn("Grass", .1);
				}
			}
			if(messageOnPort(x, "inS", i)) {
				data input = (data) x.getValOnPort("inS", i);
				int inpX = (int) input.key;
				int inpY = (int) input.value;
				if(ref.state[inpX][inpY].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepM") == 0){
						reproductionTime = (double) input.getReproductionTime();
						if(ref.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeTime = ref.sheepLifeT;
							grassEaten();

						}else {
							lifeTime = (double) input.getLifeTime();
						}
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						ref.state[inpX][inpY] = "Empty";
						nextEvent();
						drawn = false;
						holdIn("Sheep", .1);
					} else if(type.compareTo("SheepR") == 0){
						reproductionTime = ref.sheepReproduceT;
						lifeTime = ref.sheepLifeT;
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						nextEvent();
						drawn = false;
						reproduce();
						holdIn("Sheep", .1);
					}
				}else if(ref.state[inpX][inpY].compareTo("Grass") == 0){
					ref.state[xcoord][ycoord] = "Grass";
					grassReproductionTime = 1;
					drawn = false;
					spread();
					holdIn("Grass", .1);
				}
			}
			if(messageOnPort(x, "inSW", i)) {
				data input = (data) x.getValOnPort("inSW", i);
				int inpX = (int) input.key;
				int inpY = (int) input.value;
				if(ref.state[inpX][inpY].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepM") == 0){
						reproductionTime = (double) input.getReproductionTime();
						if(ref.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeTime = ref.sheepLifeT;
							grassEaten();

						}else {
							lifeTime = (double) input.getLifeTime();
						}
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						ref.state[inpX][inpY] = "Empty";
						nextEvent();
						drawn = false;
						holdIn("Sheep", .1);
					} else if(type.compareTo("SheepR") == 0){
						reproductionTime = ref.sheepReproduceT;
						lifeTime = ref.sheepLifeT;
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						nextEvent();
						drawn = false;
						reproduce();
						holdIn("Sheep", .1);
					}
				}else if(ref.state[inpX][inpY].compareTo("Grass") == 0){
					ref.state[xcoord][ycoord] = "Grass";
					grassReproductionTime = 1;
					drawn = false;
					spread();
					holdIn("Grass", .1);
				}
			}
			if(messageOnPort(x, "inW", i)) {
				data input = (data) x.getValOnPort("inW", i);
				int inpX = (int) input.key;
				int inpY = (int) input.value;
				if(ref.state[inpX][inpY].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepM") == 0){
						reproductionTime = (double) input.getReproductionTime();
						if(ref.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeTime = ref.sheepLifeT;
							grassEaten();

						}else {
							lifeTime = (double) input.getLifeTime();
						}
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						ref.state[inpX][inpY] = "Empty";
						nextEvent();
						drawn = false;
						holdIn("Sheep", .1);
					} else if(type.compareTo("SheepR") == 0){
						reproductionTime = ref.sheepReproduceT;
						lifeTime = ref.sheepLifeT;
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						nextEvent();
						drawn = false;
						reproduce();
						holdIn("Sheep", .1);
					}
				}else if(ref.state[inpX][inpY].compareTo("Grass") == 0){
					ref.state[xcoord][ycoord] = "Grass";
					grassReproductionTime = 1;
					drawn = false;
					spread();
					holdIn("Grass", .1);
				}
			}
			if(messageOnPort(x, "inNW" ,i)) {
				data input = (data) x.getValOnPort("inNW", i);
				int inpX = (int) input.key;
				int inpY = (int) input.value;
				if(ref.state[inpX][inpY].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepM") == 0){
						reproductionTime = (double) input.getReproductionTime();
						if(ref.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeTime = ref.sheepLifeT;
							grassEaten();
						}else {
							lifeTime = (double) input.getLifeTime();
						}
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						ref.state[inpX][inpY] = "Empty";
						nextEvent();
						drawn = false;
						holdIn("Sheep", .1);
					} else if(type.compareTo("SheepR") == 0){
						reproductionTime = ref.sheepReproduceT;
						lifeTime = ref.sheepLifeT;
						moveTime = ref.sheepMoveT;
						ref.state[xcoord][ycoord] = "Sheep";
						nextEvent();
						drawn = false;
						reproduce();
						holdIn("Sheep", .1);
					}
				}else if(ref.state[inpX][inpY].compareTo("Grass") == 0){
					ref.state[xcoord][ycoord] = "Grass";
					grassReproductionTime = 1;
					drawn = false;
					spread();
					holdIn("Grass", .1);
				}
			}
		}
		writeOutput(tmp);

	}
	
	public void nextEvent() {
		if(lifeTime <= reproductionTime) {
			if(lifeTime <= moveTime) {
				currentTime = "lifeTime";
			}else {
				currentTime = "moveTime";
			}
		}else if(reproductionTime <= moveTime){
			currentTime = "reproductionTime";
		}else {
			currentTime = "moveTime";
		}
	}
	
	public void deltint() {
		if(phaseIs("Empty")) {
			ref.state[xcoord][ycoord] = "Empty";
			passivateIn("Empty");
		}else if(phaseIs("Sheep")) {
			ref.state[xcoord][ycoord] = "Sheep";
		if(drawn) {
			if(currentTime.compareTo("lifeTime") == 0) {
				ref.state[xcoord][ycoord] = "Empty";
				drawn = false;
				holdIn("Empty", .1);
			}else if(currentTime.compareTo("moveTime") == 0) {
				//ref.state[xcoord][ycoord] = "Empty";
				drawn = false;
				holdIn("Empty", .1);
			}else if(currentTime.compareTo("reproductionTime") == 0) {
				if(lifeTime <= reproductionTime) {
					if(lifeTime <= moveTime) {
						currentTime = "lifeTime";
						drawn = true;
						holdIn("Sheep", lifeTime);
					}else {
						currentTime = "moveTime";
						drawn = true;
						holdIn("Sheep", moveTime);
					}
				}else if(reproductionTime <= moveTime){
					currentTime = "reproductionTime";
					drawn = true;
					holdIn("Sheep", reproductionTime);
				}else {
					currentTime = "moveTime";
					drawn = true;
					holdIn("Sheep", moveTime);
				}
			}
		}else {
				if(lifeTime <= reproductionTime) {
					if(lifeTime <= moveTime) {
						currentTime = "lifeTime";
						drawn = true;
						holdIn("Sheep", lifeTime);
					}else {
						currentTime = "moveTime";
						drawn = true;
						holdIn("Sheep", moveTime);
					}
				}else if(reproductionTime <= moveTime){
					currentTime = "reproductionTime";
					drawn = true;
					holdIn("Sheep", reproductionTime);
				}else {
					currentTime = "moveTime";
					drawn = true;
					holdIn("Sheep", moveTime);
				}
			}
		}else if(phaseIs("Grass")) {
			ref.state[xcoord][ycoord] = "Grass";
			holdIn("Grass", ref.grassReproduceT - .1);
			grassReproductionTime = -1;
		}
	}
	
	public void deltcon(double e, message x) {
	    deltext(e, x);
		deltint();
	  }
	
	public message out() {
		writeOutput(tmp);

		message m = super.out();
		if(drawn) {
			if(currentTime.compareTo("lifeTime") == 0) {
				moveTime = moveTime - lifeTime;
				reproductionTime = reproductionTime - lifeTime;
				lifeTime = 0;
			}else if(currentTime.compareTo("moveTime") == 0) {
				lifeTime = lifeTime - moveTime;
				reproductionTime = reproductionTime - moveTime;
				moveTime = 0;
			}else if(currentTime.compareTo("reproductionTime") == 0) {
				moveTime = moveTime - reproductionTime;
				lifeTime = lifeTime - reproductionTime;
				reproductionTime = 0;
			}
		}
		System.out.println(lifeTime);
		if(phaseIs("Sheep")) {
			m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.red, Color.red)));
			if(currentTime.compareTo("lifeTime") == 0 && drawn) {
				die();
				m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.white, Color.white)));
			}else if(drawn){
				String used = "";
				if(currentTime.compareTo("reproductionTime") == 0) {
					reproductionTime = ref.sheepReproduceT;
					ArrayList<String> directions = new ArrayList();
					if(ref.state[N[0]][N[1]].compareTo("Sheep") != 0) {
						directions.add("outN");
					}
					if(ref.state[NE[0]][NE[1]].compareTo("Sheep") != 0) {
						directions.add("outNE");
					}
					if(ref.state[E[0]][E[1]].compareTo("Sheep") != 0) {
						directions.add("outE");
					}
					if(ref.state[SE[0]][SE[1]].compareTo("Sheep") != 0) {
						directions.add("outSE");
					}
					if(ref.state[S[0]][S[1]].compareTo("Sheep") != 0) {
						directions.add("outS");
					}
					if(ref.state[SW[0]][SW[1]].compareTo("Sheep") != 0) {
						directions.add("outSW");
					}
					if(ref.state[W[0]][W[1]].compareTo("Sheep") != 0) {
						directions.add("outW");
					}
					if(ref.state[NW[0]][NW[1]].compareTo("Sheep") != 0) {
						directions.add("outNW");
					}
					if(!directions.isEmpty()) {
						int dir = ref.rand.nextInt(directions.size());
						used = directions.get(dir);
						ref.state[xcoord][ycoord] = "Sheep";
						m.add(makeContent(directions.get(dir), new data (new Integer(xcoord), new Integer(ycoord), ref.sheepReproduceT, ref.sheepLifeT, "SheepR")));
					}
				}
				if(currentTime.compareTo("moveTime") == 0) {
					ArrayList<String> perferred = new ArrayList();
					if(ref.state[N[0]][N[1]].compareTo("Grass") == 0) {
						perferred.add("outN");
					}
					if(ref.state[NE[0]][NE[1]].compareTo("Grass") == 0) {
						perferred.add("outNE");
					}
					if(ref.state[E[0]][E[1]].compareTo("Grass") == 0) {
						perferred.add("outE");
					}
					if(ref.state[SE[0]][SE[1]].compareTo("Grass") == 0) {
						perferred.add("outSE");
					}
					if(ref.state[S[0]][S[1]].compareTo("Grass") == 0) {
						perferred.add("outS");
					}
					if(ref.state[SW[0]][SW[1]].compareTo("Grass") == 0) {
						perferred.add("outSW");
					}
					if(ref.state[W[0]][W[1]].compareTo("Grass") == 0) {
						perferred.add("outW");
					}
					if(ref.state[NW[0]][NW[1]].compareTo("Grass") == 0) {
						perferred.add("outNW");
					}
					perferred.remove(used);
					if(!perferred.isEmpty()) {
						int dir = ref.rand.nextInt(perferred.size());
						m.add(makeContent(perferred.get(dir), new data (new Integer(xcoord), new Integer(ycoord), reproductionTime, lifeTime, "SheepM")));
					}else {
						ArrayList<String> directions = new ArrayList();
						if(ref.state[N[0]][N[1]].compareTo("Sheep") != 0) {
							directions.add("outN");
						}
						if(ref.state[NE[0]][NE[1]].compareTo("Sheep") != 0) {
							directions.add("outNE");
						}
						if(ref.state[E[0]][E[1]].compareTo("Sheep") != 0) {
							directions.add("outE");
						}
						if(ref.state[SE[0]][SE[1]].compareTo("Sheep") != 0) {
							directions.add("outSE");
						}
						if(ref.state[S[0]][S[1]].compareTo("Sheep") != 0) {
							directions.add("outS");
						}
						if(ref.state[SW[0]][SW[1]].compareTo("Sheep") != 0) {
							directions.add("outSW");
						}
						if(ref.state[W[0]][W[1]].compareTo("Sheep") != 0) {
							directions.add("outW");
						}
						if(ref.state[NW[0]][NW[1]].compareTo("Sheep") != 0) {
							directions.add("outNW");
						}
						directions.remove(used);
						if(!directions.isEmpty()) {
							int dir = ref.rand.nextInt(directions.size());
							
							m.add(makeContent(directions.get(dir), new data (new Integer(xcoord), new Integer(ycoord), reproductionTime, lifeTime, "SheepM")));
						}else {
							m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.red, Color.red)));
						}
					}
				}
			}
		}else if(phaseIs("Grass")) {
			m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.green, Color.green)));
			//pick random neighbor and create new grass
			if(grassReproductionTime < 0) {
				grassReproductionTime = ref.grassReproduceT;
				ArrayList<String> directions = new ArrayList();
				if(ref.state[N[0]][N[1]].compareTo("Empty") == 0) {
					directions.add("outN");
				}
				if(ref.state[NE[0]][NE[1]].compareTo("Empty") == 0) {
					directions.add("outNE");
				}
				if(ref.state[E[0]][E[1]].compareTo("Empty") == 0) {
					directions.add("outE");
				}
				if(ref.state[SE[0]][SE[1]].compareTo("Empty") == 0) {
					directions.add("outSE");
				}
				if(ref.state[S[0]][S[1]].compareTo("Empty") == 0) {
					directions.add("outS");
				}
				if(ref.state[SW[0]][SW[1]].compareTo("Empty") == 0) {
					directions.add("outSW");
				}
				if(ref.state[W[0]][W[1]].compareTo("Empty") == 0) {
					directions.add("outW");
				}
				if(ref.state[NW[0]][NW[1]].compareTo("Empty") == 0) {
					directions.add("outNW");
				}
				if(!directions.isEmpty()) {
					int dir = ref.rand.nextInt(directions.size());
					m.add(makeContent(directions.get(dir), new data (new Integer(xcoord), new Integer(ycoord), 0, 0, "grassR")));
				}
			}
		}else if(phaseIs("Empty")) {
			m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.white, Color.white)));
		}
		return m;
	}
}
