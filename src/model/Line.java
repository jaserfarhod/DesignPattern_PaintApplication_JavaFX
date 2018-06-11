package model;

import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Shape implements Cloneable, Serializable {

    public Line() {
        super(null, null, 0, Color.BLACK.toString());
    }

    public Line(Point p1, Point p2, double thickness, Color color) {
        super(p1, p2, thickness, color.toString());
    }

    @Override
    protected void drawStep(GraphicsContext gc) {
        gc.strokeLine(getP1().getX(), getP1().getY(), getP2().getX(), getP2().getY());
    }

    public Line clone() {
        return (Line) super.clone();
    }

    @Override
    public String toString(){
        return "Line";
    }
}
