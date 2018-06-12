package model.command;

import model.Shape;

import java.util.ArrayList;

/**
 * Delete command that implements Undo/Redo functionality.
 */
public class DeleteCommand implements CommandInterface {
    private ArrayList<Shape> shapes;
    private Shape shape;
    private int index;


    /**
     * DeleteCommand called and get's the current drawn shapes.
     * and the shape to delete.
     *
     * @param shapes all shapes on canvas
     * @param shape  to be deleted
     */
    public DeleteCommand(ArrayList<Shape> shapes, Shape shape) {
        this.shapes = shapes;
        this.shape = shape;
    }

    /**
     * Redo, do the delete
     */
    @Override
    public void redo() {
        this.index = shapes.indexOf(shape);
        shapes.remove(shape);
    }

    /**
     * Undo, add back the shape to the list
     */
    @Override
    public void undo() {
        shapes.add(this.index, shape);
    }
}
