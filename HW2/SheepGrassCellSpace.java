package HW2;

import simView.*;
import twoDCellSpace.TwoDimCell;
import twoDCellSpace.TwoDimCellSpace;
import twoDCellSpace.lightSpreadCell;
import twoDCellSpace.lightSpreadCellSpace;
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.heapSim.HeapCoord;
import genDevs.simulation.realTime.*;
import GenCol.*;
import genDevs.plots.*;
import java.util.*;
import java.awt.*;

import java.text.*;
import java.io.*;

public class SheepGrassCellSpace extends TwoDimCellSpace {
	  static GlobalRef ref = GlobalRef.getInstance();
	  SheepGrassCellSpace q;


	  public static void main(String args[]){
		SheepGrassCellSpace q = new SheepGrassCellSpace();
		ref.setSpace(q);
		//coordinator r = new coordinator(q);
	    TunableCoordinator r = new TunableCoordinator(q);
	    r.setTimeScale(.03);
		//HeapCoord r = new HeapCoord(q);
	  r.initialize();
	  r.simulate(9999999);
	  //System.exit(0);
	  }
	  
	  SheepGrassCellSpace(){
		  this(40, 40);
	  }
	  
	  SheepGrassCellSpace(int x, int y){
		  super("Predator Prey model", x, y);
		  
		  this.numCells = x*y;
		  ref.setDim(x, y);
		  
		  for (int i = 0; i < xDimCellspace; i++) {
		      for (int j = 0; j < yDimCellspace; j++) {
		        SheepGrassCell cell = new SheepGrassCell(i, j);
		        //fc.visible = false;
		        ref.cell_ref[i][j] = cell;
		        ref.state[i][j] = "Empty";
		        addCell(cell, xDimCellspace, yDimCellspace);
		      }
		    }
		  
		  switch(5) {
		  case 1:
			  ref.state[14][14] = "Grass";
			  break;
		  case 2:
			  ref.state[0][0] = "Grass";
			  ref.state[14][14] = "Grass";
			  ref.state[24][24] = "Grass";
			  ref.state[24][14] = "Grass";
			  break;
		  case 3:
			  ref.state[13][13] = "Sheep";
			  break;
		  case 4:
			  ref.state[13][13] = "Sheep";
			  ref.state[18][13] = "Sheep";
			  ref.state[13][20] = "Sheep";
			  break;
		  case 5:
			  ref.state[11][11] = "Grass";
			  ref.state[11][14] = "Grass";
			  ref.state[11][12] = "Grass";
			  ref.state[12][13] = "Sheep";
			  break;
		  case 6:
			  ref.state[14][14] = "Grass";
			  ref.state[13][14] = "Grass";
			  ref.state[14][15] = "Grass";
			  ref.state[13][13] = "Sheep";
			  ref.state[0][0] = "Grass";
			  ref.state[0][1] = "Grass";
			  ref.state[1][1] = "Grass";
			  ref.state[1][10] = "Grass";
			  ref.state[1][0] = "Sheep";
			  ref.state[14][28] = "Grass";
			  ref.state[14][29] = "Grass";
			  ref.state[13][28] = "Grass";
			  ref.state[13][27] = "Sheep";
			  break;
		  }
		  
		  
		  doNeighborToNeighborCoupling();
		  DoBoundaryToBoundaryCoupling();
		  
		  CellGridPlot t = new CellGridPlot("SheepGrassCellSpace", .1,"", 400, "", 400);
		  t.setCellSize(20);
		  t.setCellGridViewLocation(0, 0);
		  add(t);
		  // t.setHidden(false);
		  
		  coupleOneToAll(this, "stop", "stop");
		  coupleOneToAll(this, "start", "start");
		  coupleAllTo("outDraw", t, "drawCellToScale");
	  }

///////////////////////////////////////////////////////////////////////////////////////
// The following are two utility functions that can be useful for you to finish the homework.
// Feel free to modify them, and/or copy them to other places of your code that work for your model

/**
 * Add couplings among boundary cells to make the cell space wrapped
 */
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




}
// End SheepGrassCellSpace
