import java.awt.*;
import java.util.ArrayList;

/**
 * This class represents a grid in the game window. It is responsible for drawing and keeping track of all
 * the blocks on the grid.
 */
public class Grid {

    //The positions of the grid.
    private int x, y;

    //The number of rows and columns.
    private int rows, columns;

    //All of the blocks in this grid.
    private ArrayList<Block> blocks = new ArrayList<>(0);
    
    //The colours to use in drawing.
    final static private Color GRID_BACKGROUND_COLOUR = new Color(0, 70, 100);
    final static private Color GRID_LINES_COLOUR = new Color(0, 170, 227);
    final static private Color OUTLINE_COLOUR = Color.BLACK;
    final static private Color TERMINAL_LINE_COLOUR = new Color(161, 0, 4);

    //The grid`s outline`s stroke width.
    final static private int OUTLINE_WIDTH = 5;

    //Whether or not to draw the red terminal line at the top.
    private boolean drawTerminalLine;
    //Where the terminal line is.
    final private static int TERMINAL_LINE_ROW = 0;

    /**
     * Constructor. Takes in positions and dimensions as parameters.
     * @param x The left coordinate of the grid.
     * @param y The top coordinate of the grid.
     * @param columns The number of columns on the grid.
     * @param rows The number of rows on the grid.
     */
    public Grid(int x, int y, int columns, int rows) {
        setX(x);
        setY(y);
        setColumns(columns);
        setRows(rows);

        //Defaults.
        setDrawTerminalLine(false);
    }

    /**
     * Draw the grid.
     */
    public void draw(Graphics g) {
        drawGrid(g);
        drawBlocks(g);
    }

    /**
     * Draw the lines of the grid.
     * @param g The graphics object.
     */
    private void drawGrid(Graphics g) {
        int blockWidth = Block.getBlockWidth();
        int blockHeight = Block.getBlockHeight();

        //Calculate dimensions of the grid.
        Dimension size = new Dimension(blockWidth * columns, blockHeight * rows);

        //Draw the background.
    	g.setColor(GRID_BACKGROUND_COLOUR);
    	g.fillRect(x, y, size.width, size.height);

    	//Draw the terminal line.
        if (drawTerminalLine == true) {
            g.setColor(TERMINAL_LINE_COLOUR);
            g.fillRect(x, y, size.width, blockHeight);
        }

        //Offset is basically the top left position of this grid within the window.
    	int offsetX = this.x;
    	int offsetY = this.y;
    	
    	g.setColor(GRID_LINES_COLOUR);
    	
    	//Calculate the width and height of the grid in pixels.
        int gridHeight = (blockHeight * rows);
        int gridWidth = (blockWidth * columns);

    	//Vertical lines.
    	for (int x = 0; x < columns; x ++) {
    	    int drawX = (x * blockWidth) + blockWidth;
    	    g.drawLine(drawX + offsetX, offsetY, drawX + offsetX, gridHeight + offsetY);
        }

    	//Horizontal lines.
        for (int y = 0; y < rows; y ++) {
    	    int drawY = (y * blockHeight) + blockHeight;
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
     * Draw all of the game blocks that are on the grid.
     * @param g The graphics object used to draw.
     */
    private void drawBlocks(Graphics g) {
    	for (Block block : blocks) {
    	    block.draw(g,x,y);
        }
    }

    /**
     * Remove all blocks from the grid.
     */
    public void removeAllBlocks() {
        blocks.clear();
    }

    /**
     * Checks to see if there is a full horizontal line made up of blocks
     * in the specified row. Does not count blocks that are considered to be part of a game piece.
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
     * a game piece.
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
     * Drops all of the blocks above the row by one unit (as long as it is not a part of a game piece).
     * @param row The row that was just deleted.
     */
    public void dropBlocks(int row) {

        for (Block block : blocks) {
            //Make sure its not a part of a game piece.
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
     * Gets all of the blocks in the grid.
     * @return All the blocks in the form of an arrayList.
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
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
     * Set the x coordinate of the grid.
     * @param x The new x coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y coordinate of the grid.
     * @param y The new y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set the number of rows in the grid.
     * @param rows The new number of rows.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Set the number of columns in the grid.
     * @param columns The new number of columns.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Get the number of rows in the grid.
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the number of columns in the grid.
     * @return The number of colunms.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Get the x coordinate of the grid in relation to the window.
     * @return The x position, in pixels.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate of the grid in relation to the window.
     * @return The y position, in pixels.
     */
    public int getY() {
        return y;
    }
}
