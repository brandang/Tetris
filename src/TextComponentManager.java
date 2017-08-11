import java.awt.*;
import java.util.ArrayList;

/**
 * Class that manages TextComponent objects. It has the ability to store a list of TextComponent
 * objects, and draw all of them.
 * Because I even though did not explicitly create a constructor, it still exits, but it does not do anything.
 */
public class TextComponentManager {

    //The list of components.
    private ArrayList<TextComponent> components = new ArrayList<>(0);

    /**
     * Adds the textComponent to this manager.
     * @param textComponent The component to add.
     */
    public void addComponent(TextComponent textComponent) {
        components.add(textComponent);
    }

    /**
     * Removes all components in this manager.
     */
    public void removeAllComponents() {
        components.clear();
    }

    /**
     * Draw all of the components on the screen. Does so by calling each components` draw method.
     * @param g The graphics object.
     */
    public void drawComponents(Graphics g) {
        for (TextComponent component : components) {
            component.draw(g);
        }
    }

    /*  Getter  */
    public ArrayList<TextComponent> getComponents() {
        return components;
    }
}
