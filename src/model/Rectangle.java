package model;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Shape implements Cloneable, Serializable {

    public Rectangle() {
        super(null, null, 0, Color.BLACK.toString());
    }


    @Override
    protected void drawStep(GraphicsContext gc) {
        if (isFilled()) {
            // x,y,w,h
            gc.fillRect(getP1().getX(), getP1().getY(),
                    getP2().getX() - getP1().getX(), getP2().getY() - getP1().getY());
        } else {
            gc.strokeRect(getP1().getX(), getP1().getY(),
                    getP2().getX() - getP1().getX(), getP2().getY() - getP1().getY());
        }
    }

    public Rectangle clone() {
        return (Rectangle) super.clone();
    }

    @Override
    public String toString() {
        return "Rectangle";
    }
}
