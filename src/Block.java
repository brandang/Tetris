import java.awt.*;
/**
 * Class that represents a game block. It stores its position on the grid, and has methods to draw itself.
 */
public class Block {

    //True if it is currently a part of a Tetromino, false if it is not.
    private boolean partOfGamePiece;

    //The 'position' of the block on the game grid. The 'position' can be thought of as
    //the index in the 2D array. For example, a block in the top left corner has x = 0 and y = 0.
    private int x, y;

    //The fill colour of the block.
    private Color colour;
    //The outline colour of each block.
    private final static Color OUTLINE_COLOUR = Color.BLACK;

    //The size of each block. Cannot be modified.
    final static private int BLOCK_W = 50;
    final static private int BLOCK_H = 50;
    //The PADDING of each block within it's cell. This ensures that block does not visually fill up entire cell.
    final static private int PADDING = 2;
    //The stroke width of the outline of the shape.
    final static private int OUTLINE_WIDTH = 4;

    //The size of the diameter of the arc at the corners of the background shape. Used to draw.
    final static private int CORNER_SIZE = 15;

    /**
     * Constructor.
     * @param x The x position of the block on the grid.
     * @param y The y position of the block on the grid.
     * @param colour The fill colour of the block.
     */
    public Block(int x, int y, Color colour) {
        this.x = x;
        this.y = y;
        this.colour = colour;

        //By default, make it so that it is a part of a Tetromino.
        setPartOfGamePiece(true);
    }

    /**
     * Draws the block, according to the x and y positions of the block. Also account for the position
     * of the grid relative to the game window, which is defined by offsetX and offsetY.
     * @param g The graphics object.
     * @param offsetX The left edge of the grid.
     * @param offsetY The top edge of the grid.
     */
    public void draw(Graphics g, int offsetX, int offsetY) {

        //Calculate where to draw the block in relation to the grid.
        //Do this by multiplying the 'cell position' by the size of each block/cell.
        int realX = x* BLOCK_W;
        int realY = y* BLOCK_H;
        //OffsetX and offsetY are needed because the grid does not necessarily
        //have a top left corner at (0,0) in the game window.
        //Add the offset to the positions.
        //Also add PADDING, to make sure block visually does not take up entire cell.
        realX += offsetX + PADDING;
        realY += offsetY + PADDING;

        //Draw the block in the calculated position with the right colour.

        //Draw outline.
        g.setColor(OUTLINE_COLOUR);
        g.fillRoundRect(realX, realY,BLOCK_W - OUTLINE_WIDTH, BLOCK_H - OUTLINE_WIDTH,CORNER_SIZE,
                CORNER_SIZE);
        //Draw inner fill, on top of the outline.
        g.setColor(colour);
        g.fillRoundRect(realX + OUTLINE_WIDTH,realY + OUTLINE_WIDTH, BLOCK_W - (OUTLINE_WIDTH * 3),
                BLOCK_H - (OUTLINE_WIDTH * 3), CORNER_SIZE, CORNER_SIZE);

    }

    /*  Getters and Setters */

    /**
     * Returns whether or not this block is currently a part of the Tetromino.
     * @return True for yes, false for no.
     */
    public boolean isPartOfGamePiece() {
        return partOfGamePiece;
    }

    /**
     * Set whether or not this block is currently a part of a Tetromino.
     * @param b True for yes, false for no.
     */
    public void setPartOfGamePiece(boolean b) {
        partOfGamePiece = b;
    }

    /**
     * Set the x position within the grid.
     * @param x The x index.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y position within the grid.
     * @param y The y index.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the x index within the grid.
     * @return The x index.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y index within the grid.
     * @return The y index.
     */
    public int getY() {
        return y;
    }

    /**
     * Get the colour of the block.
     * @return The fill colour.
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Returns the width of each block.
     * @return The width.
     */
    public static int getBlockWidth() {
    	return BLOCK_W;
    }
    
    /**
     * Returns the height of each block.
     * @return The height.
     */
    public static int getBlockHeight() {
    	return BLOCK_H;
    }
}
