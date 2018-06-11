package model;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The shape abstract class
 */
public abstract class Shape implements Cloneable, Serializable {

    private Point p1;
    private Point p2;
    private boolean isFilled;
    private double thickness;
    private String color;   //Represented as string because javafx.scene.paint.Color cannot be serialized

    public Shape(Point p1, Point p2, double thickness, String color) {
        this.p1 = p1;
        this.p2 = p2;
        this.thickness = thickness;
        this.color = color;
    }

    public void init(Point p1, Point p2, Color color, double thicknessValue, boolean filled) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = color.toString();
        this.thickness = thicknessValue;
        this.isFilled = filled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public double getThickness() {
        return thickness;
    }

    public String getColor() {
        return color;
    }

    @Override
    public Shape clone() {
        try {
            return (Shape) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    //Template method
    public final void draw(GraphicsContext gc) {
        Color color = Color.web(this.color);
        gc.setStroke(color);
        gc.setFill(color);
        gc.setLineWidth(thickness);
        drawStep(gc);
    }

    protected abstract void drawStep(GraphicsContext gc);

}
