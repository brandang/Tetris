import java.awt.*;
/**
 * whata
 * Class that represents a game block.
 */
public class Block {

    //True if it is currently a part of a game piece, false if it is not.
    private boolean partOfGamePiece;

    //The 'position' of the block on the game grid. The 'position' can be thought of as
    //the index in the 2D array. For example, top left corner means x = 0 and y = 0.
    private int x, y;

    //The colour of the block.
    private Color colour;
    private final static Color OUTLINE_COLOUR = Color.BLACK;

    //The size of each block. Cannot be modified.
    final static private int blockW = 50;
    final static private int blockH = 50;

    //The size of the diameter of the arc at the corners of the background shape.
    final static private int CORNER_SIZE = 15;

    /**
     * Constructor.
     * @param x The x position of the block on the grid.
     * @param y The y position of the block on the grid.
     * @param colour The colour of the block.
     */
    public Block(int x, int y, Color colour) {
        this.x = x;
        this.y = y;
        this.colour = colour;

        //By default, make it so that it is a part of a game piece.
        setPartOfGamePiece(true);
    }

    /**
     * Draws the block, according to the x and y positions, as well as the position
     * of the grid, which is defined by offsetX and offsetY.
     * @param g The graphics object.
     * @param offsetX The left edge of the grid.
     * @param offsetY The top edge of the grid.
     */
    public void draw(Graphics g, int offsetX, int offsetY) {

        //Calculate where to draw the block in relation to the grid.
        //Do this by multiplying the 'cell position' by the size of each block/cell.
        int realX = x*blockW;
        int realY = y*blockH;
        //OffsetX and offsetY are needed because the grid does not necessarily
        //have a top left corner at (0,0).
        //Add the offset to the positions.
        realX += offsetX + 2;
        realY += offsetY + 2;

        //Draw the block in the calculated position with the right colour.
        g.setColor(OUTLINE_COLOUR);
        g.fillRoundRect(realX,realY,blockW-4,blockH-4,CORNER_SIZE,CORNER_SIZE);
        g.setColor(colour);
        g.fillRoundRect(realX+4,realY+4,blockW-12,blockH-12,CORNER_SIZE,CORNER_SIZE);

    }

    /*  Getters and Setters */

    /**
     * Returns whether or not this block is currently a part of the game piece.
     * @return True for yes, false for no.
     */
    public boolean isPartOfGamePiece() {
        return partOfGamePiece;
    }

    /**
     * Set whether or not this block is currently a part of a game piece.
     * @param b True for yes, false for no.
     */
    public void setPartOfGamePiece(boolean b) {
        partOfGamePiece = b;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColour() {
        return colour;
    }

    /**
     * Returns the width of each block.
     * @return The width.
     */
    public static int getBlockWidth() {
    	return blockW;
    }
    
    /**
     * Returns the height of each block.
     * @return The height.
     */
    public static int getBlockHeight() {
    	return blockH;
    }
}
