package PredatorPrey.Rpat;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import GenCol.Pair;
import genDevs.modeling.message;
import genDevs.plots.DrawCellEntity;
import twoDCellSpace.TwoDimCell;

public class sheepGrassSystem extends TwoDimCell {
    GlobalRef globalRef = GlobalRef.getInstance();
    public boolean Sheep;
    public boolean Grass;
    public double lifeT;
    public double MoveT;
    public double reproductionT;
    public double grassRepT;
    public Random rand = new Random(1212123);

 
    public String currentPhase = "";
    public boolean isOccupied = false;
   public boolean isDominant ;
   double tmp=0;
	
    int births=0;
    int death = 0;
    int spreads = 0;
    int eaten = 0;
    public int[] neighborNorth;
    public int[] neighborNorthEast;
    public int[] neighborEast;
    public int[] neighborSouthEast;
    public int[] neighborSouth;
    public int[] neighborSouthWest;
    public int[] neighborWest;
    public int[] neighborNorthWest;
    public ArrayList<int []> adjacentCells = new ArrayList<>();


  
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
//        	int totalSheepAtTime= births-death;
//        	int totalGrassAtTime=spreads-eaten;
        	
        	
        //	 SimpleDateFormat dateFormat = new SimpleDateFormat("ss");
         //    String timestamp = dateFormat.format(new Date());
            FileWriter fw = new FileWriter("sheep_outputOG.csv", true); 
            fw.write(tmp + "," + births + "," + death + "," +spreads+ "," +eaten+ "\n");  
//            FileWriter fw2 = new FileWriter("sheep_outputTotal.csv", true); 
//            fw2.write(tmp + "," + totalSheepAtTime + "," + totalGrassAtTime + "\n");  
            fw.close();
//            fw2.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
//    private long getTime() {
//        Instant currentInstant = Instant.now();
//        return currentInstant.getEpochSecond();
//    }
    
    @Override
    public void initialize() {
        super.initialize();
        setNeighborReferences();

       
    

    	System.out.println("int starts with cell "+xcoord+"_"+ycoord+"and dominancy" +isDominant );

    String state = globalRef.state[xcoord][ycoord];
	if(state.compareTo("Sheep") == 0) {
		isOccupied = false;
		isDominant = globalRef.isDominant[xcoord][ycoord];
		reproductionT = globalRef.sheepReproduceT;
		lifeT = globalRef.sheepLifeT;
		MoveT = globalRef.sheepMoveT;
		if(lifeT <= reproductionT) {
			if(lifeT <= MoveT) {
				currentPhase = "lifeTime";
				holdIn("Sheep", 0.05);
			}else {
				currentPhase = "moveTime";
				holdIn("Sheep", 0.05);
			}
		}else if(reproductionT <= MoveT){
			currentPhase = "reproductionTime";
			holdIn("Sheep", 0.05);
		}else {
			currentPhase = "moveTime";
			holdIn("Sheep", 0.05);
		}
	}else if(state.compareTo("Grass") == 0) {
		grassRepT = globalRef.grassReproduceT;
		holdIn("Grass", 0.05);
	}else {
		holdIn("Empty", 0.05);
	}
	System.out.println("int ends with cell "+xcoord+"_"+ycoord+"and dominancy" +isDominant );
	


}

    private void setNeighborReferences() {
        neighborNorth = globalRef.getNeighbor(xcoord, ycoord, 0);
        neighborNorthEast = globalRef.getNeighbor(xcoord, ycoord, 1);
        neighborEast = globalRef.getNeighbor(xcoord, ycoord, 2);
        neighborSouthEast = globalRef.getNeighbor(xcoord, ycoord, 3);
        neighborSouth = globalRef.getNeighbor(xcoord, ycoord, 4);
        neighborSouthWest = globalRef.getNeighbor(xcoord, ycoord, 5);
        neighborWest = globalRef.getNeighbor(xcoord, ycoord, 6);
        neighborNorthWest = globalRef.getNeighbor(xcoord, ycoord, 7);

        Collections.addAll(adjacentCells, neighborNorth, neighborNorthEast,neighborEast,neighborSouthEast,neighborSouth,neighborSouthWest,neighborWest,neighborNorthWest);
    }
  //DELTEXT STARTS  
    @SuppressWarnings("static-access")
	@Override
    public void deltext(double e, message x) {
    	Continue(e);
    	tmp=e;
    
    	System.out.println("deltext starts with cell "+xcoord+"_"+ycoord  );

		for(int i = 0; i < x.getLength(); i++) {
			if(messageOnPort(x, "inN", i)) {
				SheepGrassEnt input = (SheepGrassEnt) x.getValOnPort("inN", i);
				int key = (int) input.key;
				int value = (int) input.value;
				boolean dom = (boolean) input.isDominant;

				if(globalRef.state[key][value].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepMoving") == 0){
						reproductionT = (double) input.getReproductionTime();
						if(globalRef.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeT = globalRef.sheepLifeT;
							grassEaten();

						}else {
							lifeT = (double) input.getLifeTime();
						}
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						globalRef.state[key][value] = "Empty";
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=false;
							globalRef.isDominant[key][value]=true;							
						}
						nextState();
						isDominant=dom;

						isOccupied = false;
						holdIn("Sheep", 0.05);
					} else if(type.compareTo("SheepReproducing") == 0){
						reproductionT = globalRef.sheepReproduceT;
						lifeT = globalRef.sheepLifeT;
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						isDominant=dom;
						nextState();
						reproduce();

						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=true;
							globalRef.isDominant[key][value]=true;							
						}
						isOccupied = false;
						holdIn("Sheep", 0.05);
					}
				}else if(globalRef.state[key][value].compareTo("Grass") == 0){
					globalRef.state[xcoord][ycoord] = "Grass";
					grassRepT = 1;
					isOccupied = false;
					spread();

					holdIn("Grass", 0.05);
				}
			}
			if(messageOnPort(x, "inNE", i)) {
				SheepGrassEnt input = (SheepGrassEnt) x.getValOnPort("inNE", i);
				int key = (int) input.key;
				int value = (int) input.value;
				boolean dom = (boolean) input.isDominant;

				if(globalRef.state[key][value].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepMoving") == 0){
						reproductionT = (double) input.getReproductionTime();
						if(globalRef.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeT = globalRef.sheepLifeT;
							grassEaten();

						}else {
							lifeT = (double) input.getLifeTime();
						}
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						globalRef.state[key][value] = "Empty";
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=false;
							globalRef.isDominant[key][value]=true;							
						}
						nextState();
						isDominant=dom;

						isOccupied = false;
						holdIn("Sheep", 0.05);
					} else if(type.compareTo("SheepReproducing") == 0){
						reproductionT = globalRef.sheepReproduceT;
						lifeT = globalRef.sheepLifeT;
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						isDominant=dom;
						nextState();
						reproduce();

						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=true;
							globalRef.isDominant[key][value]=true;							
						}
						isOccupied = false;
						holdIn("Sheep", 0.05);
					}
				}else if(globalRef.state[key][value].compareTo("Grass") == 0){
					globalRef.state[xcoord][ycoord] = "Grass";
					grassRepT = 1;
					isOccupied = false;
					spread();

					holdIn("Grass", 0.05);
				}
			}
			if(messageOnPort(x, "inE", i)) {
				SheepGrassEnt input = (SheepGrassEnt) x.getValOnPort("inE", i);
				int key = (int) input.key;
				int value = (int) input.value;
				boolean dom = (boolean) input.isDominant;

				if(globalRef.state[key][value].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepMoving") == 0){
						reproductionT = (double) input.getReproductionTime();
						if(globalRef.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeT = globalRef.sheepLifeT;
							grassEaten();

						}else {
							lifeT = (double) input.getLifeTime();
						}
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						globalRef.state[key][value] = "Empty";
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=false;
							globalRef.isDominant[key][value]=true;		
					//		globalRef.checkAndDisplaceSheep(xcoord, ycoord);
						}
						nextState();
						isDominant=dom;
						isOccupied = false;
						holdIn("Sheep", 0.05);
					} else if(type.compareTo("SheepReproducing") == 0){
						reproductionT = globalRef.sheepReproduceT;
						lifeT = globalRef.sheepLifeT;
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						isDominant=dom;
						nextState();
						isOccupied = false;
						reproduce();
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=true;
							globalRef.isDominant[key][value]=true;		
					//		globalRef.checkAndDisplaceSheep(xcoord, ycoord);

						}
						holdIn("Sheep", 0.05);
					}
				}else if(globalRef.state[key][value].compareTo("Grass") == 0){
					globalRef.state[xcoord][ycoord] = "Grass";
					grassRepT = 1;
					isOccupied = false;
					spread();
					holdIn("Grass", 0.05);
				}
			}
			if(messageOnPort(x, "inSE", i)) {
				SheepGrassEnt input = (SheepGrassEnt) x.getValOnPort("inSE", i);
				int key = (int) input.key;
				int value = (int) input.value;
				boolean dom = (boolean) input.isDominant;

				if(globalRef.state[key][value].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepMoving") == 0){
						reproductionT = (double) input.getReproductionTime();
						if(globalRef.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeT = globalRef.sheepLifeT;
							grassEaten();

						}else {
							lifeT = (double) input.getLifeTime();
						}
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						globalRef.state[key][value] = "Empty";
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=false;
							globalRef.isDominant[key][value]=true;		
		//					globalRef.checkAndDisplaceSheep(xcoord, ycoord);

						}
						nextState();
						isOccupied = false;
						isDominant=dom;
						holdIn("Sheep", 0.05);
					} else if(type.compareTo("SheepReproducing") == 0){
						reproductionT = globalRef.sheepReproduceT;
						lifeT = globalRef.sheepLifeT;
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						isDominant=dom;
						nextState();
						reproduce();
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=true;
							globalRef.isDominant[key][value]=true;	
		//					globalRef.checkAndDisplaceSheep(xcoord, ycoord);

						}
						isOccupied = false;
						holdIn("Sheep", 0.05);
					}
				}else if(globalRef.state[key][value].compareTo("Grass") == 0){
					globalRef.state[xcoord][ycoord] = "Grass";
					grassRepT = 1;
					isOccupied = false;
					spread();
					holdIn("Grass", 0.05);
				}
			}
			if(messageOnPort(x, "inS", i)) {
				SheepGrassEnt input = (SheepGrassEnt) x.getValOnPort("inS", i);
				int key = (int) input.key;
				int value = (int) input.value;
				boolean dom = (boolean) input.isDominant;

				if(globalRef.state[key][value].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepMoving") == 0){
						reproductionT = (double) input.getReproductionTime();
						if(globalRef.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeT = globalRef.sheepLifeT;
							grassEaten();

						}else {
							lifeT = (double) input.getLifeTime();
						}
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						globalRef.state[key][value] = "Empty";
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=false;
							globalRef.isDominant[key][value]=true;	
	//						globalRef.checkAndDisplaceSheep(xcoord, ycoord);

						}
						nextState();
						isDominant=dom;
						isOccupied = false;
						holdIn("Sheep", 0.05);
					} else if(type.compareTo("SheepReproducing") == 0){
						reproductionT = globalRef.sheepReproduceT;
						lifeT = globalRef.sheepLifeT;
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						isDominant=dom;
						nextState();
						reproduce();
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=true;
							globalRef.isDominant[key][value]=true;	
							//globalRef.checkAndDisplaceSheep(xcoord, ycoord);

						}
						isOccupied = false;
						holdIn("Sheep", 0.05);
					}
				}else if(globalRef.state[key][value].compareTo("Grass") == 0){
					globalRef.state[xcoord][ycoord] = "Grass";
					grassRepT = 1;
					isOccupied = false;
					spread();					
					holdIn("Grass", 0.05);
				}
			}
			if(messageOnPort(x, "inSW", i)) {
				SheepGrassEnt input = (SheepGrassEnt) x.getValOnPort("inSW", i);
				int key = (int) input.key;
				int value = (int) input.value;
				boolean dom = (boolean) input.isDominant;

				if(globalRef.state[key][value].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepMoving") == 0){
						reproductionT = (double) input.getReproductionTime();
						if(globalRef.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeT = globalRef.sheepLifeT;
							grassEaten();

						}else {
							lifeT = (double) input.getLifeTime();
						}
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						globalRef.state[key][value] = "Empty";
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=false;
							globalRef.isDominant[key][value]=true;		
				//			globalRef.checkAndDisplaceSheep(xcoord, ycoord);

						}
						nextState();
						isDominant=dom;
						isOccupied = false;
						holdIn("Sheep", 0.05);
					} else if(type.compareTo("SheepReproducing") == 0){
						reproductionT = globalRef.sheepReproduceT;
						lifeT = globalRef.sheepLifeT;
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						nextState();
						reproduce();
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=true;
							globalRef.isDominant[key][value]=true;							
						}
						isOccupied = false;
						
						holdIn("Sheep", 0.05);
					}
				}else if(globalRef.state[key][value].compareTo("Grass") == 0){
					globalRef.state[xcoord][ycoord] = "Grass";
					grassRepT = 1;
					isOccupied = false;
					spread();
					holdIn("Grass", 0.05);
				}
			}
			if(messageOnPort(x, "inW", i)) {
				SheepGrassEnt input = (SheepGrassEnt) x.getValOnPort("inW", i);
				int key = (int) input.key;
				int value = (int) input.value;
				boolean dom = (boolean) input.isDominant;
				if(globalRef.state[key][value].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepMoving") == 0){
						reproductionT = (double) input.getReproductionTime();
						if(globalRef.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeT = globalRef.sheepLifeT;
							grassEaten();

						}else {
							lifeT = (double) input.getLifeTime();
						}
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						globalRef.state[key][value] = "Empty";
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=false;
							globalRef.isDominant[key][value]=true;	
	//						globalRef.checkAndDisplaceSheep(xcoord, ycoord);

						}
						nextState();
						isDominant=dom;
						isOccupied = false;
						holdIn("Sheep", 0.05);
					} else if(type.compareTo("SheepReproducing") == 0){
						reproductionT = globalRef.sheepReproduceT;
						lifeT = globalRef.sheepLifeT;
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						isDominant=dom;
						nextState();
						reproduce();
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=true;
							globalRef.isDominant[key][value]=true;							
						}
						isOccupied = false;
						holdIn("Sheep", 0.05);
					}
				}else if(globalRef.state[key][value].compareTo("Grass") == 0){
					globalRef.state[xcoord][ycoord] = "Grass";
					grassRepT = 1;
					isOccupied = false;
					spread();
					holdIn("Grass", 0.05);
				}
			}
			if(messageOnPort(x, "inNW" ,i)) {
				SheepGrassEnt input = (SheepGrassEnt) x.getValOnPort("inNW", i);
				int key = (int) input.key;
				int value = (int) input.value;
				boolean dom=(boolean) input.isDominant;
				if(globalRef.state[key][value].compareTo("Sheep") == 0) {
					String type = (String) input.getType();
					if(type.compareTo("SheepMoving") == 0){
						reproductionT = (double) input.getReproductionTime();
						if(globalRef.state[xcoord][ycoord].compareTo("Grass") == 0) {
							lifeT = globalRef.sheepLifeT;
							grassEaten();

						}else {
							lifeT = (double) input.getLifeTime();
						}
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						globalRef.state[key][value] = "Empty";
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=false;
							globalRef.isDominant[key][value]=true;	
			//				globalRef.checkAndDisplaceSheep(xcoord, ycoord);

						}
						isDominant=dom;
						nextState();
						isOccupied = false;
						holdIn("Sheep", 0.05);
					} else if(type.compareTo("SheepReproducing") == 0){
						reproductionT = globalRef.sheepReproduceT;
						lifeT = globalRef.sheepLifeT;
						MoveT = globalRef.sheepMoveT;
						globalRef.state[xcoord][ycoord] = "Sheep";
						isDominant=dom;
						nextState();
						reproduce();
						if(dom) {
							globalRef.isDominant[xcoord][ycoord]=true;
							globalRef.isDominant[key][value]=true;							
						}
						isOccupied = false;
						holdIn("Sheep", 0.05);
					}
				}else if(globalRef.state[key][value].compareTo("Grass") == 0){
					globalRef.state[xcoord][ycoord] = "Grass";
					grassRepT = 1;
					isOccupied = false;
					spread();
					holdIn("Grass", 0.05);
				}
			}
		}
		System.out.println("deltext ends with cell "+xcoord+"_"+ycoord+"and dominancy" +isDominant );

	}

	
//DELTINT STARTS
@Override
public void deltint() {
	System.out.println("deltint starts with cell "+xcoord+"_"+ycoord+"_"+currentPhase+"" );

	if(phaseIs("Empty")) {
		globalRef.state[xcoord][ycoord] = "Empty";
		passivateIn("Empty");
	}else if(phaseIs("Sheep")) {
//		if(!isDominant) {
//			globalRef.isDominant[xcoord][ycoord]=false;
//		}
//		else {
//			globalRef.isDominant[xcoord][ycoord]=true;
//
//		}
		globalRef.state[xcoord][ycoord] = "Sheep";
	if(isOccupied) {
		if(currentPhase.compareTo("lifeTime") == 0) {
			globalRef.state[xcoord][ycoord] = "Empty";
			isOccupied = false;
			holdIn("Empty", 0.05);
		}else if(currentPhase.compareTo("moveTime") == 0) {
			//globalRef.state[xcoord][ycoord] = "Empty";
//			if(!globalRef.isDominant[xcoord][ycoord] ){
//			isDominant=false;
//			}
			isOccupied = false;

			holdIn("Empty", 0.05);
		}else if(currentPhase.compareTo("reproductionTime") == 0) {
			if(globalRef.isDominant[xcoord][ycoord] ){
				isDominant=true;
				}
			else {
				isDominant=false;
			}
			if(lifeT <= reproductionT) {
				if(lifeT <= MoveT) {
					currentPhase = "lifeTime";
					isOccupied = true;
					holdIn("Sheep", lifeT);
				}else {
					currentPhase = "moveTime";
					isOccupied = true;
					holdIn("Sheep", MoveT);
				}
			}else if(reproductionT <= MoveT){
				currentPhase = "reproductionTime";
				isOccupied = true;
				holdIn("Sheep", reproductionT);
			}else {
				currentPhase = "moveTime";
				isOccupied = true;
				holdIn("Sheep", MoveT);
			}
		}
	}else {
			if(lifeT <= reproductionT) {
				if(lifeT <= MoveT) {
					currentPhase = "lifeTime";
					isOccupied = true;
					holdIn("Sheep", lifeT);
				}else {
					currentPhase = "moveTime";
					isOccupied = true;
					holdIn("Sheep", MoveT);
				}
			}else if(reproductionT <= MoveT){
				currentPhase = "reproductionTime";
				isOccupied = true;
				holdIn("Sheep", reproductionT);
			}else {
				currentPhase = "moveTime";
				isOccupied = true;
				holdIn("Sheep", MoveT);
			}
		}
	}else if(phaseIs("Grass")) {
		globalRef.state[xcoord][ycoord] = "Grass";
		holdIn("Grass", globalRef.grassReproduceT );
		grassRepT = -1;
	}
	writeOutput(tmp);

	//if(e<=10&&e>=9) {
	
//	}
	
//	System.out.println("deltint ends with cell "+xcoord+"_"+ycoord+"and dominancy" +isDominant );
//	 if(getTime() % 100 == 0) {
//	       writeOutput();
//	    }
	}


public void deltcon(double e, message x) {
    deltext(e, x);
	deltint();
  }


public message out() {
	message m = super.out();
	System.out.println("out starts with cell "+xcoord+"_"+ycoord+"and dominancy" +isDominant );
	if(isOccupied) {
		if(currentPhase.compareTo("lifeTime") == 0) {
			MoveT = MoveT - lifeT;
			reproductionT = reproductionT - lifeT;
			lifeT = 0;
		}else if(currentPhase.compareTo("moveTime") == 0) {
			lifeT = lifeT - MoveT;
			reproductionT = reproductionT - MoveT;
			MoveT = 0;
		}else if(currentPhase.compareTo("reproductionTime") == 0) {
			MoveT = MoveT - reproductionT;
			lifeT = lifeT - reproductionT;
			reproductionT = 0;
		}
	}
writeOutput(tmp);

	if(phaseIs("Sheep")) {
		if(isDominant) {
			m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.black, Color.black)));
			globalRef.isDominant[xcoord][ycoord]=true;

			//	m.add(makeContent(directions.get(dir), new SheepGrassEnt (new Integer(xcoord), new Integer(ycoord), globalRef.sheepReproduceT, globalRef.sheepLifeT, "SheepReproducing",isDominant)));

		}
		else{
			globalRef.isDominant[xcoord][ycoord]=false;
		m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.red, Color.red)));
		}

		if(currentPhase.compareTo("lifeTime") == 0 && isOccupied) {
			m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.white, Color.white)));
			die();
			
		}
		
	
		else if(isOccupied){
			String used = "";
			if(currentPhase.compareTo("reproductionTime") == 0) {
				reproductionT = globalRef.sheepReproduceT;
				ArrayList<String> directions = new ArrayList();
				if(globalRef.state[neighborNorth[0]][neighborNorth[1]].compareTo("Sheep") != 0) {
					directions.add("outN");
				}
				if(globalRef.state[neighborEast[0]][neighborEast[1]].compareTo("Sheep") != 0) {
					directions.add("outNE");
				}
				if(globalRef.state[neighborEast[0]][neighborEast[1]].compareTo("Sheep") != 0) {
					directions.add("outE");
				}
				if(globalRef.state[neighborSouthEast[0]][neighborSouthEast[1]].compareTo("Sheep") != 0) {
					directions.add("outSE");
				}
				if(globalRef.state[neighborSouth[0]][neighborSouth[1]].compareTo("Sheep") != 0) {
					directions.add("outS");
				}
				if(globalRef.state[neighborSouthWest[0]][neighborSouthWest[1]].compareTo("Sheep") != 0) {
					directions.add("outSW");
				}
				if(globalRef.state[neighborWest[0]][neighborWest[1]].compareTo("Sheep") != 0) {
					directions.add("outW");
				}
				if(globalRef.state[neighborNorthWest[0]][neighborNorthWest[1]].compareTo("Sheep") != 0) {
					directions.add("outNW");
				}
				if(!directions.isEmpty()) {
					int dir = globalRef.rand.nextInt(directions.size());
					used = directions.get(dir);
					globalRef.state[xcoord][ycoord] = "Sheep";
					boolean isDom=rand.nextInt(15) == 0;
//
				//	if(isDominant) {
			//		globalRef.isDominant[xcoord][ycoord]=isDominant ;
						m.add(makeContent(directions.get(dir), new SheepGrassEnt (new Integer(xcoord), new Integer(ycoord), globalRef.sheepReproduceT, globalRef.sheepLifeT, "SheepReproducing",isDom)));
				

					
					reproduce();

				}
			}
			
			if(currentPhase.compareTo("moveTime") == 0) {
				ArrayList<String> priority = new ArrayList();
				if(globalRef.state[neighborNorth[0]][neighborNorth[1]].compareTo("Grass") == 0) {
					priority.add("outN");
				}
				if(globalRef.state[neighborEast[0]][neighborEast[1]].compareTo("Grass") == 0) {
					priority.add("outNE");
				}
				if(globalRef.state[neighborEast[0]][neighborEast[1]].compareTo("Grass") == 0) {
					priority.add("outE");
				}
				if(globalRef.state[neighborSouthEast[0]][neighborSouthEast[1]].compareTo("Grass") == 0) {
					priority.add("outSE");
				}
				if(globalRef.state[neighborSouth[0]][neighborSouth[1]].compareTo("Grass") == 0) {
					priority.add("outS");
				}
				if(globalRef.state[neighborSouthWest[0]][neighborSouthWest[1]].compareTo("Grass") == 0) {
					priority.add("outSW");
				}
				if(globalRef.state[neighborWest[0]][neighborWest[1]].compareTo("Grass") == 0) {
					priority.add("outW");
				}
				if(globalRef.state[neighborNorthWest[0]][neighborNorthWest[1]].compareTo("Grass") == 0) {
					priority.add("outNW");
				}
				priority.remove(used);
				if(!priority.isEmpty()) {
					int dir = globalRef.rand.nextInt(priority.size());
					m.add(makeContent(priority.get(dir), new SheepGrassEnt (new Integer(xcoord), new Integer(ycoord), reproductionT, lifeT, "SheepMoving", isDominant)));
				}
				else {
					ArrayList<String> directions = new ArrayList();
					if(globalRef.state[neighborNorth[0]][neighborNorth[1]].compareTo("Sheep") != 0 && (!globalRef.isDominant[neighborNorth[0]][neighborNorth[1]])) {
						directions.add("outN");
					}
					if(globalRef.state[neighborNorthEast[0]][neighborNorthEast[1]].compareTo("Sheep") != 0 && (!globalRef.isDominant[neighborNorthEast[0]][neighborNorthEast[1]])) {
						directions.add("outNE");
					}
					if(globalRef.state[neighborEast[0]][neighborEast[1]].compareTo("Sheep") != 0 && (!globalRef.isDominant[neighborEast[0]][neighborEast[1]])) {
						directions.add("outE");
					}
					if(globalRef.state[neighborSouthEast[0]][neighborSouthEast[1]].compareTo("Sheep") != 0 && (!globalRef.isDominant[neighborSouthEast[0]][neighborSouthEast[1]])) {
						directions.add("outSE");
					}
					if(globalRef.state[neighborSouth[0]][neighborSouth[1]].compareTo("Sheep") != 0 && (!globalRef.isDominant[neighborSouth[0]][neighborSouth[1]])) {
						directions.add("outS");
					}
					if(globalRef.state[neighborSouthWest[0]][neighborSouthWest[1]].compareTo("Sheep") != 0 && (!globalRef.isDominant[neighborSouthWest[0]][neighborSouthWest[1]])) {
						directions.add("outSW");
					}
					if(globalRef.state[neighborWest[0]][neighborWest[1]].compareTo("Sheep") != 0 && (!globalRef.isDominant[neighborWest[0]][neighborWest[1]])) {
						directions.add("outW");
					}
					if(globalRef.state[neighborNorthWest[0]][neighborNorthWest[1]].compareTo("Sheep") != 0 && (!globalRef.isDominant[neighborNorthWest[0]][neighborNorthWest[1]])) {
						directions.add("outNW");
					}
					directions.remove(used);
					if(!directions.isEmpty()) {
						int dir = globalRef.rand.nextInt(directions.size());
					//	if(isDominant) {
						m.add(makeContent(directions.get(dir), new SheepGrassEnt (new Integer(xcoord), new Integer(ycoord), reproductionT, lifeT, "SheepMoving",isDominant)));

						
					}
					else {
						m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.red, Color.red)));
					}
				}
			}
			
			

			
		}
	}else if(phaseIs("Grass")) {
		m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.green, Color.green)));
		//pick random neighbor and create new grass
		if(grassRepT < 0) {
			grassRepT = globalRef.grassReproduceT;
			ArrayList<String> directions = new ArrayList();
			if(globalRef.state[neighborNorth[0]][neighborNorth[1]].compareTo("Empty") == 0) {
				directions.add("outN");
			}
			if(globalRef.state[neighborEast[0]][neighborEast[1]].compareTo("Empty") == 0) {
				directions.add("outNE");
			}
			if(globalRef.state[neighborEast[0]][neighborEast[1]].compareTo("Empty") == 0) {
				directions.add("outE");
			}
			if(globalRef.state[neighborSouthEast[0]][neighborSouthEast[1]].compareTo("Empty") == 0) {
				directions.add("outSE");
			}
			if(globalRef.state[neighborSouth[0]][neighborSouth[1]].compareTo("Empty") == 0) {
				directions.add("outS");
			}
			if(globalRef.state[neighborSouthWest[0]][neighborSouthWest[1]].compareTo("Empty") == 0) {
				directions.add("outSW");
			}
			if(globalRef.state[neighborWest[0]][neighborWest[1]].compareTo("Empty") == 0) {
				directions.add("outW");
			}
			if(globalRef.state[neighborNorthWest[0]][neighborNorthWest[1]].compareTo("Empty") == 0) {
				directions.add("outNW");
			}
			if(!directions.isEmpty()) {
				int dir = globalRef.rand.nextInt(directions.size());
				m.add(makeContent(directions.get(dir), new SheepGrassEnt (new Integer(xcoord), new Integer(ycoord), 0, 0, "grassR",isDominant)));
				spread();
}
		}
	}else if(phaseIs("Empty")) {
		m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale", x_pos, y_pos, Color.white, Color.white)));
		globalRef.decrementGrassCount();
	}
	System.out.println("out ends with cell "+xcoord+"_"+ycoord+"and dominancy" +isDominant );
	return m;
}
public void nextState() {
	if(lifeT <= reproductionT) {
		if(lifeT <= MoveT) {
			currentPhase = "lifeTime";
		}else {
			currentPhase = "moveTime";
		}
	}else if(reproductionT <= MoveT){
		currentPhase = "reproductionTime";
	}else {
		currentPhase = "moveTime";
	}
}




public sheepGrassSystem(int i, int j) {
      super(new Pair(new Integer(i), new Integer(j)));
  }

}