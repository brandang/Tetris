import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URI;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Class that is the game window. It contains the game logic and draw methods.
 */
public class GamePanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener{

    //Enable/Disable outputs for debugging.
    final private boolean debug = false;

    //Text that displays in the 'Controls Screen.'
    final private static String CONTROLS_TEXT = "The controls of this game are simple. To play the game use the" +
            " keyboard, only the arrow keys need to be used. The left and right arrow keys are used to move the Tetris " +
            "game piece. The up key is to rotate the game piece 90 degrees counter-clockwise, while the down key is to " +
            " drop the game piece to the ground.";

    //Text that displays the Instructions.
    final private static String INSTRUCTIONS_TEXT = "Tetris!!! This game is my version of the popular puzzle game. The" +
            " objective is to move and rotate game pieces to manipulate where they land. Once a game piece lands, " +
            "a next game piece is randomly chosen. When a full horizontal line of blocks is formed on the grid, " +
            "that row is deleted. All of the blocks above that line will fall down by one cell. One point is earned" +
            " from this. The game ends when any block lands on the terminal line at the top of the grid." +
            " Good luck, and have FUN!";

    //Link to webpage that describes the game.
    final private static String TETRIS_INFO_URL = "https://en.wikipedia.org/wiki/Tetris#Gameplay";

    //The dropTimer for drawing.
    private Timer animationTimer;

    //Target is a good 60 FPS, 4K resolution :)
    final static private double TARGET_FPS = 60;
    //Amount of time, in millis, to delay to get target FPS
    private double delayTime;

    //Time at which the program started.
    private long programStartTime;
    private long programCurrentTime;
    //The number of frames drawn since program started.
    private long framesDrawn = 0;
    //Time elapsed since program started.
    private long elapsedTime;
    //The actual calculated FPS.
    private int actualFPS;

    //The last mouseEvent that was received.
    private MouseEvent previousMouseEvent = null;

    //Size of the screen.
    private Dimension size;
    
    //The game state.
    private enum State {MAIN_MENU,HOW_TO_PLAY,CONTROLS,GAME_ON,GAME_OVER};
    private State state = State.MAIN_MENU;
    
    //The text font.
    final static private Font TEXT_FONT = new Font("Arial",Font.PLAIN,20);
    
    //The button colours.
    final static private Color PRIMARY_BUTTON_COLOUR = new Color(120, 120, 120);
    final static private Color SECONDARY_BUTTON_COLOUR = new Color(150, 150, 150);

	//The other colours.
    final static private Color SIDEPANEL_COLOUR = new Color(190, 190, 190);
    final static private Color MENU_BACKGROUND_COLOUR = new Color(0, 70, 100);

	//Lets make all the buttons the same size.
	final static private int BUTTON_WIDTH = 300;
	final static private int BUTTON_HEIGHT = 100;

    //Stores whether or not user is currently holding down the mouse.
    private boolean mouseDown = false;
    
    //The main game grid.
    private Grid gameGrid;
    //The side grid to display the next game piece.
    private Grid sideGrid;

    //The button manager, to manage the buttons.
    private ButtonManager buttonManager;

    //All the buttons. There will be a maximum of three buttons at any one time.
    private Button but1,but2,but3;

    //The textboxes.
    private TextBox textBox1, textBox2, textBox3;

    //Manager for the textboxes.
    private TextComponentManager textBoxManager;

    //The game pieces.
    private GamePiece currentGamePiece;
    private GamePiece nextGamePiece;

    //The menu overlays for game over and pauseGame screens.
    private Overlay menuOverlay = null;

    //Timer. Used to drop the game block by one cell after a set amount of time.
    private Timer dropTimer;
    //How long to wait before dropping game piece again by one cell.
    private int dropTime;
    final private static int INITIAL_DROP_TIME = 500;

    //Whether or not to pauseGame the game.
    private boolean pauseGame = false;

    //The score.
    private int score = 0;

    int hell = 1 + 2;
    
    /**
     * Constructor.
     */
    public GamePanel() {
        super();
        
        //Time that the program started at.
        programStartTime = System.currentTimeMillis();

        //Set the size of the window.
        size = new Dimension(1100,800);
        setPreferredSize(size);
        
        //Register to receive mouse events.
        addMouseListener(this);
        addMouseMotionListener(this);
        //Resister to receive keyboard events.
        addKeyListener(this);
        
        this.setFocusable(true);
        this.requestFocus();
        
        //Set up the game.
        buttonManager = new ButtonManager();
        textBoxManager = new TextComponentManager();

        //Initially go to the main menu.
        goToMainMenu();
    }
    
    /**
     * Method that must be called whenever we are going to the main menu.
     */
    public void goToMainMenu() {
    	//Set the state.
        updateState(State.MAIN_MENU);

    	//Get the center of the screen.
    	int centerX = (int) (size.getWidth()/2);
    	
    	//Set up the buttons. They will be drawn horizontally center to the screen.
    	but1 = new Button(centerX-(BUTTON_WIDTH/2), 300, BUTTON_WIDTH, BUTTON_HEIGHT, "Play",
                PRIMARY_BUTTON_COLOUR, SECONDARY_BUTTON_COLOUR);
    	but2 = new Button(centerX-(BUTTON_WIDTH/2), 450, BUTTON_WIDTH, BUTTON_HEIGHT, "How to Play",
                PRIMARY_BUTTON_COLOUR, SECONDARY_BUTTON_COLOUR);
    	but3 = new Button(centerX-(BUTTON_WIDTH/2), 600, BUTTON_WIDTH, BUTTON_HEIGHT, "Controls",
                PRIMARY_BUTTON_COLOUR, SECONDARY_BUTTON_COLOUR);
    	
    	//Add these buttons.
    	buttonManager.addComponent(but1);
    	buttonManager.addComponent(but2);
    	buttonManager.addComponent(but3);
    	buttonManager.prepareButtons(previousMouseEvent);

    	//Add the title.
        textBox1 = new TextBox(0,80, (int) size.getWidth(),150,"Tetris");
        textBox1.setTextAlignment(TextBox.TEXT_ALIGN_CENTER);
        textBox1.setTopPadding(100);
        Font titleFont = new Font("Arial",Font.BOLD,50);
        textBox1.setTextFont(titleFont);
        textBoxManager.addComponent(textBox1);
    }
    
    /**
     * Method that must be called when we are going to the 'How to Play' screen.
     */
    public void goToInstructions() {
    	//Set the state.
    	updateState(State.HOW_TO_PLAY);
    	
    	//Get the center of the screen.
    	int centerX = (int) (size.getWidth()/2);
    	
    	/*
    	Set up the buttons. Sorry for the messy parameters, theres a ton of calculations to get the position.

        centerX-(BUTTON_WIDTH/2) for the x parameter basically centers the button in the middle of the screen
        horizontally.
        */
    	but1 = new Button(centerX-(BUTTON_WIDTH/2)-((int)(BUTTON_WIDTH*1.1)), 600, BUTTON_WIDTH, BUTTON_HEIGHT,
                "More on Tetris (Open Webpage)", PRIMARY_BUTTON_COLOUR, SECONDARY_BUTTON_COLOUR);
    	but2 = new Button(centerX-(BUTTON_WIDTH/2), 600, BUTTON_WIDTH, BUTTON_HEIGHT, "Continue to Controls",
                PRIMARY_BUTTON_COLOUR, SECONDARY_BUTTON_COLOUR);
    	but3 = new Button(centerX-(BUTTON_WIDTH/2)+((int)(BUTTON_WIDTH*1.1)), 600, BUTTON_WIDTH, BUTTON_HEIGHT,
                "Return to Main Menu", PRIMARY_BUTTON_COLOUR, SECONDARY_BUTTON_COLOUR);

        //Add these buttons.
        buttonManager.addComponent(but1);
        buttonManager.addComponent(but2);
        buttonManager.addComponent(but3);
        buttonManager.prepareButtons(previousMouseEvent);

        //Add the text.
        textBox1 = new TextBox(50,100,(int)size.getWidth()-100,(int)size.getHeight()/2,INSTRUCTIONS_TEXT);
        textBox1.setLineSpacing(30);
        textBox1.setTopPadding(80);
        textBoxManager.addComponent(textBox1);
    }
    
    /**
     * Method that must be called when we are going to the 'Controls' screen.
     */
    public void goToControls() {
    	//Update game state.
        updateState(State.CONTROLS);
    	
    	//Get the center of the screen.
    	int centerX = (int) (size.getWidth()/2);
    	
    	//Set up button.
    	but1 = new Button(centerX-(BUTTON_WIDTH/2), 600, BUTTON_WIDTH, BUTTON_HEIGHT, "Return to Main Menu",
                PRIMARY_BUTTON_COLOUR, SECONDARY_BUTTON_COLOUR);

    	//Add to button manager.
    	buttonManager.addComponent(but1);
        buttonManager.prepareButtons(previousMouseEvent);

    	//Set up text box that displays information on the controls, and add it to the text box manager.
        textBox1 = new TextBox(50,100,(int) size.getWidth()-100,(int) size.getHeight()/2, CONTROLS_TEXT);
        textBox1.setLineSpacing(30);
        textBox1.setTopPadding(80);
        textBoxManager.addComponent(textBox1);
    }
    
    /**
     * Method that needs to be called when starting a new game.
     * Resets all the game variables.
     */
    public void goToGame() {
    	
    	//Update the game state.
        updateState(State.GAME_ON);
    	
    	//Update the buttons.
    	buttonManager.removeAllComponents();
    	//Menu button that will be in top right corner of the screen.
    	but1 = new Button(size.width-BUTTON_WIDTH, 0, BUTTON_WIDTH, BUTTON_HEIGHT, "Menu",
                PRIMARY_BUTTON_COLOUR, SECONDARY_BUTTON_COLOUR);
    	buttonManager.addComponent(but1);
        buttonManager.prepareButtons(previousMouseEvent);
    	
    	//Create a game grid.
    	gameGrid = new Grid(BUTTON_WIDTH,0, 10,16);
    	gameGrid.setDrawTerminalLine(true);
    	//Grid to display panel.
    	sideGrid = new Grid(50,200,4,4);

    	//Text descriptions and displays.
        textBox1 = new TextBox(50,100,200,100, "Next Game Piece");
        textBox1.setTextAlignment(TextBox.TEXT_ALIGN_CENTER);
        textBox1.setTopPadding(50);
        textBox2 = new TextBox((int)size.getWidth()-BUTTON_WIDTH, 200, BUTTON_WIDTH, 100, "Score: 0");
        textBox2.setTextAlignment(TextBox.TEXT_ALIGN_LEFT);
        textBox2.setTopPadding(50);
        textBoxManager.addComponent(textBox1);
        textBoxManager.addComponent(textBox2);

        //Create the game piece that is next in line.
        nextGamePiece = new GamePiece(sideGrid);
        nextGamePiece.generateNewPiece();

        //Create the first game piece.
        currentGamePiece = new GamePiece(gameGrid);
        currentGamePiece.generateNewPiece();

        //Set initial variables.
        dropTime = INITIAL_DROP_TIME;
        setupDropTimer(dropTime);
        score = 0;
    }

    /**
     * Method that needs to be called whenever the game screen/state changes; for example, it must be called when the
     * program goes to the main menu. This method deletes all textboxes and buttons used in the previous screen.
     * @param newState The new state in which to change into.
     */
    private void updateState(State newState) {
        state = newState;
        buttonManager.removeAllComponents();
        textBoxManager.removeAllComponents();
    }


    /**
     * End the game. Ask user whether to play again or return to menu.
     */
    private void gameOver() {
        //Update the state.
        updateState(State.GAME_OVER);
    	//Pause the game.
        pauseGame();
        //Stop the drop timer.
        dropTimer.stop();
        dropTimer = null;
        //Set up menu overlay.
    	menuOverlay = new Overlay(100,100,(int)size.getWidth()-200,(int)size.getHeight()-200,"Game Over!",size);

    	//Create buttons.
        but2 = new Button(150,500,BUTTON_WIDTH,BUTTON_HEIGHT,"Play Again",PRIMARY_BUTTON_COLOUR,SECONDARY_BUTTON_COLOUR);
        menuOverlay.addButton(but2);
        but3 = new Button(650,500,BUTTON_WIDTH,BUTTON_HEIGHT,"Return to Main Menu",PRIMARY_BUTTON_COLOUR,SECONDARY_BUTTON_COLOUR);
    	menuOverlay.addButton(but3);

    	//Create explanatory text box.
    	TextBox textBox = new TextBox(150,250,800,200,"Game Over! Your final score is " + score + "!");
    	textBox.setTopPadding(30);
    	textBox.setTextAlignment(TextComponent.TEXT_ALIGN_CENTER);
    	menuOverlay.addTextBox(textBox);
    }

    /**
     * Pauses the game. Make all the buttons not inside of the menu overlay unclickable.
     */
    private void pauseGame() {
        //Stop the dropTimer.
        if (dropTimer != null) {
            dropTimer.stop();
        }
        pauseGame = true;
        buttonManager.setClickable(false);
    }

    /**
     * Resumes the game. Makes the buttons clickable again.
     */
    private void resumeGame() {
        //Resume the timer.
        if (dropTimer != null) {
            dropTimer.start();
        }
        pauseGame = false;
        buttonManager.setClickable(true);
    }

    /**
     * Removes the overlay by setting it equal to null.
     */
    private void removeOverlay() {
        menuOverlay = null;
    }

    /**
     * Create the pause menu. It has the option to resume or restart the game.
     */
    private void createPauseMenu() {
        //Set up menu overlay.
        menuOverlay = new Overlay(100,100,(int)size.getWidth()-200,(int)size.getHeight()-200,"Game Paused",size);

        //Buttons.
        but2 = new Button(150,500,BUTTON_WIDTH,BUTTON_HEIGHT,"Resume",PRIMARY_BUTTON_COLOUR,SECONDARY_BUTTON_COLOUR);
        menuOverlay.addButton(but2);
        but3 = new Button(650,500,BUTTON_WIDTH,BUTTON_HEIGHT,"Restart",PRIMARY_BUTTON_COLOUR,SECONDARY_BUTTON_COLOUR);
        menuOverlay.addButton(but3);

        //Textbox.
        TextBox textBox = new TextBox(150,250,800,200,"The game is paused. Resume the game, or restart it.");
        textBox.setTopPadding(30);
        textBox.setTextAlignment(TextComponent.TEXT_ALIGN_CENTER);
        menuOverlay.addTextBox(textBox);
    }

    /**
     * Overridden paint method.
     */
    @Override
    public void paint(Graphics g) {

        //Check if the dropTimer has been started.
        if (animationTimer == null) {
            //If not, set it up and start it.
            setupAnimationTimer();
        }

        //Clear the panel.
        size = getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, size.width, size.height);

        //It is currently in the main menu.
        if (state == State.MAIN_MENU) {
            //Fill background.
            g.setColor(MENU_BACKGROUND_COLOUR);
            g.fillRect(0, 0, size.width, size.height);
        }
        //The game is currently operating.
        else if (state == State.GAME_ON) {
        	drawGame(g);
        }
        //Game is currently in the controls screen.
        else if (state == State.CONTROLS) {
            //Fill background.
            g.setColor(MENU_BACKGROUND_COLOUR);
            g.fillRect(0, 0, size.width, size.height);
        }
        //Instructions screen.
        else if (state == State.HOW_TO_PLAY) {
            //Fill background.
            g.setColor(MENU_BACKGROUND_COLOUR);
            g.fillRect(0, 0, size.width, size.height);
        }
        //The game is over.
        else if (state == State.GAME_OVER) {
            //Continue drawing the game, its in the background.
            drawGame(g);
        }
        
        //Draw the text boxes on top.
        textBoxManager.drawComponents(g);

        //Draw all appropriate buttons on top of everything else.
        buttonManager.drawComponents(g);

        //Draw the overlay, if it exists.
        if (menuOverlay != null) {
            menuOverlay.draw(g);
        }

        //Display debug information.
        if (debug == true) {
            g.setColor(Color.red);
            programCurrentTime = System.currentTimeMillis();
            elapsedTime = (programCurrentTime - programStartTime);
            actualFPS = (int) ((framesDrawn * 1000) / elapsedTime);
            g.drawString("FPS: " + actualFPS, 20, 20);
            g.drawString("Frames: " + framesDrawn, 20, 40);
            g.drawString("programCurrentTime: " + programCurrentTime, 20, 60);
            g.drawString("programStartTime: " + programStartTime, 20, 80);
            g.drawString("elapsedTime: " + elapsedTime, 20, 100);
            g.drawString("delayTime: " + delayTime, 20, 120);
        }
    }

    /**
     * Draws the game grids and the side panels for the game.
     * @param g The graphics object.
     */
    private void drawGame(Graphics g) {
        //Draw the menu panels on the sides. Their widths will be equal to the width of a button.
        g.setColor(SIDEPANEL_COLOUR);
        g.fillRect(0, 0, BUTTON_WIDTH, size.height);
        g.fillRect(size.width-BUTTON_WIDTH, 0, BUTTON_WIDTH, size.height);

        //Draw the game grids.
        gameGrid.draw(g);
        sideGrid.draw(g);
    }

    /**
     * Sets up the animationTimer.
     */
    private void setupAnimationTimer() {
        //Calculate time to delay.
        delayTime = 1000/TARGET_FPS;

        //Calculate how much to multiply speed to ensure consistency.
        double movementMultiplier = (delayTime/1000);

        //Start the dropTimer.
        //The first parameter is the dropTimer interval, in milliseconds.
        //The second parameter is the object that will receive events from the dropTimer.
        animationTimer = new Timer((int)delayTime,this);
        animationTimer.start();

    }

    /**
     * Set up the drop timer; the amount of time before each drop.
     * @param dropTime The amount of time in milliseconds.
     */
    private void setupDropTimer(int dropTime) {
        if (dropTimer != null) {
            dropTimer.stop();
        }
        dropTimer = new Timer(dropTime,this);
        dropTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Check if the action we are receiving is from the dropTimer.
        if (e.getSource() == animationTimer) {
            //Time to repaint the panel.
            framesDrawn++;
            repaint();
        }
        else if (e.getSource() == dropTimer) {
            //Only drop when the game is not paused.
            if (pauseGame == false) {
                dropGamePiece();
            }
        }
    }

    /**
     * Drop the gamepiece by one cell/row. Also detects when it has hit the bottom.
     */
    private void dropGamePiece() {
        if (currentGamePiece != null) {
            boolean canDropDown = currentGamePiece.moveDown();

            //Land the game piece.
            if (canDropDown == false) {
                //Release all of the blocks that were formerly a part of the piece.
                currentGamePiece.releaseGamePiece();
                //Use the next game piece.
                currentGamePiece = nextGamePiece;
                movePieceToGameGrid(currentGamePiece);

                //Remove all blocks from the side grid panel.
                sideGrid.removeAllBlocks();
                //Generate a new piece that will be next in line.
                nextGamePiece = new GamePiece(sideGrid);
                nextGamePiece.generateNewPiece();
                
                //Check to see if any line was formed. If yes, add to score and delete row.
                checkLinesFormed();

                //Check to see if any block landed in the terminal line. If yes, the game is over.
                boolean blockTerminated = gameGrid.blockOnTerminalLine();
                if (blockTerminated == true) {
                	gameOver();
                }
            }
        }
    }


    /**
     * Move the current game piece to the game grid.
     * @param gamePiece The game piece to move.
     */
    private void movePieceToGameGrid(GamePiece gamePiece) {
        //Change the grid from the sideGrid to the gameGrid.
        gamePiece.changeGrid(gameGrid);

        //Move up so that it initially starts outside and above of the game grid.
        for (int i = 0; i < gamePiece.getSIZE(); i ++) {
            gamePiece.moveUp();
        }

        //Center the game piece, as much as possible.
        int gridWidth = gamePiece.getGrid().getColumns();
        int offsetX = (gridWidth-(4))/2;
        for (int i = 0; i < offsetX; i ++) {
            gamePiece.moveRight();
        }
    }

    /**
     * Checks to see if any horizontal line was formed on the grid.
     */
    private void checkLinesFormed() {
    	int rows = gameGrid.getRows();
    	//Start at 1, because 0 is the terminal line.
    	for (int i = 1; i < rows; i ++) {
    		boolean lineFormed = gameGrid.horizontalLineFormed(i);
    		if (lineFormed == true) {
    		    //Delete row and drop down all of the blocks that are above it.
    			gameGrid.deleteRow(i);
    			gameGrid.dropBlocks(i);
    			increaseScore();
    		}
    	}
    }
    
    /**
     * Add to the score.
     */
    private void increaseScore() {
    	score ++;
    	textBox2.setText("Score: " + score);
    }

    /**
     * Checks to see which button was clicked. Take the according action.
     * @param e The mouse event.
     */
    private void checkButtonsClicked(MouseEvent e) {
        //Get the button that was pressed, if any.
        TextComponent clickedButton = buttonManager.getClickedButton(e);

        //The program is currently in the main menu.
        if (state == State.MAIN_MENU) {
            //User pressed 'Play'.
            if (but1 == clickedButton) {
                //Start the game.
                goToGame();
            }
            //User clicked 'How to Play'.
            else if (but2 == clickedButton) {
                goToInstructions();
            }
            //User clicked 'Controls'.
            else if (but3 == clickedButton) {
                goToControls();
            }
        }
        //The program is currently in the instructions menu.
        else if (state == State.HOW_TO_PLAY) {
            //User pressed 'More on Tetris'.
            if (but1 == clickedButton) {
                //Open up a webpage that further describes the game.
                try {
                    //Method obtained from online reference.
                    Desktop.getDesktop().browse(new URI(TETRIS_INFO_URL));
                } catch (Exception exception) {

                }
            }
            //User clicked 'Continue to Controls'.
            else if (but2 == clickedButton) {
                goToControls();
            }
            //User clicked 'Return to Main Menu'.
            else if (but3 == clickedButton) {
                goToMainMenu();
            }
        }
        //Program is in controls screen.
        else if (state == State.CONTROLS) {
            //User clicked return to main menu.
            if (but1 == clickedButton) {
                goToMainMenu();
            }
        }
        //The game is currently running.
        else if (state == State.GAME_ON) {
            //User pressed the menu button.
            if (but1 == clickedButton) {
                pauseGame();
                createPauseMenu();
            }
            //Get the button that was clicked from the overlay menu, if any.
            TextComponent overlayButton = menuOverlay.getClickedButton(e);
            //User pressed 'Resume'.
            if (but2 == overlayButton) {
                //Unpause the game.
                resumeGame();
                //Remove the overlay.
                removeOverlay();
            }
            //User pressed 'Restart'.
            else if (but3 == overlayButton) {
                //Unpause the game.
                resumeGame();
                //Remove the overlay.
                removeOverlay();
                //Restart the game.
                goToGame();
            }
        }
        //Game is over.
        else if (state == State.GAME_OVER) {
            //Get the button that was clicked from the overlay menu, if any.
            TextComponent overlayButton = menuOverlay.getClickedButton(e);
            //User pressed 'Play Again'.
            if (but2 == overlayButton) {
                //Unpause the game.
                resumeGame();
                //Remove the overlay.
                removeOverlay();
                //Start the game over.
                goToGame();
            }
            //User pressed 'Return to Main Menu'.
            else if (but3 == overlayButton) {
                //Unpause the game.
                resumeGame();
                //Remove the overlay.
                removeOverlay();
                //Go to the main menu.
                goToMainMenu();
            }

        }
    }

    /*  Events that are used. */

    @Override
    public void mouseReleased(MouseEvent e) {
        //The mouse has just been released; check to see what button was clicked.
        checkButtonsClicked(e);
        previousMouseEvent = e;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    	//Update the state of the buttons depending on if the mouse is hovering over them.
    	buttonManager.updateButtonStates(e);

    	//Send to the overlay, if it exists.
        if (menuOverlay != null) {
        menuOverlay.updateButtonStates(e);
        }

        //Store the mouse event.
        previousMouseEvent = e;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Interesting: when user holds down mouse while moving it, it is a mouseDragged event.
        //Update the state of the buttons depending on if the mouse is hovering over them.
        buttonManager.updateButtonStates(e);
        previousMouseEvent = e;
    }
    
    @Override
	public void keyPressed(KeyEvent e) {
        if (state == State.GAME_ON) {
            //Only move when the game is not paused.
            if (pauseGame == false) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    currentGamePiece.moveLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    currentGamePiece.moveRight();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    dropGamePiece();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    currentGamePiece.rotate();
                }
                //Make sure nothing is out of bounds.
                currentGamePiece.stayWithinBounds();
            }
        }
	}
    
    /*	Unused inherited methods. Why are we forced to implement them if we are not gonna use them?? Doesn`t make sense!	*/
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
