package model.command;

import model.Shape;


/**
 * The concrete Change Filled Command, which deals with modifying if a shape
 * is color filled or not.
 */
public class ChangeFilledCommand implements CommandInterface {

    /**
     * Reference to the shape which is being modified
     */
    private boolean filled;
    private Shape shape;

    /**
     * Change if a shape is filled or not
     *
     * @param shape  the shape to change
     * @param filled if it should be filled or not
     */
    public ChangeFilledCommand(Shape shape, boolean filled) {
        this.shape = shape;
        this.shape.setFilled(filled);
        this.filled = filled;
    }

    /**
     * Redo the change depending if it was filled or not before
     */
    @Override
    public void redo() {
        this.filled = !filled;
        shape.setFilled(filled);
    }

    /**
     * Redo the change depending if it was filled or not before
     */
    @Override
    public void undo() {
        this.filled = !filled;
        shape.setFilled(filled);
    }
}
