package model.factory;

import model.Line;
import model.Oval;
import model.Rectangle;
import model.Shape;
import model.Star;

import java.util.Hashtable;

/**
 * The shape prototype class that has a list of
 * made shapes that it clones to the client.
 * Part of sample from https://dzone.com/articles/design-patterns-prototype
 */
public class ShapePrototype {

    private static Hashtable<Shapes, Shape> shapePrototypes = new Hashtable<>();

    static {
        shapePrototypes.put(Shapes.RECTANGLE, new Rectangle());
        shapePrototypes.put(Shapes.OVAL, new Oval());
        shapePrototypes.put(Shapes.LINE, new Line());
        shapePrototypes.put(Shapes.STAR, new Star());
    }

    /**
     * To create a new shape
     *
     * @param shapeType the shape to create
     * @return a clone of the shape type
     */
    public static Shape getShapePrototype(Shapes shapeType) {
        try {
            return shapePrototypes.get(shapeType).clone();
        } catch (NullPointerException ex) {
            return null;
        }
    }
}
