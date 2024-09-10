package PredatorPrey.Rpat;

import genDevs.plots.CellGridPlot;
import genDevs.simulation.coordinator;
import twoDCellSpace.TwoDimCell;
import twoDCellSpace.TwoDimCellSpace;

public class SheepGrassCellSpace
    extends TwoDimCellSpace {

	static GlobalRef ref =GlobalRef.getInstance();
	  SheepGrassCellSpace r;
	  public SheepGrassCellSpace() {
		  //
		  this(40, 40);
		  //  this(2,2);
		  // this(20,20);
		}

	  public static void main(String args[]){
			SheepGrassCellSpace r = new SheepGrassCellSpace();
				ref.setSpace(r);
		  //CoordinatorInterface r;


		coordinator q = new coordinator(r);
		//  HeapCoord q = new HeapCoord(r);
		//TunableCoordinator q = new TunableCoordinator(r);
		// q.setTimeScale(0.1);
		  q.initialize();

		  q.simulate(10000000);
	  }
SheepGrassCellSpace(int x, int y) {
    super("SheepGrass Cell Space", x, y);
    
    this.numCells = x*y;
	  ref.setDim(x, y);
	  
	  for (int i = 0; i < xDimCellspace; i++) {
	      for (int j = 0; j < yDimCellspace; j++) {
	        sheepGrassSystem cell = new sheepGrassSystem(i, j);
	        //fc.visible = false;
	        ref.cell_ref[i][j] = cell;
	        ref.state[i][j] = "Empty";
	        addCell(cell, xDimCellspace, yDimCellspace);
	      }
	    }
	  
	  switch(6) {
	  case 1:
		  ref.state[20][20] = "Grass";
		  break;
	  case 2:
		  ref.state[10][0] = "Grass";
		  ref.state[4][14] = "Grass";
		  ref.state[2][22] = "Grass";
		  ref.state[33][18] = "Grass";
		  break;
	  case 3:
		  ref.state[20][20] = "Sheep";
		  break;
	  case 4:
		  ref.state[11][13] = "Sheep";
		  ref.state[17][38] = "Sheep";
		  ref.isDominant[11][13] = true;
		  ref.state[8][32] = "Sheep";
		  ref.isDominant[17][38] = true;
		  ref.isDominant[8][32] = true;


		  break;
	  case 5:
		  ref.state[11][11] = "Grass";
		  ref.state[11][14] = "Grass";
		  ref.state[11][12] = "Grass";
		  ref.state[11][13] = "Sheep";
		  ref.isDominant[11][13] = true;
		  ref.state[11][15] = "Grass";

		  break;
	  case 6:
		  ref.state[19][20] = "Grass";
		  ref.state[18][19] = "Grass";
		  ref.state[19][21] = "Grass";
		  ref.state[18][21] = "Grass";
		  ref.state[20][18] = "Sheep";
		  ref.state[20][20] = "Grass";
		  ref.state[20][21] = "Grass";
		  ref.state[21][18] = "Grass";
		  ref.state[21][15] = "Grass";
		  ref.state[21][20] = "Sheep";
		  ref.state[21][19] = "Grass";
		  ref.state[21][17] = "Sheep";
		  ref.isDominant[13][18] = true;
		  ref.isDominant[21][20] = true;
		  ref.isDominant[20][18] = true;



		  break;
	  }
	  

     	 
     	 
     	 hideAll(); //hides only components so far

     	    // Do the couplings

     	    doNeighborToNeighborCoupling();
     	    DoBoundaryToBoundaryCoupling();

     	    coupleOneToAll(this, "stop", "stop");
     	    coupleOneToAll(this, "start", "start");

     	    CellGridPlot t = new CellGridPlot("SheepGrassCellSpace", .5,
     	                                      "", 400, "", 400);
     	    
     	    

     	    t.setCellSize(20);
     	    t.setCellGridViewLocation(0, 0);
     	    add(t);
     	    // t.setHidden(false);
     	    coupleAllTo("outDraw", t, "drawCellToScale");
     	    }


    	 

  private void DoBoundaryToBoundaryCoupling()
  {
      //top and bottom rows
      for( int x = 1; x < xDimCellspace-1; x++ )
      {
          // (x,0) -- bottom to top
          addCoupling(withId(x, 0), "outS", withId(x, yDimCellspace-1), "inN");
          addCoupling(withId(x, 0), "outSW", withId(x-1, yDimCellspace-1), "inNE");
          addCoupling(withId(x, 0), "outSE", withId(x+1, yDimCellspace-1), "inNW");

          // (x,29) -- top to bottom
          addCoupling(withId(x, yDimCellspace-1), "outN", withId(x, 0), "inS");
          addCoupling(withId(x, yDimCellspace-1), "outNE", withId(x+1, 0), "inSW");
          addCoupling(withId(x, yDimCellspace-1), "outNW", withId(x-1, 0), "inSE");
      }

      //west and east columns
      for( int y = 1; y < yDimCellspace-1; y++ )
      {
          // (0,y) -- West - east
          addCoupling(withId(0, y), "outW", withId(xDimCellspace-1, y), "inE");
          addCoupling(withId(0, y), "outSW", withId(xDimCellspace-1, y-1), "inNE");
          addCoupling(withId(0, y), "outNW", withId(xDimCellspace-1, y+1), "inSE");

          // (29,y) -- West - east
          addCoupling(withId(xDimCellspace-1, y), "outE", withId(0, y), "inW");
          addCoupling(withId(xDimCellspace-1, y), "outNE", withId(0, y+1), "inSW");
          addCoupling(withId(xDimCellspace-1, y), "outSE", withId(0, y-1), "inNW");
      }
      //corners
      //(0, 0)
      addCoupling(withId(0, 0), "outNW", withId(xDimCellspace-1, 1), "inSE");
      addCoupling(withId(0, 0), "outW", withId(xDimCellspace-1, 0), "inE");
      addCoupling(withId(0, 0), "outSW", withId(xDimCellspace-1, yDimCellspace-1), "inNE");
      addCoupling(withId(0, 0), "outS", withId(0, yDimCellspace-1), "inN");
      addCoupling(withId(0, 0), "outSE", withId(1, yDimCellspace-1), "inNW");
      //(29, 0)
      addCoupling(withId(xDimCellspace-1, 0), "outSW", withId(xDimCellspace-2, yDimCellspace-1), "inNE");
      addCoupling(withId(xDimCellspace-1, 0), "outE", withId(0, 0), "inW");
      addCoupling(withId(xDimCellspace-1, 0), "outSE", withId(0, yDimCellspace-1), "inNW");
      addCoupling(withId(xDimCellspace-1, 0), "outS", withId(xDimCellspace-1, yDimCellspace-1), "inN");
      addCoupling(withId(xDimCellspace-1, 0), "outNE", withId(0, 1), "inSW");
      //(0, 29)
      addCoupling(withId(0, yDimCellspace-1), "outSW", withId(xDimCellspace-1, yDimCellspace-2), "inNE");
      addCoupling(withId(0, yDimCellspace-1), "outW", withId(xDimCellspace-1, yDimCellspace-1), "inE");
      addCoupling(withId(0, yDimCellspace-1), "outNE", withId(1, 0), "inSW");
      addCoupling(withId(0, yDimCellspace-1), "outN", withId(0, 0), "inS");
      addCoupling(withId(0, yDimCellspace-1), "outNW", withId(xDimCellspace-1, 0), "inSE");
      //(29, 29)
      addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outNW", withId(xDimCellspace-2, 0), "inSE");
      addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outE", withId(0, yDimCellspace-1), "inW");
      addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outSE", withId(0, yDimCellspace-2), "inNW"); // Xiaolin Hu, 10/16/2016
      addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outN", withId(xDimCellspace-1, 0), "inS");
      addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outNE", withId(0, 0), "inSW");
  }

	/**
	 * Get the x and y coordinate (int[2]) of a neighbor cell based on the direction in a wrapped cell space
	 * @param myCell: the center cell
	 * @param direction: the direction defines which neighbor cell to get. 0 - N; 1 - NE; 2 - E; ... (clokewise)
	 * @return the x and y coordinate
	 */
  
 
  
//  
//  ArrayList<TimeStepData> timeStepDataList = new ArrayList<>();
//
//  for (int timeStep = 0; timeStep < 2000000; timeStep++) {
//
//      int currentSheepPop = ref.getSheepCount();
//      int currentGrassPop = ref.getGrassCount();
//      timeStepDataList.add(new TimeStepData(timeStep, currentSheepPop, currentGrassPop));
//  }
////  System.out.println(currentSheepPop);
////  System.out.println(currentGrassPop);
//  try (PrintWriter writer = new PrintWriter(new File("population_data.csv"))) {
//      StringBuilder sb = new StringBuilder();
//      sb.append("TimeStep,SheepPopulation,GrassPopulation\n");
//
//      for (TimeStepData data : timeStepDataList) {
//          sb.append(data.gettimeStep()).append(",");
//          sb.append(data.getSheepPopulation()).append(",");
//          sb.append(data.getGrassPopulation()).append("\n");
//      }
//
//      writer.write(sb.toString());
//  } catch (FileNotFoundException e) {
//      e.printStackTrace();
//  }
//}
//
// // System.exit(0);
//  
//  
//  public class TimeStepData extends SheepGrassCellSpace {
//	    private int timeStep;
//	    private int sheepPopulation;
//	    private int grassPopulation;
//
//	    public TimeStepData(int timeStep, int sheepPopulation, int grassPopulation) {
//	        this.timeStep = timeStep;
//	        this.sheepPopulation =  ref.getSheepCount();
//
//	        this.grassPopulation = ref.getGrassCount();
//	    }
//
//	    public int gettimeStep() {
//	    	return timeStep;
//	    }
//	    public int getSheepPopulation() {
//	    	return sheepPopulation;
//	    }
//	    public int getGrassPopulation() {
//	    	return grassPopulation;
//	    }
//  }
//  
//	  

	
  

  public int[] getNeighborXYCoord(TwoDimCell myCell, int direction )
  {
      int[] myneighbor = new int[2];
      int tempXplus1 = myCell.getXcoord()+1;
      int tempXminus1 = myCell.getXcoord()-1;
      int tempYplus1 = myCell.getYcoord()+1;
      int tempYminus1 = myCell.getYcoord()-1;

      if( tempXplus1 >= xDimCellspace)
          tempXplus1 = 0;

      if( tempXminus1 < 0 )
          tempXminus1 = xDimCellspace-1;

      if( tempYplus1 >= yDimCellspace)
          tempYplus1 = 0;

      if( tempYminus1 < 0 )
          tempYminus1 = yDimCellspace-1;

      // N
      if( (direction == 0) )
      {
          myneighbor[0] = myCell.getXcoord();
          myneighbor[1] = tempYplus1;
      }
      // NE
      else if( (direction == 1) )
      {
          myneighbor[0] = tempXplus1;
          myneighbor[1] = tempYplus1;
      }
      // E
      else if( (direction == 2) )
      {
          myneighbor[0] = tempXplus1;
          myneighbor[1] = myCell.getYcoord();
      }
      // SE
      else if( (direction == 3) )
      {
          myneighbor[0] = tempXplus1;
          myneighbor[1] = tempYminus1;
      }
      // S
      else if( (direction == 4) )
      {
          myneighbor[0] = myCell.getXcoord();
          myneighbor[1] = tempYminus1;
      }
      // SW
      else if( (direction == 5) )
      {
          myneighbor[0] = tempXminus1;
          myneighbor[1] = tempYminus1;
      }
      // W
      else if( (direction == 6) )
      {
          myneighbor[0] = tempXminus1;
          myneighbor[1] = myCell.getYcoord();
      }
      // NW
      //( (direction == 7) )
      else
      {
          myneighbor[0] = tempXminus1;
          myneighbor[1] = tempYplus1;
      }
      return myneighbor;
  }

  /**
   * Automatically generated by the SimView program.
   * Do not edit this manually, as such changes will get overwritten.
   */
 
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
   
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
   
}
// End lightSpreadCellSpace
