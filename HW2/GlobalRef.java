
package HW2;

import genDevs.modeling.*;
import twoDCellSpace.TwoDimCell;

import java.io.*;
import java.util.Random;


/**
 * This class defines an GlobalRef that makes it easy to find a cell and its
 * information, such as current state of the cell, and the cell's reference
 *
 * @author  Xiaolin Hu
 * @Date: Sept. 2007
 */
public class GlobalRef {
  protected static int xDim;
  protected static int yDim;
  protected static GlobalRef _instance=null;

  public String[][] state;
  public IODevs[][] cell_ref;
  public SheepGrassCellSpace space;
  public Random rand = new Random(88788);
  
  public double grassReproduceT = 1.5; // change to the appropriate value for your model 
  public double sheepMoveT = 1.0; // change to the appropriate value for your model 
  public double sheepLifeT  = 3.0; // change to the appropriate value for your model 
  public double sheepReproduceT = 3.5; // change to the appropriate value for your model 
  

  private GlobalRef(){}  // construction function

  public static GlobalRef getInstance(){
    if(_instance!=null) return _instance;
    else {
      _instance = new GlobalRef();
      return _instance;
    }
  }

  public void setDim(int x, int y){
    xDim = x;
    yDim = y;
    state = new String[xDim][yDim];
    cell_ref = new IODevs[xDim][yDim];
  }
  
  public void setSpace(SheepGrassCellSpace space) {
	  this.space = space;
  }

  public int[] getNeighbor(int x, int y, int direction) {
	  int [] val = space.getNeighborXYCoord((TwoDimCell) cell_ref[x][y], direction);
	  //System.out.println(val[0]);
	  return val;
  }
}
