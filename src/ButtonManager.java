import java.awt.event.MouseEvent;

/**
 * Class that manages buttons. It handles drawing, and checks to see if the user
 * is clicking any button, or if the mouse is hovering over them.
 * Extends the TextComponentManager class.
 */
public class ButtonManager extends TextComponentManager {

	private boolean clickable;

	public ButtonManager() {
		clickable = true;
	}

	/**
	 * Checks each button to determine which one, if any, was clicked.
	 * Returns the clicked button. Limitation: can only return one button.
	 * If there are multiple buttons overlapping, only one of the buttons will be returned.
	 * One possible solution is to return an array of all the buttons currently being clicked on,
	 * but for simplicity, assume that none of the buttons overlap.
	 * @param e The mouseEvent.
	 * @return The clicked button.
	 */
	public TextComponent getClickedButton(MouseEvent e) {
		//Loop through buttons, return first one being clicked on by mouse.
		for (int i = 0; i < getComponents().size(); i ++) {
			TextComponent button = getComponents().get(i);
			boolean mouseOver = button.checkMouseOver(e);
			//Return the reference to the button.
			if (mouseOver == true) {
				return button;
			}
		}
		//Returns null if no button is being clicked.
		return null;
	}

	/**
	 * Check whether or not the mouse is hovering over the buttons. Set the state of
	 * the button accordingly.
	 * @param e The mouse event.
	 */
	public void updateButtonStates(MouseEvent e) {
		//Loop through each button.
		for (int i = 0; i < getComponents().size(); i ++) {
			if (clickable == false) {
				getComponents().get(i).setClickable(false);
			}
			//Check to see if the mouse is over the button, and update the state accordingly.
			getComponents().get(i).checkMouseOver(e);
		}
	}

	/**
	 * Method that must be called after adding buttons to the button manager. Sets the initial states
	 * of all the buttons. For example, if the mouse is initially hovering over one of the buttons,
	 * update that button state accordingly.
	 * @param lastMouseEvent The last mouse event.
	 */
	public void prepareButtons (MouseEvent lastMouseEvent) {
		//Only prepare if the MouseEvent exists.
		if (lastMouseEvent != null) {
			updateButtonStates(lastMouseEvent);
		}
	}

	/**
	 * Sets whether or not the buttons are clickable.
	 * @param clickable True for yes, false for no.
	 */
	public void setClickable(boolean clickable) {
		for (TextComponent button : getComponents()) {
			button.setClickable(clickable);
		}
	}
}
