import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/*
NOTES:
I originally intended to add a few more features to this program. However, I am unable to, due to time constraints.
I have fixed all of the bugs that I have found, but due to the size and complexity of this program (WHY DID I
OVER COMPLICATE EVERYTHING??!), there may be some bugs that I have not murdered yet :)
Also, some parts are not very well commented, because I don`t really know how to explain them using words.
 */

/**
 * The only purpose of this class is to create and display the main
 * window. 
 * Implements Runnable, which means it can run in a separate thread.
 * Runnable objects must have a method called "run".
 */
public class GUIMain implements Runnable{

	/*
	To do

	To make this rotate properly:

	Here is possibility 1:
	rotate around a pivot (the pivot should be centered as much as possible)
	if rotation possible, than rotate
	else, if it is not possible, shift it left/right until it is possible
	if it is still not possible, than rotation is impossible

	OR: keep on doing the above three times (for three rotations)
	if all of them fails, then there is no way to rotate it

	fix glitch where block has 'landed' when in reality it has just hit the side of another block

	 */
	public static void main(String[] args) {
		//Use invokeLater command to tell Java to create GUI in separate thread, specifically, the Event Dispatching Thread.
		//It does this by calling the run method.
		GUIMain thread = new GUIMain();
		SwingUtilities.invokeLater(thread);
	}
	
	@Override
	public void run() {
		//Set up and launch the GUI here.
		
		//Create a JFRame, which represents the application window.
		JFrame mainFrame = new JFrame("Tetris");

		//Create an instance of our CustomPanel.
		GamePanel panel = new GamePanel();
		
		//Disable resizing.
		mainFrame.setResizable(false);
		
		//Add panel to the window.
		mainFrame.add(panel);

		//Tell the Frame that it should end the application when somebody
		//clicks close.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Tell the layout manager to arrange all of the components in the window.
		//In this case there`s only one component.
		mainFrame.pack();

		//Make the frame visible.
		mainFrame.setVisible(true);
	}
}