package model.factory;

import model.Line;
import model.Oval;
import model.Rectangle;
import model.Star;

/**
 * The abstract factory abstract class.
 */
public abstract class AbstractShapeFactory {
    private static final ShapeFactory SHAPE_FACTORY = new ShapeFactory();

    public static AbstractShapeFactory getFactory() {
        return SHAPE_FACTORY;
    }

    public abstract Line createLine();

    public abstract Oval createOval();

    public abstract Rectangle createRectangle();

    public abstract Star createStar();
}
