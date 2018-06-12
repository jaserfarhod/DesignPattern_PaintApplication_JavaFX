package model.command;

import javafx.scene.paint.Color;
import model.Shape;

/**
 * The concrete Change Color Command, which deals with editing a shapes color.
 */
public class ChangeColorCommand implements CommandInterface {

    /**
     * References to the shape, and it's new color as well as old color.
     */
    private String prevColor;
    private String newColor;
    private Shape shape;

    /**
     * Add a shape and the new color it should have
     *
     * @param shape    the shape to change color
     * @param newColor the new color
     */
    public ChangeColorCommand(Shape shape, Color newColor) {
        this.shape = shape;
        this.newColor = newColor.toString();
        this.prevColor = shape.getColor();
        shape.setColor(this.newColor);
    }

    /**
     * Redo the change, to the new color
     */
    @Override
    public void redo() {
        shape.setColor(newColor);
    }

    /**
     * Undo the change, to the old color
     */
    @Override
    public void undo() {
        shape.setColor(prevColor);
    }
}
