package model.factory;

import model.*;

/**
 * The concrete AbstractShapeFactory class, ShapeFactory.
 * Implements Prototype, to deliver shapes.
 */
public class ShapeFactory extends AbstractShapeFactory {

    /**
     * Create a template Line
     */
    @Override
    public Line createLine() {
        return (Line) ShapePrototype.getShapePrototype(Shapes.LINE);
    }

    /**
     * Create a template Oval
     */
    @Override
    public Oval createOval() {
        return (Oval) ShapePrototype.getShapePrototype(Shapes.OVAL);
    }


    /**
     * Create a template Rectangle
     */
    @Override
    public Rectangle createRectangle() {
        return (Rectangle) ShapePrototype.getShapePrototype(Shapes.RECTANGLE);
    }

    /**
     * Create a template Star
     */
    @Override
    public Star createStar() {
        return (Star) ShapePrototype.getShapePrototype(Shapes.STAR);
    }
}
