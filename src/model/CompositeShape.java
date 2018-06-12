package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A composite of shapes, basically a group of shapes,
 * that are in itself a new shape.
 */
public class CompositeShape extends Shape implements Cloneable {

    private List<Shape> shapes;
    private Point templateP1;
    private Point templateP2;

    CompositeShape() {
        super(new Point(Double.MAX_VALUE, Double.MAX_VALUE), new Point(0, 0), 0, Color.BLACK.toString());
        shapes = new ArrayList<>();
    }

    @Override
    public void init(Point p1, Point p2, Color color, double thicknessValue, boolean filled) {
        super.setP1(p1);
        super.setP2(p2);

        setColor(color.toString());
        setThickness(thicknessValue);
        setFilled(filled);
    }

    /**
     * Adds all marked shapes into this composite shapes
     *
     * @param markedShapes the marked shapes to be made into composite
     */
    public void addShapes(List<Shape> markedShapes) {
        //this.shapes = shapes will give a reference to the marked list that will be empty when "edit" clears
        this.shapes.addAll(markedShapes);

        // set P1 & P2 for the "Template Composite"
        // min( comp_min, ( min( shape p1 , shape p2)) to make line also work
        for (Shape s : shapes) {
            getP1().setX(Math.min(getP1().getX(),
                    Math.min(s.getP1().getX(), s.getP2().getX())));

            getP1().setY(Math.min(getP1().getY(),
                    Math.min(s.getP1().getY(), s.getP2().getY())));

            getP2().setX(Math.max(getP2().getX(),
                    Math.max(s.getP1().getX(), s.getP2().getX())));

            getP2().setY(Math.max(getP2().getY(),
                    Math.max(s.getP1().getY(), s.getP2().getY())));
        }
        templateP1 = new Point(getP1().getX(), getP1().getY());
        templateP2 = new Point(getP2().getX(), getP2().getY());
    }

    /**
     * Modifies all shapes in this composite to
     * a new filled option
     *
     * @param filled the new option
     */
    @Override
    public void setFilled(boolean filled) {
        for (Shape s : shapes) {
            s.setFilled(filled);
        }
    }

    /**
     * Changes the thickness of all shapes in this composite
     *
     * @param thickness the new thickness
     */
    @Override
    public void setThickness(double thickness) {
        for (Shape s : shapes) {
            s.setThickness(thickness);
        }
    }

    /**
     * Changes the color of all shapes in this composite.
     *
     * @param color the new color
     */
    @Override
    public void setColor(String color) {
        for (Shape s : shapes) {
            s.setColor(color);
        }
    }

    /**
     * Places the shapes in the composite with the same relation to each other
     * as the original shapes had before turned into a composite.
     * <p>
     * The math:
     * X1 (new p1x) + ( old shape p1x val - old p1x / old p2x - old p1x) * new width
     * Y1 (new p1y) + ( old shape p1y val - old p1y / old p2y - old p1y) * new height
     * X2 (new p1x) + ( old shape p2x val - old p2x / old p2x - old p1x) * new width
     * Y2 (new p1y) + ( old shape p2y val - old p2y / old p2y - old p1y) * new height
     */
    private void moveShapesToFitInsideComposite() {

        for (Shape s : shapes) {

            if (s instanceof CompositeShape) {
                ((CompositeShape) s).moveShapesToFitInsideComposite();
            }

            Point oldPoint1 = s.getP1();
            Point oldPoint2 = s.getP2();

            s.setP1(new Point(
                    getP1().getX() +
                            (((oldPoint1.getX() - templateP1.getX()) / (templateP2.getX() - templateP1.getX())) *
                                    (getP2().getX() - getP1().getX()))
                    ,
                    getP1().getY() +
                            (((oldPoint1.getY() - templateP1.getY()) / (templateP2.getY() - templateP1.getY())) *
                                    (getP2().getY() - getP1().getY()))
            ));

            s.setP2(new Point(
                    getP1().getX() +
                            (((oldPoint2.getX() - templateP1.getX()) / (templateP2.getX() - templateP1.getX())) *
                                    (getP2().getX() - getP1().getX()))
                    ,
                    getP1().getY() +
                            (((oldPoint2.getY() - templateP1.getY()) / (templateP2.getY() - templateP1.getY())) *
                                    (getP2().getY() - getP1().getY()))
            ));
        }
        templateP1 = this.getP1();
        templateP2 = this.getP2();
    }

    @Override
    protected void drawStep(GraphicsContext gc) {
        moveShapesToFitInsideComposite();
        for (Shape s : shapes) {
            s.draw(gc);
        }
    }

    public CompositeShape clone() {
        CompositeShape clone = (CompositeShape) super.clone();
        clone.setNewListForClone();
        for (Shape s : shapes) {
            clone.cloneAddShape(s.clone());
        }

        clone.setP1(new Point(this.getP1().getX(), this.getP1().getY()));    //new ref for the composite to be drawn
        clone.setP2(new Point(this.getP2().getX(), this.getP2().getY()));

        return clone;
    }

    /**
     * Must only be used by clone()
     */
    private void cloneAddShape(Shape clonedShape) {
        this.shapes.add(clonedShape);
    }

    /**
     * Must only be used by clone()
     */
    private void setNewListForClone() {
        //clear the old list
        this.shapes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Composite";
    }
}
