package model.command;

import model.Shape;

import java.util.List;

/**
 * The concrete Add Command, which deals with adding new shapes.
 */
public class AddCommand implements CommandInterface {

    /**
     * References to the drawn shapes and the particular shape to add.
     */
    private Shape shape;
    private List<Shape> shapes;

    /**
     * Add the shape to a list of shapes
     *
     * @param shapes the list of shapes
     * @param shape  the shape to add
     */
    public AddCommand(List<Shape> shapes, Shape shape) {
        this.shape = shape;
        this.shapes = shapes;
        this.shapes.add(this.shape);
    }

    /**
     * Redo the add
     */
    @Override
    public void redo() {
        shapes.add(shape);
    }

    /**
     * Undo the add
     */
    @Override
    public void undo() {
        shapes.remove(shape);
    }
}
