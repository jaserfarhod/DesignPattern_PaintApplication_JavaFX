package model.command;

import model.Shape;

/**
 * The concrete Change Thickness Command, which deals with increasing the shapes line thickness
 */
public class ChangeThicknessCommand implements CommandInterface {

    /**
     * References to the shape itself, and old thickness and new thickness.
     */
    private Shape shape;
    private double oldThickness;
    private double newThickness;

    /**
     * Change thickness of edge for a shape
     * @param shape the shape to change
     * @param newThickness the thickness to change to
     */
    public ChangeThicknessCommand(Shape shape, double newThickness) {
        this.shape = shape;
        this.oldThickness = shape.getThickness();
        this.newThickness = newThickness;
        shape.setThickness(newThickness);
    }

    /**
     * Redo, to the new thickness
     */
    @Override
    public void redo() {
        shape.setThickness(newThickness);
    }

    /**
     * Undo, to the old thickness
     */
    @Override
    public void undo() {
        shape.setThickness(oldThickness);
    }
}
