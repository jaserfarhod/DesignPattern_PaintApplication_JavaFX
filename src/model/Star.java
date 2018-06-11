package model;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Star extends Shape implements Cloneable, Serializable {

    private Point p1;
    private Point p2;
    private Point p3;
    private Point p4;
    private Point p5;

    public Star() {
        super(null, null, 0, Color.BLACK.toString());

        p1 = new Point();
        p2 = new Point();
        p3 = new Point();
        p4 = new Point();
        p5 = new Point();
    }

    @Override
    protected void drawStep(GraphicsContext gc) {
        setPoints();

        if (isFilled()) {
            gc.fillPolygon(new double[]{p1.getX(), p2.getX(), p3.getX(), p4.getX(), p5.getX()},
                    new double[]{p1.getY(), p2.getY(), p3.getY(), p4.getY(), p5.getY()}, 5);
        } else {
            gc.strokePolygon(new double[]{p1.getX(), p2.getX(), p3.getX(), p4.getX(), p5.getX()},
                    new double[]{p1.getY(), p2.getY(), p3.getY(), p4.getY(), p5.getY()}, 5);
        }
    }

    private void setPoints() {
        p1.setX(getP1().getX());
        p1.setY(getP1().getY() + ((getP2().getY() - getP1().getY()) * 0.3));

        p2.setX(getP1().getX() + ((getP2().getX() - getP1().getX()) * 0.8));
        p2.setY(getP2().getY());

        p3.setX(getP1().getX() + ((getP2().getX() - getP1().getX()) * 0.5));
        p3.setY(getP1().getY());

        p4.setX(getP1().getX() + ((getP2().getX() - getP1().getX()) * 0.2));
        p4.setY(getP2().getY());

        p5.setX(getP2().getX());
        p5.setY(getP1().getY() + ((getP2().getY() - getP1().getY()) * 0.3));
    }

    public Star clone() {
        return (Star) super.clone();
    }

    @Override
    public String toString() {
        return "Star";
    }
}
