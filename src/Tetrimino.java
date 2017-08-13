import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a Tetrimino. A Tetrimino is a game piece that the user can control.
 * It is made up of many blocks, which form shapes. Once the Tetrimino has 'landed', however, the blocks are
 * declared to be no longer a part of the Tetrimino, and the Tetrimino is destroyed as an entity
 * (although its blocks will remain).
 *
 * Each Tetrimino will be contained within a 4 by 4 grid.
 * The pivot around which the game piece rotates is the center of the 4 by 4 grid.
 *
 */
public class Tetrimino {

    //The colour of the Tetrimino. All of the blocks in the Tetrimino will inherit this.
    private Color colour;

    //The Grid object that the Tetrimino is on.
    private Grid grid;

    //Reference to all of the blocks on the grid, obtained from Grid object.
    //This allows this Tetrimino to modify the blocks in the Grid.
    private ArrayList<Block> gridBlocks = new ArrayList<>(0);

    //All of the blocks that are a part of the Tetrimino. Will include some of the blocks found in 'gridBlocks'.
    private ArrayList<Block> gamePieceBlocks = new ArrayList<>(0);

    //The maximum number of blocks that can be placed end to end horizontally or vertically.
    final private static int SIZE = 4;

    /**
     * Constructor. Creates a new Tetrimino.
     * @param grid The Grid that will hold this Tetrimino.
     */
    public Tetrimino(Grid grid) {

        this.grid = grid;
        //Get the arrayList of Blocks from the Grid object. This allows us to modify the blocks in the Grid from here.
        this.gridBlocks = grid.getBlocks();
    }

    /**
     * Creates a random new game piece with a random shape, a random colour, and a random orientation.
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
        }

        //Rotate randomly.
        gen = random.nextInt(4);
        for (int i = 0; i < gen; i ++) {
            rotate();
        }

        //Add this game piece to the Grid to be drawn.
        addBlocksToGrid();
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
     * Rotates the game piece 90 degrees clockwise.
     */
    public void rotate() {

        /*
        This method is very complex and difficult!
        There are soooo many potential problems.
        Firstly, when rotated, the game piece may collide with other blocks. We need to prevent rotation if it
        is not possible to do so. Therefore, create a copy of all of the blocks in the piece, and apply the rotation to them.
        Check to see if the rotation results in any overlaps/collisions with other blocks. If no, it is safe
        to rotate.
        */

        //Get copy.
        ArrayList<Block> tempBlocks = copyBlocks(gamePieceBlocks);
        //Attempt to rotate these blocks.
        boolean success = attemptRotate(tempBlocks);

        //Successful; actually rotate now.
        if (success == true) {
            attemptRotate(gamePieceBlocks);
        }

    }

    /**
     * Rotates the blocks.
     * @param blocks The blocks to rotate.
     * @return True if rotation successful, false if rotation resulted in overlaps.
     */
    private boolean attemptRotate(ArrayList<Block> blocks) {
        //Loop through each block.
        int offsetX = 1000;
        int offsetY = 1000;

        /*
        Problem: we cant rotate the entire 'grid', as it will lead to unexpected results. Instead, we need to do a
        'localized' rotation - that is, rotate just around the game piece. Its kinda hard to explain.
         Offset represents the locations of the left most block and the top most blocks in the game piece.
         We rotate starting from there. If we instead start rotating from (0,0), the game piece will most likely shift
         as well as rotate, which is not what we want. This is kinda like setting the 'pivot' (sorta) of where to
         rotate.
         */
        //Get left most and top most position.
        for (Block block : blocks) {
            if (block.getX() < offsetX) {
                offsetX = block.getX();
            }
            if (block.getY() < offsetY) {
                offsetY = block.getY();
            }
        }

        //Rotate just the 4 by 4 mini grid that contains the game piece.
        for (Block block : blocks) {
            int oldX = block.getX()-offsetX;
            int oldY = block.getY()-offsetY;
            /*
            To rotate:
            New x position is equal to the old y position.
            The new y depends on the oldX.
            The diagram below shows why:
            123
            456
            789
            rotated becomes:
            369
            258
            147

            The formula is:
            newX = oldY
            newY = SIZE - oldX - 1
            Note that we also have to account for the offset, which the scenario above ignores.
             */
            //New x position is equal to the old y position.
            int newX = oldY + offsetX;
            //The new y depends on the oldX.
            int newY = SIZE - oldX - 1 + offsetY;
            //Update the blocks` position.
            block.setX(newX);
            block.setY(newY);
        }

        /*
        Now, make sure that it stays within the boundaries.
         */
        keepWithinGrid(blocks);
        //Make sure its not touching any stationary blocks.
        if (hasCollided(blocks) == true) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Check to see if list of blocks has collided with any stationary blocks.
     * @param blocks The blocks to check against the stationary blocks.
     * @return True for yes, false for no.
     */
    private boolean hasCollided(ArrayList<Block> blocks) {
        for (Block gamePieceBlock : blocks) {
            //Check with finsihed bocks
            for (Block stationaryBlock : gridBlocks) {
                if (stationaryBlock.isPartOfGamePiece() == false) {

                    //test
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
     * Keeps the game piece within bounds.
     */
    public void stayWithinBounds() {
        keepWithinGrid(gamePieceBlocks);
    }

    /**
     * Make sure the block group stays within the grid. If it is not, shift it so that it is.
     */
    public void keepWithinGrid(ArrayList<Block> blocks) {
        //Get the 'indices' of boundaries. Blocks are not allowed to go past these.
        int boundLeft = 0;
        int boundRight = grid.getColumns()-1;
        int boundBottom = grid.getRows()-1;

        //Loop through
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

            //Note that we don`t have to check for collision with the top since there is no way that the user can move
            // the piece up.
        }
    }

    /**
     * Moves the game piece down one row/cell on the grid, if possible.
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
     * Move the game piece one cell up.
     */
    public void moveUp() {
        shiftUp(gamePieceBlocks);
    }

    /**
     * Move the game piece one cell left, if possible.
     */
    public void moveLeft() {
        if (canShiftLeft() == true) {
            shiftLeft(gamePieceBlocks);
        }
    }

    /**
     * Move the game piece one cell right, if possible.
     */
    public void moveRight() {
        if (canShiftRight() == true) {
            shiftRight(gamePieceBlocks);
        }
    }

    /**
     * Moves the game piece down one row/cell on the grid.
     */
    public void shiftDown(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            int oldY = block.getY();
            int newY = oldY + 1;
            block.setY(newY);
        }
    }

    /**
     * Moves the game piece up one row/cell on the grid.
     */
    public void shiftUp(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            int oldY = block.getY();
            int newY = oldY -1;
            block.setY(newY);
        }
    }

    /**
     * Moves the game piece one column/cell left on the grid.
     */
    public void shiftLeft(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            int oldX = block.getX();
            int newX = oldX -1;
            block.setX(newX);
        }
    }

    /**
     * Moves the game piece one column/cell right on the grid.
     */
    public void shiftRight(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            int oldX = block.getX();
            int newX = oldX +1;
            block.setX(newX);
        }
    }

    /**
     * Determines whether or not the game piece can shift down.
     * @return True for yes, false for no.
     */
    private boolean canShiftDown() {
        for (Block gamePieceBlock : gamePieceBlocks) {
            //Check boundary.
            if (gamePieceBlock.getY() >= grid.getRows() - 1) {
                return false;
            }
            //Check with finsihed bocks
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
     * Determines whether or not the game piece can shift left.
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
     * Determines whether or not the game piece can shift right.
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
     * Add this game piece to the game grid. Sets the position of the piece to the center of the grid, 4 cells above
     * the top boundary.
     * @param newGrid The game grid to add this game piece to.
     */
    public void changeGrid(Grid newGrid) {

        //Change grid object.
        grid = newGrid;
        //Get the new blocks.
        gridBlocks = grid.getBlocks();

        //Add the blocks of this game piece to the grid.
        addBlocksToGrid();

    }

    public void addS() {

    }

    /**
     * Add all of the blocks belonging to this game piece to its Grid.
     */
    private void addBlocksToGrid() {
//        System.out.println("before " + gridBlocks.size());
        for (Block block : gamePieceBlocks) {
            gridBlocks.add(block);
        }
//        System.out.println("after " + gridBlocks.size());
    }

    /**
     * Release the game piece: that means each block will no longer be associated with a game piece.
     */
    public void releaseGamePiece() {
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
     * Gets all of the blocks in this game piece.
     * @return The blocks, stored inside an arrayList.
     */
    public ArrayList<Block> getBlocks() {
        return gamePieceBlocks;
    }

    public static int getSIZE() {
        return SIZE;
    }
}
