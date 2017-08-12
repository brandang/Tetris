import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Overlay is like a panel that pops up over everything else.
 * It has the ability to add buttons and text boxes to it.
 */
public class Overlay extends TextComponent{

    //Colours.
    final private static Color BACKGROUND_COLOUR = new Color(0, 101, 145);
    final private static Color OUTLINE_COLOUR = new Color(56, 194, 0);
    //Colour to overlay the rest of the screen with.
    final private static Color OVERLAY_TRANSPARENT_COLOUR = new Color(0, 0, 0,150);

    //Managers for buttons and text boxes.
    private ButtonManager buttonManager;
    private TextComponentManager textComponentManager;

    //The size of the game window.
    private Dimension windowSize;

    //The size of the outline.
    final private static int OUTLINE_SIZE = 15;

    /**
     * Constructor.
     * @param x The x coordinate of the Overlay menu.
     * @param y The y coordinate of the Overlay menu.
     * @param w The width of the Overlay menu.
     * @param h The height of the Overlay menu.
     * @param title The title of the menu.
     * @param windowSize The size of the window.
     */
    public Overlay(int x, int y, int w, int h, String title, Dimension windowSize) {

        super(x, y, w, h, title);
        buttonManager = new ButtonManager();
        textComponentManager = new TextComponentManager();

        setWindowSize(windowSize);

        //Add the title.
        TextBox textBox = new TextBox(getX(), getY(), getW(),60, title);
        textBox.setTextAlignment(TEXT_ALIGN_CENTER);
        textBox.setTopPadding(30);
        textBox.setCornerSize(20);
        textComponentManager.addComponent(textBox);

        setCornerSize(20);
    }

    /**
     * Add button.
     * @param button
     */
    public void addButton(Button button) {
        buttonManager.addComponent(button);
    }

    /**
     * Add text box.
     * @param textBox
     */
    public void addTextBox(TextBox textBox) {
        textComponentManager.addComponent(textBox);
    }

    /**
     * Updates the buttons to their states.
     * @param e
     */
    public void updateButtonStates(MouseEvent e) {
        buttonManager.updateButtonStates(e);
    }

    /**
     * Draw the overlay. Note that this draw method needs to be called AFTER everything else has been drawn.
     * @param g The graphics object.
     */
    public void draw(Graphics g) {
        drawBackground(g, windowSize);
        drawTextComponents(g);
    }

    /**
     * Draw the background of the overlay.
     * @param g
     * @param screenSize
     */
    private void drawBackground(Graphics g, Dimension screenSize) {
        g.setColor(OVERLAY_TRANSPARENT_COLOUR);
        g.fillRect(0,0, getWindowSize().width, getWindowSize().height);
        g.setColor(OUTLINE_COLOUR);
        g.fillRoundRect(getX(), getY(), getW(),getH(), getCornerSize(), getCornerSize());
        g.setColor(BACKGROUND_COLOUR);
        g.fillRoundRect(getX() + OUTLINE_SIZE,getY() + OUTLINE_SIZE,getW() - (OUTLINE_SIZE*2),getH()
                - (OUTLINE_SIZE * 2), getCornerSize(), getCornerSize());
    }

    /**
     * Draw the buttons and text boxes of the overlay.
     * @param g
     */
    private void drawTextComponents(Graphics g) {
        buttonManager.drawComponents(g);
        textComponentManager.drawComponents(g);
    }

    /**
     * Checks to see which button, if any, was clicked.
     * @param e
     * @return
     */
    public Button getClickedButton(MouseEvent e) {
        return (Button) buttonManager.getClickedButton(e);
    }

    /**
     * Update the size of the window.
     * @param windowSize The size of the window, as a Dimension object..
     */
    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }

    /**
     * Get the last known size of the window.
     * @return The last known size of the window, as a Dimension object.
     */
    public Dimension getWindowSize() {
        return windowSize;
    }
}
