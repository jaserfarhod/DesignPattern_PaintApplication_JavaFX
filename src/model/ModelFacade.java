/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.command.*;
import model.factory.AbstractShapeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.util.PaintLogging;

/**
 * @author Robert & Jaser
 */
public class ModelFacade extends Observable {

    private CommandInvoker commandInvoker;
    private AbstractShapeFactory factory;
    private ArrayList<Shape> shapes;
    private ArrayList<Shape> markedShapes;
    private ArrayList<Shape> compositeShapes;
    private double canvasWidth;
    private double canvasHeight;

    public ModelFacade(double width, double height) {


        commandInvoker = new CommandInvoker();
        factory = AbstractShapeFactory.getFactory();
        shapes = new ArrayList<>();
        markedShapes = new ArrayList<>();
        compositeShapes = new ArrayList<>();
        this.canvasWidth = width;
        this.canvasHeight = height;
    }

    /**
     * Draw a new shape according to the given parameters
     *
     * @param shapeType      the shape to draw
     * @param p1             upper left corner of the shape
     * @param p2             lower right corner of the shape
     * @param color          the color of the shape
     * @param thicknessValue if shape isn't filled, the thickness of the lines
     * @param isFilled       if the shape is filled or not
     * @param gc             the GraphicsContext from the canvas to draw on
     */
    public void drawShape(String shapeType, Point p1, Point p2, Color color, double thicknessValue, boolean isFilled, GraphicsContext gc) {
        PaintLogging.logInfo("ModelFacade | drawShape - method\n" +
                "Type: " + shapeType);

        Shape shape = createShape(shapeType.toLowerCase());

        if (shape != null) {
            shape.init(p1, p2, color, thicknessValue, isFilled);
            shape.draw(gc);

            commandInvoker.setCommand(new AddCommand(shapes, shape));
            subjectNotify();
        }
    }

    /**
     * Draw a composite from the list of composites
     *
     * @param compositeNr which composite
     * @param p1          coordinate for upper left
     * @param p2          coordinate for lower right
     * @param color       color for the composite
     * @param thickness   thickness of the composite
     * @param filled      if the composite is filled
     * @param gc          to draw the composite
     */
    public void drawComposite(int compositeNr, Point p1, Point p2, Color color, Double thickness, boolean filled, GraphicsContext gc) {

        Shape shape = compositeShapes.get(compositeNr).clone();
        shape.init(p1, p2, color, thickness, filled);
        shape.draw(gc);

        commandInvoker.setCommand(new AddCommand(shapes, shape));

        subjectNotify();
    }

    /**
     * Creates a shape from ShapeFactory
     *
     * @param shapeType string name of the shape
     * @return shape if found else null.
     */
    private Shape createShape(String shapeType) {
        switch (shapeType) {
            case "line":
                return factory.createLine();
            case "oval":
                return factory.createOval();
            case "rectangle":
                return factory.createRectangle();
            case "star":
                return factory.createStar();
        }
        return null;
    }

    /**
     * Create a composit shape from the marked items
     */
    public void createComposite() {
        PaintLogging.logInfo("in createComposite() method");

        CompositeShape shape = new CompositeShape();
        shape.addShapes(markedShapes);
        compositeShapes.add(shape);
    }

    /**
     * Return the list of shapes
     *
     * @return the list of shapes
     */
    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = (ArrayList<Shape>) shapes;
    }

    public void markItems(Point clicked, GraphicsContext gc) {
        for (Shape shape : shapes) {
            //Math.min && Math.max fix so that the line shape can be marked
            if (clicked.getX() > Math.min(shape.getP1().getX(), shape.getP2().getX()) &&
                    clicked.getY() > Math.min(shape.getP1().getY(), shape.getP2().getY()) &&
                    clicked.getX() < Math.max(shape.getP1().getX(), shape.getP2().getX()) &&
                    clicked.getY() < Math.max(shape.getP1().getY(), shape.getP2().getY())) {

                setMarkedShape(shape);
//                reRender(gc);
                subjectNotify();
            }
        }
    }

    private void setMarkedShape(Shape shape) {

        if (markedShapes.contains(shape)) {
            markedShapes.remove(shape);
        } else {
            markedShapes.add(shape);
        }
    }

    /**
     * Should clear all marks when not in edit mode.
     */
    public void clearMarkedShapes() {
        markedShapes.clear();
    }

    public void delete(GraphicsContext gc) {
        PaintLogging.logInfo("inside delete(gc) method" +
                "\n markedShapes length: " + markedShapes.size());

        for (Shape shape : markedShapes) {
            commandInvoker.setCommand(new DeleteCommand(shapes, shape));
            commandInvoker.redo();
        }
        markedShapes.clear();

//        reRender(gc);
        subjectNotify();
    }

    public void undo(GraphicsContext gc) {
        commandInvoker.undo();
//        reRender(gc);
        subjectNotify();
    }

    public void redo(GraphicsContext gc) {
        commandInvoker.redo();
//        reRender(gc);
        subjectNotify();
    }

    /**
     * Redraws the canvas
     */
    public void reRender(GraphicsContext gc) {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        for (Shape s : shapes) {
            s.draw(gc);
        }
        // also draw markedBoundry
        drawMarkedShapesBoundry(gc);

        //setChanged();
        //notifyObservers(shapes);
    }

    /**
     * Draws a marking outline around the marked shape.
     *
     * @param gc to draw on
     */
    private void drawMarkedShapesBoundry(GraphicsContext gc) {
        Point p1 = new Point();
        Point p2 = new Point();

        //reRender all shapes before adding "marked rect"

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);

        for (Shape s : markedShapes) {

            //Math.min && Math.max fix so that the marking around lines also show.
            p1.setX(Math.min(s.getP1().getX(), s.getP2().getX()));
            p1.setY(Math.min(s.getP1().getY(), s.getP2().getY()));

            p2.setX(Math.max(s.getP1().getX(), s.getP2().getX()));
            p2.setY(Math.max(s.getP1().getY(), s.getP2().getY()));

            gc.strokeRect(p1.getX() - 6.0, p1.getY() - 6.0,
                    p2.getX() - p1.getX() + 12.0, p2.getY() - p1.getY() + 12.0);
        }
    }

    /**
     * Changes the color of all marked shapes
     */
    public void editMarkedColor(GraphicsContext gc, Color color) {
        for (Shape shape : markedShapes) {
            commandInvoker.setCommand(new ChangeColorCommand(shape, color));
        }
//        reRender(gc);
        subjectNotify();
    }

    /**
     * Changes the filled value of all marked shapes
     */
    public void editMarkedFilled(GraphicsContext gc, boolean filled) {
        for (Shape s : markedShapes) {
            commandInvoker.setCommand(new ChangeFilledCommand(s, filled));
//            reRender(gc);
            subjectNotify();
        }
    }

    /**
     * Changes the thickness value of all marked shapes
     */
    public void editMarkedThickness(GraphicsContext gc, double thickness) {
        for (Shape shape : markedShapes) {
            commandInvoker.setCommand(new ChangeThicknessCommand(shape, thickness));
//            reRender(gc);
            subjectNotify();
        }
    }

    // Called by the subject, i.e. this class, to notify observers of state change
    private void subjectNotify() {
        setChanged(); //
        notifyObservers(shapes); //
    }
}
