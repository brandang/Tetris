import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a Tetromino. A Tetromino is a game piece that the user can control.
 * It is made up of many blocks, which form shapes. Once the Tetromino has 'landed', however, the blocks are
 * declared to be no longer a part of the Tetromino, and the Tetromino is destroyed as an entity
 * (although its blocks will remain).
 *
 * Each Tetromino will be contained within a 4 by 4 grid.
 * The pivot around which the Tetromino rotates is the center of the 4 by 4 grid.
 *
 */
public class Tetromino {

    //The colour of the Tetromino. All of the blocks in the Tetromino will inherit this.
    private Color colour;

    //The Grid object that the Tetromino is on.
    private Grid grid;

    //Reference to all of the blocks on the grid, obtained from Grid object.
    //This allows this Tetromino to modify the blocks in the Grid.
    private ArrayList<Block> gridBlocks = new ArrayList<>(0);

    //All of the blocks that are a part of the Tetromino. Will include some of the blocks found in 'gridBlocks'.
    private ArrayList<Block> gamePieceBlocks = new ArrayList<>(0);

    //The maximum number of blocks that can be placed end to end horizontally or vertically.
    final private static int SIZE = 4;

    //The origin around which to rotate.
    private Point origin;

    //Whether or not the Tetromino has been generated yet.
    private boolean generated = false;

    //The maximum number of shifts that may be undertaken to attempt to accommodate Tetromino after rotation in order
    // to avoid collisions with stationary blocks.
    final private static int MAX_SHIFT_DISTANCE = 2;

    /**
     * Constructor. Creates a new Tetromino.
     * @param grid The Grid that will hold this Tetromino.
     */
    public Tetromino(Grid grid) {

        this.grid = grid;
        //Get the arrayList of Blocks from the Grid object. This allows us to modify the blocks in the Grid from here.
        this.gridBlocks = grid.getBlocks();
    }

    /**
     * Creates a random new Tetromino with a random shape, a random colour, and a random orientation.
     */
    public void generateNewPiece() {

        Random random = new Random();

        //First, generate a random colour.
        colour = chooseRandomColour(random);

        //Secondly, generate a random shape, based on number.
        int gen = random.nextInt(7);

        /*
        There are 7 types of blocks that can be generated, shown below. Let 0 represent empty space and 1
        represent a block.

        I Piece
        0100
        0100
        0100
        0100

        J Piece
        0010
        0010
        0110
        0000

        L Piece
        0100
        0100
        0110
        0000

        O Piece
        0000
        0110
        0110
        0000

        S piece
        0000
        0011
        0110
        0000

        Z piece
        0000
        0110
        0011
        0000

        T piece
        0000
        0111
        0010
        0000
         */

        //Create shapes based on the generated number.

        //I Piece.
        if (gen == 0) {
            Block block = new Block(1,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(1,1,colour);
            gamePieceBlocks.add(block);
            block = new Block(1,2,colour);
            gamePieceBlocks.add(block);
            block = new Block(1,3,colour);
            gamePieceBlocks.add(block);
            //Specify the origin around which to rotate.
            origin = new Point(1, 1);
        }
        //J Piece.
        else if (gen == 1) {
            Block block = new Block(2,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,1,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,2,colour);
            gamePieceBlocks.add(block);
            block = new Block(1,2,colour);
            gamePieceBlocks.add(block);
            //Specify the origin around which to rotate.
            origin = new Point(2, 1);
        }
        //L Piece.
        else if (gen == 2) {
            Block block = new Block(1,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(1,1,colour);
            gamePieceBlocks.add(block);
            block = new Block(1,2,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,2,colour);
            gamePieceBlocks.add(block);
            //Specify the origin around which to rotate.
            origin = new Point(1, 1);
        }
        //O Piece.
        else if (gen == 3) {
            Block block = new Block(1,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(1,1,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,1,colour);
            gamePieceBlocks.add(block);
            //Specify the origin around which to rotate.
            origin = new Point(1, 0);
        }
        //S Piece.
        else if (gen == 4) {
            Block block = new Block(3,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,1,colour);
            gamePieceBlocks.add(block);
            block = new Block(1,1,colour);
            gamePieceBlocks.add(block);
            //Specify the origin around which to rotate.
            origin = new Point(2, 0);
        }
        //Z Piece.
        else if (gen == 5) {
            Block block = new Block(1,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,1,colour);
            gamePieceBlocks.add(block);
            block = new Block(3,1,colour);
            gamePieceBlocks.add(block);
            //Specify the origin around which to rotate.
            origin = new Point(2, 1);
        }
        //T Piece.
        else if (gen == 6) {
            Block block = new Block(1,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(3,0,colour);
            gamePieceBlocks.add(block);
            block = new Block(2,1,colour);
            gamePieceBlocks.add(block);
            //Specify the origin around which to rotate.
            origin = new Point(2, 0);
        }

        //Rotate randomly.
        gen = random.nextInt(4);
        for (int i = 0; i < gen; i ++) {
            rotate();
        }

        //Add this Tetromino to the Grid to be drawn.
        addBlocksToGrid();

        generated = true;
    }

    /**
     * Choose a random colour.
     * @return A random colour.
     */
    private Color chooseRandomColour(Random random) {

        //Generate a random number. Get the colour based off of that.
        int gen = random.nextInt(8);

        if (gen == 0) {
            return Color.BLUE;
        }
        else if (gen == 1) {
            return Color.RED;
        }
        else if (gen == 2) {
            return Color.CYAN;
        }
        else if (gen == 3) {
            return Color.GREEN;
        }
        else if (gen == 4) {
            return Color.YELLOW;
        }
        else if (gen == 5) {
            return Color.ORANGE;
        }
        else if (gen == 6) {
            return Color.GRAY;
        }
        else {
            return Color.PINK;
        }
    }

    /**
     * Rotates the Tetromino 90 degrees counter-clockwise, if the Tetromino has been generated.
     */
    public void rotate() {

        //Can only rotate if the Tetromino has been created.
        if (generated) {

            /*
            There are many potential problems that would prevent a Tetromino from being rotated. First of all,
            the Tetromino may collide with other blocks. If it is not possible to rotate, prevent it from doing so. Do this
            by creating a copy of all the blocks in the Tetromino, and then applying a rotation to them. Finally, check to
            if the rotation results in any overlaps/collisions with other blocks. If no, it is safe to rotate.
             */

            //Get copy.
            ArrayList<Block> tempBlocks = copyBlocks(gamePieceBlocks);
            //Attempt to rotate these blocks.
            boolean success = attemptRotate(tempBlocks,true);

            //Successful; actually rotate now.
            if (success == true) {
                attemptRotate(gamePieceBlocks,false);
            }
        }
    }

    /**
     * Rotates the blocks.
     * @param blocks The blocks to rotate.
     * @param updateOrigin Whether or not to update the origin after rotation. Origin may need to be updated when
     * Tetromino is out of bounds and is shifted to compensate after rotation.
     * @return True if rotation successful, false if rotation resulted in overlaps.
     */
    private boolean attemptRotate(ArrayList<Block> blocks, boolean updateOrigin) {

        /*
        System.out.println(originX);
        int offsetX = -((int) originX);
        int offsetY = -((int) originY);

        for (Block block : blocks) {

            int oldX = block.getX() + offsetX;
            int oldY = block.getY() + offsetY;
            System.out.println("old x " + oldX);
            //New x position is equal to the old y position.
            int newX = oldY - 0 - (int)offsetX;
            //The new y depends on the oldX.
            int newY = oldX - (int)offsetY;

            System.out.println("new y " + newY);
            //Update the blocks` position.rotatedCoordinates[i].x += origin.x;
        rotatedCoordinates[i].y += origin.y;
            block.setX(newX);
            block.setY(newY);
        }*/


        /*
        Problem: it is not possible to simply rotate the Tetromino based solely on it's block`s positions on the grid,
        as doing so would also shift the Tetromino. Therefore, in order to solve this, rotation must be done around
        an origin, which must be centered in the Tetromino. The origin must be at (0, 0) in order to rotate properly.
        Therefore, each of the blocks must be translated in relation with the origin. The Tetromino must be translated
        so that the origin is moved to (0, 0).
         */

        //Loop through all the blocks in the Tetromino.
        for (Block block : blocks) {

            //The origin needs to be at (0, 0). Translate the block`s coordinates so that it is in relation to the
            //origin.
            Point translatedCoordinate = new Point(block.getX() - (int) origin.getX(), block.getY() - (int) origin.getY());

            //Multiply be -1 because y values increase as you go down vertically, instead of increasing as you go up.
            translatedCoordinate.y *= -1;

            //Copy the coordinates so that calculations can be done with them.
            Point rotatedCoordinates = (Point) translatedCoordinate.clone();

            /*
            To rotate counter clockwise, the formula is:
            New x coordinate = (Xcosθ) - (Ysinθ), where x and y are the distances from the origin, and θ is the angle
            in which to rotate.
            New y coordinate = (Xsinθ) - (Ycosθ), where x and y are the distances from the origin, and θ is the angle
            in which to rotate.
            θ is the angle in which to rotate, in radians. Since we are rotating 90 degrees counter clockwise, θ is
            equal to (Math.PI/2).
             */
            rotatedCoordinates.x = (int)Math.round(translatedCoordinate.x * Math.cos(Math.PI/2) - translatedCoordinate.y
                    * Math.sin(Math.PI/2));
            rotatedCoordinates.y = (int)Math.round(translatedCoordinate.x * Math.sin(Math.PI/2) + translatedCoordinate.y
                    * Math.cos(Math.PI/2));

            //Flip the y values again.
            rotatedCoordinates.y *= -1;

            //Move back to original position before translation.
            rotatedCoordinates.x += origin.getX();
            rotatedCoordinates.y += origin.getY();

            //Set new coordinates.
            block.setX(rotatedCoordinates.x);
            block.setY(rotatedCoordinates.y);
        }


        /*
        Now, make sure that it stays within the boundaries. If it is specified that the origin is not to be updated,
        make a copy of the origin and revert it after calling keepWithinGrid. This is because keepWithinGrid may
        change the origin.
         */
        if (updateOrigin == true) {
            Point originClone = (Point) origin.clone();
            keepWithinGrid(blocks);
            origin = originClone;
        }
        else {
            keepWithinGrid(blocks);
        }

        //Tetromino has collided with a stationary block. See if it is possible to shift left or right to avoid
        // collision.
        if (hasCollided(blocks)) {
            for (int i = 0; i < MAX_SHIFT_DISTANCE; i ++) {
                //If the origin is not
                if (updateOrigin == false) {
                    //Prevent origin from changing, since it is specified not to change.
                    Point temp = (Point) origin.clone();
                    //Shift right. Do not check if it is possible, just do it.
                    shiftRight(blocks);
                    origin = temp;
                }
                if (hasCollided(blocks) == false) {
                    return true;
                }
            }
            if (hasCollided(blocks)) {
                for (int i = 0; i < 4; i ++) {
                    if (updateOrigin == false) {
                        //Prevent origin from changing, since it is specified not to change.
                        Point temp = (Point) origin.clone();
                        shiftRight(blocks);
                        origin = temp;
                    }
                    if (hasCollided(blocks) == false) {
                        return true;
                    }
                }
            }
        }
        if (hasCollided(blocks)) {
            return false;
        }
        return true;
    }

    /**
     * Check to see if list of blocks has collided with any stationary blocks.
     * @param blocks The blocks to check against the stationary blocks.
     * @return True for yes, false for no.
     */
    private boolean hasCollided(ArrayList<Block> blocks) {
        for (Block gamePieceBlock : blocks) {
            //Check with stationary blocks.
            for (Block stationaryBlock : gridBlocks) {
                if (stationaryBlock.isPartOfGamePiece() == false) {

//                    System.out.println(gamePieceBlock.getX()+" "+stationaryBlock.getX()+" "+gamePieceBlock.getY()+" "+stationaryBlock.getY());
                    if (gamePieceBlock.getY() == stationaryBlock.getY() && gamePieceBlock.getX() == stationaryBlock.getX()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

 /*   public void dropToBottom() {
        boolean keepDropping = true;
        while (keepDropping == true) {
            keepDropping = moveDown();
        }
    }*/

    /**
     * Keeps the Tetromino within bounds.
     */
    public void stayWithinBounds() {
        keepWithinGrid(gamePieceBlocks);
    }

    /**
     * Make sure the Tetromino stays within the Grid. If it is not, shift it so that it is.
     */
    public void keepWithinGrid(ArrayList<Block> blocks) {

        //Get the 'indices' of boundaries. Blocks are not allowed to go past these.
        int boundLeft = 0;
        int boundRight = grid.getColumns()-1;
        int boundBottom = grid.getRows()-1;

        //Loop through.
        for (Block block : blocks) {
            while (block.getX() < boundLeft) {
                shiftRight(blocks);
            }
            while (block.getX() > boundRight) {
                shiftLeft(blocks);
            }
            while (block.getY() > boundBottom) {
                shiftUp(blocks);
            }

            //Note that we don`t have to check for collision with the top since it is considered that the top is not
            // boundary.
        }
    }

    /**
     * Move the Tetromino to the mouse horizontally. Only do so if the mouse is within the Grid.
     * @param e The mouse event.
     */
    public void moveToMouse(MouseEvent e) {

        //Only move if the mouse is inside of the Grid.
        if (getGrid().isMouseInsideGrid(e) == true) {
            //Convert mouse location to a coordinate on the Grid.
            int newX = getMousePosition(e);
            //Move Tetromino horizontally to the specified position, if it is not out of bounds.
            if (newX != -1) {
                shiftToPosition(newX);
            }
        }
    }

    /**
     * Calculates what column on the Grid the mouse currently occupies. Takes the Grid`s position in the window into
     * account. If the mouse is out of bounds, -1 will be returned.
     * @param e The mouse event.
     * @return The column that the mouse is in. If mouse is out of bounds, -1 is returned.
     */
    private int getMousePosition(MouseEvent e) {

        //The mouse`s 'real' location on the Grid, after adjusting for the Grid`s position in the Window.
        int realX = e.getX() - getGrid().getX();

        //Get the column of the Grid that the mouse occupies.
        int column = (int) Math.floor(realX / getGrid().getCellSize());

        //Out of bounds.
        if (column < 0 || column > getGrid().getColumns() - 1) {
            return -1;
        }
        else {
            return column;
        }
    }

    /**
     * Moves the origin horizontally to the specified position. Translate the blocks accordingly to stay with the
     * origin.
     * @param newX The new x position to move origin to.
     */
    private void shiftToPosition(int newX) {
        //Calculate how to move.
        int move = (int) (newX - origin.getX());
        for (int i = 0; i < (Math.abs(move)); i ++) {
            //Move left.
            if (move < 0) {
                moveLeft();
            }
            //Move right.
            else if (move > 0) {
                moveRight();
            }
        }
    }

    /**
     * Moves the Tetromino down one row/cell on the Grid, if possible.
     * @return True if successful, false if it is not.
     */
    public boolean moveDown() {
        //Determine if there is a block(s) in the way.
        boolean ableToShift = canShiftDown();

        if (ableToShift == true) {
            shiftDown(gamePieceBlocks);
        }

        return ableToShift;
    }

    /**
     * Move the Tetromino one cell up.
     */
    public void moveUp() {
        shiftUp(gamePieceBlocks);
    }

    /**
     * Move the Tetromino one cell left, if possible.
     */
    public void moveLeft() {
        if (canShiftLeft() == true) {
            shiftLeft(gamePieceBlocks);
        }
    }

    /**
     * Move the Tetromino one cell right, if possible.
     */
    public void moveRight() {
        if (canShiftRight() == true) {
            shiftRight(gamePieceBlocks);
        }
    }

    /**
     * Moves the Tetromino down one row/cell on the Grid. Also adjusts the origin to compensate.
     */
    public void shiftDown(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            int oldY = block.getY();
            int newY = oldY + 1;
            block.setY(newY);
        }
        //Shift origin too.
        origin.y ++;
    }

    /**
     * Moves the Tetromino up one row/cell on the Grid. Also adjusts the origin to compensate.
     */
    public void shiftUp(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            int oldY = block.getY();
            int newY = oldY -1;
            block.setY(newY);
        }
        //Shift origin too.
        origin.y --;
    }

    /**
     * Moves the Tetromino one column/cell left on the Grid. Also adjusts the origin to compensate.
     */
    public void shiftLeft(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            int oldX = block.getX();
            int newX = oldX -1;
            block.setX(newX);
        }
        //Shift origin too.
        origin.x --;
    }

    /**
     * Moves the Tetromino one column/cell right on the Grid. Also adjusts the origin to compensate.
     */
    public void shiftRight(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            int oldX = block.getX();
            int newX = oldX +1;
            block.setX(newX);
        }
        //Shift origin too.
        origin.x ++;
    }

    /**
     * Determines whether or not the Tetromino can shift down.
     * @return True for yes, false for no.
     */
    private boolean canShiftDown() {
        for (Block gamePieceBlock : gamePieceBlocks) {
            //Check boundary.
            if (gamePieceBlock.getY() >= grid.getRows() - 1) {
                return false;
            }
            //Check with stationary blocks.
            for (Block stationaryBlock : gridBlocks) {
                if (stationaryBlock.isPartOfGamePiece() == false) {
                    if (gamePieceBlock.getY() + 1 >= stationaryBlock.getY() && gamePieceBlock.getX() == stationaryBlock.getX()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines whether or not the Tetromino can shift left.
     * @return True for yes, false for no.
     */
    private boolean canShiftLeft() {
        for (Block gamePieceBlock : gamePieceBlocks) {
            //Check boundary.
            if (gamePieceBlock.getX() -1 < 0) {
                return false;
            }
            //Check with finsihed bocks
            for (Block stationaryBlock : gridBlocks) {
                if (stationaryBlock.isPartOfGamePiece() == false) {
                    if (gamePieceBlock.getX() - 1 == stationaryBlock.getX() && gamePieceBlock.getY() == stationaryBlock.getY()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines whether or not the Tetromino can shift right.
     * @return True for yes, false for no.
     */
    private boolean canShiftRight() {
        for (Block gamePieceBlock : gamePieceBlocks) {
            //Check boundary.
            if (gamePieceBlock.getX() + 1 > grid.getColumns()-1) {
                return false;
            }
            //Check with finsihed bocks
            for (Block stationaryBlock : gridBlocks) {
                if (stationaryBlock.isPartOfGamePiece() == false) {
                    if (gamePieceBlock.getX() + 1 == stationaryBlock.getX() && gamePieceBlock.getY() == stationaryBlock.getY()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Add this Tetromino to the game Grid. Sets the position of the piece to the center of the Grid, 4 cells above
     * the top boundary.
     * @param newGrid The game Grid to add this Tetromino to.
     */
    public void changeGrid(Grid newGrid) {

        //Change Grid object.
        grid = newGrid;
        //Get the new blocks.
        gridBlocks = grid.getBlocks();

        //Add the blocks of this Tetromino to the grid.
        addBlocksToGrid();

    }

    /**
     * Add all of the blocks belonging to this Tetromino to its Grid.
     */
    private void addBlocksToGrid() {
//        System.out.println("before " + gridBlocks.size());
        for (Block block : gamePieceBlocks) {
            gridBlocks.add(block);
        }
//        System.out.println("after " + gridBlocks.size());
    }

    /**
     * Release the Tetromino: that means each block will no longer be associated with a Tetromino.
     */
    public void releaseBlocks() {
        for (Block block : gamePieceBlocks) {
            block.setPartOfGamePiece(false);
        }
    }

    /**
     * Make a 'deep copy' of the blocks. The new blocks will be completely different objects with the same value: it
     * is not copying the reference.
     * @param blocks The blocks to copy.
     * @return The new copy.
     */
    private ArrayList<Block> copyBlocks(ArrayList<Block> blocks) {
        ArrayList<Block> copiedBlocks = new ArrayList<>(blocks.size());
        for (Block block : blocks) {
            //Copy block. Cant clone, cause thats just gonna return a reference to same object.
            Block newBlock = new Block(block.getX(),block.getY(),block.getColour());
            newBlock.setPartOfGamePiece(block.isPartOfGamePiece());
            copiedBlocks.add(newBlock);
        }

        return copiedBlocks;
    }

    /*  Getters and setters */

    public Grid getGrid() {
        return grid;
    }

    /**
     * Gets all of the blocks in this Tetromino.
     * @return The blocks, stored inside an arrayList.
     */
    public ArrayList<Block> getBlocks() {
        return gamePieceBlocks;
    }

    public static int getSIZE() {
        return SIZE;
    }
}
