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
    private static Hashtable<String, Shape> shapePrototypes = new Hashtable<>();

    static {
        shapePrototypes.put("rectangle", new Rectangle());
        shapePrototypes.put("oval", new Oval());
        shapePrototypes.put("line", new Line());
        shapePrototypes.put("star", new Star());
    }

    /**
     * To create a new shape
     * @param type the shape to create
     * @return a clone of the shape type
     */
    public static Shape getShapePrototype(String type) {
        try {
            return shapePrototypes.get(type.toLowerCase()).clone();
        } catch (NullPointerException ex) {
            return null;
        }
    }
}
