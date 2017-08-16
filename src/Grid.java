import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * This class represents a Grid in the game window. It is responsible for drawing and keeping track of all
 * the blocks on the Grid.
 */
public class Grid {

    //The positions of the Grid.
    private int x, y;

    //The number of rows and columns.
    private int rows, columns;

    //All of the blocks in this Grid.
    private ArrayList<Block> blocks = new ArrayList<>(0);
    
    //The colours to use in drawing.
    final static private Color GRID_BACKGROUND_COLOUR = new Color(0, 70, 100);
    final static private Color GRID_LINES_COLOUR = new Color(0, 170, 227);
    final static private Color OUTLINE_COLOUR = Color.BLACK;
    final static private Color TERMINAL_LINE_COLOUR = new Color(161, 0, 4);

    //The Grid`s outline`s stroke width.
    final static private int OUTLINE_WIDTH = 5;

    //Whether or not to draw the red terminal line at the top.
    private boolean drawTerminalLine;
    //Where the terminal line is.
    final private static int TERMINAL_LINE_ROW = 0;

    //The size of each cell.
    private int cellSize;

    /**
     * Constructor. Takes in positions and dimensions as parameters.
     * @param x The left coordinate of the Grid.
     * @param y The top coordinate of the Grid.
     * @param columns The number of columns on the Grid.
     * @param rows The number of rows on the Grid.
     */
    public Grid(int x, int y, int columns, int rows) {
        setX(x);
        setY(y);
        setColumns(columns);
        setRows(rows);

        //Defaults.
        setDrawTerminalLine(false);
        setCellSize(Block.getBlockWidth());
    }

    //TEST
   /* public void test() {
        for (int i = 0; i < rows; i ++) {
            Block block = new Block(0,i,Color.WHITE);
            block.setPartOfGamePiece(false);
            getBlocks().add(block);
            block = new Block(3,i,Color.WHITE);
            block.setPartOfGamePiece(false);
            getBlocks().add(block);
        }
    }*/

    /**
     * Draw the Grid.
     */
    public void draw(Graphics g) {
        drawGrid(g);
        drawBlocks(g);
    }

    /**
     * Draw the lines of the Grid.
     * @param g The graphics object.
     */
    private void drawGrid(Graphics g) {

        //Obtain dimensions of the Grid.
        Dimension size = getGridDimensions();

        //Draw the background.
    	g.setColor(GRID_BACKGROUND_COLOUR);
    	g.fillRect(x, y, size.width, size.height);

    	//Draw the terminal line.
        if (drawTerminalLine == true) {
            g.setColor(TERMINAL_LINE_COLOUR);
            g.fillRect(x, y, size.width, getCellSize());
        }

        //Offset is basically the top left position of this Grid within the window.
    	int offsetX = this.x;
    	int offsetY = this.y;
    	
    	g.setColor(GRID_LINES_COLOUR);
    	
    	//Calculate the width and height of the Grid in pixels.
        int gridHeight = (getCellSize() * rows);
        int gridWidth = (getCellSize() * columns);

    	//Vertical lines.
    	for (int x = 0; x < columns; x ++) {
    	    int drawX = (x * getCellSize()) + getCellSize();
    	    g.drawLine(drawX + offsetX, offsetY, drawX + offsetX, gridHeight + offsetY);
        }

    	//Horizontal lines.
        for (int y = 0; y < rows; y ++) {
    	    int drawY = (y * getCellSize()) + getCellSize();
    	    g.drawLine(offsetX, drawY + offsetY, offsetX + gridWidth, drawY + offsetY);
        }

    	//Grid outline.
    	g.setColor(OUTLINE_COLOUR);
    	g.fillRect(offsetX, offsetY, OUTLINE_WIDTH, gridHeight);
    	g.fillRect(offsetX + gridWidth - OUTLINE_WIDTH, offsetY, OUTLINE_WIDTH, gridHeight);
    	g.fillRect(offsetX, offsetY, gridWidth, OUTLINE_WIDTH);
    	g.fillRect(offsetX, offsetY + gridHeight - OUTLINE_WIDTH, gridWidth, OUTLINE_WIDTH);
    }

    /**
     * Draw all of the game blocks that are on the Grid.
     * @param g The graphics object used to draw.
     */
    private void drawBlocks(Graphics g) {
    	for (Block block : blocks) {
    	    block.draw(g,x,y);
        }
    }

    /**
     * Remove all blocks from the Grid.
     */
    public void removeAllBlocks() {
        blocks.clear();
    }

    /**
     * Checks to see if there is a full horizontal line made up of blocks
     * in the specified row. Does not count blocks that are considered to be part of a Tetromino.
     * @param row The row in which to check.
     * @return True for yes, false for no.
     */
    public boolean horizontalLineFormed(int row) {

        int blockNum = 0;
        for (Block block : blocks) {
        	if (block.isPartOfGamePiece() == false) {
                //Block is in this row.
	            if (block.getY() == row) {
	                blockNum ++;
	            }
        	}
//            System.out.println(block.getY());
        }
//        System.out.println(blockNum);

        //The row is full.
        if (blockNum >= columns) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Deletes all of the blocks in the specified row.
     * @param row The row in which to delete all the blocks.
     */
    public void deleteRow(int row) {
       /* for (Block block : blocks) {
            if (block.getY() == row) {
                blocks.remove(blocks.get(1));
            }
        }*/
        
        //Remove all blocks that are on this row.
        ArrayList<Block> toRemove = new ArrayList<>();
        //Can`t delete directly from 'blocks' during iteration, because it will lead to ConcurrentModificationException.
        for (Block block : blocks) {
            if (block.getY() == row) {
                toRemove.add(block);
            }
        }
        blocks.removeAll(toRemove);
    }
    
    /**
     * Checks to see if any block is on the terminal line. Does not count blocks that are considered to be a part of
     * a Tetromino.
     * @return True if yes, false for no.
     */
    public boolean blockOnTerminalLine() {

        for (Block block : blocks) {
    		if (block.isPartOfGamePiece() == false) {
    			if (block.getY() == TERMINAL_LINE_ROW) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    /**
     * Method that needs to be called when a row is deleted.
     * Drops all of the blocks above the row by one unit (as long as it is not a part of a Tetromino).
     * @param row The row that was just deleted.
     */
    public void dropBlocks(int row) {

        for (Block block : blocks) {
            //Make sure its not a part of a Tetromino.
            if (block.isPartOfGamePiece() == false) {
                //Move all blocks above down.
                if (block.getY() < row) {
                    int oldY = block.getY();
                    int newY = oldY+1;
//                    System.out.println("dropped " + oldY + " " + newY);
                    block.setY(newY);
                }
            }
        }
    }

    /**
     * Checks to see if the mouse is inside of the Grid.
     * @param e The mouse event.
     * @return True for yes, false for no.
     */
    public boolean isMouseInsideGrid(MouseEvent e) {
        //Check horizontally.
        if (e.getX() >= getX() && e.getX() <= getX() + getGridDimensions().getWidth()) {
            //Check vertically.
            if (e.getY() >= getY() && e.getY() <= getY() + getGridDimensions().getHeight()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all of the blocks in the Grid.
     * @return All the blocks in the form of an arrayList.
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    /**
     * Returns the height and the width of the Grid. Calculates it from multiplying the size of each cell by the number
     * of columns and rows.
     * @return The dimensions.
     */
    public Dimension getGridDimensions() {
        return new Dimension(getCellSize() * getColumns(), getCellSize() * getRows());
    }

    /*  Getters and Setters */

    /**
     * Set whether or not to draw the terminal line.
     * @param drawTerminalLine True for yes, false for no.
     */
    public void setDrawTerminalLine(boolean drawTerminalLine) {
        this.drawTerminalLine = drawTerminalLine;
    }

    /**
     * Set the x coordinate of the Grid.
     * @param x The new x coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y coordinate of the Grid.
     * @param y The new y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set the number of rows in the Grid.
     * @param rows The new number of rows.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Set the number of columns in the Grid.
     * @param columns The new number of columns.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Set the size of the each cell in the Grid.
     * @param cellSize The new cell size.
     */
    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    /**
     * Get the number of rows in the Grid.
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the number of columns in the Grid.
     * @return The number of colunms.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Get the x coordinate of the Grid in relation to the window.
     * @return The x position, in pixels.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate of the Grid in relation to the window.
     * @return The y position, in pixels.
     */
    public int getY() {
        return y;
    }

    /**
     * Get the size of the cells.
     * @return The size of the cells, in pixels.
     */
    public int getCellSize() {
        return cellSize;
    }

}
