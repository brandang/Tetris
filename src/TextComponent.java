import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A text area component. It is basically a box with text in it. It is the parent class
 * for TextBox and Button.
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

        //Set defaults - they can be modified via setters.
        textAlignment = TEXT_ALIGN_LEFT;
        textFont = new Font("Arial",Font.PLAIN,20);
        textColour = Color.WHITE;
        mouseOver = false;
        cornerSize = 15;

        clickable = false;
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
        mouseOver = false;

        //Check to see if mouse is hovering over this.
        if (mouseX > getX() && mouseX < getX() + getW()) {
            if (mouseY > getY() && mouseY < getY() + getH()) {
                //It is hovering over it: adjust variable.
                mouseOver = true;
            }
        }
        return mouseOver;
    }

    /*  Getters and Setters */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public String getText() {
        return text;
    }

    public Font getTextFont() {
        return textFont;
    }

    public Color getTextColour() {
        return textColour;
    }

    public int getTextAlignment() {
        return textAlignment;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public int getCornerSize() {
        return cornerSize;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    public void setTextColour(Color textColour) {
        this.textColour = textColour;
    }

    public void setTextAlignment(int textAlignment) {
        this.textAlignment = textAlignment;
    }

    public void setCornerSize(int cornerSize) {
        this.cornerSize = cornerSize;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
