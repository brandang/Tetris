import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * The only purpose of this class is to create and display the main
 * window. 
 * Implements Runnable, to run in a separate thread.
 */
public class GUIMain implements Runnable{

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