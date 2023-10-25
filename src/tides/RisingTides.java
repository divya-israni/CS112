package tides;

import java.util.*;

/**
 * This class contains methods that provide information about select terrains 
 * using 2D arrays. Uses floodfill to flood given maps and uses that 
 * information to understand the potential impacts. 
 * Instance Variables:
 *  - a double array for all the heights for each cell
 *  - a GridLocation array for the sources of water on empty terrain 
 * 
 * @author Original Creator Keith Scharz (NIFTY STANFORD) 
 * @author Vian Miranda (Rutgers University)
 */
public class RisingTides {

    // Instance variables
    private double[][] terrain;     // an array for all the heights for each cell
    private GridLocation[] sources; // an array for the sources of water on empty terrain 

    /**
     * DO NOT EDIT!
     * Constructor for RisingTides.
     * @param terrain passes in the selected terrain 
     */
    public RisingTides(Terrain terrain) {
        this.terrain = terrain.heights;
        this.sources = terrain.sources;
    }

    /**
     * Find the lowest and highest point of the terrain and output it.
     * 
     * @return double[][], with index 0 and index 1 being the lowest and 
     * highest points of the terrain, respectively
     */
    public double[] elevationExtrema() {

        double max = Integer.MIN_VALUE;
        double min = Integer.MAX_VALUE;
        double[] extremas = new double[2];
        for(int i=0; i<terrain.length; i++){
            for(int j=0; j<terrain[i].length; j++){
                if (terrain[i][j]<min){
                    min = terrain[i][j];
                    extremas[0] = min;
                }
                if(terrain[i][j]>max){
                    max = terrain[i][j];
                    extremas[1] = max;
                }
            }
        }
        return extremas;
    }

    /**
     * Implement the floodfill algorithm using the provided terrain and sources.
     * 
     * All water originates from the source GridLocation. If the height of the 
     * water is greater than that of the neighboring terrain, flood the cells. 
     * Repeat iteratively till the neighboring terrain is higher than the water 
     * height.
     * 
     * 
     * @param height of the water
     * @return boolean[][], where flooded cells are true, otherwise false
     */

    public boolean[][] floodedRegionsIn(double height) { 
        
        boolean[][] resultingArray = new boolean[terrain.length][terrain[0].length];
        ArrayList<GridLocation> waterSources = new ArrayList<GridLocation>();
        for(int i=0; i<sources.length; i++){
            waterSources.add(sources[i]);
            resultingArray[waterSources.get(i).row][waterSources.get(i).col] = true;
        }
        while (waterSources.size()!=0){

            GridLocation original = waterSources.remove(0);
            
            if (original.row<terrain.length-1 && terrain[original.row+1][original.col] <= height && resultingArray[original.row+1][original.col] != true){ //south
                GridLocation flood = new GridLocation(original.row+1, original.col);
                waterSources.add(flood);
                resultingArray[flood.row][flood.col] = true; 
            }

            if (original.row>0 && terrain[original.row-1][original.col] <= height && resultingArray[original.row-1][original.col] != true){ //north
                GridLocation flood = new GridLocation(original.row-1, original.col);
                waterSources.add(flood);
                resultingArray[flood.row][flood.col] = true;
            }

            if (original.col<terrain[0].length-1 && terrain[original.row][original.col+1] <= height && resultingArray[original.row][original.col+1] != true){ //east
                GridLocation flood = new GridLocation(original.row, original.col+1);
                waterSources.add(flood);
                resultingArray[flood.row][flood.col] = true;
            }

            if(original.col>0 && terrain[original.row][original.col-1] <= height && resultingArray[original.row][original.col-1] != true){//west
                GridLocation flood = new GridLocation(original.row, original.col-1);
                waterSources.add(flood);
                resultingArray[flood.row][flood.col] = true;
            }
        }
        return resultingArray; 
    }

    /**
     * Checks if a given cell is flooded at a certain water height.
     * 
     * @param height of the water
     * @param cell location 
     * @return boolean, true if cell is flooded, otherwise false
     */
    public boolean isFlooded(double height, GridLocation cell) {
        
        boolean[][] copy = floodedRegionsIn(height);
        return copy[cell.row][cell.col]; 
    }

    /**
     * Given the water height and a GridLocation find the difference between 
     * the chosen cells height and the water height.
     * 
     * If the return value is negative, the Driver will display "meters below"
     * If the return value is positive, the Driver will display "meters above"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param cell location
     * @return double, representing how high/deep a cell is above/below water
     */
    public double heightAboveWater(double height, GridLocation cell) {
        double difference = terrain[cell.row][cell.col] - height;
        return difference;
    }

    /**
     * Total land available (not underwater) given a certain water height.
     * 
     * @param height of the water
     * @return int, representing every cell above water
     */
    public int totalVisibleLand(double height) {
        
        boolean[][] copy = floodedRegionsIn(height);
        int count = 0;
        for (int i=0; i<copy.length; i++){
            for(int j=0; j<copy[i].length; j++){
                if(!copy[i][j])
                    count++;
            }
        }
        return count; 
    } 


    /**
     * Given 2 heights, find the difference in land available at each height. 
     * 
     * If the return value is negative, the Driver will display "Will gain"
     * If the return value is positive, the Driver will display "Will lose"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param newHeight the future height of the water
     * @return int, representing the amount of land lost or gained
     */
    public int landLost(double height, double newHeight) {
        
        int currentLand = totalVisibleLand(height); 
        int futureLand = totalVisibleLand(newHeight);
        return currentLand - futureLand;
    }

    private boolean isUnioned(WeightedQuickUnionUF uf, GridLocation cellA, GridLocation cellB){
        if (uf.find(cellA) == uf.find(cellB))
            return true;
        return false;
    }
    /**
     * Count the total number of islands on the flooded terrain.
     * 
     * Parts of the terrain are considered "islands" if they are completely 
     * surround by water in all 8-directions. Should there be a direction (ie. 
     * left corner) where a certain piece of land is connected to another 
     * landmass, this should be considered as one island. A better example 
     * would be if there were two landmasses connected by one cell. Although 
     * seemingly two islands, after further inspection it should be realized 
     * this is one single island. Only if this connection were to be removed 
     * (height of water increased) should these two landmasses be considered 
     * two separate islands.
     * 
     * @param height of the water
     * @return int, representing the total number of islands
     */
    public int numOfIslands(double height) {
        GridLocation cell;
        WeightedQuickUnionUF islands = new WeightedQuickUnionUF(terrain.length, terrain[0].length);
        ArrayList<GridLocation> islandLocs = new ArrayList<GridLocation>();   
        GridLocation root; 
        boolean[][] floodedOrNot = floodedRegionsIn(height); 
        for(int i=0; i<terrain.length; i++){
            for(int j=0; j<terrain[0].length; j++){ 
                cell = new GridLocation(i, j);
                if (!floodedOrNot[i][j] && i>0 && j>0 && i<terrain.length-1 && j<terrain[0].length-1){
                    //top left
                    if (!isUnioned(islands, cell, new GridLocation(i-1, j-1))){
                        if (!floodedOrNot[i-1][j-1]){                          
                            islands.union(cell, new GridLocation(i-1, j-1));
                        }
                    }

                    //left
                    if (!isUnioned(islands, cell, new GridLocation(i, j-1))){
                        if (!floodedOrNot[i-1][j-1]){                         
                            islands.union(cell, new GridLocation(i, j-1));
                        }
                    }

                    //bottom left
                    if (!isUnioned(islands, cell, new GridLocation(i+1, j-1))){
                        if (!floodedOrNot[i+1][j-1]){
                            islands.union(cell, new GridLocation(i+1, j-1));
                        }
                    }

                    //top 
                    if (!isUnioned(islands, cell, new GridLocation(i-1, j))){
                        if (!floodedOrNot[i-1][j]){
                            islands.union(cell, new GridLocation(i-1, j));
                        }
                    }

                    //bottom
                    if (!isUnioned(islands, cell, new GridLocation(i+1, j))){
                        if (!floodedOrNot[i+1][j]){
                            islands.union(cell, new GridLocation(i+1, j));
                        }
                    }

                    //top right
                    if (!isUnioned(islands, cell, new GridLocation(i-1, j+1))){
                        if (!floodedOrNot[i-1][j+1]){
                            islands.union(cell, new GridLocation(i-1, j+1));
                        }
                    }

                    //right
                    if (!isUnioned(islands, cell, new GridLocation(i-1, j+1))){
                        if (!floodedOrNot[i][j+1]){
                            islands.union(cell, new GridLocation(i, j+1));
                        }
                    }

                    //bottom right
                    if (!isUnioned(islands, cell, new GridLocation(i+1, j+1))){
                        if (!floodedOrNot[i+1][j+1]){
                            islands.union(cell, new GridLocation(i+1, j+1));
                        }
                    }                   
                }
                else if (i==0 && !floodedOrNot[i][j] && floodedOrNot[i+1][j]){
                    if(j>0 && j<terrain[0].length-1){
                        if(!floodedOrNot[i][j+1])
                            islands.union(cell, new GridLocation(i, j+1));
                        if(!floodedOrNot[i][j-1])
                            islands.union(cell, new GridLocation(i, j-1));
                    }
                    if(j==0){
                        if(!isUnioned(islands, cell, new GridLocation(i, j+1))){
                            if(!floodedOrNot[i][j+1])
                                islands.union(cell, new GridLocation(i, j+1));
                        }
                    }
                    if(j==terrain[0].length-1){
                        if(!isUnioned(islands, cell, new GridLocation(i, j-1))){
                            if(!floodedOrNot[i][j-1])
                                islands.union(cell, new GridLocation(i, j-1));
                        }
                    }
                }
                else if (i==terrain.length-1 && !floodedOrNot[i][j] && floodedOrNot[i-1][j]){
                    if(j>0 && j<terrain[0].length-1){
                        if(!floodedOrNot[i][j+1])
                            islands.union(cell, new GridLocation(i, j+1));
                        if(!floodedOrNot[i][j-1])
                            islands.union(cell, new GridLocation(i, j-1));
                    }
                    if(j==0){
                        if(!isUnioned(islands, cell, new GridLocation(i, j+1))){
                            if(!floodedOrNot[i][j+1])
                                islands.union(cell, new GridLocation(i, j+1));
                        }
                    }
                    if(j==terrain[0].length-1){
                        if(!isUnioned(islands, cell, new GridLocation(i, j-1))){
                            if(!floodedOrNot[i][j-1])
                                islands.union(cell, new GridLocation(i, j-1));
                        }
                    }
                }
                else if (j==0 && !floodedOrNot[i][j] && floodedOrNot[i][j+1]){
                    if(i>0 && i<terrain.length-1){
                        if(!floodedOrNot[i+1][j])
                            islands.union(cell, new GridLocation(i+1, j));
                        if(!floodedOrNot[i-1][j])
                            islands.union(cell, new GridLocation(i-1, j));
                    }
                    if(i==0){
                        if(!isUnioned(islands, cell, new GridLocation(i+1, j))){
                            if(!floodedOrNot[i+1][j])
                                islands.union(cell, new GridLocation(i+1, j));
                        }
                    }
                    if(i==terrain.length-1){
                        if(!isUnioned(islands, cell, new GridLocation(i-1, j))){
                            if(!floodedOrNot[i-1][j])
                                islands.union(cell, new GridLocation(i-1, j));
                        }
                    }
                }
                else if(j==terrain[0].length-1 && !floodedOrNot[i][j] && floodedOrNot[i][j-1]){
                    if(i>0 && i<terrain.length-1){
                        if(!floodedOrNot[i+1][j])
                            islands.union(cell, new GridLocation(i+1, j));
                        if(!floodedOrNot[i-1][j])
                            islands.union(cell, new GridLocation(i-1, j));
                    }
                    if(i==0){
                        if(!isUnioned(islands, cell, new GridLocation(i+1, j))){
                            if(!floodedOrNot[i+1][j])
                                islands.union(cell, new GridLocation(i+1, j));
                        }
                    }
                    if(i==terrain.length-1){
                        if(!isUnioned(islands, cell, new GridLocation(i-1, j))){
                            if(!floodedOrNot[i-1][j])
                                islands.union(cell, new GridLocation(i-1, j));
                        }
                    }
                }
            }
        }
        for (int i = 0; i < terrain.length; i++){
            for (int j = 0; j < terrain[0].length; j++){
                if (!floodedOrNot[i][j]){
                    root = islands.find(new GridLocation(i, j));
                    if (!islandLocs.contains(root))
                        islandLocs.add(root);
                }
            }
        }
        return islandLocs.size(); 
    }
}