
package PredatorPrey.Rpat;
import java.util.Random;

import genDevs.modeling.IODevs;
import twoDCellSpace.TwoDimCell;


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
  private final long SEED = 58795;
  public Random rand = new Random(SEED);
  public Boolean[][] isDominant;
 // public boolean isDom;


  private SheepGrassCellSpace space;
  public double domLifeTime = 5.0;
  public double grassReproduceT = 2.0; // change to the appropriate value for your model 
  public double sheepMoveT = 1.5; // change to the appropriate value for your model 
  public double sheepLifeT  = 3.0; // change to the appropriate value for your model 
  public double sheepReproduceT = 4.0; // change to the appropriate value for your model 
  private int sheepCount = 0;
  private int grassCount = 0;
//private boolean[][] dominantSheep;
  
  

  public synchronized void incrementSheepCount() {
      sheepCount++;
  }

  public synchronized void decrementSheepCount() {
      sheepCount--;
  }
  
  public int getSheepCount() {
	  return sheepCount;
	  
  }
 

  public synchronized void incrementGrassCount() {
      grassCount++;
  }

  public synchronized void decrementGrassCount() {
      grassCount--;
  }
  
  public int getGrassCount() {
	  return grassCount;
  }
  public void checkAndDisplaceSheep(int x, int y) {
      // dominant or  lot
      if (isDominantSheep(x, y)) {
          // Check each cell in the 3x3 grid around (x, y)
          for (int dx = -1; dx <= 1; dx++) {
              for (int dy = -1; dy <= 1; dy++) {
                  int checkX = (x + dx + xDim) % xDim; // 
                  int checkY = (y + dy + yDim) % yDim;

                  //logic to displace non-dominant sheep
                  if (state[checkX][checkY].equals("Sheep") && !isDominantSheep(checkX, checkY)) {

                      state[checkX][checkY] = "Empty";
                  }
              }
          }
      }
  }
  boolean isDominantSheep(int x, int y) {
      if (x >= 0 && x < xDim && y >= 0 && y < yDim) {
    	  if(isDominant[x][y]==true) {
          return isDominant[x][y];
    	  }
      }
      return false;
  }
  
  public void setDominance(int x, int y, boolean isDominant) {
      if (x >= 0 && x < xDim && y >= 0 && y < yDim) {
    	  isDominant=true;
		GlobalRef.getInstance().isDominant[x][y] = true;
      }
  }


  private GlobalRef(){}  // construction function

  public static GlobalRef getInstance(){
    if(_instance!=null) return _instance;
    else {
      _instance = new GlobalRef();
      return _instance;
    }
  }
  public void setSpace(SheepGrassCellSpace space) {
	  this.space = space;
}

public int[] getNeighbor(int x, int y, int direction) {
	  int [] val =space.getNeighborXYCoord((TwoDimCell) cell_ref[x][y], direction);

	  return val;
}


  public void setDim(int x, int y){
    xDim = x;
    yDim = y;
    state = new String[xDim][yDim];
    cell_ref = new IODevs[xDim][yDim];
    isDominant = new Boolean[xDim][yDim]; 

  }


}
