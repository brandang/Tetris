import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A text area component. It is a box with text in it. It contains methods for drawing itself on the screen.
 */
public abstract class TextComponent {

    //Position in the window and the dimensions.
    private int x, y, w, h;
    //The text to display.
    private String text;
    //The font and colour of the text.
    private Font textFont;
    private Color textColour;

    //Constants used to define where to draw text.
    final public static int TEXT_ALIGN_LEFT = 1;
    final public static int TEXT_ALIGN_CENTER = 2;
    private int textAlignment;

    //The padding for the text.
    private int sidePadding, topPadding;

    //The size of the diameter of the arc at the corners of the background shape.
    private int cornerSize;

    //Whether or not the mouse is hovering over this TextComponent.
    private boolean mouseOver;

    //Whether or not component is clickable.
    private boolean clickable;

    /**
     * Constructor.
     * @param x The left edge.
     * @param y The top edge.
     * @param w The width.
     * @param h The height.
     * @param text The text to display.
     */
    public TextComponent(int x, int y, int w, int h, String text) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = text;

        //Set defaults.
        setTextAlignment(TEXT_ALIGN_LEFT);
        setTextFont(new Font("Arial", Font.PLAIN, 20));
        setTextColour(Color.WHITE);
        setMouseOver(false);
        setCornerSize(15);
        setClickable(false);
    }

    /**
     * Draws the component.
     * @param g The graphics object.
     */
    public abstract void draw(Graphics g);

    /**
     * Checks whether or not the mouse is hovering over this textComponent.
     * Set the state of this textComponent accordingly.
     * @param e The mouse event.
     * @return True for yes, false for no.
     */
    public boolean checkMouseOver(MouseEvent e) {
        //Get mouse positions.
        int mouseX = e.getX();
        int mouseY = e.getY();

        //Initially assume it is not hovering over it.
        setMouseOver(false);

        //Check to see if mouse is hovering over this.
        if (mouseX > getX() && mouseX < getX() + getW()) {
            if (mouseY > getY() && mouseY < getY() + getH()) {
                //It is hovering over it: adjust variable.
                setMouseOver(true);
            }
        }
        return isMouseOver();
    }

    /**
     * Calculates the real x position of the text in the game window so that the text will be aligned in the
     * center of the TextComponent. Accounts for padding.
     * @return The x coordinate to draw text so that it is centered horizontally.
     * @param text The text to calculate for.
     * @param g The graphics object.
     * @return The real x position that will center the text horizontally.
     */
    public int getCenterTextHorizontal(String text, Graphics g) {

        //Get information on rendering text on screen.
        FontMetrics fontMetrics = g.getFontMetrics(getTextFont());

        //Calculate where to draw the text in order to center it.
        int textWidth = fontMetrics.stringWidth(text);

        //Amount of space that needs to be added to center the text.
        int adjustment = (getW() - (getSidePadding() * 2) - textWidth) / 2;
        int drawX = getX() + getSidePadding() + adjustment;

//        int centerX = getX() + (getW()/2);
        return drawX;
    }

    /**
     * Calculates the real y position of the text in the game window in order to align it in the center of the
     * text component.
     * @return The real y position that will center the text horizontally.
     */
    public int getCenterTextVertical() {
        int drawY = getY()+(getH()/2);
        return drawY;
    }

    /*  Getters and Setters */

    /**
     * Get the x position.
     * @return The x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y position.
     * @return The y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Get the width.
     * @return The width in pixels.
     */
    public int getW() {
        return w;
    }

    /**
     * Get the height.
     * @return The height in pixels.
     */
    public int getH() {
        return h;
    }

    /**
     * Get the text.
     * @return The text.
     */
    public String getText() {
        return text;
    }

    /**
     * Get the text font.
     * @return The font.
     */
    public Font getTextFont() {
        return textFont;
    }

    /**
     * Get the text colour.
     * @return The colour.
     */
    public Color getTextColour() {
        return textColour;
    }

    /**
     * Gets the text alignment. Compare using TEXT_ALIGN_LEFT and TEXT_ALIGN_CENTER.
     * @return The text alignment, as an int.
     */
    public int getTextAlignment() {
        return textAlignment;
    }

    /**
     * Whether or not the mouse is assumed to be hovering over, based on the last call checkMouseOver().
     * @return True for yes, false for no.
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Get the radius of the corner of the graphical object to draw.
     * @return The radius of the corner.
     */
    public int getCornerSize() {
        return cornerSize;
    }

    /**
     * Whether or not this is clickable by the mouse.
     * @return True for yes, false for no.
     */
    public boolean isClickable() {
        return clickable;
    }

    /**
     * Get the side padding for the text.
     * @return The side padding, in pixels.
     */
    public int getSidePadding() {
        return sidePadding;
    }

    /**
     * Get the top padding for the text.
     * @return The top padding in pixels.
     */
    public int getTopPadding() {
        return topPadding;
    }

    /**
     * Set the x position of the TextComponent.
     * @param x The new x coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y position of the TextComponent.
     * @param y The new y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set the width.
     * @param w The new width.
     */
    public void setW(int w) {
        this.w = w;
    }

    /**
     * Set the height.
     * @param h The new height.
     */
    public void setH(int h) {
        this.h = h;
    }

    /**
     * Set the text to display.
     * @param text The new text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Set the text font.
     * @param textFont The new font.
     */
    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    /**
     * Set the text colour.
     * @param textColour The new text colour.
     */
    public void setTextColour(Color textColour) {
        this.textColour = textColour;
    }

    /**
     * Set the textAlignment. Can be either set to TEXT_ALIGN_LEFT or TEXT_ALIGN_CENTER.
     * @param textAlignment The text Alignment to set. Should be either TEXT_ALIGN_LEFT or TEXT_ALIGN_CENTER.
     */
    public void setTextAlignment(int textAlignment) {
        if (textAlignment == TEXT_ALIGN_LEFT || textAlignment == TEXT_ALIGN_CENTER) {
            this.textAlignment = textAlignment;
        }
    }

    /**
     * Set the size of the corner of the grapical shape to draw.
     * @param cornerSize The radius of the corner size.
     */
    public void setCornerSize(int cornerSize) {
        this.cornerSize = cornerSize;
    }

    /**
     * Set whether or not this is clickable.
     * @param clickable True for yes, false for no.
     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    /**
     * Set the side padding for the text.
     * @param sidePadding The new side padding, in pixels.
     */
    public void setSidePadding(int sidePadding) {
        this.sidePadding = sidePadding;
    }

    /**
     * Set the top padding for the text.
     * @param topPadding The new top padding, in pixels.
     */
    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    /**
     * Set whether or not the mouse is hovering over this object. Can only be accessed within this class.
     * @param mouseOver True for yes, false for no.
     */
    private void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
}
