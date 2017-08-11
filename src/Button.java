import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Class that represents a button. Extends TextComponent.
 * It has methods to draw, and to check if a mouse is hovering over it.
 * It has a primary draw method and a secondary draw method, to represent the states of the button.
 */
public class Button extends TextComponent {

	//The primary colour is the default color. The secondary colour is when the user hovers the mouse over or clicks the button.
	private Color primaryColour;
	private Color secondaryColour;

	/**
	 * Constructor.
	 * @param x The left edge.
	 * @param y The top edge.
	 * @param w The width.
	 * @param h The height.
	 * @param text The text of the button.
	 * @param primaryColour The primary colour of the button.
	 * @param secondaryColour The secondary colour of the button.
	 */
	public Button(int x, int y, int w, int h, String text, Color primaryColour, Color secondaryColour) {
		super(x,y,w,h,text);
		//Set defaults.
		this.primaryColour = primaryColour;
		this.secondaryColour = secondaryColour;
		setCornerSize(40);
		setClickable(true);
	}
	
	/**
	 * Draw the button, by calling the according method.
	 * @param g The graphics object.
	 */
	public void draw(Graphics g) {

		//If the button is clickable, check to see if mouse is hovering over it.
		if (isClickable() == true) {
			//The mouse is hovering over button. Draw the secondary state.
			if (isMouseOver() == true) {
				drawSecondary(g);
			}
			//Else, draw the primary state.
			else if (isMouseOver() == false) {
				drawPrimary(g);
			}
		}
		//Else, just draw the primary state.
		else if (isClickable() == false) {
			drawPrimary(g);
		}
	}

	/**
	 * Draw the button in its primary state.
	 * @param g The graphics object.
	 */
	private void drawPrimary(Graphics g) {
		g.setColor(primaryColour);
		g.fillRoundRect(getX(), getY(), getW(), getH(), getCornerSize(), getCornerSize());
		drawButtonText(g);
	}

	/**
	 * Draw the button in its secondary state.
	 * @param g The graphics object.
	 */
	private void drawSecondary(Graphics g) {
		g.setColor(secondaryColour);
		g.fillRoundRect(getX(), getY(), getW(), getH(), getCornerSize(), getCornerSize());
		drawButtonText(g);
	}
	
	/**
	 * Draw the button text in the center of the button.
	 */
	private void drawButtonText(Graphics g) {
		
		//Set colour and font.
		g.setColor(getTextColour());
		g.setFont(getTextFont());
		
		//Calculate the width of the button text.
		//Method obtained by online reference.
		FontMetrics fontMetrics = g.getFontMetrics(getTextFont());
		int textWidth = fontMetrics.stringWidth(getText());
		
		//Get the center of the button.
		int centerX = getX()+(getW()/2);
		int centerY = getY()+(getH()/2);
		
		//Draw the text in the center of the button.
		g.drawString(getText(), centerX-(textWidth/2), centerY);
	}
}
